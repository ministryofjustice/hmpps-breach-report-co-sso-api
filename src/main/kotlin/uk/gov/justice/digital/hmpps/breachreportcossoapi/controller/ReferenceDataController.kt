package uk.gov.justice.digital.hmpps.breachreportcossoapi.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.breachreportcossoapi.service.ReferenceDataService
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse

@Validated
@RestController
@PreAuthorize("hasRole('ROLE_BREACH__CO_SSO__RW')")
@RequestMapping(value = ["/cosso/referencedata"], produces = ["application/json"])
class ReferenceDataController(
  private val referenceDataService: ReferenceDataService,
) {
  @GetMapping("screeninformation/{screenTitle}")
  @Operation(
    summary = "Retrieve a cosso record by uuid",
    description = "Calls through the co sso service to retrieve co sso record",
    security = [SecurityRequirement(name = "co-sso-api-ui-role")],
    responses = [
      ApiResponse(responseCode = "200", description = "cosso record returned"),
      ApiResponse(
        responseCode = "401",
        description = "Unauthorized to access this endpoint",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "403",
        description = "Forbidden to access this endpoint",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
      ),
    ],
  )
  fun getScreenInformationForScreen(@PathVariable screenTitle: String) = referenceDataService.getScreenInformationByScreenName(screenTitle)
}
