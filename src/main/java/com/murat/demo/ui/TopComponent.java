
package com.murat.demo.ui;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.addons.searchbox.SearchBox;
import org.vaadin.addons.searchbox.SearchBox.ButtonPosition;
import org.vaadin.alump.labelbutton.LabelButton;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

public class TopComponent extends HorizontalLayout {

	private SearchBox sb;

	public TopComponent() {
		// client = ElasticHelper.getClient();
		sb = new SearchBox("Search", ButtonPosition.RIGHT);

		sb.setWidth(10, Unit.PERCENTAGE);
		sb.setSearchMode(SearchBox.SearchMode.EAGER);
		sb.setDebounceTime(200);
		sb.addStyleName("sb-pspulse");
		addComponent(sb);
		
		sb.getSearchButton().addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {

				((MyUI) UI.getCurrent()).getMainScreen();
				sb.getSearchField().clear();
			}
		});

		// header of the menu
		addStyleName(ValoTheme.MENU_TITLE);
		addStyleName("dockbar");
		addStyleName("dockbar-newcolor");
		LabelButton titleLabel = new LabelButton(
			"PSPulse",
			event -> ((MyUI) UI.getCurrent()).navigateToHomePage());
		titleLabel.addStyleName("labelbutton-position");
		titleLabel.addStyleName("lbl-pspulsetitle");
		Image image = new Image(null, new ThemeResource("img/pulse.ico"));
		image.setStyleName("logo");
		image.setWidth(26, Unit.PIXELS);
		image.setHeight(26, Unit.PIXELS);
		addComponent(image);
		addComponent(titleLabel);

	}

	public SearchBox getSb() {

		return sb;
	}

}
