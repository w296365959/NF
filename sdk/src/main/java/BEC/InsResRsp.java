package BEC;

public final class InsResRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.STInsRes> vtInsRes = null;

    public java.util.ArrayList<BEC.STInsRes> getVtInsRes()
    {
        return vtInsRes;
    }

    public void  setVtInsRes(java.util.ArrayList<BEC.STInsRes> vtInsRes)
    {
        this.vtInsRes = vtInsRes;
    }

    public InsResRsp()
    {
    }

    public InsResRsp(java.util.ArrayList<BEC.STInsRes> vtInsRes)
    {
        this.vtInsRes = vtInsRes;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtInsRes) {
            ostream.writeList(0, vtInsRes);
        }
    }

    static java.util.ArrayList<BEC.STInsRes> VAR_TYPE_4_VTINSRES = new java.util.ArrayList<BEC.STInsRes>();
    static {
        VAR_TYPE_4_VTINSRES.add(new BEC.STInsRes());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtInsRes = (java.util.ArrayList<BEC.STInsRes>)istream.readList(0, false, VAR_TYPE_4_VTINSRES);
    }

}

