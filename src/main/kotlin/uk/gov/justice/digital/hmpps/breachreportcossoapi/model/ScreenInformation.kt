package uk.gov.justice.digital.hmpps.breachreportcossoapi.model

import java.util.UUID

data class ScreenInformation(
  val id: UUID? = null,
  val screenName: String?,
  val fieldName: String?,
  val fieldText: String?,
)
