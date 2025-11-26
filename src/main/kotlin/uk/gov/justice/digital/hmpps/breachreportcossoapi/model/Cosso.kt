package uk.gov.justice.digital.hmpps.breachreportcossoapi.model

import jakarta.validation.constraints.Pattern
import java.time.LocalDateTime
import java.time.ZonedDateTime

data class Cosso(
  @field:Pattern(regexp = "^[A-Z][0-9]{6}") var crn: String,
  var reviewRequiredDate: LocalDateTime? = null,
  var reviewEvent: String? = null,
  var completedDate: ZonedDateTime? = null,
)
