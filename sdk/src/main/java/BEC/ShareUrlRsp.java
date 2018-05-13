package BEC;

public final class ShareUrlRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.ShareUrl> vtShareUrl = null;

    public java.util.ArrayList<BEC.ShareUrl> getVtShareUrl()
    {
        return vtShareUrl;
    }

    public void  setVtShareUrl(java.util.ArrayList<BEC.ShareUrl> vtShareUrl)
    {
        this.vtShareUrl = vtShareUrl;
    }

    public ShareUrlRsp()
    {
    }

    public ShareUrlRsp(java.util.ArrayList<BEC.ShareUrl> vtShareUrl)
    {
        this.vtShareUrl = vtShareUrl;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtShareUrl) {
            ostream.writeList(0, vtShareUrl);
        }
    }

    static java.util.ArrayList<BEC.ShareUrl> VAR_TYPE_4_VTSHAREURL = new java.util.ArrayList<BEC.ShareUrl>();
    static {
        VAR_TYPE_4_VTSHAREURL.add(new BEC.ShareUrl());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtShareUrl = (java.util.ArrayList<BEC.ShareUrl>)istream.readList(0, false, VAR_TYPE_4_VTSHAREURL);
    }

}

