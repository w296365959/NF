package BEC;

public final class SecInfo4Json extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public String sCHNShortName = "";

    public PlateInfo stPlateInfo = null;

    public java.util.ArrayList<ConcInfo> vConcInfo = null;

    public int iIsHgt = 0;

    public int iIsRzrq = 0;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public String getSCHNShortName()
    {
        return sCHNShortName;
    }

    public void  setSCHNShortName(String sCHNShortName)
    {
        this.sCHNShortName = sCHNShortName;
    }

    public PlateInfo getStPlateInfo()
    {
        return stPlateInfo;
    }

    public void  setStPlateInfo(PlateInfo stPlateInfo)
    {
        this.stPlateInfo = stPlateInfo;
    }

    public java.util.ArrayList<ConcInfo> getVConcInfo()
    {
        return vConcInfo;
    }

    public void  setVConcInfo(java.util.ArrayList<ConcInfo> vConcInfo)
    {
        this.vConcInfo = vConcInfo;
    }

    public int getIIsHgt()
    {
        return iIsHgt;
    }

    public void  setIIsHgt(int iIsHgt)
    {
        this.iIsHgt = iIsHgt;
    }

    public int getIIsRzrq()
    {
        return iIsRzrq;
    }

    public void  setIIsRzrq(int iIsRzrq)
    {
        this.iIsRzrq = iIsRzrq;
    }

    public SecInfo4Json()
    {
    }

    public SecInfo4Json(String sDtSecCode, String sCHNShortName, PlateInfo stPlateInfo, java.util.ArrayList<ConcInfo> vConcInfo, int iIsHgt, int iIsRzrq)
    {
        this.sDtSecCode = sDtSecCode;
        this.sCHNShortName = sCHNShortName;
        this.stPlateInfo = stPlateInfo;
        this.vConcInfo = vConcInfo;
        this.iIsHgt = iIsHgt;
        this.iIsRzrq = iIsRzrq;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        if (null != sCHNShortName) {
            ostream.writeString(1, sCHNShortName);
        }
        if (null != stPlateInfo) {
            ostream.writeMessage(2, stPlateInfo);
        }
        if (null != vConcInfo) {
            ostream.writeList(3, vConcInfo);
        }
        ostream.writeInt32(4, iIsHgt);
        ostream.writeInt32(5, iIsRzrq);
    }

    static PlateInfo VAR_TYPE_4_STPLATEINFO = new PlateInfo();

    static java.util.ArrayList<ConcInfo> VAR_TYPE_4_VCONCINFO = new java.util.ArrayList<ConcInfo>();
    static {
        VAR_TYPE_4_VCONCINFO.add(new ConcInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.sCHNShortName = (String)istream.readString(1, false, this.sCHNShortName);
        this.stPlateInfo = (PlateInfo)istream.readMessage(2, false, VAR_TYPE_4_STPLATEINFO);
        this.vConcInfo = (java.util.ArrayList<ConcInfo>)istream.readList(3, false, VAR_TYPE_4_VCONCINFO);
        this.iIsHgt = (int)istream.readInt32(4, false, this.iIsHgt);
        this.iIsRzrq = (int)istream.readInt32(5, false, this.iIsRzrq);
    }

}

