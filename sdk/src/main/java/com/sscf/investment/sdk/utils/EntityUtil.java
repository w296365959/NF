package com.sscf.investment.sdk.utils;

import com.sscf.investment.sdk.net.EntityObject;
import com.dengtacj.thoth.Message;

import java.util.ArrayList;
import BEC.AHExtendInfo;
import BEC.AHExtendRsp;
import BEC.AHPlateDesc;
import BEC.AHPlateRsp;
import BEC.BannerInfo;
import BEC.CapitalDetailDesc;
import BEC.CapitalDetailRsp;
import BEC.CapitalFlow;
import BEC.CapitalFlowRsp;
import BEC.CapitalMainFlowDesc;
import BEC.CapitalMainFlowRsp;
import BEC.ChangeStatRsp;
import BEC.CommonSearchRsp;
import BEC.CompanyIndustrialChainRsp;
import BEC.ConfigDetail;
import BEC.DiscoveryNewsContentRsp;
import BEC.DtActivityDetail;
import BEC.GetConfigRsp;
import BEC.GetDiscBannerRsp;
import BEC.GetDtActivityListRsp;
import BEC.GetPrivTopListRsp;
import BEC.GetRealTimeHotStockRsp;
import BEC.GetSecBsTopRsp;
import BEC.MarketAd;
import BEC.MarketAdListRsp;
import BEC.NewsDesc;
import BEC.NewsList;
import BEC.NewsRsp;
import BEC.PlateQuoteDesc;
import BEC.PlateQuoteRsp;
import BEC.PlateStockListRsp;
import BEC.PrivInfo;
import BEC.QuoteRsp;
import BEC.QuoteSimpleRsp;
import BEC.RealTimeStockItem;
import BEC.SKLStatisticsInfo;
import BEC.SKLStatisticsRsp;
import BEC.SecInfo;
import BEC.SecQuote;
import BEC.SecSimpleQuote;

/**
 * Created by davidwei on 2017/04/27
 */
public final class EntityUtil {

    public static SecQuote entityToSecQuote(final boolean success, final EntityObject data) {
        final ArrayList<SecQuote> quotes = entityToSecQuoteList(success, data);
        if (quotes != null && quotes.size() > 0) {
            return quotes.get(0);
        }
        return null;
    }

    public static ArrayList<SecQuote> entityToSecQuoteList(final boolean success, final EntityObject data) {
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof QuoteRsp) {
                return ((QuoteRsp) entity).vSecQuote;
            }
        }
        return null;
    }

    public static SecSimpleQuote entityToSecSimpleQuote(final boolean success, final EntityObject data) {
        final ArrayList<SecSimpleQuote> quotes = entityToSecSimpleQuoteList(success, data);
        if (quotes != null && quotes.size() > 0) {
            return quotes.get(0);
        }
        return null;
    }

    public static ArrayList<SecSimpleQuote> entityToSecSimpleQuoteList(final boolean success, final EntityObject data) {
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof QuoteSimpleRsp) {
                return ((QuoteSimpleRsp) entity).vSecSimpleQuote;
            }
        }
        return null;
    }

    public static ArrayList<SecSimpleQuote> entityToSecSimpleQuoteListInPlate(final boolean success, final EntityObject data) {
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof PlateStockListRsp) {
                return ((PlateStockListRsp) entity).vSecSimpleQuote;
            }
        }
        return null;
    }

    public static ArrayList<CapitalFlow> entityToCapitalFlowList(final boolean success, final EntityObject data) {
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof CapitalFlowRsp) {
                return ((CapitalFlowRsp) entity).vCapitalFlow;
            }
        }
        return null;
    }

    public static CapitalFlow entityToCapitalFlow(final boolean success, final EntityObject data) {
        final ArrayList<CapitalFlow> capitalFlows = entityToCapitalFlowList(success, data);
        if (capitalFlows != null && capitalFlows.size() > 0) {
            return capitalFlows.get(0);
        }
        return null;
    }

    public static ArrayList<NewsDesc> entityToMarketNewsList(final boolean success, final EntityObject data) {
        ArrayList<NewsDesc> newsList = null;
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof DiscoveryNewsContentRsp) {
                newsList = ((DiscoveryNewsContentRsp) entity).vtNewsDesc;
            }
        }
        return newsList;
    }

    public static ArrayList<MarketAd> entityToAdList(final boolean success, final EntityObject data) {
        ArrayList<MarketAd> adList = null;
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof MarketAdListRsp) {
                adList = ((MarketAdListRsp) entity).vMarketAd;
            }
        }
        return adList;
    }

    public static ArrayList<BannerInfo> entityToBannerList(final boolean success, final EntityObject data) {
        ArrayList<BannerInfo> bannerList = null;
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof GetDiscBannerRsp) {
                bannerList = ((GetDiscBannerRsp) entity).vBannerInfo;
            }
        }
        return bannerList;
    }

    public static ArrayList<DtActivityDetail> entityToActivityList(final boolean success, final EntityObject data) {
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof GetDtActivityListRsp) {
                return ((GetDtActivityListRsp) entity).vList;
            }
        }
        return null;
    }

    public static DtActivityDetail entityToDtActivityDetail(final boolean success, final EntityObject data) {
        final ArrayList<DtActivityDetail> list = entityToActivityList(success, data);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public static CommonSearchRsp entityToCommonSearchRsp(final boolean success, final EntityObject data) {
        CommonSearchRsp searchRsp = null;
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof CommonSearchRsp) {
                searchRsp = (CommonSearchRsp) entity;
            }
        }
        return searchRsp;
    }

    public static ArrayList<Message> entityToSearchResultList(final boolean success, final EntityObject data) {
        final CommonSearchRsp rsp = entityToCommonSearchRsp(success, data);
        ArrayList<Message> resultList = null;
        if (rsp != null && rsp.sShowOrder != null) {
            final String[] order = rsp.sShowOrder.split(",");
            final int length = order.length;
            int type;
            resultList = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                type = NumberUtil.parseInt(order[i], 0);
                switch (type) {
                    case 1: //1:代表股票
                        resultList.addAll(rsp.stSecResult.vtSecInfoResultItem);
                        break;
                    case 2: // 2:代表行业
                        resultList.addAll(rsp.stPlateResult.vtPlateResultItem);
                        break;
                    case 3: // 主题
                        break;
                    default:
                        break;
                }
            }
        }
        return resultList;
    }

    public static ArrayList<SecInfo> entityToCommonSearchSecInfoResultItem(final boolean success, final EntityObject data) {
        final CommonSearchRsp rsp = entityToCommonSearchRsp(success, data);
        ArrayList<SecInfo> secInfos = null;
        if (rsp != null && rsp.stSecResult != null) {
            secInfos = rsp.stSecResult.vtSecInfoResultItem;
        }
        return secInfos;
    }

    public static ArrayList<PlateQuoteDesc> entityToPlateQuoteDescList(final boolean success, final EntityObject data) {
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof PlateQuoteRsp) {
                return ((PlateQuoteRsp) entity).vPlateQuoteDesc;
            }
        }
        return null;
    }

    public static ArrayList<AHPlateDesc> entityToAHPlateDescList(final boolean success, final EntityObject data) {
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof AHPlateRsp) {
                return ((AHPlateRsp) entity).vAHPlateDesc;
            }
        }
        return null;
    }

    public static ArrayList<PrivInfo> entityToPrivInfoList(final boolean success, final EntityObject data) {
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof GetPrivTopListRsp) {
                return ((GetPrivTopListRsp) entity).vPrivInfo;
            }
        }
        return null;
    }

    public static AHExtendInfo entityToAHExtendInfo(final boolean success, final EntityObject data) {
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof AHExtendRsp) {
                return ((AHExtendRsp) entity).stAHExtendInfo;
            }
        }
        return null;
    }

    public static ArrayList<CapitalDetailDesc> entityToCapitalDetailDescList(final boolean success, final EntityObject data) {
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof CapitalDetailRsp) {
                return ((CapitalDetailRsp) entity).vCapitalDetailDesc;
            }
        }
        return null;
    }

    public static ArrayList<CapitalMainFlowDesc> entityToCapitalMainFlowDescList(final boolean success, final EntityObject data) {
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof CapitalMainFlowRsp) {
                return ((CapitalMainFlowRsp) entity).vCapitalMainFlowDesc;
            }
        }
        return null;
    }

    public static ArrayList<RealTimeStockItem> entityToRealTimeStockItemList(final boolean success, final EntityObject data) {
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof GetRealTimeHotStockRsp) {
                return ((GetRealTimeHotStockRsp) entity).vItem;
            }
        }
        return null;
    }

    public static GetSecBsTopRsp entityToGetSecBsTopRsp(final boolean success, final EntityObject data) {
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof GetSecBsTopRsp) {
                return (GetSecBsTopRsp) entity;
            }
        }
        return null;
    }

    public static ArrayList<SKLStatisticsInfo> entityToSKLStatisticsInfoList(final boolean success, final EntityObject data) {
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof SKLStatisticsRsp) {
                return ((SKLStatisticsRsp) entity).vtSKLStatisticsInfo;
            }
        }
        return null;
    }

    public static ChangeStatRsp entityToChangeStatRsp(final boolean success, final EntityObject data) {
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof ChangeStatRsp) {
                return ((ChangeStatRsp) entity);
            }
        }
        return null;
    }

    public static CompanyIndustrialChainRsp entityToCompanyIndustrialChainRsp(final boolean success, final EntityObject data) {
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof CompanyIndustrialChainRsp) {
                return ((CompanyIndustrialChainRsp) entity);
            }
        }
        return null;
    }

    public static NewsRsp entityToNewsRsp(final boolean success, final EntityObject data) {
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof NewsRsp) {
                return ((NewsRsp) entity);
            }
        }
        return null;
    }

    public static ArrayList<BEC.NewsDesc> entityToNewsList(final boolean success, final EntityObject data) {
        final NewsRsp rsp = entityToNewsRsp(success, data);
        if (rsp != null) {
            final NewsList newsList = rsp.stNewsList;
            if (newsList != null) {
                return newsList.vNewsDesc;
            }
        }
        return null;
    }

    public static byte[] entityToConfigData(final boolean success, final EntityObject data, final int type) {
        final GetConfigRsp rsp = entityToGetConfigRsp(success, data);
        if(rsp != null) {
            final ArrayList<ConfigDetail> configs = ((GetConfigRsp) data.getEntity()).vList;
            for (ConfigDetail config : configs) {
                if (config.iType == type) {
                    return config.vData;
                }
            }
        }
        return null;
    }

    private static GetConfigRsp entityToGetConfigRsp(final boolean success, final EntityObject data) {
        if (success) {
            final Object entity = data.getEntity();
            if (entity != null && entity instanceof GetConfigRsp) {
                return ((GetConfigRsp) entity);
            }
        }
        return null;
    }
}
