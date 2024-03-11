package guard.passer.core.mapper;

import guard.passer.core.database.entity.Company;
import guard.passer.core.database.entity.User;
import guard.passer.core.database.reporitory.CompanyRepository;
import guard.passer.core.dto.UserCreateEditDto;
import guard.passer.core.dto.UserReadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class UserCreateEditMapper implements Mapper<UserCreateEditDto, User> {

    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User map(UserCreateEditDto userDto) {
        User user = new User();
        copy(userDto, user);
        return user;
    }

    @Override
    public User map(UserCreateEditDto userDto, User user) {
        copy(userDto, user);
        return user;
    }

    private void copy(UserCreateEditDto userDto, User user) {
        user.setUsername(userDto.getUsername());
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setBirthDate(userDto.getBirthDate());
        user.setRole(userDto.getRole());
        user.setCompany(getCompany(userDto.getCompanyId()));

        Optional.ofNullable(userDto.getRawPassword())
                .filter(rawPassword -> StringUtils.hasText(rawPassword))
                .map(rawPassword -> passwordEncoder.encode(rawPassword))
                .ifPresent(password -> user.setPassword(password));

        Optional.ofNullable(userDto.getImage())
                .filter(Predicate.not(multipartFile -> multipartFile.isEmpty()))
                .ifPresent(multipartFile -> user.setImage(multipartFile.getOriginalFilename()));
    }


    private Company getCompany(Integer companyId) {
        return Optional.ofNullable(companyId)
                .flatMap(integer -> companyRepository.findById(integer))
                .orElse(null);
    }
}

















