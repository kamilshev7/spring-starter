package guard.passer.core.database.reporitory;

import guard.passer.core.database.entity.Phone;
import guard.passer.core.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface PhoneRepository extends JpaRepository<Phone, Long> {


    @Modifying(clearAutomatically = true)
    void deleteById(Long id);

    @Modifying(clearAutomatically = true)
    void deleteAllByOwner(User owner);
}
