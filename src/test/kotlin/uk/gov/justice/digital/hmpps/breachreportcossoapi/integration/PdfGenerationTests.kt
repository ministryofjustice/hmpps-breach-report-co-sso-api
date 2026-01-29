package uk.gov.justice.digital.hmpps.breachreportcossoapi.integration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ContentDisposition
import org.springframework.http.MediaType
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.Cosso
import uk.gov.justice.digital.hmpps.breachreportcossoapi.repository.CossoRepository
import java.time.Duration
import java.util.UUID

class PdfGenerationTests : IntegrationTestBase() {

  @Autowired
  private lateinit var cossoRepository: CossoRepository

  @Test
  fun `get PDF should return a 200 response`() {
    webTestClient.post()
      .uri("/cosso")
      .headers(setAuthorisation(roles = listOf("ROLE_COSSO")))
      .bodyValue(Cosso(crn = "X800001"))
      .exchange()
      .expectStatus()
      .isCreated

    val cosso = cossoRepository.findByCrn("X800001")
    assertThat(cosso.first().crn).isEqualTo("X800001")

    webTestClient
      .mutate().responseTimeout(Duration.ofSeconds(30)).build()
      .get()
      .uri("/cosso/" + cosso[0].id + "/pdf")
      .headers(setAuthorisation(roles = listOf("ROLE_COSSO")))
      .exchange()
      .expectStatus()
      .isOk
      .expectHeader()
      .contentType(MediaType.APPLICATION_PDF)
      .expectHeader()
      .contentDisposition(ContentDisposition.attachment().filename("Breach_report_co_sso_" + cosso[0].crn + ".pdf").build())
  }

  @Test
  fun `get PDF should return a 404 response if not found`() {
    webTestClient.post()
      .uri("/cosso")
      .headers(setAuthorisation(roles = listOf("ROLE_COSSO")))
      .bodyValue(Cosso(crn = "X800002"))
      .exchange()
      .expectStatus()
      .isCreated

    val cosso = cossoRepository.findByCrn("X800002")
    assertThat(cosso.first().crn).isEqualTo("X800002")

    webTestClient.get()
      .uri("/cosso/" + UUID.randomUUID() + "/pdf")
      .headers(setAuthorisation(roles = listOf("ROLE_COSSO")))
      .exchange()
      .expectStatus().isNotFound
  }
}
