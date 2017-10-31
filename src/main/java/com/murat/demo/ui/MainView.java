
package com.murat.demo.ui;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class MainView extends Panel implements View {

	public static final String VIEW_NAME = "Home";


	public MainView() {
		build();
		populate();
	}

	private void build() {

		this.addStyleName("layout-activityflow");
		setHeight(650, Unit.PIXELS);

		VerticalLayout flowLayout = new VerticalLayout();
		flowLayout.addStyleName("deneme2");
		
		CssLayout layoutDeneme = new CssLayout();
		layoutDeneme.addComponent(flowLayout);
		layoutDeneme.setWidth(100,Unit.PERCENTAGE);
		
		setContent(layoutDeneme);
	}

	public void populate() {

		
	}


	@Override
	public void enter(ViewChangeEvent event) {

	}

}
