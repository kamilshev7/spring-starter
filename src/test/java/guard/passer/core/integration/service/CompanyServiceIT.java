package guard.passer.core.integration.service;

import guard.passer.core.config.DatabaseProperties;
import guard.passer.core.dto.CompanyReadDto;
import guard.passer.core.integration.annotation.IT;
import guard.passer.core.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
@RequiredArgsConstructor
class CompanyServiceIT {

    private final static Integer COMPANY_ID = 1;

    private final CompanyService companyService;
    private final DatabaseProperties databaseProperties;

    @Test
    void findById() {
        Optional<CompanyReadDto> actualResult = companyService.findById(COMPANY_ID);

        assertTrue(actualResult.isPresent());

        CompanyReadDto expectedResult = new CompanyReadDto(COMPANY_ID, null);
        actualResult.ifPresent(actual -> Assertions.assertEquals(expectedResult, actual));
    }
}