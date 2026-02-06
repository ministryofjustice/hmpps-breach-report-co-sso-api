package uk.gov.justice.digital.hmpps.breachreportcossoapi.model

import java.time.LocalDateTime
import java.util.UUID

data class Requirement(
  val id: UUID? = null,
  val cossoId: UUID,
  val deliusRequirementId: Long? = null,
  val requirementTypeMainCategoryDescription: String? = null,
  val requirementTypeSubCategoryDescription: String? = null,
  val requirementLength: String? = null,
  val requirementSecondLength: String? = null,
  val createdByUser: String? = null,
  val createdDatetime: LocalDateTime? = null,
  val lastUpdatedUser: String? = null,
  val lastUpdatedDatetime: LocalDateTime? = null,
)
