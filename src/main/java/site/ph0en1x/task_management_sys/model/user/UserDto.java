package site.ph0en1x.task_management_sys.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import site.ph0en1x.task_management_sys.web.validation.onCreate;
import site.ph0en1x.task_management_sys.web.validation.onUpdate;

import java.util.Set;

@Data
@NoArgsConstructor
@Schema(description = "User DTO")
public class UserDto {

    @Schema(description = "User ID", example = "3")
    @NotNull(message = "Id must be not null", groups = onUpdate.class)
    private Long id;

    @Schema(description = "User's email. Used as а username", example = "Weasley-junior7@magic-ministry.bmm")
    @NotNull(message = "Username must be not null", groups = {onCreate.class, onUpdate.class})
    @Length(max = 255, message = "Username must be less than 255 symbols", groups = {onCreate.class})
    @NotBlank(message = "Password confirmation must be not blank")
    @Email(message = "Email must be email))")
    private String email;

    @Schema(description = "User's password", example = "!StrongPassword12345%$")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "password name must be not null", groups = {onCreate.class, onUpdate.class})
    @NotBlank(message = "Password must be not blank")
    private String password;

    @Schema(description = "User password confirmation", example = "!StrongPassword12345%$")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "password confirmation must be not null", groups = {onUpdate.class, onCreate.class})
    @NotBlank(message = "Password confirmation must be not blank")
    private String passwordConfirmation;

    @Schema(description = "Name of user", example = "Ronald")
    @NotNull(message = "Name must be not null", groups = {onCreate.class, onUpdate.class})
    @Length(max = 255, message = "Name must be less than 255 symbols",
            groups = {onCreate.class, onUpdate.class} )
    @NotBlank(message = "Firstname must be not blank")
    private String firstName;

    @Schema(description = "User's Last name", example = "Weasley")
    @NotNull(message = "Last name must be not null", groups = {onCreate.class, onUpdate.class})
    @Length(max = 255, message = "Last name must be less than 255 symbols",
            groups = {onCreate.class, onUpdate.class})
    @NotBlank(message = "Lastname must be not blank")
    private String lastName;
}
