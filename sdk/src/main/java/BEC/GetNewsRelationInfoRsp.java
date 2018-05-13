package BEC;

public final class GetNewsRelationInfoRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sNewsId = "";

    public java.util.ArrayList<BEC.RelatedNews> vRelatedNews = null;

    public java.util.ArrayList<BEC.RelatedSecurity> vRelatedPlates = null;

    public java.util.ArrayList<BEC.RelatedSecurity> vRelatedStocks = null;

    public java.util.ArrayList<String> vRelatedTags = null;

    public String getSNewsId()
    {
        return sNewsId;
    }

    public void  setSNewsId(String sNewsId)
    {
        this.sNewsId = sNewsId;
    }

    public java.util.ArrayList<BEC.RelatedNews> getVRelatedNews()
    {
        return vRelatedNews;
    }

    public void  setVRelatedNews(java.util.ArrayList<BEC.RelatedNews> vRelatedNews)
    {
        this.vRelatedNews = vRelatedNews;
    }

    public java.util.ArrayList<BEC.RelatedSecurity> getVRelatedPlates()
    {
        return vRelatedPlates;
    }

    public void  setVRelatedPlates(java.util.ArrayList<BEC.RelatedSecurity> vRelatedPlates)
    {
        this.vRelatedPlates = vRelatedPlates;
    }

    public java.util.ArrayList<BEC.RelatedSecurity> getVRelatedStocks()
    {
        return vRelatedStocks;
    }

    public void  setVRelatedStocks(java.util.ArrayList<BEC.RelatedSecurity> vRelatedStocks)
    {
        this.vRelatedStocks = vRelatedStocks;
    }

    public java.util.ArrayList<String> getVRelatedTags()
    {
        return vRelatedTags;
    }

    public void  setVRelatedTags(java.util.ArrayList<String> vRelatedTags)
    {
        this.vRelatedTags = vRelatedTags;
    }

    public GetNewsRelationInfoRsp()
    {
    }

    public GetNewsRelationInfoRsp(String sNewsId, java.util.ArrayList<BEC.RelatedNews> vRelatedNews, java.util.ArrayList<BEC.RelatedSecurity> vRelatedPlates, java.util.ArrayList<BEC.RelatedSecurity> vRelatedStocks, java.util.ArrayList<String> vRelatedTags)
    {
        this.sNewsId = sNewsId;
        this.vRelatedNews = vRelatedNews;
        this.vRelatedPlates = vRelatedPlates;
        this.vRelatedStocks = vRelatedStocks;
        this.vRelatedTags = vRelatedTags;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sNewsId) {
            ostream.writeString(0, sNewsId);
        }
        if (null != vRelatedNews) {
            ostream.writeList(1, vRelatedNews);
        }
        if (null != vRelatedPlates) {
            ostream.writeList(2, vRelatedPlates);
        }
        if (null != vRelatedStocks) {
            ostream.writeList(3, vRelatedStocks);
        }
        if (null != vRelatedTags) {
            ostream.writeList(4, vRelatedTags);
        }
    }

    static java.util.ArrayList<BEC.RelatedNews> VAR_TYPE_4_VRELATEDNEWS = new java.util.ArrayList<BEC.RelatedNews>();
    static {
        VAR_TYPE_4_VRELATEDNEWS.add(new BEC.RelatedNews());
    }

    static java.util.ArrayList<BEC.RelatedSecurity> VAR_TYPE_4_VRELATEDPLATES = new java.util.ArrayList<BEC.RelatedSecurity>();
    static {
        VAR_TYPE_4_VRELATEDPLATES.add(new BEC.RelatedSecurity());
    }

    static java.util.ArrayList<BEC.RelatedSecurity> VAR_TYPE_4_VRELATEDSTOCKS = new java.util.ArrayList<BEC.RelatedSecurity>();
    static {
        VAR_TYPE_4_VRELATEDSTOCKS.add(new BEC.RelatedSecurity());
    }

    static java.util.ArrayList<String> VAR_TYPE_4_VRELATEDTAGS = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VRELATEDTAGS.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sNewsId = (String)istream.readString(0, false, this.sNewsId);
        this.vRelatedNews = (java.util.ArrayList<BEC.RelatedNews>)istream.readList(1, false, VAR_TYPE_4_VRELATEDNEWS);
        this.vRelatedPlates = (java.util.ArrayList<BEC.RelatedSecurity>)istream.readList(2, false, VAR_TYPE_4_VRELATEDPLATES);
        this.vRelatedStocks = (java.util.ArrayList<BEC.RelatedSecurity>)istream.readList(3, false, VAR_TYPE_4_VRELATEDSTOCKS);
        this.vRelatedTags = (java.util.ArrayList<String>)istream.readList(4, false, VAR_TYPE_4_VRELATEDTAGS);
    }

}

