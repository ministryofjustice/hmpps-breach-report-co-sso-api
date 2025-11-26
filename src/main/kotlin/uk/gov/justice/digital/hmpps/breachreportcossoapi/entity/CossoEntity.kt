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
import java.time.ZonedDateTime
import java.util.*

@Entity
@Table(name = "cosso")
@EntityListeners(AuditingEntityListener::class)
data class CossoEntity(
  @Id
  val id: UUID = UUID.randomUUID(),
  var crn: String,
  var reviewRequiredDate: LocalDateTime? = null,
  var reviewEvent: String? = null,
  var completedDate: ZonedDateTime? = null,
  @CreatedBy
  var createdByUser: String? = null,
  @CreatedDate
  var createdDatetime: LocalDateTime? = null,
  @LastModifiedDate
  var lastUpdatedDatetime: LocalDateTime? = null,
  @LastModifiedBy
  var lastUpdatedUser: String? = null,
)
