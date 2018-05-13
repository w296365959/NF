package BEC;

public final class BeaconStatData extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public long iTime = 0;

    public int eType = 0;

    public String sData = "";

    public java.util.Map<Integer, Integer> mNumberData = null;

    public int eFunc = BEC.BEACON_STAT_FUNCTION.LOG_STAT;

    public long getITime()
    {
        return iTime;
    }

    public void  setITime(long iTime)
    {
        this.iTime = iTime;
    }

    public int getEType()
    {
        return eType;
    }

    public void  setEType(int eType)
    {
        this.eType = eType;
    }

    public String getSData()
    {
        return sData;
    }

    public void  setSData(String sData)
    {
        this.sData = sData;
    }

    public java.util.Map<Integer, Integer> getMNumberData()
    {
        return mNumberData;
    }

    public void  setMNumberData(java.util.Map<Integer, Integer> mNumberData)
    {
        this.mNumberData = mNumberData;
    }

    public int getEFunc()
    {
        return eFunc;
    }

    public void  setEFunc(int eFunc)
    {
        this.eFunc = eFunc;
    }

    public BeaconStatData()
    {
    }

    public BeaconStatData(long iTime, int eType, String sData, java.util.Map<Integer, Integer> mNumberData, int eFunc)
    {
        this.iTime = iTime;
        this.eType = eType;
        this.sData = sData;
        this.mNumberData = mNumberData;
        this.eFunc = eFunc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeUInt32(0, iTime);
        ostream.writeInt32(1, eType);
        if (null != sData) {
            ostream.writeString(2, sData);
        }
        if (null != mNumberData) {
            ostream.writeMap(3, mNumberData);
        }
        ostream.writeInt32(4, eFunc);
    }

    static java.util.Map<Integer, Integer> VAR_TYPE_4_MNUMBERDATA = new java.util.HashMap<Integer, Integer>();
    static {
        VAR_TYPE_4_MNUMBERDATA.put(0, 0);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iTime = (long)istream.readUInt32(0, false, this.iTime);
        this.eType = (int)istream.readInt32(1, false, this.eType);
        this.sData = (String)istream.readString(2, false, this.sData);
        this.mNumberData = (java.util.Map<Integer, Integer>)istream.readMap(3, false, VAR_TYPE_4_MNUMBERDATA);
        this.eFunc = (int)istream.readInt32(4, false, this.eFunc);
    }

}

