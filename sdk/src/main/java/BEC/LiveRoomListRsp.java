package BEC;

public final class LiveRoomListRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.VideoDetail> vtVideoDetail = null;

    public java.util.ArrayList<BEC.VideoDetail> getVtVideoDetail()
    {
        return vtVideoDetail;
    }

    public void  setVtVideoDetail(java.util.ArrayList<BEC.VideoDetail> vtVideoDetail)
    {
        this.vtVideoDetail = vtVideoDetail;
    }

    public LiveRoomListRsp()
    {
    }

    public LiveRoomListRsp(java.util.ArrayList<BEC.VideoDetail> vtVideoDetail)
    {
        this.vtVideoDetail = vtVideoDetail;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtVideoDetail) {
            ostream.writeList(0, vtVideoDetail);
        }
    }

    static java.util.ArrayList<BEC.VideoDetail> VAR_TYPE_4_VTVIDEODETAIL = new java.util.ArrayList<BEC.VideoDetail>();
    static {
        VAR_TYPE_4_VTVIDEODETAIL.add(new BEC.VideoDetail());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtVideoDetail = (java.util.ArrayList<BEC.VideoDetail>)istream.readList(0, false, VAR_TYPE_4_VTVIDEODETAIL);
    }

}

