package uk.gov.justice.digital.hmpps.breachreportcossoapi.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.breachreportcossoapi.entity.RequirementEntity
import uk.gov.justice.digital.hmpps.breachreportcossoapi.exception.NotFoundException
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.Requirement
import uk.gov.justice.digital.hmpps.breachreportcossoapi.repository.CossoRepository
import uk.gov.justice.digital.hmpps.breachreportcossoapi.repository.RequirementRepository
import java.util.UUID

@Service
class RequirementService(
  private val requirementRepository: RequirementRepository,
  private val cossoRepository: CossoRepository,
) {

  fun createRequirement(model: Requirement): Requirement {
    val entity = model.toEntity()
    return requirementRepository.save(entity).toModel()
  }

  fun updateRequirement(id: UUID, model: Requirement): Requirement {
    val existing = requirementRepository.findById(id).orElseThrow {
      NotFoundException("Requirement id $id was not found")
    }

    val updated = model.toEntity(existing)
    return requirementRepository.save(updated).toModel()
  }

  fun deleteRequirement(id: UUID) {
    if (!requirementRepository.existsById(id)) {
      throw NotFoundException("Requirement id $id not found")
    }
    requirementRepository.deleteById(id)
  }

  fun getRequirement(id: UUID): Requirement = requirementRepository.findById(id).orElseThrow {
    NotFoundException("Requirement id $id not found")
  }.toModel()

  fun fetchCossoRequirements(id: UUID): List<Requirement> = requirementRepository.findAllByCossoId(id).map { it.toModel() }

  fun RequirementEntity.toModel(): Requirement = Requirement(
    id = id,
    cossoId = cosso?.id,
    deliusRequirementId = deliusRequirementId,
    requirementTypeMainCategoryDescription = requirementTypeMainCategoryDescription,
    requirementTypeSubCategoryDescription = requirementTypeSubCategoryDescription,
    requirementLength = requirementLength,
    requirementSecondLength = requirementSecondLength,
    notes = notes,
    failure = failure,
    failureReason = failureReason,
    createdByUser = createdByUser,
    createdDatetime = createdDatetime,
    lastUpdatedUser = lastUpdatedUser,
    lastUpdatedDatetime = lastUpdatedDatetime,
  )

  fun Requirement.toEntity(existing: RequirementEntity? = null): RequirementEntity {
    val cossoEntity = cossoId?.let {
      cossoRepository.findById(it).orElseThrow {
        IllegalArgumentException("Cosso $it not found")
      }
    }

    return existing?.copy(
      cosso = cossoEntity,
      deliusRequirementId = deliusRequirementId ?: existing.deliusRequirementId,
      requirementTypeMainCategoryDescription = requirementTypeMainCategoryDescription,
      requirementTypeSubCategoryDescription = requirementTypeSubCategoryDescription,
      requirementLength = requirementLength,
      requirementSecondLength = requirementSecondLength,
      notes = notes,
      failure = failure,
      failureReason = failureReason,
    ) ?: RequirementEntity(
      id = id ?: UUID.randomUUID(),
      cosso = cossoEntity,
      deliusRequirementId = deliusRequirementId ?: 0L,
      requirementTypeMainCategoryDescription = requirementTypeMainCategoryDescription,
      requirementTypeSubCategoryDescription = requirementTypeSubCategoryDescription,
      requirementLength = requirementLength,
      requirementSecondLength = requirementSecondLength,
      notes = notes,
      failure = failure,
      failureReason = failureReason,
    )
  }
}
