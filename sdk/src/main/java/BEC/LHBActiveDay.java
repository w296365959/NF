package BEC;

public final class LHBActiveDay extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<String> vLHBActiveDay = null;

    public java.util.Map<String, java.util.ArrayList<String>> mLHBStockActiveDay = null;

    public java.util.Map<String, java.util.ArrayList<String>> mLHBSaleDepActiveDay = null;

    public java.util.ArrayList<String> getVLHBActiveDay()
    {
        return vLHBActiveDay;
    }

    public void  setVLHBActiveDay(java.util.ArrayList<String> vLHBActiveDay)
    {
        this.vLHBActiveDay = vLHBActiveDay;
    }

    public java.util.Map<String, java.util.ArrayList<String>> getMLHBStockActiveDay()
    {
        return mLHBStockActiveDay;
    }

    public void  setMLHBStockActiveDay(java.util.Map<String, java.util.ArrayList<String>> mLHBStockActiveDay)
    {
        this.mLHBStockActiveDay = mLHBStockActiveDay;
    }

    public java.util.Map<String, java.util.ArrayList<String>> getMLHBSaleDepActiveDay()
    {
        return mLHBSaleDepActiveDay;
    }

    public void  setMLHBSaleDepActiveDay(java.util.Map<String, java.util.ArrayList<String>> mLHBSaleDepActiveDay)
    {
        this.mLHBSaleDepActiveDay = mLHBSaleDepActiveDay;
    }

    public LHBActiveDay()
    {
    }

    public LHBActiveDay(java.util.ArrayList<String> vLHBActiveDay, java.util.Map<String, java.util.ArrayList<String>> mLHBStockActiveDay, java.util.Map<String, java.util.ArrayList<String>> mLHBSaleDepActiveDay)
    {
        this.vLHBActiveDay = vLHBActiveDay;
        this.mLHBStockActiveDay = mLHBStockActiveDay;
        this.mLHBSaleDepActiveDay = mLHBSaleDepActiveDay;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vLHBActiveDay) {
            ostream.writeList(0, vLHBActiveDay);
        }
        if (null != mLHBStockActiveDay) {
            ostream.writeMap(1, mLHBStockActiveDay);
        }
        if (null != mLHBSaleDepActiveDay) {
            ostream.writeMap(2, mLHBSaleDepActiveDay);
        }
    }

    static java.util.ArrayList<String> VAR_TYPE_4_VLHBACTIVEDAY = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VLHBACTIVEDAY.add("");
    }

    static java.util.Map<String, java.util.ArrayList<String>> VAR_TYPE_4_MLHBSTOCKACTIVEDAY = new java.util.HashMap<String, java.util.ArrayList<String>>();
    static {
        java.util.ArrayList<String> VAR_TYPE_4_MLHBSTOCKACTIVEDAY_V_C = new java.util.ArrayList<String>();
        VAR_TYPE_4_MLHBSTOCKACTIVEDAY_V_C.add("");
        VAR_TYPE_4_MLHBSTOCKACTIVEDAY.put("", VAR_TYPE_4_MLHBSTOCKACTIVEDAY_V_C);
    }

    static java.util.Map<String, java.util.ArrayList<String>> VAR_TYPE_4_MLHBSALEDEPACTIVEDAY = new java.util.HashMap<String, java.util.ArrayList<String>>();
    static {
        java.util.ArrayList<String> VAR_TYPE_4_MLHBSALEDEPACTIVEDAY_V_C = new java.util.ArrayList<String>();
        VAR_TYPE_4_MLHBSALEDEPACTIVEDAY_V_C.add("");
        VAR_TYPE_4_MLHBSALEDEPACTIVEDAY.put("", VAR_TYPE_4_MLHBSALEDEPACTIVEDAY_V_C);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vLHBActiveDay = (java.util.ArrayList<String>)istream.readList(0, false, VAR_TYPE_4_VLHBACTIVEDAY);
        this.mLHBStockActiveDay = (java.util.Map<String, java.util.ArrayList<String>>)istream.readMap(1, false, VAR_TYPE_4_MLHBSTOCKACTIVEDAY);
        this.mLHBSaleDepActiveDay = (java.util.Map<String, java.util.ArrayList<String>>)istream.readMap(2, false, VAR_TYPE_4_MLHBSALEDEPACTIVEDAY);
    }

}

