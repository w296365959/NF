package BEC;

public final class FlashNewsContentReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<String> vtId = null;

    public java.util.ArrayList<String> getVtId()
    {
        return vtId;
    }

    public void  setVtId(java.util.ArrayList<String> vtId)
    {
        this.vtId = vtId;
    }

    public FlashNewsContentReq()
    {
    }

    public FlashNewsContentReq(java.util.ArrayList<String> vtId)
    {
        this.vtId = vtId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtId) {
            ostream.writeList(0, vtId);
        }
    }

    static java.util.ArrayList<String> VAR_TYPE_4_VTID = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VTID.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtId = (java.util.ArrayList<String>)istream.readList(0, false, VAR_TYPE_4_VTID);
    }

}

