package BEC;

public final class SscfInfoUserMap extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<Integer, UserInfo> stAccountId = null;

    public java.util.Map<Integer, UserInfo> getStAccountId()
    {
        return stAccountId;
    }

    public void  setStAccountId(java.util.Map<Integer, UserInfo> stAccountId)
    {
        this.stAccountId = stAccountId;
    }

    public SscfInfoUserMap()
    {
    }

    public SscfInfoUserMap(java.util.Map<Integer, UserInfo> stAccountId)
    {
        this.stAccountId = stAccountId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stAccountId) {
            ostream.writeMap(0, stAccountId);
        }
    }

    static java.util.Map<Integer, UserInfo> VAR_TYPE_4_STACCOUNTID = new java.util.HashMap<Integer, UserInfo>();
    static {
        VAR_TYPE_4_STACCOUNTID.put(0, new UserInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stAccountId = (java.util.Map<Integer, UserInfo>)istream.readMap(0, false, VAR_TYPE_4_STACCOUNTID);
    }

}

