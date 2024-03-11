package guard.passer.core.dto;

import java.time.LocalDate;

public record UserFilter(String firstname,
                         String lastname,
                         LocalDate birthDate) {
}
