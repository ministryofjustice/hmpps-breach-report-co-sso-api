package uk.gov.justice.digital.hmpps.breachreportcossoapi.integration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.Cosso
import uk.gov.justice.digital.hmpps.breachreportcossoapi.repository.CossoRepository
import java.time.LocalDateTime
import java.time.ZonedDateTime

class CossoCrudTests : IntegrationTestBase() {

  @Autowired
  private lateinit var cossoRepository: CossoRepository

  @Test
  fun `should create a Cosso record`() {
    webTestClient.post()
      .uri("/cosso")
      .headers(setAuthorisation(roles = listOf("ROLE_CO_SSO")))
      .bodyValue(Cosso(crn = "X000001"))
      .exchange()
      .expectStatus()
      .isCreated

    val cosso = cossoRepository.findByCrn("X000001").single()
    assertThat(cosso.crn).isEqualTo("X000001")
    assertThat(cosso.id).isNotNull()
  }

  @Test
  fun `should update a Cosso record`() {
    webTestClient.post()
      .uri("/cosso")
      .headers(setAuthorisation(roles = listOf("ROLE_CO_SSO")))
      .bodyValue(Cosso(crn = "X000002"))
      .exchange()
      .expectStatus()
      .isCreated

    val cosso = cossoRepository.findByCrn("X000002").single()
    assertThat(cosso.crn).isEqualTo("X000002")

    val cossoBody = Cosso(
      crn = "X000002",
      completedDate = ZonedDateTime.now(),
      reviewEvent = "Merge",
      reviewRequiredDate = LocalDateTime.now(),
    )

    webTestClient.put()
      .uri("/cosso/" + cosso.id)
      .headers(setAuthorisation(roles = listOf("ROLE_CO_SSO")))
      .bodyValue(cossoBody)
      .exchange()
      .expectStatus()
      .isOk

    val updatedCosso = cossoRepository.findByCrn("X000002").single()
    assertThat(updatedCosso.crn).isEqualTo("X000002")
    assertThat(updatedCosso.reviewEvent).isEqualTo("Merge")
  }

  @Test
  fun `should update a Cosso record to completed`() {
    webTestClient.post()
      .uri("/cosso")
      .headers(setAuthorisation(roles = listOf("ROLE_CO_SSO")))
      .bodyValue(Cosso(crn = "X600002"))
      .exchange()
      .expectStatus()
      .isCreated

    val cosso = cossoRepository.findByCrn("X600002").single()
    assertThat(cosso.crn).isEqualTo("X600002")

    val cossoBody = Cosso(
      crn = "X600002",
      completedDate = ZonedDateTime.now(),
      reviewEvent = "Merge",
      reviewRequiredDate = LocalDateTime.now(),
    )

    webTestClient.put()
      .uri("/cosso/" + cosso.id)
      .headers(setAuthorisation(roles = listOf("ROLE_CO_SSO")))
      .bodyValue(cossoBody)
      .exchange()
      .expectStatus()
      .isOk

    val updatedCosso = cossoRepository.findByCrn("X600002").single()
    assertThat(updatedCosso.crn).isEqualTo("X600002")
    assertThat(updatedCosso.reviewEvent).isEqualTo("Merge")
    assertThat(updatedCosso.completedDate).isNotNull()
  }

  @Test
  fun `should fail to create if the crn is too long`() {
    webTestClient.post()
      .uri("/cosso")
      .headers(setAuthorisation(roles = listOf("ROLE_CO_SSO")))
      .bodyValue(Cosso(crn = "X000001123456789123456"))
      .exchange()
      .expectStatus().isBadRequest
      .expectBody().jsonPath("$.userMessage").isEqualTo("""Field: crn - must match "^[A-Z][0-9]{6}"""")
  }

  @Test
  fun `should delete a Cosso record`() {
    webTestClient.post()
      .uri("/cosso")
      .headers(setAuthorisation(roles = listOf("ROLE_CO_SSO")))
      .bodyValue(Cosso(crn = "X000004"))
      .exchange()
      .expectStatus()
      .isCreated

    val cosso = cossoRepository.findByCrn("X000004")
    assertThat(cosso.first().crn).isEqualTo("X000004")
    assertThat(cosso.first().id).isNotNull()

    webTestClient.delete()
      .uri("/cosso/" + cosso.first().id)
      .headers(setAuthorisation(roles = listOf("ROLE_CO_SSO")))
      .exchange()
      .expectStatus()
      .isOk

    assertThat(cossoRepository.findById(cosso.first().id)).isEmpty
  }
}
