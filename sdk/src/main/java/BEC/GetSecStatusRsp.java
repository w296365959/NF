package BEC;

public final class GetSecStatusRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<String, java.util.ArrayList<SecStatusInfo>> mSecStatus = null;

    public java.util.Map<String, java.util.ArrayList<SecStatusInfo>> getMSecStatus()
    {
        return mSecStatus;
    }

    public void  setMSecStatus(java.util.Map<String, java.util.ArrayList<SecStatusInfo>> mSecStatus)
    {
        this.mSecStatus = mSecStatus;
    }

    public GetSecStatusRsp()
    {
    }

    public GetSecStatusRsp(java.util.Map<String, java.util.ArrayList<SecStatusInfo>> mSecStatus)
    {
        this.mSecStatus = mSecStatus;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != mSecStatus) {
            ostream.writeMap(0, mSecStatus);
        }
    }

    static java.util.Map<String, java.util.ArrayList<SecStatusInfo>> VAR_TYPE_4_MSECSTATUS = new java.util.HashMap<String, java.util.ArrayList<SecStatusInfo>>();
    static {
        java.util.ArrayList<SecStatusInfo> VAR_TYPE_4_MSECSTATUS_V_C = new java.util.ArrayList<SecStatusInfo>();
        VAR_TYPE_4_MSECSTATUS_V_C.add(new SecStatusInfo());
        VAR_TYPE_4_MSECSTATUS.put("", VAR_TYPE_4_MSECSTATUS_V_C);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.mSecStatus = (java.util.Map<String, java.util.ArrayList<SecStatusInfo>>)istream.readMap(0, false, VAR_TYPE_4_MSECSTATUS);
    }

}

