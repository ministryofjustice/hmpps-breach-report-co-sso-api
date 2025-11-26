package uk.gov.justice.digital.hmpps.breachreportcossoapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.breachreportcossoapi.entity.CossoEntity
import java.util.*

@Repository
interface CossoRepository : JpaRepository<CossoEntity, UUID> {
  fun findByCrn(crn: String): List<CossoEntity>
  fun deleteByCrn(crn: String)
  fun findByCrnAndCompletedDateIsNull(crn: String?): List<CossoEntity>
}
