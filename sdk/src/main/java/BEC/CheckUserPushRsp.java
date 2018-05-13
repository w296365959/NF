package BEC;

public final class CheckUserPushRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<String, Integer> mpCheckResult = null;

    public java.util.Map<String, Integer> getMpCheckResult()
    {
        return mpCheckResult;
    }

    public void  setMpCheckResult(java.util.Map<String, Integer> mpCheckResult)
    {
        this.mpCheckResult = mpCheckResult;
    }

    public CheckUserPushRsp()
    {
    }

    public CheckUserPushRsp(java.util.Map<String, Integer> mpCheckResult)
    {
        this.mpCheckResult = mpCheckResult;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != mpCheckResult) {
            ostream.writeMap(0, mpCheckResult);
        }
    }

    static java.util.Map<String, Integer> VAR_TYPE_4_MPCHECKRESULT = new java.util.HashMap<String, Integer>();
    static {
        VAR_TYPE_4_MPCHECKRESULT.put("", 0);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.mpCheckResult = (java.util.Map<String, Integer>)istream.readMap(0, false, VAR_TYPE_4_MPCHECKRESULT);
    }

}

