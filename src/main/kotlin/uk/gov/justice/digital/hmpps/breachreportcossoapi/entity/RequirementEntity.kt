package uk.gov.justice.digital.hmpps.breachreportcossoapi.entity

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "cosso_requirement")
data class RequirementEntity(
  @Id
  val id: UUID = UUID.randomUUID(),
  @ManyToOne
  @JoinColumn(name = "cosso_id")
  var cosso: CossoEntity,
  var deliusRequirementId: Long,
  var requirementTypeMainCategoryDescription: String? = null,
  var requirementTypeSubCategoryDescription: String? = null,
  var requirementLength: Long? = null,
  var requirementSecondLength: Long? = null,
  @CreatedBy
  var createdByUser: String? = null,
  @CreatedDate
  var createdDatetime: LocalDateTime? = null,
  @LastModifiedBy
  var lastUpdatedUser: String? = null,
  @LastModifiedDate
  var lastUpdatedDatetime: LocalDateTime? = null
)
