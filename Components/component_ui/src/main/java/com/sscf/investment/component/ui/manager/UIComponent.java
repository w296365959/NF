package com.sscf.investment.component.ui.manager;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.IComponent;
import com.dengtacj.component.managers.IThemeManager;

/**
 * Created by davidwei on 2017-09-05.
 */

public final class UIComponent implements IComponent {

    @Override
    public void onCreate() {
        final ComponentManager componentManager = ComponentManager.getInstance();
        componentManager.addManager(IThemeManager.class.getName(), new ThemeManager());
    }

    @Override
    public void onDestroy() {
        final ComponentManager componentManager = ComponentManager.getInstance();
        componentManager.removeManager(IThemeManager.class.getName());
    }
}
