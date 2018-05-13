package BEC;

public final class AccountID2GuidList extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<String, Integer> mpGuids = null;

    public java.util.Map<String, Integer> getMpGuids()
    {
        return mpGuids;
    }

    public void  setMpGuids(java.util.Map<String, Integer> mpGuids)
    {
        this.mpGuids = mpGuids;
    }

    public AccountID2GuidList()
    {
    }

    public AccountID2GuidList(java.util.Map<String, Integer> mpGuids)
    {
        this.mpGuids = mpGuids;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != mpGuids) {
            ostream.writeMap(0, mpGuids);
        }
    }

    static java.util.Map<String, Integer> VAR_TYPE_4_MPGUIDS = new java.util.HashMap<String, Integer>();
    static {
        VAR_TYPE_4_MPGUIDS.put("", 0);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.mpGuids = (java.util.Map<String, Integer>)istream.readMap(0, false, VAR_TYPE_4_MPGUIDS);
    }

}

