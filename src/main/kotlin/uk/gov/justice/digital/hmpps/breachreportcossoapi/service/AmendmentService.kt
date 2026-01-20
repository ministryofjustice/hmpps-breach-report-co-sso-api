package uk.gov.justice.digital.hmpps.breachreportcossoapi.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.breachreportcossoapi.entity.AmendmentEntity
import uk.gov.justice.digital.hmpps.breachreportcossoapi.exception.NotFoundException
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.Amendment
import uk.gov.justice.digital.hmpps.breachreportcossoapi.repository.AmendmentRepository
import uk.gov.justice.digital.hmpps.breachreportcossoapi.repository.CossoRepository
import java.util.UUID

@Service
class AmendmentService(
  private val amendmentRepository: AmendmentRepository,
  private val cossoRepository: CossoRepository,
) {

  fun createAmendment(model: Amendment): Amendment {
    val entity = model.toEntity()
    return amendmentRepository.save(entity).toModel()
  }

  fun updateAmendment(id: UUID, model: Amendment): Amendment {
    val existing = amendmentRepository.findById(id).orElseThrow {
      NotFoundException("Amendment id $id was not found")
    }

    val updated = model.toEntity(existing)
    return amendmentRepository.save(updated).toModel()
  }

  fun deleteAmendment(id: UUID) {
    if (!amendmentRepository.existsById(id)) {
      throw NotFoundException("Amendment id $id not found")
    }
    amendmentRepository.deleteById(id)
  }

  fun getAmendment(id: UUID): Amendment = amendmentRepository.findById(id).orElseThrow {
    NotFoundException("Amendment id $id not found")
  }.toModel()

  fun AmendmentEntity.toModel(): Amendment = Amendment(
    cossoId = cosso.id,
    amendmentDetails = amendmentDetails,
    amendmentReason = amendmentReason,
    amendmentDate = amendmentDate,
  )

  fun Amendment.toEntity(existing: AmendmentEntity? = null): AmendmentEntity {
    val cossoEntity = cossoId.let {
      cossoRepository.findById(it).orElseThrow {
        IllegalArgumentException("Cosso $it not found")
      }
    }

    return existing?.copy(
      cosso = cossoEntity,
      amendmentDetails = amendmentDetails,
      amendmentReason = amendmentReason,
      amendmentDate = amendmentDate,
    ) ?: AmendmentEntity(
      cosso = cossoEntity,
      amendmentDetails = amendmentDetails,
      amendmentReason = amendmentReason,
      amendmentDate = amendmentDate,
    )
  }
}
