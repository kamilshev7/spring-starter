package guard.passer.core.dto;

import guard.passer.core.database.entity.UserRole;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Value
public class PhoneReadDto {
    Long id;
    String phoneNumber;
    Long ownerId;
}

