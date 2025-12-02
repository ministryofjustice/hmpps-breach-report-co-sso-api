package uk.gov.justice.digital.hmpps.breachreportcossoapi.model

import java.util.UUID

data class Contact(
  var cossoId: UUID? = null,
  var contactTypeDescription: String? = null,
  var contactPerson: String? = null,
  var contactLocationId: UUID? = null,
  var formSent: Boolean? = null,
)
