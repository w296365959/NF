package com.sscf.investment.socialize.component;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.IComponent;
import com.dengtacj.component.managers.IShareManager;
import com.sscf.investment.socialize.component.manager.ShareManager;

/**
 * Created by davidwei on 2017-09-12.
 */

public class SocialComponent implements IComponent {

    @Override
    public void onCreate() {
        final ComponentManager componentManager = ComponentManager.getInstance();
        componentManager.addManager(IShareManager.class.getName(), new ShareManager());
    }

    @Override
    public void onDestroy() {
        final ComponentManager componentManager = ComponentManager.getInstance();
        componentManager.removeManager(IShareManager.class.getName());
    }
}
