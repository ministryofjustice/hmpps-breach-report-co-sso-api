package uk.gov.justice.digital.hmpps.breachreportcossoapi.entity

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
import java.time.LocalDateTime
import java.util.UUID

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "cosso_requirement")
data class RequirementEntity(
  @Id
  val id: UUID = UUID.randomUUID(),
  @ManyToOne
  @JoinColumn(name = "cosso_id")
  var cosso: CossoEntity? = null,
  var deliusRequirementId: Long,
  var requirementTypeMainCategoryDescription: String? = null,
  var requirementTypeSubCategoryDescription: String? = null,
  var requirementLength: String? = null,
  var requirementSecondLength: String? = null,
  @CreatedBy
  var createdByUser: String? = null,
  @CreatedDate
  var createdDatetime: LocalDateTime? = null,
  @LastModifiedBy
  var lastUpdatedUser: String? = null,
  @LastModifiedDate
  var lastUpdatedDatetime: LocalDateTime? = null,
  var notes: String? = null,
  var failure: Boolean? = null,
  var failureReason: String? = null,
)
