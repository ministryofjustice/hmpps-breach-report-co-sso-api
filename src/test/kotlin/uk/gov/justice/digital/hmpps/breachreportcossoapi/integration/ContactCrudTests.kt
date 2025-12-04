package uk.gov.justice.digital.hmpps.breachreportcossoapi.integration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import uk.gov.justice.digital.hmpps.breachreportcossoapi.entity.AddressEntity
import uk.gov.justice.digital.hmpps.breachreportcossoapi.entity.ContactEntity
import uk.gov.justice.digital.hmpps.breachreportcossoapi.entity.CossoEntity
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.Contact
import uk.gov.justice.digital.hmpps.breachreportcossoapi.repository.AddressRepository
import uk.gov.justice.digital.hmpps.breachreportcossoapi.repository.ContactRepository
import uk.gov.justice.digital.hmpps.breachreportcossoapi.repository.CossoRepository
import java.time.LocalDateTime
import java.util.UUID

class ContactCrudTests : IntegrationTestBase() {

  @Autowired
  private lateinit var cossoRepository: CossoRepository

  @Autowired
  private lateinit var contactRepository: ContactRepository

  @Autowired
  private lateinit var addressRepository: AddressRepository

  private fun createCosso(crn: String = "X100001"): CossoEntity = cossoRepository.save(
    CossoEntity(
      id = UUID.randomUUID(),
      crn = crn,
      createdByUser = "test.user",
      createdDatetime = LocalDateTime.now(),
      lastUpdatedUser = "test.user",
      lastUpdatedDatetime = LocalDateTime.now(),
    ),
  )

  private fun createAddress(): AddressEntity = addressRepository.save(
    AddressEntity(
      id = UUID.randomUUID(),
      addressId = 1001,
      status = "Main",
      buildingName = "Building",
      addressNumber = "1",
      streetName = "Street",
      district = "District",
      townCity = "London",
      county = "County",
      postcode = "AA11AA",
      officeDescription = "Description",
      createdByUser = "test.user",
      createdDatetime = LocalDateTime.now(),
      lastUpdatedUser = "test.user",
      lastUpdatedDatetime = LocalDateTime.now(),
    ),
  )

  @Test
  fun `should create a Contact record`() {
    val cosso = createCosso("X100001")

    val payload = Contact(
      cossoId = cosso.id,
      contactTypeDescription = "Contact Type",
      contactPerson = "Mr John Smith",
      formSent = false,
    )

    webTestClient.post()
      .uri("/cosso/contact")
      .headers(setAuthorisation(roles = listOf("ROLE_COSSO")))
      .bodyValue(payload)
      .exchange()
      .expectStatus().isCreated

    val saved = contactRepository.findAll().first { it.cosso?.id == cosso.id }

    assertThat(saved.id).isNotNull()
    assertThat(saved.cosso?.id).isEqualTo(cosso.id)
    assertThat(saved.contactTypeDescription).isEqualTo("Contact Type")
    assertThat(saved.contactPerson).isEqualTo("Mr John Smith")
    assertThat(saved.formSent).isFalse()
  }

  @Test
  fun `should update a Contact record`() {
    val cosso = createCosso("X100002")

    val existing = contactRepository.save(
      ContactEntity(
        id = UUID.randomUUID(),
        cosso = cosso,
        contactTypeDescription = "Visit",
        contactPerson = "John",
        formSent = false,
        createdByUser = "test.user",
        createdDatetime = LocalDateTime.now(),
        lastUpdatedUser = "test.user",
        lastUpdatedDatetime = LocalDateTime.now(),
      ),
    )

    val updatePayload = Contact(
      cossoId = cosso.id,
      contactTypeDescription = "Updated Visit",
      contactPerson = "Jane Smith",
      formSent = true,
    )

    webTestClient.put()
      .uri("/cosso/contact/${existing.id}")
      .headers(setAuthorisation(roles = listOf("ROLE_COSSO")))
      .bodyValue(updatePayload)
      .exchange()
      .expectStatus().isOk

    val updated = contactRepository.findById(existing.id).orElseThrow()

    assertThat(updated.contactTypeDescription).isEqualTo("Updated Visit")
    assertThat(updated.contactPerson).isEqualTo("Jane Smith")
    assertThat(updated.formSent).isTrue()
  }

  @Test
  fun `should update contact with contact location`() {
    val cosso = createCosso("X100003")
    val address = createAddress()

    val existing = contactRepository.save(
      ContactEntity(
        id = UUID.randomUUID(),
        cosso = cosso,
        contactTypeDescription = "Initial",
        contactPerson = "User",
        formSent = false,
        createdByUser = "test.user",
        createdDatetime = LocalDateTime.now(),
        lastUpdatedUser = "test.user",
        lastUpdatedDatetime = LocalDateTime.now(),
      ),
    )

    val updatePayload = Contact(
      cossoId = cosso.id,
      contactTypeDescription = "Updated Type",
      contactPerson = "New Person",
      contactLocationId = address.id,
      formSent = true,
    )

    webTestClient.put()
      .uri("/cosso/contact/${existing.id}")
      .headers(setAuthorisation(roles = listOf("ROLE_COSSO")))
      .bodyValue(updatePayload)
      .exchange()
      .expectStatus().isOk

    val updated = contactRepository.findById(existing.id).orElseThrow()

    assertThat(updated.contactLocation?.id).isEqualTo(address.id)
    assertThat(updated.contactTypeDescription).isEqualTo("Updated Type")
    assertThat(updated.contactPerson).isEqualTo("New Person")
    assertThat(updated.formSent).isTrue()
  }

  @Test
  fun `should get a Contact record`() {
    val cosso = createCosso("X100004")

    val existing = contactRepository.save(
      ContactEntity(
        id = UUID.randomUUID(),
        cosso = cosso,
        contactTypeDescription = "Review",
        contactPerson = "Officer B",
        formSent = true,
        createdByUser = "test.user",
        createdDatetime = LocalDateTime.now(),
        lastUpdatedUser = "test.user",
        lastUpdatedDatetime = LocalDateTime.now(),
      ),
    )

    webTestClient.get()
      .uri("/cosso/contact/${existing.id}")
      .headers(setAuthorisation(roles = listOf("ROLE_COSSO")))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .jsonPath("$.contactTypeDescription").isEqualTo("Review")
      .jsonPath("$.contactPerson").isEqualTo("Officer B")
      .jsonPath("$.formSent").isEqualTo(true)
  }

  @Test
  fun `should delete a Contact record`() {
    val cosso = createCosso("X100005")

    val contact = contactRepository.save(
      ContactEntity(
        id = UUID.randomUUID(),
        cosso = cosso,
        contactTypeDescription = "Delete Me",
        contactPerson = "User",
        formSent = false,
        createdByUser = "test.user",
        createdDatetime = LocalDateTime.now(),
        lastUpdatedUser = "test.user",
        lastUpdatedDatetime = LocalDateTime.now(),
      ),
    )

    webTestClient.delete()
      .uri("/cosso/contact/${contact.id}")
      .headers(setAuthorisation(roles = listOf("ROLE_COSSO")))
      .exchange()
      .expectStatus().isOk

    assertThat(contactRepository.findById(contact.id)).isEmpty
  }
}
