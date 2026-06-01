package uk.gov.justice.digital.hmpps.breachreportcossoapi.integration

import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.breachreportcossoapi.entity.ScreenInformationEntity
import uk.gov.justice.digital.hmpps.breachreportcossoapi.repository.ScreenInformationRepository
import java.util.UUID

class ReferenceDataCrudTests : IntegrationTestBase() {

  @Autowired
  private lateinit var screenInformationRepository: ScreenInformationRepository

  private fun createScreenInfo(screenName: String, fldVal: String, fldTxt: String): ScreenInformationEntity = screenInformationRepository.save(
    ScreenInformationEntity(
      id = UUID.randomUUID(),
      screenName = screenName,
      fieldName = fldVal,
      fieldText = fldTxt,
    ),
  )

  // Should fetch screen information by screen name
  @Test
  fun `should create a Contact record`() {
    createScreenInfo("TestScreenA", fldVal = "ValueOne", fldTxt = "TextOne")
    createScreenInfo("TestScreenB", fldVal = "ValueTwo", fldTxt = "TextTwo")
    createScreenInfo("TestScreenA", fldVal = "ValueThree", fldTxt = "TextThree")

    webTestClient.get()
      .uri("/cosso/referencedata/screeninformation/TestScreenA")
      .headers(setAuthorisation(roles = listOf("ROLE_BREACH__CO_SSO__RW")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .jsonPath("$.length()").isEqualTo(2)
      .jsonPath("$.[0].screenName").value(containsString("TestScreenA"))
      .jsonPath("$.[0].fieldName").value(containsString("ValueOne"))
      .jsonPath("$.[0].fieldText").value(containsString("TextOne"))
      .jsonPath("$.[1].screenName").value(containsString("TestScreenA"))
      .jsonPath("$.[1].fieldName").value(containsString("ValueThree"))
      .jsonPath("$.[1].fieldText").value(containsString("TextThree"))
  }
}
