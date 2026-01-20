package uk.gov.justice.digital.hmpps.breachreportcossoapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.breachreportcossoapi.entity.AmendmentEntity
import java.util.UUID

@Repository
interface AmendmentRepository : JpaRepository<AmendmentEntity, UUID> {
  fun findAllByCossoId(cossoId: UUID): List<AmendmentEntity>
}
