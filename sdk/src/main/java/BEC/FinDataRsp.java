package BEC;

public final class FinDataRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.FinDataRst> vtFinDateRst = null;

    public java.util.ArrayList<BEC.FinDataRst> getVtFinDateRst()
    {
        return vtFinDateRst;
    }

    public void  setVtFinDateRst(java.util.ArrayList<BEC.FinDataRst> vtFinDateRst)
    {
        this.vtFinDateRst = vtFinDateRst;
    }

    public FinDataRsp()
    {
    }

    public FinDataRsp(java.util.ArrayList<BEC.FinDataRst> vtFinDateRst)
    {
        this.vtFinDateRst = vtFinDateRst;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtFinDateRst) {
            ostream.writeList(0, vtFinDateRst);
        }
    }

    static java.util.ArrayList<BEC.FinDataRst> VAR_TYPE_4_VTFINDATERST = new java.util.ArrayList<BEC.FinDataRst>();
    static {
        VAR_TYPE_4_VTFINDATERST.add(new BEC.FinDataRst());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtFinDateRst = (java.util.ArrayList<BEC.FinDataRst>)istream.readList(0, false, VAR_TYPE_4_VTFINDATERST);
    }

}

