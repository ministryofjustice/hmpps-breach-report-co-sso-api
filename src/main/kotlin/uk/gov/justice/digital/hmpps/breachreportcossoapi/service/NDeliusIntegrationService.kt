package uk.gov.justice.digital.hmpps.breachreportcossoapi.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono

@Service
class NDeliusIntegrationService(
  @Qualifier("integrationApiClient") private val webClient: WebClient,
) {
  fun getCrnForCossoUuid(cossoId: String): NDeliusCrn? = webClient.get()
    .uri("/case/{cossoId}", cossoId)
    .retrieve()
    .bodyToMono(NDeliusCrn::class.java)
    .onErrorResume(WebClientResponseException.NotFound::class.java) { Mono.empty() }
    .block()

  fun getCossoEventDocuments(crn: String, eventNumber: String): List<String> = webClient.get()
    .uri("/cosso-event-documents/{crn}/{eventNumber}", crn, eventNumber)
    .retrieve()
    .bodyToMono(CossoIdList::class.java)
    .onErrorResume(WebClientResponseException.NotFound::class.java) { Mono.empty() }
    .block()?.cossoIdList ?: emptyList()
}

data class NDeliusCrn(
  val crn: String,
)

data class CossoIdList(
  val cossoIdList: List<String>,
)
