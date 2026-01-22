package uk.gov.justice.digital.hmpps.breachreportcossoapi.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
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
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.Amendment
import uk.gov.justice.digital.hmpps.breachreportcossoapi.service.AmendmentService
import java.util.UUID

@Validated
@RestController
// TODO: Add this once role added to auth @PreAuthorize("hasRole('ROLE_CO_SSO')")
@RequestMapping(value = ["/cosso/amendment"], produces = ["application/json"])
class AmendmentController(
  private val amendmentService: AmendmentService,
) {
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
    summary = "Create a Amendment record",
    description = "Creates a new amendment for a COSSO record",
    security = [SecurityRequirement(name = "co-sso-api-ui-role")],
    responses = [
      ApiResponse(responseCode = "201", description = "Amendment created"),
      ApiResponse(responseCode = "401", description = "Unauthorized"),
      ApiResponse(responseCode = "403", description = "Forbidden"),
    ],
  )
  fun createAmendment(@RequestBody amendment: Amendment) = amendmentService.createAmendment(amendment)

  @PutMapping("/{id}")
  @Operation(
    summary = "Update a Amendment record",
    description = "Updates an existing amendment linked to a COSSO record",
    security = [SecurityRequirement(name = "co-sso-api-ui-role")],
    responses = [
      ApiResponse(responseCode = "200", description = "Amendment updated"),
      ApiResponse(responseCode = "401", description = "Unauthorized"),
      ApiResponse(responseCode = "403", description = "Forbidden"),
      ApiResponse(responseCode = "404", description = "Amendment not found"),
    ],
  )
  fun updateAmendment(@PathVariable id: UUID, @RequestBody amendment: Amendment) = amendmentService.updateAmendment(id, amendment)

  @DeleteMapping("/{id}")
  @Operation(
    summary = "Delete a Amendment record",
    description = "Deletes a amendment from a COSSO record",
    security = [SecurityRequirement(name = "co-sso-api-ui-role")],
    responses = [
      ApiResponse(responseCode = "200", description = "Amendment deleted"),
      ApiResponse(responseCode = "401", description = "Unauthorized"),
      ApiResponse(responseCode = "403", description = "Forbidden"),
      ApiResponse(responseCode = "404", description = "Amendment not found"),
    ],
  )
  fun deleteAmendment(@PathVariable id: UUID) {
    amendmentService.deleteAmendment(id)
  }

  @GetMapping("/{id}")
  @Operation(
    summary = "Get a Amendment record",
    description = "Returns the specified amendment for a COSSO record",
    security = [SecurityRequirement(name = "co-sso-api-ui-role")],
    responses = [
      ApiResponse(responseCode = "200", description = "Amendment returned"),
      ApiResponse(responseCode = "401", description = "Unauthorized"),
      ApiResponse(responseCode = "403", description = "Forbidden"),
      ApiResponse(responseCode = "404", description = "Amendment not found"),
    ],
  )
  fun getAmendment(@PathVariable id: UUID) = amendmentService.getAmendment(id)
}
