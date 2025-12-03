package uk.gov.justice.digital.hmpps.breachreportcossoapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.breachreportcossoapi.entity.AddressEntity
import java.util.UUID

@Repository
interface AddressRepository : JpaRepository<AddressEntity, UUID>
