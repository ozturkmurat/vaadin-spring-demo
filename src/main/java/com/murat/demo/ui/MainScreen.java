
package com.murat.demo.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.murat.demo.model.User;
import com.murat.demo.persistence.UserRepository;
import com.murat.demo.util.UserUtil;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * Content of the UI when the user is logged in.
 */
@SpringUI
public class MainScreen extends CssLayout {

	@Autowired
	public UserRepository userRepository;
	
	public Menu menu;
	private TopComponent topComponent;
	private MainView mainView;


	private User user;
	private UI ui;

	public MainScreen(MyUI ui) {
		user = UserUtil.getUser();

		this.userRepository = ui.userRepository;
		this.ui = ui;

		build();

	}

	private void build() {

		addStyleName("pspulse-aligncenter");
		setSizeFull();
		topComponent = new TopComponent();

		addComponent(topComponent);
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth(100,Unit.PERCENTAGE);
		layout.setHeight(100,Unit.PERCENTAGE);

		CssLayout viewContainer = new CssLayout();
		viewContainer.setWidth(100,Unit.PERCENTAGE);
		viewContainer.setHeight(100,Unit.PERCENTAGE);

		final Navigator navigator = new Navigator(ui, viewContainer);
		menu = new Menu(navigator) {

			@Override
			public void populateHome() {

				Button btnHome = menu.getViewButtons().get("Home");
				long count = -1;
				btnHome.setData(count);
				mainView.populate();
			}

			@Override
			public void populateNotification() {
			}
		};

		mainView = new MainView();
		menu.addView(mainView, MainView.VIEW_NAME, MainView.VIEW_NAME, FontAwesome.HOME);


		menu.setActiveView("Home");
		navigator.addViewChangeListener(viewChangeListener);

		VerticalLayout layoutLeftContainer = new VerticalLayout();

		layoutLeftContainer.addComponent(menu);
		layout.addComponent(layoutLeftContainer);
		layoutLeftContainer.setHeight(100, Unit.PERCENTAGE);
		layoutLeftContainer.addLayoutClickListener(new LayoutClickListener() {

			@Override
			public void layoutClick(LayoutClickEvent event) {


			}
		});
		layout.addComponent(viewContainer);

		layout.setHeight(100, Unit.PERCENTAGE);

		CssLayout layoutContent = new CssLayout();
		layoutContent.addComponent(layout);
		layoutContent.addStyleName("layout-pspulsecontent");
		addComponent(layoutContent);

	}

	// notify the view menu about view changes so that it can display which view
	// is currently active
	ViewChangeListener viewChangeListener = new ViewChangeListener() {

		@Override
		public boolean beforeViewChange(ViewChangeEvent event) {

			return true;
		}

		@Override
		public void afterViewChange(ViewChangeEvent event) {

			menu.setActiveView(event.getViewName());

		}

	};

	public TopComponent getTopComponent() {

		return topComponent;
	}

	public Menu getMenu() {

		return menu;
	}

	public MainView getMainView() {

		return mainView;
	}

}
