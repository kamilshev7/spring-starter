package guard.passer.core.mapper;

import guard.passer.core.database.entity.Company;
import guard.passer.core.database.entity.Phone;
import guard.passer.core.dto.CompanyReadDto;
import guard.passer.core.dto.PhoneReadDto;
import org.springframework.stereotype.Component;

@Component
public class PhoneReadMapper implements Mapper<Phone, PhoneReadDto>{
    @Override
    public PhoneReadDto map(Phone object) {
        return new PhoneReadDto(object.getId(), object.getPhoneNumber(), object.getOwner().getId());
    }
}
