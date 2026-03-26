package uk.gov.justice.digital.hmpps.breachreportcossoapi.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
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
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.Requirement
import uk.gov.justice.digital.hmpps.breachreportcossoapi.service.RequirementService
import java.util.UUID

@Validated
@RestController
@PreAuthorize("hasRole('ROLE_COSSO')")
@RequestMapping(value = ["/cosso/requirement"], produces = ["application/json"])
class RequirementController(
  private val requirementService: RequirementService,
) {
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
    summary = "Create a requirement record",
    description = "Creates a new requirement for a COSSO record",
    security = [SecurityRequirement(name = "co-sso-api-ui-role")],
    responses = [
      ApiResponse(responseCode = "201", description = "Requirement created"),
      ApiResponse(responseCode = "401", description = "Unauthorized"),
      ApiResponse(responseCode = "403", description = "Forbidden"),
    ],
  )
  fun createRequirement(@RequestBody requirement: Requirement) = requirementService.createRequirement(requirement)

  @PutMapping("/{id}")
  @Operation(
    summary = "Update a Requirement record",
    description = "Updates an existing requirement linked to a COSSO record",
    security = [SecurityRequirement(name = "co-sso-api-ui-role")],
    responses = [
      ApiResponse(responseCode = "200", description = "requirement updated"),
      ApiResponse(responseCode = "401", description = "Unauthorized"),
      ApiResponse(responseCode = "403", description = "Forbidden"),
      ApiResponse(responseCode = "404", description = "requirement not found"),
    ],
  )
  fun updateRequirement(@PathVariable id: UUID, @RequestBody requirement: Requirement) = requirementService.updateRequirement(id, requirement)

  @DeleteMapping("/{id}")
  @Operation(
    summary = "Delete a requirement record",
    description = "Deletes a requirement from a COSSO record",
    security = [SecurityRequirement(name = "co-sso-api-ui-role")],
    responses = [
      ApiResponse(responseCode = "200", description = "requirement deleted"),
      ApiResponse(responseCode = "401", description = "Unauthorized"),
      ApiResponse(responseCode = "403", description = "Forbidden"),
      ApiResponse(responseCode = "404", description = "requirement not found"),
    ],
  )
  fun deleteRequirement(@PathVariable id: UUID) {
    requirementService.deleteRequirement(id)
  }

  @GetMapping("/{id}")
  @Operation(
    summary = "Get a requirement record",
    description = "Returns the specified requirement for a COSSO record",
    security = [SecurityRequirement(name = "co-sso-api-ui-role")],
    responses = [
      ApiResponse(responseCode = "200", description = "requirement returned"),
      ApiResponse(responseCode = "401", description = "Unauthorized"),
      ApiResponse(responseCode = "403", description = "Forbidden"),
      ApiResponse(responseCode = "404", description = "requirement not found"),
    ],
  )
  fun getRequirement(@PathVariable id: UUID) = requirementService.getRequirement(id)

  @GetMapping("/bycossoid/{cossoId}")
  @Operation(
    summary = "Retrieve a Breach Notice requirement",
    description = "Calls through the breach notice service to retrieve a list of breach notice requirements using breach notice id",
    security = [SecurityRequirement(name = "breach-notice-api-ui-role")],
    responses = [
      ApiResponse(responseCode = "200", description = "requirements returned"),
      ApiResponse(responseCode = "401", description = "Unauthorized"),
      ApiResponse(responseCode = "403", description = "Forbidden"),
      ApiResponse(responseCode = "404", description = "CossoId not found"),
    ],
  )
  fun getCossoRequirements(@PathVariable cossoId: UUID): List<Requirement> = requirementService.fetchCossoRequirements(cossoId)
}
