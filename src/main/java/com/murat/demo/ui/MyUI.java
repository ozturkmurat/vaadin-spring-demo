
package com.murat.demo.ui;

import javax.servlet.annotation.WebServlet;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.murat.demo.model.User;
import com.murat.demo.persistence.UserRepository;
import com.murat.demo.security.authentication.AccessControl;
import com.murat.demo.security.authentication.BasicAccessControl;
import com.murat.demo.ui.LoginScreen.LoginListener;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

@Push
@SpringUI
public class MyUI extends UI {

	
	@Autowired
	public UserRepository userRepository;

	private MainScreen mainScreen;

	private User user;

	final static Logger logger = Logger.getLogger(MyUI.class);

	@Autowired
	public MyUI(UserRepository repo) {
		this.userRepository = userRepository;		
	}

	@Override
	protected void init(VaadinRequest request) {

		Responsive.makeResponsive(this);
		getPage().setTitle("Vaadin Spring Demo");
		setLocale(request.getLocale());

		checkAccess();

	}

	private void checkAccess() {

		LoginListener listener = new LoginListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void loginSuccessful() {

				showMainScreen();
			}
		};
		AccessControl accessControl = new BasicAccessControl(userRepository);
		if (!accessControl.isUserSignedIn()) {
			setContent(new LoginScreen(accessControl, listener));
		} else {
			setUser(userRepository.findUserByEmail(accessControl.getPrincipalName()));
			showMainScreen();
		}
	}

	public void showMainScreen() {

		addStyleName(ValoTheme.UI_WITH_MENU);
		mainScreen = new MainScreen(MyUI.this);
		setContent(mainScreen);
		navigateToHomePage();

	}

	public MainScreen getMainScreen() {

		return mainScreen;
	}

	public void navigateToHomePage() {

		getNavigator().navigateTo("Home");
	}

	public User getUser() {

		// TODO Auto-generated method stub
		return user;
	}

	public User setUser(User user) {

		// TODO Auto-generated method stub
		this.user = user;
		return user;
	}

	@WebServlet(urlPatterns = "/", name = "PSPulseUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class PSPulseUIServlet extends VaadinServlet {
	}
}
