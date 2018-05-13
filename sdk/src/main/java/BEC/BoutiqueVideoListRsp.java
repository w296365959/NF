package BEC;

public final class BoutiqueVideoListRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.VideoList stVideoList = null;

    public BEC.VideoList getStVideoList()
    {
        return stVideoList;
    }

    public void  setStVideoList(BEC.VideoList stVideoList)
    {
        this.stVideoList = stVideoList;
    }

    public BoutiqueVideoListRsp()
    {
    }

    public BoutiqueVideoListRsp(BEC.VideoList stVideoList)
    {
        this.stVideoList = stVideoList;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stVideoList) {
            ostream.writeMessage(0, stVideoList);
        }
    }

    static BEC.VideoList VAR_TYPE_4_STVIDEOLIST = new BEC.VideoList();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stVideoList = (BEC.VideoList)istream.readMessage(0, false, VAR_TYPE_4_STVIDEOLIST);
    }

}

