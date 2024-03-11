package guard.passer.core.integration.database.reporitory;

import guard.passer.core.database.entity.Company;
import guard.passer.core.database.reporitory.CompanyRepository;
import guard.passer.core.integration.IntegrationTestBase;
import guard.passer.core.integration.annotation.IT;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
//@Commit //по умолчанию все транзации откатываются
class CompanyRepositoryIT extends IntegrationTestBase {

    public static final Integer APPLE_ID = 4;

    private final EntityManager entityManager;
    private final CompanyRepository companyRepository;

    @Test
    void checkFindByQuery(){
        companyRepository.findByName("Google");
        companyRepository.findAllByNameContainingIgnoreCase("a");
    }

    @Test
    @Disabled
    void delete(){
        Optional<Company> optionalCompany = companyRepository.findById(APPLE_ID);
        assertTrue(optionalCompany.isPresent());
        optionalCompany.ifPresent(company -> companyRepository.delete(company));
        entityManager.flush();
        assertTrue(companyRepository.findById(APPLE_ID).isEmpty());
    }

    @Test
    void findById() {
        Company company = entityManager.find(Company.class, 1);

        assertNotNull(company);
        assertThat(company.getLocales()).hasSize(2);
    }

    @Test
    void save() {
        Company company = Company.builder()
                .name("Apple")
                .locales(Map.of(
                        "ru", "Описание",
                        "en", "description"
                ))
                .build();

        entityManager.persist(company);
        assertNotNull(company.getId());
    }

}