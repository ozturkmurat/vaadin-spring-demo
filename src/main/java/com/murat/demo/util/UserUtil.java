package com.murat.demo.util;

import com.murat.demo.model.User;
import com.murat.demo.ui.MyUI;
import com.vaadin.ui.UI;

public class UserUtil {

	public static User setUser(User user) {
		return ((MyUI) UI.getCurrent()).setUser(user);
	}

	public static User getUser() {
		return ((MyUI) UI.getCurrent()).getUser();
	}

	public static long getUserId() {
		return getUser().getId();
	}

}
