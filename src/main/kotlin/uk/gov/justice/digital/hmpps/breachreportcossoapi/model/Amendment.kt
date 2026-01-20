package uk.gov.justice.digital.hmpps.breachreportcossoapi.model

import java.time.LocalDate
import java.util.UUID

data class Amendment(
  val id: UUID? = null,
  var cossoId: UUID,
  val amendmentDetails: String?,
  val amendmentReason: String?,
  val amendmentDate: LocalDate?,
)
