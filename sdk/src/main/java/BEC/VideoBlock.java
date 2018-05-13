package BEC;

public final class VideoBlock extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int eType = 0;

    public BEC.VideoDetail stVideoDetail = null;

    public BEC.FineVideoGroup stFineVideoGroup = null;

    public int getEType()
    {
        return eType;
    }

    public void  setEType(int eType)
    {
        this.eType = eType;
    }

    public BEC.VideoDetail getStVideoDetail()
    {
        return stVideoDetail;
    }

    public void  setStVideoDetail(BEC.VideoDetail stVideoDetail)
    {
        this.stVideoDetail = stVideoDetail;
    }

    public BEC.FineVideoGroup getStFineVideoGroup()
    {
        return stFineVideoGroup;
    }

    public void  setStFineVideoGroup(BEC.FineVideoGroup stFineVideoGroup)
    {
        this.stFineVideoGroup = stFineVideoGroup;
    }

    public VideoBlock()
    {
    }

    public VideoBlock(int eType, BEC.VideoDetail stVideoDetail, BEC.FineVideoGroup stFineVideoGroup)
    {
        this.eType = eType;
        this.stVideoDetail = stVideoDetail;
        this.stFineVideoGroup = stFineVideoGroup;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, eType);
        if (null != stVideoDetail) {
            ostream.writeMessage(1, stVideoDetail);
        }
        if (null != stFineVideoGroup) {
            ostream.writeMessage(2, stFineVideoGroup);
        }
    }

    static BEC.VideoDetail VAR_TYPE_4_STVIDEODETAIL = new BEC.VideoDetail();

    static BEC.FineVideoGroup VAR_TYPE_4_STFINEVIDEOGROUP = new BEC.FineVideoGroup();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.eType = (int)istream.readInt32(0, false, this.eType);
        this.stVideoDetail = (BEC.VideoDetail)istream.readMessage(1, false, VAR_TYPE_4_STVIDEODETAIL);
        this.stFineVideoGroup = (BEC.FineVideoGroup)istream.readMessage(2, false, VAR_TYPE_4_STFINEVIDEOGROUP);
    }

}

