package uk.gov.justice.digital.hmpps.breachreportcossoapi.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.UUID

@Entity
@Table(name = "cosso")
@EntityListeners(AuditingEntityListener::class)
data class CossoEntity(
  @Id
  val id: UUID = UUID.randomUUID(),
  var crn: String,
  var titleAndFullName: String? = null,
  var dateOfForm: LocalDate? = null,
  var sheetSentBy: String? = null,
  var telephoneNumber: String? = null,
  var mobileNumber: String? = null,
  var emailAddress: String? = null,
  var completedDate: ZonedDateTime? = null,
  @ManyToOne
  @JoinColumn(name = "postal_address_id")
  var postalAddress: AddressEntity? = null,
  var dateOfBirth: LocalDateTime? = null,
  var prisonNumber: String? = null,
  @ManyToOne
  @JoinColumn(name = "work_address_id")
  var workAddress: AddressEntity? = null,
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
  @Column(name = "compliance_o_date")
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
  @CreatedBy
  var createdByUser: String? = null,
  @CreatedDate
  var createdDatetime: LocalDateTime? = null,
  @LastModifiedBy
  var lastUpdatedUser: String? = null,
  @LastModifiedDate
  var lastUpdatedDatetime: LocalDateTime? = null,
)
