package BEC;

public final class WxTalkFreeListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<WxTalkFree> vtWxTalkFree = null;

    public int iTotalCount = 0;

    public java.util.ArrayList<WxTalkFree> getVtWxTalkFree()
    {
        return vtWxTalkFree;
    }

    public void  setVtWxTalkFree(java.util.ArrayList<WxTalkFree> vtWxTalkFree)
    {
        this.vtWxTalkFree = vtWxTalkFree;
    }

    public int getITotalCount()
    {
        return iTotalCount;
    }

    public void  setITotalCount(int iTotalCount)
    {
        this.iTotalCount = iTotalCount;
    }

    public WxTalkFreeListRsp()
    {
    }

    public WxTalkFreeListRsp(java.util.ArrayList<WxTalkFree> vtWxTalkFree, int iTotalCount)
    {
        this.vtWxTalkFree = vtWxTalkFree;
        this.iTotalCount = iTotalCount;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtWxTalkFree) {
            ostream.writeList(0, vtWxTalkFree);
        }
        ostream.writeInt32(1, iTotalCount);
    }

    static java.util.ArrayList<WxTalkFree> VAR_TYPE_4_VTWXTALKFREE = new java.util.ArrayList<WxTalkFree>();
    static {
        VAR_TYPE_4_VTWXTALKFREE.add(new WxTalkFree());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtWxTalkFree = (java.util.ArrayList<WxTalkFree>)istream.readList(0, false, VAR_TYPE_4_VTWXTALKFREE);
        this.iTotalCount = (int)istream.readInt32(1, false, this.iTotalCount);
    }

}

