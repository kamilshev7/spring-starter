package guard.passer.core.mapper;

import guard.passer.core.database.entity.User;
import guard.passer.core.dto.CompanyReadDto;
import guard.passer.core.dto.PhoneReadDto;
import guard.passer.core.dto.UserReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto>{

    private final CompanyReadMapper companyReadMapper;
    private final PhoneReadMapper phoneReadMapper;

    @Override
    public UserReadDto map(User object) {
        CompanyReadDto companyReadDto = Optional.ofNullable(object.getCompany())
                .map(company -> companyReadMapper.map(company))
                .orElse(null);
        List<PhoneReadDto> phoneReadDtos = Optional.ofNullable(object.getPhones())
                .map(phones -> phones.stream()
                        .map(phone -> phoneReadMapper.map(phone)).collect(Collectors.toList())
                ).orElse(null);
        return new UserReadDto(
                object.getId(),
                object.getUsername(),
                object.getBirthDate(),
                object.getFirstname(),
                object.getLastname(),
                object.getImage(),
                object.getRole(),
                companyReadDto,
                phoneReadDtos
        );
    }
}
