package guard.passer.core.dto;

import guard.passer.core.database.entity.UserRole;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Value
public class UserReadDto {
    Long id;
    String username;
    LocalDate birthDate;
    String firstname;
    String lastname;
    String image;
    UserRole role;
    CompanyReadDto company;
    List<PhoneReadDto> phones;
}

