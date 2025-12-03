package uk.gov.justice.digital.hmpps.breachreportcossoapi.model

import java.util.UUID

data class Address(
  val id: UUID? = null,
  val addressId: Long?,
  val status: String?,
  val buildingName: String?,
  val addressNumber: String?,
  val streetName: String?,
  val district: String?,
  val townCity: String?,
  val county: String?,
  val postcode: String?,
  val officeDescription: String?,
)
