package BEC;

public final class KLineConditionRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public int ePeriod = 0;

    public int eUpThreshold = 0;

    public int iNowTime = 0;

    public java.util.ArrayList<BEC.KLineConditionInfo> vtKLineConditionInfo = null;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public int getEPeriod()
    {
        return ePeriod;
    }

    public void  setEPeriod(int ePeriod)
    {
        this.ePeriod = ePeriod;
    }

    public int getEUpThreshold()
    {
        return eUpThreshold;
    }

    public void  setEUpThreshold(int eUpThreshold)
    {
        this.eUpThreshold = eUpThreshold;
    }

    public int getINowTime()
    {
        return iNowTime;
    }

    public void  setINowTime(int iNowTime)
    {
        this.iNowTime = iNowTime;
    }

    public java.util.ArrayList<BEC.KLineConditionInfo> getVtKLineConditionInfo()
    {
        return vtKLineConditionInfo;
    }

    public void  setVtKLineConditionInfo(java.util.ArrayList<BEC.KLineConditionInfo> vtKLineConditionInfo)
    {
        this.vtKLineConditionInfo = vtKLineConditionInfo;
    }

    public KLineConditionRsp()
    {
    }

    public KLineConditionRsp(String sDtSecCode, int ePeriod, int eUpThreshold, int iNowTime, java.util.ArrayList<BEC.KLineConditionInfo> vtKLineConditionInfo)
    {
        this.sDtSecCode = sDtSecCode;
        this.ePeriod = ePeriod;
        this.eUpThreshold = eUpThreshold;
        this.iNowTime = iNowTime;
        this.vtKLineConditionInfo = vtKLineConditionInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        ostream.writeInt32(1, ePeriod);
        ostream.writeInt32(2, eUpThreshold);
        ostream.writeInt32(3, iNowTime);
        if (null != vtKLineConditionInfo) {
            ostream.writeList(4, vtKLineConditionInfo);
        }
    }

    static java.util.ArrayList<BEC.KLineConditionInfo> VAR_TYPE_4_VTKLINECONDITIONINFO = new java.util.ArrayList<BEC.KLineConditionInfo>();
    static {
        VAR_TYPE_4_VTKLINECONDITIONINFO.add(new BEC.KLineConditionInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.ePeriod = (int)istream.readInt32(1, false, this.ePeriod);
        this.eUpThreshold = (int)istream.readInt32(2, false, this.eUpThreshold);
        this.iNowTime = (int)istream.readInt32(3, false, this.iNowTime);
        this.vtKLineConditionInfo = (java.util.ArrayList<BEC.KLineConditionInfo>)istream.readList(4, false, VAR_TYPE_4_VTKLINECONDITIONINFO);
    }

}

