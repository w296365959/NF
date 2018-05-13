package BEC;

public final class SimilarKLineRst extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sSecName = "";

    public String sDtSecCode = "";

    public String sUpBan = "";

    public String sUpVal = "";

    public String sValue1 = "";

    public String sValue2 = "";

    public int eSimilarKLineType = 0;

    public java.util.ArrayList<BEC.KLineDesc> vKLineDesc = null;

    public java.util.ArrayList<BEC.SimilarKLineBase> vSimilarKLine = null;

    public java.util.ArrayList<BEC.SimilarKLineBase> vSimilarKLineSelf = null;

    public String sUpValDay60 = "";

    public String sValue1Day60 = "";

    public String sValue2Day60 = "";

    public java.util.ArrayList<BEC.KLineDesc> vKLineDescDay60 = null;

    public java.util.ArrayList<BEC.SimilarKLineBase> vSimilarKLineDay60 = null;

    public java.util.ArrayList<BEC.SimilarKLineBase> vSimilarKLineDay60Self = null;

    public String getSSecName()
    {
        return sSecName;
    }

    public void  setSSecName(String sSecName)
    {
        this.sSecName = sSecName;
    }

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public String getSUpBan()
    {
        return sUpBan;
    }

    public void  setSUpBan(String sUpBan)
    {
        this.sUpBan = sUpBan;
    }

    public String getSUpVal()
    {
        return sUpVal;
    }

    public void  setSUpVal(String sUpVal)
    {
        this.sUpVal = sUpVal;
    }

    public String getSValue1()
    {
        return sValue1;
    }

    public void  setSValue1(String sValue1)
    {
        this.sValue1 = sValue1;
    }

    public String getSValue2()
    {
        return sValue2;
    }

    public void  setSValue2(String sValue2)
    {
        this.sValue2 = sValue2;
    }

    public int getESimilarKLineType()
    {
        return eSimilarKLineType;
    }

    public void  setESimilarKLineType(int eSimilarKLineType)
    {
        this.eSimilarKLineType = eSimilarKLineType;
    }

    public java.util.ArrayList<BEC.KLineDesc> getVKLineDesc()
    {
        return vKLineDesc;
    }

    public void  setVKLineDesc(java.util.ArrayList<BEC.KLineDesc> vKLineDesc)
    {
        this.vKLineDesc = vKLineDesc;
    }

    public java.util.ArrayList<BEC.SimilarKLineBase> getVSimilarKLine()
    {
        return vSimilarKLine;
    }

    public void  setVSimilarKLine(java.util.ArrayList<BEC.SimilarKLineBase> vSimilarKLine)
    {
        this.vSimilarKLine = vSimilarKLine;
    }

    public java.util.ArrayList<BEC.SimilarKLineBase> getVSimilarKLineSelf()
    {
        return vSimilarKLineSelf;
    }

    public void  setVSimilarKLineSelf(java.util.ArrayList<BEC.SimilarKLineBase> vSimilarKLineSelf)
    {
        this.vSimilarKLineSelf = vSimilarKLineSelf;
    }

    public String getSUpValDay60()
    {
        return sUpValDay60;
    }

    public void  setSUpValDay60(String sUpValDay60)
    {
        this.sUpValDay60 = sUpValDay60;
    }

    public String getSValue1Day60()
    {
        return sValue1Day60;
    }

    public void  setSValue1Day60(String sValue1Day60)
    {
        this.sValue1Day60 = sValue1Day60;
    }

    public String getSValue2Day60()
    {
        return sValue2Day60;
    }

    public void  setSValue2Day60(String sValue2Day60)
    {
        this.sValue2Day60 = sValue2Day60;
    }

    public java.util.ArrayList<BEC.KLineDesc> getVKLineDescDay60()
    {
        return vKLineDescDay60;
    }

    public void  setVKLineDescDay60(java.util.ArrayList<BEC.KLineDesc> vKLineDescDay60)
    {
        this.vKLineDescDay60 = vKLineDescDay60;
    }

    public java.util.ArrayList<BEC.SimilarKLineBase> getVSimilarKLineDay60()
    {
        return vSimilarKLineDay60;
    }

    public void  setVSimilarKLineDay60(java.util.ArrayList<BEC.SimilarKLineBase> vSimilarKLineDay60)
    {
        this.vSimilarKLineDay60 = vSimilarKLineDay60;
    }

    public java.util.ArrayList<BEC.SimilarKLineBase> getVSimilarKLineDay60Self()
    {
        return vSimilarKLineDay60Self;
    }

    public void  setVSimilarKLineDay60Self(java.util.ArrayList<BEC.SimilarKLineBase> vSimilarKLineDay60Self)
    {
        this.vSimilarKLineDay60Self = vSimilarKLineDay60Self;
    }

    public SimilarKLineRst()
    {
    }

    public SimilarKLineRst(String sSecName, String sDtSecCode, String sUpBan, String sUpVal, String sValue1, String sValue2, int eSimilarKLineType, java.util.ArrayList<BEC.KLineDesc> vKLineDesc, java.util.ArrayList<BEC.SimilarKLineBase> vSimilarKLine, java.util.ArrayList<BEC.SimilarKLineBase> vSimilarKLineSelf, String sUpValDay60, String sValue1Day60, String sValue2Day60, java.util.ArrayList<BEC.KLineDesc> vKLineDescDay60, java.util.ArrayList<BEC.SimilarKLineBase> vSimilarKLineDay60, java.util.ArrayList<BEC.SimilarKLineBase> vSimilarKLineDay60Self)
    {
        this.sSecName = sSecName;
        this.sDtSecCode = sDtSecCode;
        this.sUpBan = sUpBan;
        this.sUpVal = sUpVal;
        this.sValue1 = sValue1;
        this.sValue2 = sValue2;
        this.eSimilarKLineType = eSimilarKLineType;
        this.vKLineDesc = vKLineDesc;
        this.vSimilarKLine = vSimilarKLine;
        this.vSimilarKLineSelf = vSimilarKLineSelf;
        this.sUpValDay60 = sUpValDay60;
        this.sValue1Day60 = sValue1Day60;
        this.sValue2Day60 = sValue2Day60;
        this.vKLineDescDay60 = vKLineDescDay60;
        this.vSimilarKLineDay60 = vSimilarKLineDay60;
        this.vSimilarKLineDay60Self = vSimilarKLineDay60Self;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sSecName) {
            ostream.writeString(0, sSecName);
        }
        if (null != sDtSecCode) {
            ostream.writeString(1, sDtSecCode);
        }
        if (null != sUpBan) {
            ostream.writeString(2, sUpBan);
        }
        if (null != sUpVal) {
            ostream.writeString(3, sUpVal);
        }
        if (null != sValue1) {
            ostream.writeString(4, sValue1);
        }
        if (null != sValue2) {
            ostream.writeString(5, sValue2);
        }
        ostream.writeInt32(6, eSimilarKLineType);
        if (null != vKLineDesc) {
            ostream.writeList(7, vKLineDesc);
        }
        if (null != vSimilarKLine) {
            ostream.writeList(8, vSimilarKLine);
        }
        if (null != vSimilarKLineSelf) {
            ostream.writeList(9, vSimilarKLineSelf);
        }
        if (null != sUpValDay60) {
            ostream.writeString(10, sUpValDay60);
        }
        if (null != sValue1Day60) {
            ostream.writeString(11, sValue1Day60);
        }
        if (null != sValue2Day60) {
            ostream.writeString(12, sValue2Day60);
        }
        if (null != vKLineDescDay60) {
            ostream.writeList(13, vKLineDescDay60);
        }
        if (null != vSimilarKLineDay60) {
            ostream.writeList(14, vSimilarKLineDay60);
        }
        if (null != vSimilarKLineDay60Self) {
            ostream.writeList(15, vSimilarKLineDay60Self);
        }
    }

    static java.util.ArrayList<BEC.KLineDesc> VAR_TYPE_4_VKLINEDESC = new java.util.ArrayList<BEC.KLineDesc>();
    static {
        VAR_TYPE_4_VKLINEDESC.add(new BEC.KLineDesc());
    }

    static java.util.ArrayList<BEC.SimilarKLineBase> VAR_TYPE_4_VSIMILARKLINE = new java.util.ArrayList<BEC.SimilarKLineBase>();
    static {
        VAR_TYPE_4_VSIMILARKLINE.add(new BEC.SimilarKLineBase());
    }

    static java.util.ArrayList<BEC.SimilarKLineBase> VAR_TYPE_4_VSIMILARKLINESELF = new java.util.ArrayList<BEC.SimilarKLineBase>();
    static {
        VAR_TYPE_4_VSIMILARKLINESELF.add(new BEC.SimilarKLineBase());
    }

    static java.util.ArrayList<BEC.KLineDesc> VAR_TYPE_4_VKLINEDESCDAY60 = new java.util.ArrayList<BEC.KLineDesc>();
    static {
        VAR_TYPE_4_VKLINEDESCDAY60.add(new BEC.KLineDesc());
    }

    static java.util.ArrayList<BEC.SimilarKLineBase> VAR_TYPE_4_VSIMILARKLINEDAY60 = new java.util.ArrayList<BEC.SimilarKLineBase>();
    static {
        VAR_TYPE_4_VSIMILARKLINEDAY60.add(new BEC.SimilarKLineBase());
    }

    static java.util.ArrayList<BEC.SimilarKLineBase> VAR_TYPE_4_VSIMILARKLINEDAY60SELF = new java.util.ArrayList<BEC.SimilarKLineBase>();
    static {
        VAR_TYPE_4_VSIMILARKLINEDAY60SELF.add(new BEC.SimilarKLineBase());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sSecName = (String)istream.readString(0, false, this.sSecName);
        this.sDtSecCode = (String)istream.readString(1, false, this.sDtSecCode);
        this.sUpBan = (String)istream.readString(2, false, this.sUpBan);
        this.sUpVal = (String)istream.readString(3, false, this.sUpVal);
        this.sValue1 = (String)istream.readString(4, false, this.sValue1);
        this.sValue2 = (String)istream.readString(5, false, this.sValue2);
        this.eSimilarKLineType = (int)istream.readInt32(6, false, this.eSimilarKLineType);
        this.vKLineDesc = (java.util.ArrayList<BEC.KLineDesc>)istream.readList(7, false, VAR_TYPE_4_VKLINEDESC);
        this.vSimilarKLine = (java.util.ArrayList<BEC.SimilarKLineBase>)istream.readList(8, false, VAR_TYPE_4_VSIMILARKLINE);
        this.vSimilarKLineSelf = (java.util.ArrayList<BEC.SimilarKLineBase>)istream.readList(9, false, VAR_TYPE_4_VSIMILARKLINESELF);
        this.sUpValDay60 = (String)istream.readString(10, false, this.sUpValDay60);
        this.sValue1Day60 = (String)istream.readString(11, false, this.sValue1Day60);
        this.sValue2Day60 = (String)istream.readString(12, false, this.sValue2Day60);
        this.vKLineDescDay60 = (java.util.ArrayList<BEC.KLineDesc>)istream.readList(13, false, VAR_TYPE_4_VKLINEDESCDAY60);
        this.vSimilarKLineDay60 = (java.util.ArrayList<BEC.SimilarKLineBase>)istream.readList(14, false, VAR_TYPE_4_VSIMILARKLINEDAY60);
        this.vSimilarKLineDay60Self = (java.util.ArrayList<BEC.SimilarKLineBase>)istream.readList(15, false, VAR_TYPE_4_VSIMILARKLINEDAY60SELF);
    }

}

