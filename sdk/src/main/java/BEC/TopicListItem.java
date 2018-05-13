package BEC;

public final class TopicListItem extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sId = "";

    public String sName = "";

    public String sDescription = "";

    public int iTimestamp = 0;

    public java.util.ArrayList<StockRela> vStockRela = null;

    public TopicMessage stTopicMessage = null;

    public String getSId()
    {
        return sId;
    }

    public void  setSId(String sId)
    {
        this.sId = sId;
    }

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public String getSDescription()
    {
        return sDescription;
    }

    public void  setSDescription(String sDescription)
    {
        this.sDescription = sDescription;
    }

    public int getITimestamp()
    {
        return iTimestamp;
    }

    public void  setITimestamp(int iTimestamp)
    {
        this.iTimestamp = iTimestamp;
    }

    public java.util.ArrayList<StockRela> getVStockRela()
    {
        return vStockRela;
    }

    public void  setVStockRela(java.util.ArrayList<StockRela> vStockRela)
    {
        this.vStockRela = vStockRela;
    }

    public TopicMessage getStTopicMessage()
    {
        return stTopicMessage;
    }

    public void  setStTopicMessage(TopicMessage stTopicMessage)
    {
        this.stTopicMessage = stTopicMessage;
    }

    public TopicListItem()
    {
    }

    public TopicListItem(String sId, String sName, String sDescription, int iTimestamp, java.util.ArrayList<StockRela> vStockRela, TopicMessage stTopicMessage)
    {
        this.sId = sId;
        this.sName = sName;
        this.sDescription = sDescription;
        this.iTimestamp = iTimestamp;
        this.vStockRela = vStockRela;
        this.stTopicMessage = stTopicMessage;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sId) {
            ostream.writeString(0, sId);
        }
        if (null != sName) {
            ostream.writeString(1, sName);
        }
        if (null != sDescription) {
            ostream.writeString(2, sDescription);
        }
        ostream.writeInt32(3, iTimestamp);
        if (null != vStockRela) {
            ostream.writeList(4, vStockRela);
        }
        if (null != stTopicMessage) {
            ostream.writeMessage(5, stTopicMessage);
        }
    }

    static java.util.ArrayList<StockRela> VAR_TYPE_4_VSTOCKRELA = new java.util.ArrayList<StockRela>();
    static {
        VAR_TYPE_4_VSTOCKRELA.add(new StockRela());
    }

    static TopicMessage VAR_TYPE_4_STTOPICMESSAGE = new TopicMessage();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sId = (String)istream.readString(0, false, this.sId);
        this.sName = (String)istream.readString(1, false, this.sName);
        this.sDescription = (String)istream.readString(2, false, this.sDescription);
        this.iTimestamp = (int)istream.readInt32(3, false, this.iTimestamp);
        this.vStockRela = (java.util.ArrayList<StockRela>)istream.readList(4, false, VAR_TYPE_4_VSTOCKRELA);
        this.stTopicMessage = (TopicMessage)istream.readMessage(5, false, VAR_TYPE_4_STTOPICMESSAGE);
    }

}

