package uk.gov.justice.digital.hmpps.breachreportcossoapi.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.breachreportcossoapi.entity.AddressEntity
import uk.gov.justice.digital.hmpps.breachreportcossoapi.entity.CossoEntity
import uk.gov.justice.digital.hmpps.breachreportcossoapi.enums.ReviewEventType
import uk.gov.justice.digital.hmpps.breachreportcossoapi.exception.NotFoundException
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.Cosso
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.CreateResponse
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.InitialiseCosso
import uk.gov.justice.digital.hmpps.breachreportcossoapi.repository.CossoRepository
import java.time.ZonedDateTime
import java.util.*

@Service
class CossoService(
  val cossoRepository: CossoRepository,
  val pdfGenerationService: PdfGenerationService,
  @Value("\${frontend.url}") val frontendUrl: String,
) {

  @Transactional
  fun initialiseCosso(initialiseCosso: InitialiseCosso) = cossoRepository.save(
    CossoEntity(crn = initialiseCosso.crn),
  ).id.let {
    CreateResponse(it, "$frontendUrl/basic-details/$it")
  }

  fun findCossoById(id: UUID): Cosso {
    val cossoEntity: CossoEntity = cossoRepository.findByIdOrNull(id) ?: throw NotFoundException(
      "CossoEntity",
      "id",
      id,
    )
    return cossoEntity.toModel()
  }

  @Transactional
  fun updateCosso(id: UUID, cosso: Cosso): Cosso {
    val cossoEntity: CossoEntity =
      cossoRepository.findByIdOrNull(id) ?: throw NotFoundException("CossoEntity", "id", id)
    return cossoRepository.save(cosso.toEntity(cossoEntity)).toModel()
  }

  @Transactional
  fun deleteCosso(id: UUID): String {
    if (!cossoRepository.existsById(id)) {
      throw NotFoundException("CossoEntity", "id", id)
    }
    val crn = findCossoById(id).crn
    cossoRepository.deleteById(id)
    return crn
  }

  fun getCossoAsPdf(id: UUID, cosso: Cosso?, draft: Boolean): ByteArray? {
    val html = pdfGenerationService.generateHtml(cosso)

    var pdfBytes = pdfGenerationService.generatePdf(html)

    if (draft) {
      pdfBytes = pdfGenerationService.addWatermark(pdfBytes)
    }

    return pdfBytes
  }

  fun Cosso.toEntity(
    cosso: CossoEntity? = null,
    postalAddressEntity: AddressEntity? = null,
    workAddressEntity: AddressEntity? = null,
  ): CossoEntity = cosso?.apply {
    crn = this@toEntity.crn
    titleAndFullName = this@toEntity.titleAndFullName
    dateOfForm = this@toEntity.dateOfForm
    sheetSentBy = this@toEntity.sheetSentBy
    telephoneNumber = this@toEntity.telephoneNumber
    mobileNumber = this@toEntity.mobileNumber
    emailAddress = this@toEntity.emailAddress
    completedDate = this@toEntity.completedDate
    postalAddressEntity?.let { postalAddress = it }
    workAddressEntity?.let { workAddress = it }
    dateOfBirth = this@toEntity.dateOfBirth
    prisonNumber = this@toEntity.prisonNumber
    probationArea = this@toEntity.probationArea
    witnessAvailability = this@toEntity.witnessAvailability
    mainOffence = this@toEntity.mainOffence
    additionalOffence = this@toEntity.additionalOffence
    sentencingCourt = this@toEntity.sentencingCourt
    sentenceType = this@toEntity.sentenceType
    sentenceLength = this@toEntity.sentenceLength
    lengthUnits = this@toEntity.lengthUnits
    suspendedCustodyLength = this@toEntity.suspendedCustodyLength
    secondLength = this@toEntity.secondLength
    secondLengthUnits = this@toEntity.secondLengthUnits
    requirementType = this@toEntity.requirementType
    requirementLength = this@toEntity.requirementLength
    requirementSecondLength = this@toEntity.requirementSecondLength
    amendmentDetails = this@toEntity.amendmentDetails
    amendmentReason = this@toEntity.amendmentReason
    whyInBreach = this@toEntity.whyInBreach
    stepsToPreventBreach = this@toEntity.stepsToPreventBreach
    complianceODate = this@toEntity.complianceODate
    riskHistory = this@toEntity.riskHistory
    recommendations = this@toEntity.recommendations
    supportingComments = this@toEntity.supportingComments
    basicDetailsSaved = this@toEntity.basicDetailsSaved
    confirmEqualities = this@toEntity.confirmEqualities
    riskOfHarmChanged = this@toEntity.riskOfHarmChanged
    signAndSendSaved = this@toEntity.signAndSendSaved
    contactSaved = this@toEntity.contactSaved
    reviewRequiredDate = this@toEntity.reviewRequiredDate
    reviewEvent = this@toEntity.reviewEvent
  } ?: CossoEntity(
    crn = crn,
    titleAndFullName = titleAndFullName,
    dateOfForm = dateOfForm,
    sheetSentBy = sheetSentBy,
    telephoneNumber = telephoneNumber,
    mobileNumber = mobileNumber,
    emailAddress = emailAddress,
    completedDate = completedDate,
    postalAddress = postalAddressEntity,
    dateOfBirth = dateOfBirth,
    prisonNumber = prisonNumber,
    workAddress = workAddressEntity,
    probationArea = probationArea,
    witnessAvailability = witnessAvailability,
    mainOffence = mainOffence,
    additionalOffence = additionalOffence,
    sentencingCourt = sentencingCourt,
    sentenceType = sentenceType,
    sentenceLength = sentenceLength,
    lengthUnits = lengthUnits,
    suspendedCustodyLength = suspendedCustodyLength,
    secondLength = secondLength,
    secondLengthUnits = secondLengthUnits,
    requirementType = requirementType,
    requirementLength = requirementLength,
    requirementSecondLength = requirementSecondLength,
    amendmentDetails = amendmentDetails,
    amendmentReason = amendmentReason,
    whyInBreach = whyInBreach,
    stepsToPreventBreach = stepsToPreventBreach,
    complianceODate = complianceODate,
    riskHistory = riskHistory,
    recommendations = recommendations,
    supportingComments = supportingComments,
    basicDetailsSaved = basicDetailsSaved,
    confirmEqualities = confirmEqualities,
    riskOfHarmChanged = riskOfHarmChanged,
    signAndSendSaved = signAndSendSaved,
    contactSaved = contactSaved,
    reviewRequiredDate = reviewRequiredDate,
    reviewEvent = reviewEvent,
  )

  fun CossoEntity.toModel(): Cosso = Cosso(
    crn = crn,
    titleAndFullName = titleAndFullName,
    dateOfForm = dateOfForm,
    sheetSentBy = sheetSentBy,
    telephoneNumber = telephoneNumber,
    mobileNumber = mobileNumber,
    emailAddress = emailAddress,
    completedDate = completedDate,
    postalAddressId = postalAddress?.id,
    dateOfBirth = dateOfBirth,
    prisonNumber = prisonNumber,
    workAddressId = workAddress?.id,
    probationArea = probationArea,
    witnessAvailability = witnessAvailability,
    mainOffence = mainOffence,
    additionalOffence = additionalOffence,
    sentencingCourt = sentencingCourt,
    sentenceType = sentenceType,
    sentenceLength = sentenceLength,
    lengthUnits = lengthUnits,
    suspendedCustodyLength = suspendedCustodyLength,
    secondLength = secondLength,
    secondLengthUnits = secondLengthUnits,
    requirementType = requirementType,
    requirementLength = requirementLength,
    requirementSecondLength = requirementSecondLength,
    amendmentDetails = amendmentDetails,
    amendmentReason = amendmentReason,
    whyInBreach = whyInBreach,
    stepsToPreventBreach = stepsToPreventBreach,
    complianceODate = complianceODate,
    riskHistory = riskHistory,
    recommendations = recommendations,
    supportingComments = supportingComments,
    basicDetailsSaved = basicDetailsSaved,
    confirmEqualities = confirmEqualities,
    riskOfHarmChanged = riskOfHarmChanged,
    signAndSendSaved = signAndSendSaved,
    contactSaved = contactSaved,
    reviewRequiredDate = reviewRequiredDate,
    reviewEvent = reviewEvent,
  )

  fun getActiveCossoForCrn(crn: String?): Collection<CossoEntity> = cossoRepository.findByCrnAndCompletedDateIsNull(crn)

  fun updateCossoCrn(cosso: CossoEntity, crn: String) {
    cosso.crn = crn
    cossoRepository.save(cosso)
  }

  fun updateReviewEvent(eventType: ReviewEventType, cosso: CossoEntity, occurredAt: ZonedDateTime) {
    cosso.reviewEvent = eventType.name
    cosso.reviewRequiredDate = occurredAt.toLocalDateTime()
    cossoRepository.save(cosso)
  }

  fun deleteAllByCrn(crn: String) {
    cossoRepository.deleteByCrn(crn)
  }
}
