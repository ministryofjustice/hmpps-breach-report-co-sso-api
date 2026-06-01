package uk.gov.justice.digital.hmpps.breachreportcossoapi.integration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.Amendment
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.Cosso
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.InitialiseCosso
import uk.gov.justice.digital.hmpps.breachreportcossoapi.repository.AmendmentRepository
import uk.gov.justice.digital.hmpps.breachreportcossoapi.repository.CossoRepository
import java.time.LocalDate
import java.util.UUID

class AmendmentCrudTests : IntegrationTestBase() {

  @Autowired
  private lateinit var cossoRepository: CossoRepository

  @Autowired
  private lateinit var amendmentRepository: AmendmentRepository

  @Test
  fun `should create an amendment record`() {
    webTestClient.post().uri("/cosso").headers(setAuthorisation(roles = listOf("ROLE_BREACH__CO_SSO__RW")))
      .bodyValue(Cosso(crn = "X200001")).exchange().expectStatus().isCreated

    val cosso = cossoRepository.findByCrn("X200001").single()

    webTestClient.post().uri("/cosso/amendment").headers(setAuthorisation(roles = listOf("ROLE_BREACH__CO_SSO__RW")))
      .bodyValue(Amendment(cossoId = cosso.id, amendmentReason = "Reason", amendmentDetails = "Details", amendmentDate = LocalDate.now())).exchange().expectStatus().isCreated

    val amendment = amendmentRepository.findAllByCossoId(cosso.id).single()
    assertThat(amendment.cosso.id).isEqualTo(cosso.id)
    assertThat(amendment.id).isNotNull()
  }

  @Test
  fun `should update an amendment record`() {
    webTestClient.post().uri("/cosso").headers(setAuthorisation(roles = listOf("ROLE_BREACH__CO_SSO__RW")))
      .bodyValue(Cosso(crn = "X200002")).exchange().expectStatus().isCreated

    val cosso = cossoRepository.findByCrn("X200002").single()

    webTestClient.post().uri("/cosso/amendment").headers(setAuthorisation(roles = listOf("ROLE_BREACH__CO_SSO__RW")))
      .bodyValue(Amendment(cossoId = cosso.id, amendmentReason = "Reason", amendmentDetails = "Details", amendmentDate = LocalDate.now())).exchange().expectStatus().isCreated

    val amendment = amendmentRepository.findAllByCossoId(cosso.id).single()

    val amendmentBody = Amendment(
      cossoId = cosso.id,
      amendmentReason = "Updated reason",
      amendmentDate = LocalDate.now().plusDays(1),
      amendmentDetails = "Updated details",
    )

    webTestClient.put().uri("/cosso/amendment/" + amendment.id).headers(setAuthorisation(roles = listOf("ROLE_BREACH__CO_SSO__RW")))
      .bodyValue(amendmentBody).exchange().expectStatus().isOk

    val updatedAmendment = amendmentRepository.findAllByCossoId(cosso.id).single()
    assertThat(updatedAmendment.cosso.id).isEqualTo(cosso.id)
    assertThat(updatedAmendment.id).isNotNull()
  }

  @Test
  fun `should fail to create if the cosso id is not found`() {
    val randomUuid = UUID.randomUUID()
    webTestClient.post().uri("/cosso/amendment").headers(setAuthorisation(roles = listOf("ROLE_BREACH__CO_SSO__RW")))
      .bodyValue(Amendment(cossoId = randomUuid, amendmentReason = "Reason", amendmentDetails = "Details", amendmentDate = LocalDate.now()))
      .exchange().expectStatus().isBadRequest.expectBody()
      .jsonPath("$.userMessage").isEqualTo("Validation failure: Cosso $randomUuid not found")
  }

  @Test
  fun `should delete a Cosso record`() {
    webTestClient.post().uri("/cosso").headers(setAuthorisation(roles = listOf("ROLE_BREACH__CO_SSO__RW")))
      .bodyValue(Cosso(crn = "X200004")).exchange().expectStatus().isCreated

    val cosso = cossoRepository.findByCrn("X200004").single()

    webTestClient.post().uri("/cosso/amendment").headers(setAuthorisation(roles = listOf("ROLE_BREACH__CO_SSO__RW")))
      .bodyValue(Amendment(cossoId = cosso.id, amendmentReason = "Reason", amendmentDetails = "Details", amendmentDate = LocalDate.now())).exchange().expectStatus().isCreated

    val amendment = amendmentRepository.findAllByCossoId(cosso.id)
    assertThat { amendment.first().cosso.id == cosso.id }
    assertThat { amendment.first().id }.isNotNull()

    webTestClient.delete().uri("/cosso/amendment/" + amendment.first().id).headers(setAuthorisation(roles = listOf("ROLE_BREACH__CO_SSO__RW")))
      .exchange().expectStatus().isOk

    assertThat(cossoRepository.findById(amendment.first().id)).isEmpty
  }

  @Test
  fun `test updating cosso record`() {
    webTestClient.post().uri("/cosso").headers(setAuthorisation(roles = listOf("ROLE_BREACH__CO_SSO__RW")))
      .bodyValue(InitialiseCosso(crn = "X200005")).exchange().expectStatus().isCreated

    val cosso = cossoRepository.findByCrn("X200005").single()

    webTestClient.post().uri("/cosso/amendment").headers(setAuthorisation(roles = listOf("ROLE_BREACH__CO_SSO__RW")))
      .bodyValue(Amendment(cossoId = cosso.id, amendmentReason = "Reason", amendmentDetails = "Details", amendmentDate = LocalDate.now())).exchange().expectStatus().isCreated

    val originalAmendment = amendmentRepository.findAllByCossoId(cosso.id).single()

    val originalLastUpdated = originalAmendment.lastUpdatedDatetime

    val updatePayload = Amendment(
      cossoId = cosso.id,
      amendmentReason = "Updated reason",
      amendmentDate = LocalDate.now().plusDays(1),
      amendmentDetails = "Updated details",
    )

    webTestClient.put().uri("/cosso/amendment/${originalAmendment.id}").headers(setAuthorisation(roles = listOf("ROLE_BREACH__CO_SSO__RW")))
      .bodyValue(updatePayload).exchange().expectStatus().isOk

    val updatedAmendment = amendmentRepository.findAllByCossoId(cosso.id).single()

    assertThat(updatedAmendment.cosso.id).isEqualTo(cosso.id)
    assertThat(updatedAmendment.amendmentReason).isEqualTo("Updated reason")
    assertThat(updatedAmendment.amendmentDetails).isEqualTo("Updated details")
    assertThat(updatedAmendment.amendmentDate).isEqualTo(LocalDate.now().plusDays(1))
    assertThat(updatedAmendment.createdDatetime).isEqualTo(originalAmendment.createdDatetime)
    assertThat(updatedAmendment.createdByUser).isEqualTo(originalAmendment.createdByUser)
    assertThat(updatedAmendment.lastUpdatedDatetime).isNotEqualTo(originalLastUpdated)
    assertThat(updatedAmendment.lastUpdatedUser).isNotBlank()
  }
}
