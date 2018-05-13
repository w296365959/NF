package BEC;

public final class AlertMessageListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<PushData> vPushData = null;

    public long iClearTimeStamp = 0;

    public java.util.Map<Integer, Long> mClearTimeStamp = null;

    public java.util.ArrayList<PushData> getVPushData()
    {
        return vPushData;
    }

    public void  setVPushData(java.util.ArrayList<PushData> vPushData)
    {
        this.vPushData = vPushData;
    }

    public long getIClearTimeStamp()
    {
        return iClearTimeStamp;
    }

    public void  setIClearTimeStamp(long iClearTimeStamp)
    {
        this.iClearTimeStamp = iClearTimeStamp;
    }

    public java.util.Map<Integer, Long> getMClearTimeStamp()
    {
        return mClearTimeStamp;
    }

    public void  setMClearTimeStamp(java.util.Map<Integer, Long> mClearTimeStamp)
    {
        this.mClearTimeStamp = mClearTimeStamp;
    }

    public AlertMessageListRsp()
    {
    }

    public AlertMessageListRsp(java.util.ArrayList<PushData> vPushData, long iClearTimeStamp, java.util.Map<Integer, Long> mClearTimeStamp)
    {
        this.vPushData = vPushData;
        this.iClearTimeStamp = iClearTimeStamp;
        this.mClearTimeStamp = mClearTimeStamp;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vPushData) {
            ostream.writeList(0, vPushData);
        }
        ostream.writeUInt32(1, iClearTimeStamp);
        if (null != mClearTimeStamp) {
            ostream.writeMap(2, mClearTimeStamp);
        }
    }

    static java.util.ArrayList<PushData> VAR_TYPE_4_VPUSHDATA = new java.util.ArrayList<PushData>();
    static {
        VAR_TYPE_4_VPUSHDATA.add(new PushData());
    }

    static java.util.Map<Integer, Long> VAR_TYPE_4_MCLEARTIMESTAMP = new java.util.HashMap<Integer, Long>();
    static {
        VAR_TYPE_4_MCLEARTIMESTAMP.put(0, 0L);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vPushData = (java.util.ArrayList<PushData>)istream.readList(0, false, VAR_TYPE_4_VPUSHDATA);
        this.iClearTimeStamp = (long)istream.readUInt32(1, false, this.iClearTimeStamp);
        this.mClearTimeStamp = (java.util.Map<Integer, Long>)istream.readMap(2, false, VAR_TYPE_4_MCLEARTIMESTAMP);
    }

}

