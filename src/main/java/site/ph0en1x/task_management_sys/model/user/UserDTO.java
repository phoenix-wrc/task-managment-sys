package site.ph0en1x.task_management_sys.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    private Long id;

    private String email;

    private String password;

    private String passwordConfirmation;

    private String firstName;

    private String lastName;
}
