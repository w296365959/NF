package BEC;

public final class GetIndexStocksRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<Integer, java.util.ArrayList<SecQuote>> mStocks = null;

    public java.util.Map<Integer, java.util.ArrayList<SecQuote>> getMStocks()
    {
        return mStocks;
    }

    public void  setMStocks(java.util.Map<Integer, java.util.ArrayList<SecQuote>> mStocks)
    {
        this.mStocks = mStocks;
    }

    public GetIndexStocksRsp()
    {
    }

    public GetIndexStocksRsp(java.util.Map<Integer, java.util.ArrayList<SecQuote>> mStocks)
    {
        this.mStocks = mStocks;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != mStocks) {
            ostream.writeMap(0, mStocks);
        }
    }

    static java.util.Map<Integer, java.util.ArrayList<SecQuote>> VAR_TYPE_4_MSTOCKS = new java.util.HashMap<Integer, java.util.ArrayList<SecQuote>>();
    static {
        java.util.ArrayList<SecQuote> VAR_TYPE_4_MSTOCKS_V_C = new java.util.ArrayList<SecQuote>();
        VAR_TYPE_4_MSTOCKS_V_C.add(new SecQuote());
        VAR_TYPE_4_MSTOCKS.put(0, VAR_TYPE_4_MSTOCKS_V_C);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.mStocks = (java.util.Map<Integer, java.util.ArrayList<SecQuote>>)istream.readMap(0, false, VAR_TYPE_4_MSTOCKS);
    }

}

