package BEC;

public final class VodVideoListRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.VideoList stVideoList = null;

    public String sNextVideoId = "";

    public BEC.VideoList getStVideoList()
    {
        return stVideoList;
    }

    public void  setStVideoList(BEC.VideoList stVideoList)
    {
        this.stVideoList = stVideoList;
    }

    public String getSNextVideoId()
    {
        return sNextVideoId;
    }

    public void  setSNextVideoId(String sNextVideoId)
    {
        this.sNextVideoId = sNextVideoId;
    }

    public VodVideoListRsp()
    {
    }

    public VodVideoListRsp(BEC.VideoList stVideoList, String sNextVideoId)
    {
        this.stVideoList = stVideoList;
        this.sNextVideoId = sNextVideoId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stVideoList) {
            ostream.writeMessage(0, stVideoList);
        }
        if (null != sNextVideoId) {
            ostream.writeString(1, sNextVideoId);
        }
    }

    static BEC.VideoList VAR_TYPE_4_STVIDEOLIST = new BEC.VideoList();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stVideoList = (BEC.VideoList)istream.readMessage(0, false, VAR_TYPE_4_STVIDEOLIST);
        this.sNextVideoId = (String)istream.readString(1, false, this.sNextVideoId);
    }

}

