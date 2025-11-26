package uk.gov.justice.digital.hmpps.breachreportcossoapi.listener

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.awspring.cloud.sqs.annotation.SqsListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.breachreportcossoapi.entity.CossoEntity
import uk.gov.justice.digital.hmpps.breachreportcossoapi.enums.ReviewEventType
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.DomainEventsMessage
import uk.gov.justice.digital.hmpps.breachreportcossoapi.service.CossoService
import uk.gov.justice.digital.hmpps.breachreportcossoapi.service.NDeliusIntegrationService
import java.time.ZonedDateTime

@Service
class DomainEventsListener(
  private val cossoService: CossoService,
  private val objectMapper: ObjectMapper,
  private val nDeliusIntegrationService: NDeliusIntegrationService,
) {

  @Transactional
  @SqsListener("hmppsbreachreportcossoqueue", factory = "hmppsQueueContainerFactoryProxy")
  fun listen(msg: String) {
    val (message, attributes) = objectMapper.readValue<SQSMessage>(msg)
    val domainEventMessage = objectMapper.readValue<DomainEventsMessage>(message)
    handleMessage(domainEventMessage)
  }

  private fun handleMessage(message: DomainEventsMessage) {
    when (message.eventType) {
      "probation-case.merge.completed" -> {
        // Update CRNs where appropriate
        val cosso = cossoService.getActiveCossoForCrn(message.sourceCrn)
        cosso.forEach {
          cossoService.updateCossoCrn(it, requireNotNull(message.targetCrn))
        }

        updateReviewEvent(ReviewEventType.MERGE, cosso, message.occurredAt)
      }

      "probation-case.unmerge.completed" -> {
        // Update CRNs where appropriate
        val cosso = cossoService.getActiveCossoForCrn(message.unmergedCrn)
        cosso.forEach {
          nDeliusIntegrationService.getCrnForCossoUuid(it.id.toString())?.crn?.let { crn ->
            cossoService.updateCossoCrn(
              it,
              crn,
            )
          }
        }

        updateReviewEvent(ReviewEventType.UNMERGE, cosso, message.occurredAt)
      }

      "probation-case.sentence.moved" -> {
        // Update CRNs where appropriate
        val cosso = cossoService.getActiveCossoForCrn(message.sourceCrn)
        cosso.forEach {
          nDeliusIntegrationService.getCrnForCossoUuid(it.id.toString())?.crn?.let { crn ->
            cossoService.updateCossoCrn(
              it,
              crn,
            )
          }
        }

        updateReviewEvent(ReviewEventType.EVENT_MOVE, cosso, message.occurredAt)
      }

      "probation-case.deleted.gdpr" -> {
        message.crn?.let { cossoService.deleteAllByCrn(it) }
      }
    }
  }

  private fun updateReviewEvent(eventType: ReviewEventType, cosso: Collection<CossoEntity>, occurredAt: ZonedDateTime) {
    cosso.forEach { cosso -> cossoService.updateReviewEvent(eventType, cosso, occurredAt) }
  }
}

data class SQSMessage(
  @JsonProperty("Message") val message: String,
  @JsonProperty("MessageAttributes") val attributes: MessageAttributes = MessageAttributes(),
)

data class MessageAttributes(
  @JsonAnyGetter @JsonAnySetter private val attributes: MutableMap<String, MessageAttribute> = mutableMapOf(),
) : MutableMap<String, MessageAttribute> by attributes {

  val eventType = attributes[EVENT_TYPE_KEY]?.value

  companion object {
    private const val EVENT_TYPE_KEY = "eventType"
  }
}

data class MessageAttribute(@JsonProperty("Type") val type: String, @JsonProperty("Value") val value: String)
