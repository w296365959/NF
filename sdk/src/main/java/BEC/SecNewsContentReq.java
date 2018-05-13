package BEC;

public final class SecNewsContentReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<String> vtId = null;

    public int eSecType = 0;

    public int eNewsType = 0;

    public java.util.ArrayList<String> getVtId()
    {
        return vtId;
    }

    public void  setVtId(java.util.ArrayList<String> vtId)
    {
        this.vtId = vtId;
    }

    public int getESecType()
    {
        return eSecType;
    }

    public void  setESecType(int eSecType)
    {
        this.eSecType = eSecType;
    }

    public int getENewsType()
    {
        return eNewsType;
    }

    public void  setENewsType(int eNewsType)
    {
        this.eNewsType = eNewsType;
    }

    public SecNewsContentReq()
    {
    }

    public SecNewsContentReq(java.util.ArrayList<String> vtId, int eSecType, int eNewsType)
    {
        this.vtId = vtId;
        this.eSecType = eSecType;
        this.eNewsType = eNewsType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtId) {
            ostream.writeList(0, vtId);
        }
        ostream.writeInt32(1, eSecType);
        ostream.writeInt32(2, eNewsType);
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
        this.eSecType = (int)istream.readInt32(1, false, this.eSecType);
        this.eNewsType = (int)istream.readInt32(2, false, this.eNewsType);
    }

}

