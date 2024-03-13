package guard.passer.core.database.reporitory;

import guard.passer.core.database.entity.Company;
import guard.passer.core.database.entity.User;
import guard.passer.core.database.entity.UserRole;
import guard.passer.core.database.pool.ConnectionPool;
import guard.passer.core.dto.PersonalInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends
        JpaRepository<User, Long>,
        FilterUserRepository,
        QuerydslPredicateExecutor<User> {

    @Query("""
            select u from User u
            where u.firstname like %:firstname% and  u.lastname like %:lastname%
            """)
    List<User> findAllBy(String firstname, String lastname);

//    @Query("""
//            select u from User u
//            left join fetch u.phones p
//            where u.id = :id
//            """)
    Optional<User> findById(Long id);

    @Modifying(clearAutomatically = true)
    @Query("""
            update User u
            set u.role = :role
            where u.id in (:ids)
            """)
    int updateRole(UserRole role, Long... ids);

    Optional<User> findFirstByOrderByIdDesc();

    @EntityGraph(attributePaths = {"company", "company.locales"})
    Slice<User> findAllBy(Pageable pageable);

    <T> List<T> findAllByCompanyId(Integer id, Class<T> tClass);

    Optional<User> findByUsername(String username);
}
