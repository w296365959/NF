package BEC;

public final class AdditionBreifInfoRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iRet = 0;

    public java.util.Map<String, String> mAdditionBreifInfo = null;

    public int getIRet()
    {
        return iRet;
    }

    public void  setIRet(int iRet)
    {
        this.iRet = iRet;
    }

    public java.util.Map<String, String> getMAdditionBreifInfo()
    {
        return mAdditionBreifInfo;
    }

    public void  setMAdditionBreifInfo(java.util.Map<String, String> mAdditionBreifInfo)
    {
        this.mAdditionBreifInfo = mAdditionBreifInfo;
    }

    public AdditionBreifInfoRsp()
    {
    }

    public AdditionBreifInfoRsp(int iRet, java.util.Map<String, String> mAdditionBreifInfo)
    {
        this.iRet = iRet;
        this.mAdditionBreifInfo = mAdditionBreifInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iRet);
        if (null != mAdditionBreifInfo) {
            ostream.writeMap(1, mAdditionBreifInfo);
        }
    }

    static java.util.Map<String, String> VAR_TYPE_4_MADDITIONBREIFINFO = new java.util.HashMap<String, String>();
    static {
        VAR_TYPE_4_MADDITIONBREIFINFO.put("", "");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iRet = (int)istream.readInt32(0, false, this.iRet);
        this.mAdditionBreifInfo = (java.util.Map<String, String>)istream.readMap(1, false, VAR_TYPE_4_MADDITIONBREIFINFO);
    }

}

