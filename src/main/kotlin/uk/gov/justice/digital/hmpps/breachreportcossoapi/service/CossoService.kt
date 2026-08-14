package uk.gov.justice.digital.hmpps.breachreportcossoapi.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.breachreportcossoapi.entity.AddressEntity
import uk.gov.justice.digital.hmpps.breachreportcossoapi.entity.AmendmentEntity
import uk.gov.justice.digital.hmpps.breachreportcossoapi.entity.ContactEntity
import uk.gov.justice.digital.hmpps.breachreportcossoapi.entity.CossoEntity
import uk.gov.justice.digital.hmpps.breachreportcossoapi.entity.OffenceEntity
import uk.gov.justice.digital.hmpps.breachreportcossoapi.entity.RequirementEntity
import uk.gov.justice.digital.hmpps.breachreportcossoapi.enums.ReviewEventType
import uk.gov.justice.digital.hmpps.breachreportcossoapi.exception.NotFoundException
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.Address
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.Amendment
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.Contact
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.Cosso
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.CreateResponse
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.InitialiseCosso
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.Offence
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.Requirement
import uk.gov.justice.digital.hmpps.breachreportcossoapi.repository.AddressRepository
import uk.gov.justice.digital.hmpps.breachreportcossoapi.repository.CossoRepository
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

@Service
class CossoService(
  val cossoRepository: CossoRepository,
  val pdfGenerationService: PdfGenerationService,
  val addressRepository: AddressRepository,
  @Value("\${frontend.url}") val frontendUrl: String,
) {

  @Transactional
  fun initialiseCosso(initialiseCosso: InitialiseCosso) = cossoRepository.save(
    CossoEntity(crn = initialiseCosso.crn),
  ).id.let {
    CreateResponse(it, "$frontendUrl/basic-details/$it")
  }

  fun findCossoById(id: UUID): Cosso {
    val cossoEntity: CossoEntity = cossoRepository.findByIdOrNull(id) ?: throw NotFoundException(
      "CossoEntity",
      "id",
      id,
    )
    return cossoEntity.toModel()
  }

  @Transactional
  fun updateCosso(id: UUID, cosso: Cosso): Cosso {
    val cossoEntity: CossoEntity =
      cossoRepository.findByIdOrNull(id) ?: throw NotFoundException("CossoEntity", "id", id)
    return cossoRepository.save(cosso.toEntity(cossoEntity)).toModel()
  }

  @Transactional
  fun deleteCosso(id: UUID): String {
    if (!cossoRepository.existsById(id)) {
      throw NotFoundException("CossoEntity", "id", id)
    }
    val crn = findCossoById(id).crn
    cossoRepository.deleteById(id)
    return crn
  }

  fun getCossoAsPdf(id: UUID, cosso: Cosso?, draft: Boolean): ByteArray? {
    val html = pdfGenerationService.generateHtml(cosso)

    var pdfBytes = pdfGenerationService.generatePdf(html)

    if (draft) {
      pdfBytes = pdfGenerationService.addWatermark(pdfBytes)
    }

    return pdfBytes
  }

  private fun Cosso.toEntity(existingEntity: CossoEntity? = null) = existingEntity?.copy(
    crn = crn,
    titleAndFullName = titleAndFullName,
    dateOfForm = dateOfForm,
    sheetSentBy = sheetSentBy,
    telephoneNumber = telephoneNumber,
    mobileNumber = mobileNumber,
    emailAddress = emailAddress,
    completedDate = completedDate,
    postalAddress = postalAddress?.toEntity(),
    dateOfBirth = dateOfBirth,
    prisonNumber = prisonNumber,
    workAddress = workAddress?.toEntity(),
    probationArea = probationArea,
    roTitleAndFullName = roTitleAndFullName,
    roTelephoneNumber = roTelephoneNumber,
    roEmailAddress = roEmailAddress,
    witnessAvailability = witnessAvailability,
    mainOffence = mainOffence,
    additionalOffence = additionalOffence,
    sentencingCourt = sentencingCourt,
    sentenceType = sentenceType,
    sentenceLength = sentenceLength,
    sentenceDate = sentenceDate,
    lengthUnits = lengthUnits,
    suspendedCustodyLength = suspendedCustodyLength,
    secondLength = secondLength,
    secondLengthUnits = secondLengthUnits,
    amendmentDetails = amendmentDetails,
    amendmentReason = amendmentReason,
    whyInBreach = whyInBreach,
    stepsToPreventBreach = stepsToPreventBreach,
    complianceToDate = complianceToDate,
    riskHistory = riskHistory,
    recommendations = recommendations,
    supportingComments = supportingComments,
    basicDetailsSaved = basicDetailsSaved,
    offenceDetailsSaved = offenceDetailsSaved,
    failuresAndEnforcementSaved = failuresAndEnforcementSaved,
    roAndWitnessDetailsSaved = roAndWitnessDetailsSaved,
    complianceToDateSaved = complianceToDateSaved,
    confirmEqualities = confirmEqualities,
    riskOfHarmChanged = riskOfHarmChanged,
    signAndSendSaved = signAndSendSaved,
    signedByRo = signedByRo,
    signature = signature,
    contactSaved = contactSaved,
    reviewRequiredDate = reviewRequiredDate,
    reviewEvent = reviewEvent,
    terminated = terminated,
    terminatedUnterminatedDate = terminatedUnterminatedDate,
    cossoRequirementList = requirementList.map {
      it.toEntity(
        existingEntity.cossoRequirementList.find { existingRequirementEntity ->
          existingRequirementEntity.id == it.id
        },
      )
    } as MutableList<RequirementEntity>,
    cossoContactList = cossoContactList.map {
      it.toEntity(
        existingEntity.cossoContactList.find { existingContactEntity ->
          existingContactEntity.id == it.id
        },
      )
    } as MutableList<ContactEntity>,
  )?.also { cosso ->
    cosso.cossoContactList.forEach { it.cosso = cosso }
  } ?: CossoEntity(
    crn = crn,
    titleAndFullName = titleAndFullName,
    dateOfForm = dateOfForm,
    sheetSentBy = sheetSentBy,
    telephoneNumber = telephoneNumber,
    mobileNumber = mobileNumber,
    emailAddress = emailAddress,
    completedDate = completedDate,
    postalAddress = postalAddress?.toEntity(),
    dateOfBirth = dateOfBirth,
    prisonNumber = prisonNumber,
    workAddress = workAddress?.toEntity(),
    probationArea = probationArea,
    roTitleAndFullName = roTitleAndFullName,
    roTelephoneNumber = roTelephoneNumber,
    roEmailAddress = roEmailAddress,
    witnessAvailability = witnessAvailability,
    mainOffence = mainOffence,
    additionalOffence = additionalOffence,
    sentencingCourt = sentencingCourt,
    sentenceType = sentenceType,
    sentenceLength = sentenceLength,
    sentenceDate = sentenceDate,
    lengthUnits = lengthUnits,
    suspendedCustodyLength = suspendedCustodyLength,
    secondLength = secondLength,
    secondLengthUnits = secondLengthUnits,
    amendmentDetails = amendmentDetails,
    amendmentReason = amendmentReason,
    whyInBreach = whyInBreach,
    stepsToPreventBreach = stepsToPreventBreach,
    complianceToDate = complianceToDate,
    riskHistory = riskHistory,
    recommendations = recommendations,
    supportingComments = supportingComments,
    basicDetailsSaved = basicDetailsSaved,
    offenceDetailsSaved = offenceDetailsSaved,
    failuresAndEnforcementSaved = failuresAndEnforcementSaved,
    roAndWitnessDetailsSaved = roAndWitnessDetailsSaved,
    complianceToDateSaved = complianceToDateSaved,
    confirmEqualities = confirmEqualities,
    riskOfHarmChanged = riskOfHarmChanged,
    signAndSendSaved = signAndSendSaved,
    signedByRo = signedByRo,
    signature = signature,
    contactSaved = contactSaved,
    reviewRequiredDate = reviewRequiredDate,
    reviewEvent = reviewEvent,
    terminated = terminated,
    terminatedUnterminatedDate = terminatedUnterminatedDate,
    cossoContactList = cossoContactList.map { it.toEntity() } as MutableList<ContactEntity>,
  )

  fun CossoEntity.toModel(): Cosso = Cosso(
    crn = crn,
    titleAndFullName = titleAndFullName,
    dateOfForm = dateOfForm,
    sheetSentBy = sheetSentBy,
    telephoneNumber = telephoneNumber,
    mobileNumber = mobileNumber,
    emailAddress = emailAddress,
    completedDate = completedDate,
    postalAddress = postalAddress?.toModel(),
    dateOfBirth = dateOfBirth,
    prisonNumber = prisonNumber,
    workAddress = workAddress?.toModel(),
    probationArea = probationArea,
    roTitleAndFullName = roTitleAndFullName,
    roTelephoneNumber = roTelephoneNumber,
    roEmailAddress = roEmailAddress,
    witnessAvailability = witnessAvailability,
    mainOffence = mainOffence,
    additionalOffence = additionalOffence,
    sentencingCourt = sentencingCourt,
    sentenceType = sentenceType,
    sentenceLength = sentenceLength,
    sentenceDate = sentenceDate,
    lengthUnits = lengthUnits,
    suspendedCustodyLength = suspendedCustodyLength,
    secondLength = secondLength,
    secondLengthUnits = secondLengthUnits,
    amendmentDetails = amendmentDetails,
    amendmentReason = amendmentReason,
    whyInBreach = whyInBreach,
    stepsToPreventBreach = stepsToPreventBreach,
    complianceToDate = complianceToDate,
    riskHistory = riskHistory,
    recommendations = recommendations,
    supportingComments = supportingComments,
    basicDetailsSaved = basicDetailsSaved,
    offenceDetailsSaved = offenceDetailsSaved,
    failuresAndEnforcementSaved = failuresAndEnforcementSaved,
    roAndWitnessDetailsSaved = roAndWitnessDetailsSaved,
    complianceToDateSaved = complianceToDateSaved,
    confirmEqualities = confirmEqualities,
    riskOfHarmChanged = riskOfHarmChanged,
    signAndSendSaved = signAndSendSaved,
    signedByRo = signedByRo,
    signature = signature,
    contactSaved = contactSaved,
    reviewRequiredDate = reviewRequiredDate,
    reviewEvent = reviewEvent,
    terminated = terminated,
    terminatedUnterminatedDate = terminatedUnterminatedDate,
    amendments = amendments.map { it.toModel() },
    cossoContactList = cossoContactList.sortedByDescending { it.contactDate }.map { it.toModel() },
    requirementList = cossoRequirementList.map { it.toModel() },
    additionalOffenceList = cossoOffenceList.map { it.toModel() },
  )

  private fun Address.toEntity(existingEntity: AddressEntity? = null) = existingEntity?.copy(
    addressId = addressId,
    status = status,
    buildingName = buildingName,
    addressNumber = addressNumber,
    streetName = streetName,
    district = district,
    townCity = townCity,
    county = county,
    postcode = postcode,
    officeDescription = officeDescription,
  ) ?: AddressEntity(
    addressId = addressId,
    status = status,
    buildingName = buildingName,
    addressNumber = addressNumber,
    streetName = streetName,
    district = district,
    townCity = townCity,
    county = county,
    postcode = postcode,
    officeDescription = officeDescription,
  )

  private fun AddressEntity.toModel() = Address(
    id = id,
    addressId = addressId,
    status = status,
    buildingName = buildingName,
    addressNumber = addressNumber,
    streetName = streetName,
    district = district,
    townCity = townCity,
    county = county,
    postcode = postcode,
    officeDescription = officeDescription,
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
      deliusContactId = deliusContactId,
    ) ?: ContactEntity(
      cosso = cossoEntity,
      contactTypeDescription = contactTypeDescription,
      contactPerson = contactPerson,
      contactLocation = addressEntity,
      formSent = formSent,
      deliusContactId = deliusContactId,
    )
  }

  private fun AmendmentEntity.toModel() = Amendment(
    id = id,
    cossoId = cosso.id,
    amendmentDetails = amendmentDetails,
    amendmentReason = amendmentReason,
    amendmentDate = amendmentDate,
  )

  fun getActiveCossoForCrn(crn: String?): Collection<CossoEntity> = cossoRepository.findByCrnAndCompletedDateIsNull(crn)

  fun updateCossoCrn(cosso: CossoEntity, crn: String) {
    cosso.crn = crn
    cossoRepository.save(cosso)
  }

  fun updateReviewEvent(eventType: ReviewEventType, cosso: CossoEntity, occurredAt: ZonedDateTime) {
    cosso.reviewEvent = eventType.name
    cosso.reviewRequiredDate = occurredAt.withZoneSameInstant(ZoneId.of("Europe/London")).toLocalDateTime()
    cossoRepository.save(cosso)
  }

  fun updateTerminatedStatus(newStatus: Boolean, cossoId: String, occurredAt: ZonedDateTime) {
    val cosso = cossoRepository.findById(UUID.fromString(cossoId)).orElseThrow { IllegalArgumentException("Cosso not found") }
    cosso.terminated = newStatus
    cosso.terminatedUnterminatedDate = occurredAt.toLocalDateTime()
    cossoRepository.save(cosso)
  }

  fun deleteAllByCrn(crn: String) {
    cossoRepository.deleteByCrn(crn)
  }

  private fun ContactEntity.toModel() = Contact(
    cossoId = this.cosso?.id,
    contactTypeDescription = this.contactTypeDescription,
    contactPerson = this.contactPerson,
    contactOutcome = this.contactOutcome,
    contactDate = this.contactDate,
    id = this.id,
    contactLocationId = this.contactLocation?.id,
    formSent = this.formSent,
    deliusContactId = this.deliusContactId,
  )

  private fun OffenceEntity.toModel() = Offence(
    id = this.id,
    cossoId = this.cosso.id,
    description = this.description,
    code = this.code,
  )

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
