
package com.murat.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.murat.demo.model.User;


public interface UserRepository extends CrudRepository<User, Long> {

	public User findUserByEmail(String email);

	public User findUserByUuid(String uuid);

	@Query(value = "SELECT user.* FROM user "+
		"INNER JOIN activity_company on user.company_id=activity_company.company_id "+
		"WHERE activity_company.activity_id = ?1", nativeQuery = true)
	public List<User> getUsersWhoSeesActivity(long activityId);

	@Query(value = "SELECT user.* FROM user WHERE user.company_id =?1", nativeQuery = true)
	public List<User> findUserListByCompanyId(long companyId);

}
