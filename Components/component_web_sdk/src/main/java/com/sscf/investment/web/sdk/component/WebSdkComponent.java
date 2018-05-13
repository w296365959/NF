package com.sscf.investment.web.sdk.component;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.IComponent;
import com.dengtacj.component.managers.IIntelligentShakeManager;
import com.dengtacj.component.managers.ILocalH5ResourceManager;
import com.dengtacj.component.managers.ISchemeManager;
import com.dengtacj.component.managers.IX5WebViewManager;
import com.sscf.investment.web.sdk.manager.IntelligentShakeManager;
import com.sscf.investment.web.sdk.manager.LocalH5ResourceManager;
import com.sscf.investment.web.sdk.manager.Scheme;
import com.sscf.investment.web.sdk.manager.X5WebViewManager;

/**
 * Created by davidwei on 2017-09-05.
 */
public final class WebSdkComponent implements IComponent {

    @Override
    public void onCreate() {
        final ComponentManager componentManager = ComponentManager.getInstance();
        componentManager.addManager(IX5WebViewManager.class.getName(), new X5WebViewManager());
        componentManager.addManager(ILocalH5ResourceManager.class.getName(), new LocalH5ResourceManager());
        componentManager.addManager(ISchemeManager.class.getName(), new Scheme());
        componentManager.addManager(IIntelligentShakeManager.class.getName(), new IntelligentShakeManager());
    }

    @Override
    public void onDestroy() {
        final ComponentManager componentManager = ComponentManager.getInstance();
        componentManager.removeManager(IX5WebViewManager.class.getName());
        componentManager.removeManager(ILocalH5ResourceManager.class.getName());
        componentManager.removeManager(ISchemeManager.class.getName());
        componentManager.removeManager(IIntelligentShakeManager.class.getName());
    }
}
