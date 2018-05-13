package BEC;

public final class SecAttr extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.PlateInfo stPlateInfo = null;

    public java.util.Map<Integer, String> mSecAttr = null;

    public BEC.PlateInfo getStPlateInfo()
    {
        return stPlateInfo;
    }

    public void  setStPlateInfo(BEC.PlateInfo stPlateInfo)
    {
        this.stPlateInfo = stPlateInfo;
    }

    public java.util.Map<Integer, String> getMSecAttr()
    {
        return mSecAttr;
    }

    public void  setMSecAttr(java.util.Map<Integer, String> mSecAttr)
    {
        this.mSecAttr = mSecAttr;
    }

    public SecAttr()
    {
    }

    public SecAttr(BEC.PlateInfo stPlateInfo, java.util.Map<Integer, String> mSecAttr)
    {
        this.stPlateInfo = stPlateInfo;
        this.mSecAttr = mSecAttr;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stPlateInfo) {
            ostream.writeMessage(0, stPlateInfo);
        }
        if (null != mSecAttr) {
            ostream.writeMap(1, mSecAttr);
        }
    }

    static BEC.PlateInfo VAR_TYPE_4_STPLATEINFO = new BEC.PlateInfo();

    static java.util.Map<Integer, String> VAR_TYPE_4_MSECATTR = new java.util.HashMap<Integer, String>();
    static {
        VAR_TYPE_4_MSECATTR.put(0, "");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stPlateInfo = (BEC.PlateInfo)istream.readMessage(0, false, VAR_TYPE_4_STPLATEINFO);
        this.mSecAttr = (java.util.Map<Integer, String>)istream.readMap(1, false, VAR_TYPE_4_MSECATTR);
    }

}

