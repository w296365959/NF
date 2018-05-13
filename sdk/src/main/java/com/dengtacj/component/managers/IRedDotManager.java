package com.dengtacj.component.managers;

/**
 * Created by davidwei on 2017-09-07
 */

public interface IRedDotManager {
    boolean getInputToolsButtonState();
    void setInputToolsButtonState(boolean state);
    void setPortfolioLiveState(final boolean state);
    boolean getPortfolioLiveState();
    void setMainBoardLiveState(final boolean state);
    boolean getMainBoardLiveState();
}
