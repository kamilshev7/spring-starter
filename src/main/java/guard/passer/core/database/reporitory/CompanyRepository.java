package guard.passer.core.database.reporitory;

import guard.passer.core.database.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

//@Repository("Company Repository") // можно и не ставить вовсе
public interface CompanyRepository extends JpaRepository<Company, Integer> {

//    @Query(name = "Company.findByName") // 2
    @Query("""   
            select c from  Company c
            join fetch c.locales cl
            where c.name = :name1
            """) // 1 priority
    Optional<Company> findByName(String name1); // 3

    List<Company> findAllByNameContainingIgnoreCase(String fragment);
}
