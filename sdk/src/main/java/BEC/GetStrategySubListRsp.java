package BEC;

public final class GetStrategySubListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<StrategySubItem> vList = null;

    public int iUpdateTimeStamp = 0;

    public java.util.ArrayList<StrategySubItem> getVList()
    {
        return vList;
    }

    public void  setVList(java.util.ArrayList<StrategySubItem> vList)
    {
        this.vList = vList;
    }

    public int getIUpdateTimeStamp()
    {
        return iUpdateTimeStamp;
    }

    public void  setIUpdateTimeStamp(int iUpdateTimeStamp)
    {
        this.iUpdateTimeStamp = iUpdateTimeStamp;
    }

    public GetStrategySubListRsp()
    {
    }

    public GetStrategySubListRsp(java.util.ArrayList<StrategySubItem> vList, int iUpdateTimeStamp)
    {
        this.vList = vList;
        this.iUpdateTimeStamp = iUpdateTimeStamp;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vList) {
            ostream.writeList(0, vList);
        }
        ostream.writeInt32(1, iUpdateTimeStamp);
    }

    static java.util.ArrayList<StrategySubItem> VAR_TYPE_4_VLIST = new java.util.ArrayList<StrategySubItem>();
    static {
        VAR_TYPE_4_VLIST.add(new StrategySubItem());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vList = (java.util.ArrayList<StrategySubItem>)istream.readList(0, false, VAR_TYPE_4_VLIST);
        this.iUpdateTimeStamp = (int)istream.readInt32(1, false, this.iUpdateTimeStamp);
    }

}

