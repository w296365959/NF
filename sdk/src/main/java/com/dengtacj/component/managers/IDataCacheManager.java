package com.dengtacj.component.managers;

import java.util.List;
import BEC.SecQuote;
import BEC.SecSimpleQuote;

/**
 * Created by davidwei on 2017-09-05
 */
public interface IDataCacheManager {
    void setSecSimpleQuotes(final List<SecSimpleQuote> quotes);
    void setSecSimpleQuote(final SecSimpleQuote quote);
    SecSimpleQuote getSecSimpleQuote(final String dtSecCode);
    void setSecQuote(final SecQuote quote);
    SecQuote getSecQuote(final String dtSecCode);

}
