package BEC;

public final class FlashNewsListRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.NewsDesc> vtId = null;

    public String sNextNewsID = "";

    public java.util.ArrayList<BEC.NewsDesc> getVtId()
    {
        return vtId;
    }

    public void  setVtId(java.util.ArrayList<BEC.NewsDesc> vtId)
    {
        this.vtId = vtId;
    }

    public String getSNextNewsID()
    {
        return sNextNewsID;
    }

    public void  setSNextNewsID(String sNextNewsID)
    {
        this.sNextNewsID = sNextNewsID;
    }

    public FlashNewsListRsp()
    {
    }

    public FlashNewsListRsp(java.util.ArrayList<BEC.NewsDesc> vtId, String sNextNewsID)
    {
        this.vtId = vtId;
        this.sNextNewsID = sNextNewsID;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtId) {
            ostream.writeList(0, vtId);
        }
        if (null != sNextNewsID) {
            ostream.writeString(1, sNextNewsID);
        }
    }

    static java.util.ArrayList<BEC.NewsDesc> VAR_TYPE_4_VTID = new java.util.ArrayList<BEC.NewsDesc>();
    static {
        VAR_TYPE_4_VTID.add(new BEC.NewsDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtId = (java.util.ArrayList<BEC.NewsDesc>)istream.readList(0, false, VAR_TYPE_4_VTID);
        this.sNextNewsID = (String)istream.readString(1, false, this.sNextNewsID);
    }

}

