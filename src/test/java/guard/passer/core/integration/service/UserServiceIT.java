package guard.passer.core.integration.service;

import guard.passer.core.database.entity.UserRole;
import guard.passer.core.dto.UserCreateEditDto;
import guard.passer.core.dto.UserReadDto;
import guard.passer.core.integration.IntegrationTestBase;
import guard.passer.core.service.UserService;
import liquibase.pro.packaged.I;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
public class UserServiceIT extends IntegrationTestBase {

    public static final Long USER_1 = 1L;
    public static final String USERNAME_1 = "ivan@gmail.com";
    public static final Integer COMPANY_1 = 1;

    private final UserService userService;

    @Test
    void findAll() {
        List<UserReadDto> result = userService.findAll();
        assertThat(result).hasSize(5);
    }

    @Test
    void findById(){
        Optional<UserReadDto> user = userService.findById(USER_1);
        assertTrue(user.isPresent());
        user.ifPresent(userReadDto -> assertEquals(USERNAME_1, userReadDto.getUsername()));
    }

    @Test
    void create(){
        UserCreateEditDto userCreateEditDto = new UserCreateEditDto(
                "test@",
                null,
                LocalDate.now(),
                "test",
                "testov",
                UserRole.USER,
                COMPANY_1,
                null
        );
        UserReadDto actualResult = userService.create(userCreateEditDto);
        assertEquals(userCreateEditDto.getUsername(), actualResult.getUsername());
        assertEquals(userCreateEditDto.getFirstname(), actualResult.getFirstname());
        assertEquals(userCreateEditDto.getLastname(), actualResult.getLastname());
        assertEquals(userCreateEditDto.getBirthDate(), actualResult.getBirthDate());
        assertSame(userCreateEditDto.getRole(), actualResult.getRole());
        assertEquals(userCreateEditDto.getCompanyId(), actualResult.getCompany().id());
    }

    @Test
    void update(){
        UserCreateEditDto userCreateEditDto =
                new UserCreateEditDto(
                "test@",
                null,
                LocalDate.now(),
                "test",
                "testov",
                UserRole.USER,
                COMPANY_1,
                null
        );
        Optional<UserReadDto> actualResult = userService.update(USER_1, userCreateEditDto);
        actualResult.ifPresent(
                result -> {
                    assertEquals(userCreateEditDto.getUsername(), result.getUsername());
                    assertEquals(userCreateEditDto.getFirstname(), result.getFirstname());
                    assertEquals(userCreateEditDto.getLastname(), result.getLastname());
                    assertEquals(userCreateEditDto.getBirthDate(), result.getBirthDate());
                    assertSame(userCreateEditDto.getRole(), result.getRole());
                    assertEquals(userCreateEditDto.getCompanyId(), result.getCompany().id());
                }
        );
    }

    @Test
    void delete(){
       assertFalse(userService.delete(-1L));
        assertTrue(userService.delete(USER_1));
    }
}
