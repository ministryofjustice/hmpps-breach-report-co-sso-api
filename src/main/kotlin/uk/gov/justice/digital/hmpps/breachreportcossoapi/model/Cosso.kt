package uk.gov.justice.digital.hmpps.breachreportcossoapi.model

import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import jakarta.validation.constraints.Pattern
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime

data class Cosso(
  @field:Pattern(regexp = "^[A-Z][0-9]{6}")
  var crn: String,
  var titleAndFullName: String? = null,
  var dateOfForm: LocalDate? = null,
  var sheetSentBy: String? = null,
  var telephoneNumber: String? = null,
  var mobileNumber: String? = null,
  var emailAddress: String? = null,
  var completedDate: ZonedDateTime? = null,
  var postalAddress: Address? = null,
  var dateOfBirth: LocalDateTime? = null,
  var prisonNumber: String? = null,
  var workAddress: Address? = null,
  var probationArea: String? = null,
  var roTitleAndFullName: String? = null,
  var roTelephoneNumber: String? = null,
  var roEmailAddress: String? = null,
  var witnessAvailability: String? = null,
  var mainOffence: String? = null,
  var additionalOffence: String? = null,
  var sentencingCourt: String? = null,
  var sentenceType: String? = null,
  var sentenceLength: String? = null,
  var sentenceDate: LocalDate? = null,
  var lengthUnits: String? = null,
  var suspendedCustodyLength: String? = null,
  var secondLength: String? = null,
  var secondLengthUnits: String? = null,
  var amendmentDetails: String? = null,
  var amendmentReason: String? = null,
  var whyInBreach: String? = null,
  var stepsToPreventBreach: String? = null,
  var complianceToDate: String? = null,
  var riskHistory: String? = null,
  var recommendations: String? = null,
  var supportingComments: String? = null,
  var basicDetailsSaved: Boolean? = null,
  var offenceDetailsSaved: Boolean? = null,
  var failuresAndEnforcementSaved: Boolean? = null,
  var roAndWitnessDetailsSaved: Boolean? = null,
  var complianceToDateSaved: Boolean? = null,
  var confirmEqualities: Boolean? = null,
  var riskOfHarmChanged: Boolean? = null,
  var signAndSendSaved: Boolean? = null,
  var signedByRo: Boolean? = null,
  var signature: String? = null,
  var contactSaved: Boolean? = null,
  var reviewRequiredDate: LocalDateTime? = null,
  var reviewEvent: String? = null,
  var amendments: List<Amendment> = emptyList(),
  @field:JsonSetter(nulls = Nulls.AS_EMPTY)
  var cossoContactList: List<Contact> = emptyList(),
  @field:JsonSetter(nulls = Nulls.AS_EMPTY)
  var requirementList: List<Requirement> = emptyList(),
  @field:JsonSetter(nulls = Nulls.AS_EMPTY)
  var additionalOffenceList: List<Offence> = emptyList(),
)
