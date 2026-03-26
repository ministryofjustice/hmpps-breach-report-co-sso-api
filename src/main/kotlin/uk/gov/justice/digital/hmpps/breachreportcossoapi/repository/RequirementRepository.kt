package uk.gov.justice.digital.hmpps.breachreportcossoapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.breachreportcossoapi.entity.RequirementEntity
import java.util.UUID

@Repository
interface RequirementRepository : JpaRepository<RequirementEntity, UUID> {
  fun findAllByCossoId(cossoId: UUID): List<RequirementEntity>
}
