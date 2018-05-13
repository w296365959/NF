package BEC;

public final class WxTeachListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<WxTeachTitle> vtWxTeachTitle = null;

    public int iTotalCount = 0;

    public java.util.ArrayList<WxTeachTitle> getVtWxTeachTitle()
    {
        return vtWxTeachTitle;
    }

    public void  setVtWxTeachTitle(java.util.ArrayList<WxTeachTitle> vtWxTeachTitle)
    {
        this.vtWxTeachTitle = vtWxTeachTitle;
    }

    public int getITotalCount()
    {
        return iTotalCount;
    }

    public void  setITotalCount(int iTotalCount)
    {
        this.iTotalCount = iTotalCount;
    }

    public WxTeachListRsp()
    {
    }

    public WxTeachListRsp(java.util.ArrayList<WxTeachTitle> vtWxTeachTitle, int iTotalCount)
    {
        this.vtWxTeachTitle = vtWxTeachTitle;
        this.iTotalCount = iTotalCount;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtWxTeachTitle) {
            ostream.writeList(0, vtWxTeachTitle);
        }
        ostream.writeInt32(1, iTotalCount);
    }

    static java.util.ArrayList<WxTeachTitle> VAR_TYPE_4_VTWXTEACHTITLE = new java.util.ArrayList<WxTeachTitle>();
    static {
        VAR_TYPE_4_VTWXTEACHTITLE.add(new WxTeachTitle());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtWxTeachTitle = (java.util.ArrayList<WxTeachTitle>)istream.readList(0, false, VAR_TYPE_4_VTWXTEACHTITLE);
        this.iTotalCount = (int)istream.readInt32(1, false, this.iTotalCount);
    }

}

