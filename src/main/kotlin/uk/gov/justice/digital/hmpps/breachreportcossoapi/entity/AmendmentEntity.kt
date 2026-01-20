package uk.gov.justice.digital.hmpps.breachreportcossoapi.entity

import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "amendment")
@EntityListeners(AuditingEntityListener::class)
data class AmendmentEntity(
  @Id
  val id: UUID = UUID.randomUUID(),
  @ManyToOne
  @JoinColumn(name = "cosso_id")
  var cosso: CossoEntity,
  var amendmentDetails: String? = null,
  var amendmentReason: String? = null,
  var amendmentDate: LocalDate? = null,
  @CreatedBy
  var createdByUser: String? = null,
  @CreatedDate
  var createdDatetime: LocalDateTime? = null,
  @LastModifiedBy
  var lastUpdatedUser: String? = null,
  @LastModifiedDate
  var lastUpdatedDatetime: LocalDateTime? = null,
)
