package guard.passer.core.integration.service;

import guard.passer.core.config.CachingConfig;
import guard.passer.core.database.entity.UserRole;
import guard.passer.core.dto.PhoneReadDto;
import guard.passer.core.dto.UserCreateEditDto;
import guard.passer.core.dto.UserReadDto;
import guard.passer.core.integration.IntegrationTestBase;
import guard.passer.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.cache.CacheManager;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Commit;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void updatePhones() {
        Optional<UserReadDto> user1 = userService.findById(2L);
        Optional<PhoneReadDto> phoneById1 = userService.findPhoneById(3L);
        UserCreateEditDto userCreateEditDto = new UserCreateEditDto(
                "petr@gmail.com",
                null,
                LocalDate.now(),
                "Petr",
                "Petrov",
                UserRole.USER,
                COMPANY_1,
                List.of("+7 777 777 77 74", "+7 777 777 77 75"),
                new MockMultipartFile("test", new byte[0])
        );
        Optional<PhoneReadDto> phoneById2 = userService.findPhoneById(3L);
        Optional<UserReadDto> user2 = userService.update(2L, userCreateEditDto);
        System.out.println();
//        assertEquals(3, userService.findById(2L).get().getPhones().size());
//        assertEquals(3L, userService.findPhoneById(3L).get().getId());
//        assertTrue(userService.deleteAllPhonesByUser(2L));
//        assertTrue(userService.findPhoneById(3L).isEmpty());
//        assertEquals(0, userService.findById(2L).get().getPhones().size());
    }

    @Test
    void deleteAllPhonesByUserId() {
        assertEquals(3, userService.findById(2L).get().getPhones().size());
        assertEquals(3L, userService.findPhoneById(3L).get().getId());
        assertTrue(userService.deleteAllPhonesByUser(2L));
        assertTrue(userService.findPhoneById(3L).isEmpty());
        assertEquals(0, userService.findById(2L).get().getPhones().size());
    }

    @Test
    void deletePhoneById() {
        Optional<UserReadDto> user1 = userService.findById(2L);
        assertEquals(3, user1.get().getPhones().size());
        assertEquals(3L, userService.findPhoneById(3L).get().getId());
        assertTrue(userService.deletePhoneById(3L));
        assertTrue(userService.findPhoneById(3L).isEmpty());
        Optional<UserReadDto> user2 = userService.findById(2L);
        assertEquals(2, user2.get().getPhones().size());
        System.out.println();
    }

    @Test
    void createAndDeleteUserById() {
        UserCreateEditDto userCreateEditDto = new UserCreateEditDto(
                "test1@",
                null,
                LocalDate.now(),
                "test1",
                "testov1",
                UserRole.USER,
                COMPANY_1,
                List.of("+7 777 777 77 71", "+7 777 777 77 72", "+7 777 777 77 73"),
                new MockMultipartFile("test", new byte[0])
        );
        UserReadDto actualResult = userService.create(userCreateEditDto);
        assertEquals(7L, userService.findPhoneById(7L).get().getId());
        assertTrue(userService.delete(actualResult.getId()));
        assertTrue(userService.findPhoneById(7L).isEmpty());
        assertTrue(userService.findById(actualResult.getId()).isEmpty());
    }

    @Test
    void createAndFindById() {
        UserCreateEditDto userCreateEditDto = new UserCreateEditDto(
                "test1@",
                null,
                LocalDate.now(),
                "test1",
                "testov1",
                UserRole.USER,
                COMPANY_1,
                List.of("+7 777 777 77 71", "+7 777 777 77 72", "+7 777 777 77 73"),
                new MockMultipartFile("test", new byte[0])
        );
        UserReadDto actualResult = userService.create(userCreateEditDto);
        Optional<UserReadDto> user1 = userService.findById(actualResult.getId());
        Optional<UserReadDto> user2 = userService.findById(actualResult.getId());
        Optional<PhoneReadDto> phoneById1 = userService.findPhoneById(7L);
        boolean b = userService.deletePhoneById(7L);
        Optional<PhoneReadDto> phoneById2 = userService.findPhoneById(7L);
        Optional<UserReadDto> user3 = userService.findById(actualResult.getId());
        System.out.println();
    }

    @Test
    void findById() {
        Optional<UserReadDto> user = userService.findById(USER_1);
        assertTrue(user.isPresent());
        user.ifPresent(userReadDto -> assertEquals(USERNAME_1, userReadDto.getUsername()));
    }

    @Test
    void create() {
        UserCreateEditDto userCreateEditDto = new UserCreateEditDto(
                "test@",
                null,
                LocalDate.now(),
                "test",
                "testov",
                UserRole.USER,
                COMPANY_1,
                List.of("+7 777 777 77 71", "+7 777 777 77 72", "+7 777 777 77 73"),
                new MockMultipartFile("test", new byte[0])
        );
        UserReadDto actualResult = userService.create(userCreateEditDto);
        assertEquals(userCreateEditDto.getUsername(), actualResult.getUsername());
        assertEquals(userCreateEditDto.getFirstname(), actualResult.getFirstname());
        assertEquals(userCreateEditDto.getLastname(), actualResult.getLastname());
        assertEquals(userCreateEditDto.getBirthDate(), actualResult.getBirthDate());
        assertSame(userCreateEditDto.getRole(), actualResult.getRole());
        assertEquals(userCreateEditDto.getCompanyId(), actualResult.getCompany().id());
        assertEquals(userCreateEditDto.getPhones().size(), actualResult.getPhones().size());
    }

    @Test
    void update() {
        UserCreateEditDto userCreateEditDto =
                new UserCreateEditDto(
                        "test@",
                        null,
                        LocalDate.now(),
                        "test",
                        "testov",
                        UserRole.USER,
                        COMPANY_1,
                        null,
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
    void delete() {
        assertFalse(userService.delete(-1L));
        assertTrue(userService.delete(USER_1));
        System.out.println();
    }
}
