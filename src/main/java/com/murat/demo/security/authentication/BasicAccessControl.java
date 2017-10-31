
package com.murat.demo.security.authentication;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;

import com.murat.demo.persistence.UserRepository;
import com.murat.demo.util.PasswordUtil;
import com.murat.demo.util.UserUtil;


/**
 * Default mock implementation of {@link AccessControl}. This implementation
 * accepts any string as a password, and considers the user "admin" as the only
 * administrator.
 */
public class BasicAccessControl implements AccessControl {

	@Autowired
	UserRepository userRepository;

	public BasicAccessControl(UserRepository repo) {
		this.userRepository = repo;

	}

	@Override
	public boolean signIn(String username, String password) {

		String cryptedPassword = PasswordUtil.cryptWithMD5(password);
		User user = (User) UserUtil.setUser(userRepository.findUserByEmail(username));

		if (user == null) {
			return false;
		}
		else if (user.getPassword().equals(cryptedPassword)) {
			CurrentUser.set(username);
			return true;
		}

		return false;
	}

	@Override
	public boolean isUserSignedIn() {

		return !CurrentUser.get().isEmpty();
	}

	@Override
	public boolean isUserInRole(String role) {

		if ("admin".equals(role)) {
			// Only the "admin" user is in the "admin" role
			return getPrincipalName().equals("admin");
		}

		// All users are in all non-admin roles
		return true;
	}

	@Override
	public String getPrincipalName() {

		return CurrentUser.get();
	}

}
