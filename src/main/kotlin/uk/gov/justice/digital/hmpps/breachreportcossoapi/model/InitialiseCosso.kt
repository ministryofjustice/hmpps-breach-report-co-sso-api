package uk.gov.justice.digital.hmpps.breachreportcossoapi.model

import jakarta.validation.constraints.Pattern

data class InitialiseCosso(
  @field:Pattern(regexp = "^[A-Z][0-9]{6}")
  val crn: String,
)
