package uk.gov.justice.digital.hmpps.breachreportcossoapi.model

import java.time.OffsetDateTime
import java.util.UUID

data class Contact(
  val id: UUID? = null,
  var cossoId: UUID? = null,
  var contactTypeDescription: String? = null,
  var contactPerson: String? = null,
  var contactLocationId: UUID? = null,
  var formSent: Boolean? = null,
  var deliusContactId: Long? = null,
  var contactOutcome: String? = null,
  var contactDate: OffsetDateTime? = null,
)
