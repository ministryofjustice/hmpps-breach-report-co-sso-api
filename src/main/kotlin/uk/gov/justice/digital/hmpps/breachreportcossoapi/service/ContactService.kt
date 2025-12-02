package uk.gov.justice.digital.hmpps.breachreportcossoapi.service

import org.springframework.stereotype.Service
import uk.gov.justice.digital.hmpps.breachreportcossoapi.entity.ContactEntity
import uk.gov.justice.digital.hmpps.breachreportcossoapi.exception.NotFoundException
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.Contact
import uk.gov.justice.digital.hmpps.breachreportcossoapi.repository.AddressRepository
import uk.gov.justice.digital.hmpps.breachreportcossoapi.repository.ContactRepository
import uk.gov.justice.digital.hmpps.breachreportcossoapi.repository.CossoRepository
import java.util.UUID

@Service
class ContactService(
  private val contactRepository: ContactRepository,
  private val cossoRepository: CossoRepository,
  private val addressRepository: AddressRepository,
) {

  fun createContact(model: Contact): Contact {
    val entity = model.toEntity()
    return contactRepository.save(entity).toModel()
  }

  fun updateContact(id: UUID, model: Contact): Contact {
    val existing = contactRepository.findById(id).orElseThrow {
      NotFoundException("Contact id $id was not found")
    }

    val updated = model.toEntity(existing)
    return contactRepository.save(updated).toModel()
  }

  fun deleteContact(id: UUID) {
    if (!contactRepository.existsById(id)) {
      throw NotFoundException("Contact id $id not found")
    }
    contactRepository.deleteById(id)
  }

  fun getContact(id: UUID): Contact = contactRepository.findById(id).orElseThrow {
    NotFoundException("Contact id $id not found")
  }.toModel()

  fun ContactEntity.toModel(): Contact = Contact(
    cossoId = cosso?.id,
    contactTypeDescription = contactTypeDescription,
    contactPerson = contactPerson,
    contactLocationId = contactLocation?.id,
    formSent = formSent,
  )

  fun Contact.toEntity(existing: ContactEntity? = null): ContactEntity {
    val cossoEntity = cossoId?.let {
      cossoRepository.findById(it).orElseThrow {
        IllegalArgumentException("Cosso $it not found")
      }
    }

    val addressEntity = contactLocationId?.let {
      addressRepository.findById(it).orElse(null)
    }

    return existing?.copy(
      cosso = cossoEntity,
      contactTypeDescription = contactTypeDescription,
      contactPerson = contactPerson,
      contactLocation = addressEntity,
      formSent = formSent,
    ) ?: ContactEntity(
      cosso = cossoEntity,
      contactTypeDescription = contactTypeDescription,
      contactPerson = contactPerson,
      contactLocation = addressEntity,
      formSent = formSent,
    )
  }
}
