package guard.passer.core.dto;

import guard.passer.core.database.entity.UserRole;
import guard.passer.core.validation.group.CreateAction;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Value
@FieldNameConstants
public class UserCreateEditDto {
    @Email
    String username;

    @NotBlank(groups = CreateAction.class)
    String rawPassword;

    @PastOrPresent
    LocalDate birthDate;

    @NotNull
    @Size(min = 3, max = 64)
    String firstname;

    @NotNull
    String lastname;
    UserRole role;
    Integer companyId;

    MultipartFile image;
}
