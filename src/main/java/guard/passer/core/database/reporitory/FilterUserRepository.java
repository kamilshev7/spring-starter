package guard.passer.core.database.reporitory;

import guard.passer.core.database.entity.User;
import guard.passer.core.database.entity.UserRole;
import guard.passer.core.dto.PersonalInfo;
import guard.passer.core.dto.UserFilter;

import java.util.List;

public interface FilterUserRepository {

    List<User> findAllByFilter(UserFilter filter);

    List<PersonalInfo> findAllByCompanyIdAndRole(Integer companyId, UserRole role);

    void updateCompanyAndRole(List<User> users);

    void updateCompanyAndRoleNamed(List<User> users);
}
