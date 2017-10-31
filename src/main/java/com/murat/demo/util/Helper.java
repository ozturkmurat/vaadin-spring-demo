
package com.murat.demo.util;

import com.murat.demo.ui.MyUI;
import com.murat.demo.ui.TopComponent;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.UI;

public class Helper {

	public Navigator getNavigator() {

		return ((MyUI) UI.getCurrent()).getNavigator();
	}

	public TopComponent getDockbarLayout() {

		return ((MyUI) UI.getCurrent()).getMainScreen().getTopComponent();
	}


}
