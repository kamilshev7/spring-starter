package guard.passer.core.mapper;

import guard.passer.core.database.entity.Company;
import guard.passer.core.dto.CompanyReadDto;
import org.springframework.stereotype.Component;

@Component
public class CompanyReadMapper implements Mapper<Company, CompanyReadDto>{
    @Override
    public CompanyReadDto map(Company object) {
        return new CompanyReadDto(object.getId(), object.getName());
    }
}
