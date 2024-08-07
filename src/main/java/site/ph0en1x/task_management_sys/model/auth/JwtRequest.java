package site.ph0en1x.task_management_sys.model.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request for login")
public class JwtRequest {
    @Schema(description = "Email", example = "Weasley-junior@magic-ministry.bmm")
    @NotNull(message = "Username must be not null") @NotBlank
    private String username;

    @Schema(description = "Password", example = "!StrongPassword12345%$")
    @NotNull(message = "Password must be not null")  @NotBlank
    private String password;
}
