package uk.gov.justice.digital.hmpps.breachreportcossoapi.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.Cosso
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.InitialiseCosso
import uk.gov.justice.digital.hmpps.breachreportcossoapi.service.CossoService
import uk.gov.justice.hmpps.kotlin.common.ErrorResponse
import java.util.UUID

@Validated
@RestController
// TODO: Add this once role added to auth @PreAuthorize("hasRole('ROLE_CO_SSO')")
@RequestMapping(value = ["/cosso"], produces = ["application/json"])
class CossoController(
  private val cossoService: CossoService,
) {
  @GetMapping("/{uuid}")
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
  fun getCossoById(@PathVariable uuid: UUID): Cosso? = cossoService.findCossoById(uuid)

  @PostMapping
  @Operation(
    summary = "Initialises a Cosso record",
    description = "Calls the API to initialise a cosso record",
    security = [SecurityRequirement(name = "co-sso-api-ui-role")],
    responses = [
      ApiResponse(responseCode = "201", description = "cosso record created"),
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
  @ResponseStatus(HttpStatus.CREATED)
  fun initialiseCosso(@Valid @RequestBody initialiseCosso: InitialiseCosso) = cossoService.initialiseCosso(initialiseCosso)

  @PutMapping("/{id}")
  @Operation(
    summary = "Update a Cosso record",
    description = "Calls through the cosso service to update a cosso record",
    security = [SecurityRequirement(name = "co-sso-api-ui-role")],
    responses = [
      ApiResponse(responseCode = "200", description = "Cosso record updated"),
      ApiResponse(
        responseCode = "400",
        description = "cant change the CRN on an update",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
      ),
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
      ApiResponse(
        responseCode = "404",
        description = "The Cosso id was not found",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
      ),
    ],
  )
  fun updateCosso(@PathVariable id: UUID, @RequestBody cosso: Cosso) {
    cossoService.updateCosso(id, cosso)
  }

  @DeleteMapping("/{id}")
  @Operation(
    summary = "Delete a Cosso record",
    description = "Calls through the cosso service to delete a cosso record",
    security = [SecurityRequirement(name = "co-sso-api-ui-role")],
    responses = [
      ApiResponse(responseCode = "200", description = "Cosso record deleted"),
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
      ApiResponse(
        responseCode = "404",
        description = "The Cosso id was not found",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
      ),
    ],
  )
  fun deleteCosso(@PathVariable id: UUID) {
    cossoService.deleteCosso(id)
  }

  @GetMapping("/{uuid}/pdf")
  @Operation(
    summary = "Retrieve a cosso pdf by uuid - cosso id",
    description = "Calls through the cosso service to retrieve a generated pdf",
    security = [SecurityRequirement(name = "co-sso-api-ui-role")],
    responses = [
      ApiResponse(responseCode = "200", description = "cosso pdf returned"),
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
  fun getCossoAsPdf(@PathVariable uuid: UUID): ResponseEntity<ByteArray> {
    var cosso = cossoService.findCossoById(uuid)
    var pdfBytes = cossoService.getCossoAsPdf(uuid, cosso, cosso.completedDate == null)
    var headers = HttpHeaders()
    headers.contentType = MediaType.APPLICATION_PDF
    headers.contentDisposition =
      ContentDisposition.attachment().filename("Breach_report_co_sso_" + cosso?.crn + ".pdf").build()
    return ResponseEntity.ok().headers(headers).body(pdfBytes)
  }
}
