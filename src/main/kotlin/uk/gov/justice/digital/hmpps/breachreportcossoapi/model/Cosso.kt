package uk.gov.justice.digital.hmpps.breachreportcossoapi.model

import jakarta.validation.constraints.Pattern
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.UUID

data class Cosso(
  @field:Pattern(regexp = "^[A-Z][0-9]{6}")
  var crn: String,
  var titleAndFullName: String? = null,
  var dateOfForm: LocalDateTime? = null,
  var sheetSentBy: String? = null,
  var telephoneNumber: String? = null,
  var mobileNumber: String? = null,
  var emailAddress: String? = null,
  var completedDate: ZonedDateTime? = null,
  var postalAddressId: UUID? = null,
  var dateOfBirth: LocalDateTime? = null,
  var prisonNumber: String? = null,
  var workAddressId: UUID? = null,
  var probationArea: String? = null,
  var witnessAvailability: String? = null,
  var mainOffence: String? = null,
  var additionalOffence: String? = null,
  var sentencingCourt: String? = null,
  var sentenceType: String? = null,
  var sentenceLength: String? = null,
  var lengthUnits: String? = null,
  var suspendedCustodyLength: String? = null,
  var secondLength: String? = null,
  var secondLengthUnits: String? = null,
  var requirementType: String? = null,
  var requirementLength: String? = null,
  var requirementSecondLength: String? = null,
  var amendmentDetails: String? = null,
  var amendmentReason: String? = null,
  var whyInBreach: String? = null,
  var stepsToPreventBreach: String? = null,
  var complianceODate: String? = null,
  var riskHistory: String? = null,
  var recommendations: String? = null,
  var supportingComments: String? = null,
  var basicDetailsSaved: Boolean? = null,
  var confirmEqualities: Boolean? = null,
  var riskOfHarmChanged: Boolean? = null,
  var signAndSendSaved: Boolean? = null,
  var contactSaved: Boolean? = null,
  var reviewRequiredDate: LocalDateTime? = null,
  var reviewEvent: String? = null,
)
