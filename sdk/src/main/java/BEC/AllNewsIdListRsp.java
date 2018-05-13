package BEC;

public final class AllNewsIdListRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.NewsId> vNewsId = null;

    public java.util.ArrayList<BEC.NewsId> getVNewsId()
    {
        return vNewsId;
    }

    public void  setVNewsId(java.util.ArrayList<BEC.NewsId> vNewsId)
    {
        this.vNewsId = vNewsId;
    }

    public AllNewsIdListRsp()
    {
    }

    public AllNewsIdListRsp(java.util.ArrayList<BEC.NewsId> vNewsId)
    {
        this.vNewsId = vNewsId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vNewsId) {
            ostream.writeList(0, vNewsId);
        }
    }

    static java.util.ArrayList<BEC.NewsId> VAR_TYPE_4_VNEWSID = new java.util.ArrayList<BEC.NewsId>();
    static {
        VAR_TYPE_4_VNEWSID.add(new BEC.NewsId());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vNewsId = (java.util.ArrayList<BEC.NewsId>)istream.readList(0, false, VAR_TYPE_4_VNEWSID);
    }

}

