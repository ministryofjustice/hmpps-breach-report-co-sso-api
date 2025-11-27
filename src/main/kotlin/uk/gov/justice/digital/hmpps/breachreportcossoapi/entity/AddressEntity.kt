package uk.gov.justice.digital.hmpps.breachreportcossoapi.entity

import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "address")
@EntityListeners(AuditingEntityListener::class)
data class AddressEntity(
  @Id
  val id: UUID = UUID.randomUUID(),
  var addressId: Long? = null,
  var status: String? = null,
  var buildingName: String? = null,
  var addressNumber: String? = null,
  var streetName: String? = null,
  var district: String? = null,
  var townCity: String? = null,
  var county: String? = null,
  var postcode: String? = null,
  var officeDescription: String? = null,
  @CreatedBy
  var createdByUser: String? = null,
  @CreatedDate
  var createdDatetime: LocalDateTime? = null,
  @LastModifiedBy
  var lastUpdatedUser: String? = null,
  @LastModifiedDate
  var lastUpdatedDatetime: LocalDateTime? = null,
)
