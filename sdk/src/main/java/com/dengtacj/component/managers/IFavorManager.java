package com.dengtacj.component.managers;

import com.dengtacj.component.entity.db.FavorItem;
import java.util.ArrayList;

/**
 * Created by davidwei on 2017-09-06
 */

public interface IFavorManager {
    void init();
    boolean isFavor(FavorItem favorItem);
    void addFavor(final FavorItem favorItem);
    void deleteFavor(final FavorItem favorItem);
    ArrayList<FavorItem> getFavorItems();
    int getFavorCount();
}
