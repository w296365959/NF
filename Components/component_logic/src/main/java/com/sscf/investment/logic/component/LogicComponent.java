package com.sscf.investment.logic.component;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.IComponent;
import com.dengtacj.component.managers.IFavorManager;
import com.dengtacj.component.managers.IMarketManager;
import com.dengtacj.component.managers.IMarketWarningManager;
import com.dengtacj.component.managers.IPortfolioDataManager;
import com.dengtacj.component.managers.IQuoteManager;
import com.dengtacj.component.managers.IScreenShotManager;
import com.dengtacj.component.managers.ISmartStareManager;
import com.dengtacj.component.managers.IVideoFavorManager;
import com.dengtacj.component.managers.IVideoManager;
import com.sscf.investment.logic.component.manager.FavorManager;
import com.sscf.investment.logic.component.manager.MarketManager;
import com.sscf.investment.logic.component.manager.MarketWarningManager;
import com.sscf.investment.logic.component.manager.PortfolioDataManager;
import com.sscf.investment.logic.component.manager.QuoteManager;
import com.sscf.investment.logic.component.manager.ScreenShotManager;
import com.sscf.investment.logic.component.manager.SmartStareManager;

/**
 * Created by davidwei on 2017-09-05.
 */
public final class LogicComponent implements IComponent {

    @Override
    public void onCreate() {
        final ComponentManager componentManager = ComponentManager.getInstance();
        componentManager.addManager(IPortfolioDataManager.class.getName(), new PortfolioDataManager());
        componentManager.addManager(IQuoteManager.class.getName(), new QuoteManager());
        componentManager.addManager(IMarketManager.class.getName(), new MarketManager());
        componentManager.addManager(IFavorManager.class.getName(), new FavorManager());
        componentManager.addManager(ISmartStareManager.class.getName(), new SmartStareManager());
        componentManager.addManager(IScreenShotManager.class.getName(), new ScreenShotManager());
        componentManager.addManager(IMarketWarningManager.class.getName(), new MarketWarningManager());
    }

    @Override
    public void onDestroy() {
        final ComponentManager componentManager = ComponentManager.getInstance();
        componentManager.removeManager(IPortfolioDataManager.class.getName());
        componentManager.removeManager(IQuoteManager.class.getName());
        componentManager.removeManager(IMarketManager.class.getName());
        componentManager.removeManager(IFavorManager.class.getName());
        componentManager.removeManager(IVideoFavorManager.class.getName());
        componentManager.removeManager(IVideoManager.class.getName());
        componentManager.removeManager(ISmartStareManager.class.getName());
        componentManager.removeManager(IScreenShotManager.class.getName());
        componentManager.removeManager(IMarketWarningManager.class.getName());
    }
}
