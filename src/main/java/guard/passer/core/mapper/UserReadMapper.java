package guard.passer.core.mapper;

import guard.passer.core.database.entity.User;
import guard.passer.core.dto.CompanyReadDto;
import guard.passer.core.dto.UserReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto>{

    private final CompanyReadMapper companyReadMapper;

    @Override
    public UserReadDto map(User object) {
        CompanyReadDto companyReadDto = Optional.ofNullable(object.getCompany())
                .map(company -> companyReadMapper.map(company))
                .orElse(null);
        return new UserReadDto(
                object.getId(),
                object.getUsername(),
                object.getBirthDate(),
                object.getFirstname(),
                object.getLastname(),
                object.getImage(),
                object.getRole(),
                companyReadDto
        );
    }
}
