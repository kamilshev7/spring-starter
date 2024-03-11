package guard.passer.core.integration.database.reporitory;

import guard.passer.core.database.entity.Company;
import guard.passer.core.database.entity.User;
import guard.passer.core.database.entity.UserRole;
import guard.passer.core.database.reporitory.CompanyRepository;
import guard.passer.core.database.reporitory.UserRepository;
import guard.passer.core.dto.PersonalInfo;
import guard.passer.core.integration.IntegrationTestBase;
import guard.passer.core.integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class UserRepositoryIT extends IntegrationTestBase {

    private final UserRepository userRepository;

    @Test
    void checkAudit(){
        User user = userRepository.findById(1L).get();
        user.setBirthDate(user.getBirthDate().plusYears(1L));
        userRepository.flush();
        System.out.println();
    }

    @Test
    void checkProjections(){
        List<PersonalInfo> list = userRepository.findAllByCompanyId(1, PersonalInfo.class);
        assertThat(list).hasSize(2);
    }

    @Test
    void checkPageable(){
        PageRequest pageable = PageRequest.of(1, 2, Sort.by("id"));
        Slice<User> users = userRepository.findAllBy(pageable);
        users.forEach(user -> System.out.println(user.getCompany().getName()));

        while(users.hasNext()){
            users = userRepository.findAllBy(users.nextPageable());
            users.forEach(user -> System.out.println(user.getCompany().getName()));
        }
    }
    
    @Test
    void findFirstTop(){
        Optional<User> firstUser = userRepository.findFirstByOrderByIdDesc();
        assertTrue(firstUser.isPresent());
        firstUser.ifPresent(user -> assertEquals(5L, user.getId()));
    }

    @Test
    void checkUpdateRole(){
        User ivan = userRepository.getById(1L);
        assertEquals(ivan.getRole(), UserRole.ADMIN);

        int updateRole = userRepository.updateRole(UserRole.USER, 1L, 5L);
        assertEquals(2, updateRole);

        User sameIvan = userRepository.getById(1L);
        assertEquals(sameIvan.getRole(), UserRole.USER);
    }


    @Test
    void checkQueries(){
        List<User> users = userRepository.findAllBy("a", "ov");
        assertThat(users).hasSize(3);
    }

}