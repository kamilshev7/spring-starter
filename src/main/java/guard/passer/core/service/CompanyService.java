package guard.passer.core.service;

import guard.passer.core.database.reporitory.CompanyRepository;
import guard.passer.core.dto.CompanyReadDto;
import guard.passer.core.listener.entity.AccessType;
import guard.passer.core.listener.entity.EntityEvent;
import guard.passer.core.mapper.CompanyReadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CompanyReadMapper companyReadMapper;

    public Optional<CompanyReadDto> findById(Integer id){
        return companyRepository.findById(id)
                .map(company -> {
                    eventPublisher.publishEvent(new EntityEvent(company, AccessType.READ));
                    return companyReadMapper.map(company);
                });
    }

    public List<CompanyReadDto> findAll(){
        return companyRepository.findAll().stream()
                .map(company -> companyReadMapper.map(company))
                .collect(Collectors.toList());
    }
}
