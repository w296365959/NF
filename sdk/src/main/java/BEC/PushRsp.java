package BEC;

public final class PushRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<String, Integer> mpPushResult = null;

    public java.util.Map<String, Integer> getMpPushResult()
    {
        return mpPushResult;
    }

    public void  setMpPushResult(java.util.Map<String, Integer> mpPushResult)
    {
        this.mpPushResult = mpPushResult;
    }

    public PushRsp()
    {
    }

    public PushRsp(java.util.Map<String, Integer> mpPushResult)
    {
        this.mpPushResult = mpPushResult;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != mpPushResult) {
            ostream.writeMap(0, mpPushResult);
        }
    }

    static java.util.Map<String, Integer> VAR_TYPE_4_MPPUSHRESULT = new java.util.HashMap<String, Integer>();
    static {
        VAR_TYPE_4_MPPUSHRESULT.put("", 0);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.mpPushResult = (java.util.Map<String, Integer>)istream.readMap(0, false, VAR_TYPE_4_MPPUSHRESULT);
    }

}

