package br.com.wkallil.constrollers.docs;

import br.com.wkallil.data.dto.v1.security.AccountCredentialsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface AuthControllerDocs {

    @Operation(summary = "Authenticates a user and returns a token",
            description = "Authenticates a user and return a token using the provided token", tags = {"Auth"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                            schema = @Schema(implementation = AccountCredentialsDTO.class)
                    )),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "No Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<?> signin(AccountCredentialsDTO credentialsDTO);

    @Operation(summary = "Refreshes a token for a given username",
            description = "Refreshes the authentication token using the provided refresh token", tags = {"Auth"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                            schema = @Schema(implementation = AccountCredentialsDTO.class)
                    )),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Forbidden", responseCode = "403", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "No Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<?> refreshToken(
            String username,
            String refreshToken
    );

    @Operation(summary = "Create a new User",
            description = "Adds a new user for authentication", tags = {"Auth"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                            schema = @Schema(implementation = AccountCredentialsDTO.class)
                    )),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "No Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    AccountCredentialsDTO create(AccountCredentialsDTO credentials);
}
