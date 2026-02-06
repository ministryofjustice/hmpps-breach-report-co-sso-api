package uk.gov.justice.digital.hmpps.breachreportcossoapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.breachreportcossoapi.entity.ScreenInformationEntity
import java.util.*

@Repository
interface ScreenInformationRepository : JpaRepository<ScreenInformationEntity, UUID> {
  fun findAllByscreenName(screenName: String): List<ScreenInformationEntity>
}
