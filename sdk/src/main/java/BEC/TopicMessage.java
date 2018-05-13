package BEC;

public final class TopicMessage extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sId = "";

    public String sTitle = "";

    public int iPubTime = 0;

    public String sContent = "";

    public java.util.ArrayList<StockRela> vStockRela = null;

    public String getSId()
    {
        return sId;
    }

    public void  setSId(String sId)
    {
        this.sId = sId;
    }

    public String getSTitle()
    {
        return sTitle;
    }

    public void  setSTitle(String sTitle)
    {
        this.sTitle = sTitle;
    }

    public int getIPubTime()
    {
        return iPubTime;
    }

    public void  setIPubTime(int iPubTime)
    {
        this.iPubTime = iPubTime;
    }

    public String getSContent()
    {
        return sContent;
    }

    public void  setSContent(String sContent)
    {
        this.sContent = sContent;
    }

    public java.util.ArrayList<StockRela> getVStockRela()
    {
        return vStockRela;
    }

    public void  setVStockRela(java.util.ArrayList<StockRela> vStockRela)
    {
        this.vStockRela = vStockRela;
    }

    public TopicMessage()
    {
    }

    public TopicMessage(String sId, String sTitle, int iPubTime, String sContent, java.util.ArrayList<StockRela> vStockRela)
    {
        this.sId = sId;
        this.sTitle = sTitle;
        this.iPubTime = iPubTime;
        this.sContent = sContent;
        this.vStockRela = vStockRela;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sId) {
            ostream.writeString(0, sId);
        }
        if (null != sTitle) {
            ostream.writeString(1, sTitle);
        }
        ostream.writeInt32(2, iPubTime);
        if (null != sContent) {
            ostream.writeString(3, sContent);
        }
        if (null != vStockRela) {
            ostream.writeList(4, vStockRela);
        }
    }

    static java.util.ArrayList<StockRela> VAR_TYPE_4_VSTOCKRELA = new java.util.ArrayList<StockRela>();
    static {
        VAR_TYPE_4_VSTOCKRELA.add(new StockRela());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sId = (String)istream.readString(0, false, this.sId);
        this.sTitle = (String)istream.readString(1, false, this.sTitle);
        this.iPubTime = (int)istream.readInt32(2, false, this.iPubTime);
        this.sContent = (String)istream.readString(3, false, this.sContent);
        this.vStockRela = (java.util.ArrayList<StockRela>)istream.readList(4, false, VAR_TYPE_4_VSTOCKRELA);
    }

}

