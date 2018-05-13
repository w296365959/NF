package BEC;

public final class SimilarKLineTopRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.SimilarKLineTopInfo> vtTopInfo = null;

    public java.util.ArrayList<BEC.SimilarKLineTopInfo> getVtTopInfo()
    {
        return vtTopInfo;
    }

    public void  setVtTopInfo(java.util.ArrayList<BEC.SimilarKLineTopInfo> vtTopInfo)
    {
        this.vtTopInfo = vtTopInfo;
    }

    public SimilarKLineTopRsp()
    {
    }

    public SimilarKLineTopRsp(java.util.ArrayList<BEC.SimilarKLineTopInfo> vtTopInfo)
    {
        this.vtTopInfo = vtTopInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtTopInfo) {
            ostream.writeList(0, vtTopInfo);
        }
    }

    static java.util.ArrayList<BEC.SimilarKLineTopInfo> VAR_TYPE_4_VTTOPINFO = new java.util.ArrayList<BEC.SimilarKLineTopInfo>();
    static {
        VAR_TYPE_4_VTTOPINFO.add(new BEC.SimilarKLineTopInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtTopInfo = (java.util.ArrayList<BEC.SimilarKLineTopInfo>)istream.readList(0, false, VAR_TYPE_4_VTTOPINFO);
    }

}

