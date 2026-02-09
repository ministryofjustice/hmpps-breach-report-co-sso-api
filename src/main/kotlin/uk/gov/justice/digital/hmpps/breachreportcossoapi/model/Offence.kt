package uk.gov.justice.digital.hmpps.breachreportcossoapi.model

import java.util.UUID

data class Offence(
  var id: UUID? = null,
  var cossoId: UUID,
  var description: String? = null,
  var code: String? = null,
)
