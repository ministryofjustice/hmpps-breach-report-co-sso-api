package uk.gov.justice.digital.hmpps.breachreportcossoapi.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "screen_information")
data class ScreenInformationEntity(
  @Id
  val id: UUID = UUID.randomUUID(),
  var screenName: String? = null,
  var fieldName: String? = null,
  var fieldText: String? = null,
)
