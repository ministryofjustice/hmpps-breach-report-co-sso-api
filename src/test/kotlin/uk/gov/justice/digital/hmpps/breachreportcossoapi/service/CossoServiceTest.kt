package uk.gov.justice.digital.hmpps.breachreportcossoapi.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import uk.gov.justice.digital.hmpps.breachreportcossoapi.entity.CossoEntity
import uk.gov.justice.digital.hmpps.breachreportcossoapi.enums.ReviewEventType
import uk.gov.justice.digital.hmpps.breachreportcossoapi.repository.AddressRepository
import uk.gov.justice.digital.hmpps.breachreportcossoapi.repository.CossoRepository
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

class CossoServiceTest {

  private val cossoRepository: CossoRepository = mock()
  private val pdfGenerationService: PdfGenerationService = mock()
  private val addressRepository: AddressRepository = mock()

  private lateinit var cossoService: CossoService

  @BeforeEach
  fun setUp() {
    cossoService = CossoService(
      cossoRepository = cossoRepository,
      pdfGenerationService = pdfGenerationService,
      addressRepository = addressRepository,
      frontendUrl = "http://localhost:3000",
    )
  }

  @Test
  fun `updateReviewEvent stores reviewRequiredDate in Europe-London time during BST (summer)`() {
    // 13:00 UTC on a BST date (July) = 14:00 BST
    val occurredAtUtc = ZonedDateTime.of(2026, 7, 9, 13, 0, 0, 0, ZoneOffset.UTC)
    val cosso = CossoEntity(crn = "X000001")

    cossoService.updateReviewEvent(ReviewEventType.MERGE, cosso, occurredAtUtc)

    assertThat(cosso.reviewRequiredDate).isEqualTo(
      ZonedDateTime.of(2026, 7, 9, 14, 0, 0, 0, ZoneId.of("Europe/London")),
    )
    assertThat(cosso.reviewEvent).isEqualTo(ReviewEventType.MERGE.name)
    verify(cossoRepository).save(cosso)
  }

  @Test
  fun `updateReviewEvent stores reviewRequiredDate in Europe-London time during GMT (winter)`() {
    // 13:00 UTC on a GMT date (January) = 13:00 GMT (no offset)
    val occurredAtUtc = ZonedDateTime.of(2026, 1, 15, 13, 0, 0, 0, ZoneOffset.UTC)
    val cosso = CossoEntity(crn = "X000002")

    cossoService.updateReviewEvent(ReviewEventType.UNMERGE, cosso, occurredAtUtc)

    assertThat(cosso.reviewRequiredDate).isEqualTo(
      ZonedDateTime.of(2026, 1, 15, 13, 0, 0, 0, ZoneId.of("Europe/London")),
    )
    assertThat(cosso.reviewEvent).isEqualTo(ReviewEventType.UNMERGE.name)
    verify(cossoRepository).save(cosso)
  }

  @Test
  fun `updateReviewEvent stores correct reviewEvent type`() {
    val occurredAtUtc = ZonedDateTime.of(2026, 7, 9, 10, 30, 0, 0, ZoneOffset.UTC)
    val cosso = CossoEntity(crn = "X000003")

    cossoService.updateReviewEvent(ReviewEventType.EVENT_MOVE, cosso, occurredAtUtc)

    assertThat(cosso.reviewEvent).isEqualTo("EVENT_MOVE")
  }
}
