package com.sscf.investment.main;

import com.dengtacj.component.ComponentManager;
import com.dengtacj.component.IComponent;
import com.dengtacj.component.managers.IAccountManager;
import com.dengtacj.component.managers.IBonusPointManager;
import com.dengtacj.component.managers.IDataCacheManager;
import com.dengtacj.component.managers.IFeedRequestManager;
import com.dengtacj.component.managers.IOrderManager;
import com.dengtacj.component.managers.IRedDotManager;
import com.dengtacj.component.managers.IScanManager;
import com.dengtacj.component.managers.ITradingStateManager;
import com.sscf.investment.scan.ScanManager;

/**
 * Created by davidwei on 2017-09-05.
 */
public final class DengtaComponent implements IComponent {

    @Override
    public void onCreate() {
        final ComponentManager componentManager = ComponentManager.getInstance();
        final DengtaApplication dengtaApplication = DengtaApplication.getApplication();
        componentManager.addManager(IAccountManager.class.getName(), dengtaApplication.getAccountManager());
        componentManager.addManager(IDataCacheManager.class.getName(), dengtaApplication.getDataCacheManager());
        componentManager.addManager(ITradingStateManager.class.getName(), dengtaApplication.getTradingStateManager());
        componentManager.addManager(IBonusPointManager.class.getName(), dengtaApplication.getBonusPointManager());
        componentManager.addManager(IFeedRequestManager.class.getName(), dengtaApplication.getFeedRequestManager());
        componentManager.addManager(IRedDotManager.class.getName(), dengtaApplication.getRedDotManager());
        componentManager.addManager(IOrderManager.class.getName(), dengtaApplication.getOrderManager());
        componentManager.addManager(IScanManager.class.getName(), new ScanManager());
    }

    @Override
    public void onDestroy() {
        final ComponentManager componentManager = ComponentManager.getInstance();
        componentManager.removeManager(IAccountManager.class.getName());
        componentManager.removeManager(IDataCacheManager.class.getName());
        componentManager.removeManager(ITradingStateManager.class.getName());
        componentManager.removeManager(IBonusPointManager.class.getName());
        componentManager.removeManager(IFeedRequestManager.class.getName());
        componentManager.removeManager(IRedDotManager.class.getName());
        componentManager.removeManager(IOrderManager.class.getName());
        componentManager.removeManager(IScanManager.class.getName());
    }
}
