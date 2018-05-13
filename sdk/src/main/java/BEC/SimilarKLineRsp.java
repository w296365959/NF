package BEC;

public final class SimilarKLineRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.SimilarKLineRst> vRst = null;

    public java.util.ArrayList<BEC.SimilarKLineRst> getVRst()
    {
        return vRst;
    }

    public void  setVRst(java.util.ArrayList<BEC.SimilarKLineRst> vRst)
    {
        this.vRst = vRst;
    }

    public SimilarKLineRsp()
    {
    }

    public SimilarKLineRsp(java.util.ArrayList<BEC.SimilarKLineRst> vRst)
    {
        this.vRst = vRst;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vRst) {
            ostream.writeList(0, vRst);
        }
    }

    static java.util.ArrayList<BEC.SimilarKLineRst> VAR_TYPE_4_VRST = new java.util.ArrayList<BEC.SimilarKLineRst>();
    static {
        VAR_TYPE_4_VRST.add(new BEC.SimilarKLineRst());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vRst = (java.util.ArrayList<BEC.SimilarKLineRst>)istream.readList(0, false, VAR_TYPE_4_VRST);
    }

}

