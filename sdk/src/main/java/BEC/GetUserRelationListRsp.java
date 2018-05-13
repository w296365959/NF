package BEC;

public final class GetUserRelationListRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<Long> vAccountId = null;

    public java.util.ArrayList<Long> getVAccountId()
    {
        return vAccountId;
    }

    public void  setVAccountId(java.util.ArrayList<Long> vAccountId)
    {
        this.vAccountId = vAccountId;
    }

    public GetUserRelationListRsp()
    {
    }

    public GetUserRelationListRsp(java.util.ArrayList<Long> vAccountId)
    {
        this.vAccountId = vAccountId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vAccountId) {
            ostream.writeList(0, vAccountId);
        }
    }

    static java.util.ArrayList<Long> VAR_TYPE_4_VACCOUNTID = new java.util.ArrayList<Long>();
    static {
        VAR_TYPE_4_VACCOUNTID.add(0L);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vAccountId = (java.util.ArrayList<Long>)istream.readList(0, false, VAR_TYPE_4_VACCOUNTID);
    }

}

