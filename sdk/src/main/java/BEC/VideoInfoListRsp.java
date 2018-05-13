package BEC;

public final class VideoInfoListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<VideoInfo> vtVideoInfo = null;

    public int iTotalCount = 0;

    public java.util.ArrayList<VideoInfo> getVtVideoInfo()
    {
        return vtVideoInfo;
    }

    public void  setVtVideoInfo(java.util.ArrayList<VideoInfo> vtVideoInfo)
    {
        this.vtVideoInfo = vtVideoInfo;
    }

    public int getITotalCount()
    {
        return iTotalCount;
    }

    public void  setITotalCount(int iTotalCount)
    {
        this.iTotalCount = iTotalCount;
    }

    public VideoInfoListRsp()
    {
    }

    public VideoInfoListRsp(java.util.ArrayList<VideoInfo> vtVideoInfo, int iTotalCount)
    {
        this.vtVideoInfo = vtVideoInfo;
        this.iTotalCount = iTotalCount;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtVideoInfo) {
            ostream.writeList(0, vtVideoInfo);
        }
        ostream.writeInt32(1, iTotalCount);
    }

    static java.util.ArrayList<VideoInfo> VAR_TYPE_4_VTVIDEOINFO = new java.util.ArrayList<VideoInfo>();
    static {
        VAR_TYPE_4_VTVIDEOINFO.add(new VideoInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtVideoInfo = (java.util.ArrayList<VideoInfo>)istream.readList(0, false, VAR_TYPE_4_VTVIDEOINFO);
        this.iTotalCount = (int)istream.readInt32(1, false, this.iTotalCount);
    }

}

