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
import uk.gov.justice.digital.hmpps.breachreportcossoapi.model.Contact
import uk.gov.justice.digital.hmpps.breachreportcossoapi.service.ContactService
import java.util.UUID

@Validated
@RestController
@PreAuthorize("hasRole('ROLE_COSSO')")
@RequestMapping(value = ["/cosso/contact"], produces = ["application/json"])
class ContactController(
  private val contactService: ContactService,
) {
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
    summary = "Create a Contact record",
    description = "Creates a new contact for a COSSO record",
    security = [SecurityRequirement(name = "co-sso-api-ui-role")],
    responses = [
      ApiResponse(responseCode = "201", description = "Contact created"),
      ApiResponse(responseCode = "401", description = "Unauthorized"),
      ApiResponse(responseCode = "403", description = "Forbidden"),
    ],
  )
  fun createContact(@RequestBody contact: Contact) = contactService.createContact(contact)

  @PutMapping("/{id}")
  @Operation(
    summary = "Update a Contact record",
    description = "Updates an existing contact linked to a COSSO record",
    security = [SecurityRequirement(name = "co-sso-api-ui-role")],
    responses = [
      ApiResponse(responseCode = "200", description = "Contact updated"),
      ApiResponse(responseCode = "401", description = "Unauthorized"),
      ApiResponse(responseCode = "403", description = "Forbidden"),
      ApiResponse(responseCode = "404", description = "Contact not found"),
    ],
  )
  fun updateContact(@PathVariable id: UUID, @RequestBody contact: Contact) = contactService.updateContact(id, contact)

  @DeleteMapping("/{id}")
  @Operation(
    summary = "Delete a Contact record",
    description = "Deletes a contact from a COSSO record",
    security = [SecurityRequirement(name = "co-sso-api-ui-role")],
    responses = [
      ApiResponse(responseCode = "200", description = "Contact deleted"),
      ApiResponse(responseCode = "401", description = "Unauthorized"),
      ApiResponse(responseCode = "403", description = "Forbidden"),
      ApiResponse(responseCode = "404", description = "Contact not found"),
    ],
  )
  fun deleteContact(@PathVariable id: UUID) {
    contactService.deleteContact(id)
  }

  @GetMapping("/{id}")
  @Operation(
    summary = "Get a Contact record",
    description = "Returns the specified contact for a COSSO record",
    security = [SecurityRequirement(name = "co-sso-api-ui-role")],
    responses = [
      ApiResponse(responseCode = "200", description = "Contact returned"),
      ApiResponse(responseCode = "401", description = "Unauthorized"),
      ApiResponse(responseCode = "403", description = "Forbidden"),
      ApiResponse(responseCode = "404", description = "Contact not found"),
    ],
  )
  fun getContact(@PathVariable id: UUID) = contactService.getContact(id)
}
