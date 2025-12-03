package uk.gov.justice.digital.hmpps.breachreportcossoapi.integration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.Address
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.Cosso
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.InitialiseCosso
import uk.gov.justice.digital.hmpps.breachreportcossoapi.repository.CossoRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime

class CossoCrudTests : IntegrationTestBase() {

  @Autowired
  private lateinit var cossoRepository: CossoRepository

  @Test
  fun `should create a Cosso record`() {
    webTestClient.post().uri("/cosso").headers(setAuthorisation(roles = listOf("ROLE_CO_SSO")))
      .bodyValue(Cosso(crn = "X000001")).exchange().expectStatus().isCreated

    val cosso = cossoRepository.findByCrn("X000001").single()
    assertThat(cosso.crn).isEqualTo("X000001")
    assertThat(cosso.id).isNotNull()
  }

  @Test
  fun `should update a Cosso record`() {
    webTestClient.post().uri("/cosso").headers(setAuthorisation(roles = listOf("ROLE_CO_SSO")))
      .bodyValue(Cosso(crn = "X000002")).exchange().expectStatus().isCreated

    val cosso = cossoRepository.findByCrn("X000002").single()
    assertThat(cosso.crn).isEqualTo("X000002")

    val cossoBody = Cosso(
      crn = "X000002",
      completedDate = ZonedDateTime.now(),
      reviewEvent = "Merge",
      reviewRequiredDate = LocalDateTime.now(),
    )

    webTestClient.put().uri("/cosso/" + cosso.id).headers(setAuthorisation(roles = listOf("ROLE_CO_SSO")))
      .bodyValue(cossoBody).exchange().expectStatus().isOk

    val updatedCosso = cossoRepository.findByCrn("X000002").single()
    assertThat(updatedCosso.crn).isEqualTo("X000002")
    assertThat(updatedCosso.reviewEvent).isEqualTo("Merge")
  }

  @Test
  fun `should update a Cosso record to completed`() {
    webTestClient.post().uri("/cosso").headers(setAuthorisation(roles = listOf("ROLE_CO_SSO")))
      .bodyValue(Cosso(crn = "X000003")).exchange().expectStatus().isCreated

    val cosso = cossoRepository.findByCrn("X000003").single()
    assertThat(cosso.crn).isEqualTo("X000003")

    val cossoBody = Cosso(
      crn = "X000003",
      completedDate = ZonedDateTime.now(),
      reviewEvent = "Merge",
      reviewRequiredDate = LocalDateTime.now(),
    )

    webTestClient.put().uri("/cosso/" + cosso.id).headers(setAuthorisation(roles = listOf("ROLE_CO_SSO")))
      .bodyValue(cossoBody).exchange().expectStatus().isOk

    val updatedCosso = cossoRepository.findByCrn("X000003").single()
    assertThat(updatedCosso.crn).isEqualTo("X000003")
    assertThat(updatedCosso.reviewEvent).isEqualTo("Merge")
    assertThat(updatedCosso.completedDate).isNotNull()
  }

  @Test
  fun `should fail to create if the crn is too long`() {
    webTestClient.post().uri("/cosso").headers(setAuthorisation(roles = listOf("ROLE_CO_SSO")))
      .bodyValue(Cosso(crn = "X000001123456789123456")).exchange().expectStatus().isBadRequest.expectBody()
      .jsonPath("$.userMessage").isEqualTo("""Field: crn - must match "^[A-Z][0-9]{6}"""")
  }

  @Test
  fun `should delete a Cosso record`() {
    webTestClient.post().uri("/cosso").headers(setAuthorisation(roles = listOf("ROLE_CO_SSO")))
      .bodyValue(Cosso(crn = "X000004")).exchange().expectStatus().isCreated

    val cosso = cossoRepository.findByCrn("X000004")
    assertThat(cosso.first().crn).isEqualTo("X000004")
    assertThat(cosso.first().id).isNotNull()

    webTestClient.delete().uri("/cosso/" + cosso.first().id).headers(setAuthorisation(roles = listOf("ROLE_CO_SSO")))
      .exchange().expectStatus().isOk

    assertThat(cossoRepository.findById(cosso.first().id)).isEmpty
  }

  @Test
  fun `test updating cosso record`() {
    val nowDate = LocalDate.now()
    val nowDateTime = LocalDateTime.now().withNano(0)
    val nowZoned = ZonedDateTime.now().withNano(0)
    val dob = nowDateTime.minusYears(30)

    webTestClient.post().uri("/cosso").headers(setAuthorisation(roles = listOf("ROLE_CO_SSO")))
      .bodyValue(InitialiseCosso(crn = "X000005")).exchange().expectStatus().isCreated

    val created = cossoRepository.findByCrn("X000005").single()
    val originalLastUpdated = created.lastUpdatedDatetime

    val updatePayload = Cosso(
      crn = "X000005",
      titleAndFullName = "John Example",
      dateOfForm = nowDate,
      sheetSentBy = "Officer",
      telephoneNumber = "00000000001",
      mobileNumber = "07700000000",
      emailAddress = "test@example.com",
      completedDate = nowZoned,
      postalAddress = Address(
        addressId = 1234,
        status = "Postal",
        buildingName = "Post",
        addressNumber = "1",
        streetName = "Street",
        district = "District",
        townCity = "London",
        county = "Greater London",
        postcode = "AA11AA",
        officeDescription = "Office",
      ),
      dateOfBirth = dob,
      prisonNumber = "12345678",
      workAddress = Address(
        addressId = 5678,
        status = "Main",
        buildingName = "Work",
        addressNumber = "2",
        streetName = "Work Street",
        district = "District",
        townCity = "London",
        county = "Greater London",
        postcode = "AA11AA",
        officeDescription = "Office",
      ),
      probationArea = "London",
      witnessAvailability = "Always",
      mainOffence = "Robbery",
      additionalOffence = "Theft",
      sentencingCourt = "Example Crown Court",
      sentenceType = "Custodial",
      sentenceLength = "5",
      lengthUnits = "Years",
      suspendedCustodyLength = "None",
      secondLength = "2",
      secondLengthUnits = "Months",
      requirementType = "Community Order",
      requirementLength = "150",
      requirementSecondLength = "Hours",
      amendmentDetails = "Updated for test",
      amendmentReason = "Testing update",
      whyInBreach = "No attendance",
      stepsToPreventBreach = "Weekly reporting",
      complianceODate = "01/01/2025",
      riskHistory = "Risk history text",
      recommendations = "Recommendations text",
      supportingComments = "Supporting comments text",
      basicDetailsSaved = true,
      confirmEqualities = true,
      riskOfHarmChanged = false,
      signAndSendSaved = true,
      contactSaved = true,
      reviewRequiredDate = nowDateTime,
      reviewEvent = "EVENT_MOVE",
    )

    webTestClient.put().uri("/cosso/${created.id}").headers(setAuthorisation(roles = listOf("ROLE_CO_SSO")))
      .bodyValue(updatePayload).exchange().expectStatus().isOk

    val updated = cossoRepository.findByCrn("X000005").single()

    assertThat(updated.postalAddress?.postcode).isEqualTo("AA11AA")
    assertThat(updated.workAddress?.buildingName).isEqualTo("Work")
    assertThat(updated.titleAndFullName).isEqualTo("John Example")
    assertThat(updated.dateOfForm).isEqualTo(nowDate)
    assertThat(updated.sheetSentBy).isEqualTo("Officer")
    assertThat(updated.telephoneNumber).isEqualTo("00000000001")
    assertThat(updated.mobileNumber).isEqualTo("07700000000")
    assertThat(updated.emailAddress).isEqualTo("test@example.com")
    assertThat(updated.completedDate?.withNano(0)).isEqualTo(nowZoned)
    assertThat(updated.dateOfBirth).isEqualTo(dob)
    assertThat(updated.prisonNumber).isEqualTo("12345678")
    assertThat(updated.probationArea).isEqualTo("London")
    assertThat(updated.witnessAvailability).isEqualTo("Always")
    assertThat(updated.mainOffence).isEqualTo("Robbery")
    assertThat(updated.additionalOffence).isEqualTo("Theft")
    assertThat(updated.sentencingCourt).isEqualTo("Example Crown Court")
    assertThat(updated.sentenceType).isEqualTo("Custodial")
    assertThat(updated.sentenceLength).isEqualTo("5")
    assertThat(updated.lengthUnits).isEqualTo("Years")
    assertThat(updated.suspendedCustodyLength).isEqualTo("None")
    assertThat(updated.secondLength).isEqualTo("2")
    assertThat(updated.secondLengthUnits).isEqualTo("Months")
    assertThat(updated.requirementType).isEqualTo("Community Order")
    assertThat(updated.requirementLength).isEqualTo("150")
    assertThat(updated.requirementSecondLength).isEqualTo("Hours")
    assertThat(updated.amendmentDetails).isEqualTo("Updated for test")
    assertThat(updated.amendmentReason).isEqualTo("Testing update")
    assertThat(updated.whyInBreach).isEqualTo("No attendance")
    assertThat(updated.stepsToPreventBreach).isEqualTo("Weekly reporting")
    assertThat(updated.complianceODate).isEqualTo("01/01/2025")
    assertThat(updated.riskHistory).isEqualTo("Risk history text")
    assertThat(updated.recommendations).isEqualTo("Recommendations text")
    assertThat(updated.supportingComments).isEqualTo("Supporting comments text")
    assertThat(updated.basicDetailsSaved).isTrue()
    assertThat(updated.confirmEqualities).isTrue()
    assertThat(updated.riskOfHarmChanged).isFalse()
    assertThat(updated.signAndSendSaved).isTrue()
    assertThat(updated.contactSaved).isTrue()
    assertThat(updated.reviewRequiredDate).isEqualTo(nowDateTime)
    assertThat(updated.reviewEvent).isEqualTo("EVENT_MOVE")
    assertThat(updated.createdDatetime).isEqualTo(created.createdDatetime)
    assertThat(updated.createdByUser).isEqualTo(created.createdByUser)
    assertThat(updated.lastUpdatedDatetime).isNotEqualTo(originalLastUpdated)
    assertThat(updated.lastUpdatedUser).isNotBlank()
  }
}
