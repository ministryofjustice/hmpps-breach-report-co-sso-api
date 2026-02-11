package uk.gov.justice.digital.hmpps.breachreportcossoapi.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.breachreportcossoapi.entity.ScreenInformationEntity
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.ScreenInformation
import uk.gov.justice.digital.hmpps.breachreportcossoapi.repository.ScreenInformationRepository

@Service
class ReferenceDataService(
  private val screenInformationRepository: ScreenInformationRepository,
) {

  fun getScreenInformationByScreenName(screenName: String): List<ScreenInformation> = screenInformationRepository.findAllByscreenName(screenName).map { it.toModel() }

  fun ScreenInformationEntity.toModel(): ScreenInformation = ScreenInformation(
    screenName = screenName,
    fieldName = fieldName,
    fieldText = fieldText,
  )
}
