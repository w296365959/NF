package BEC;

public final class ConsultRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.STConsult> vtConsult = null;

    public java.util.ArrayList<BEC.STConsult> getVtConsult()
    {
        return vtConsult;
    }

    public void  setVtConsult(java.util.ArrayList<BEC.STConsult> vtConsult)
    {
        this.vtConsult = vtConsult;
    }

    public ConsultRsp()
    {
    }

    public ConsultRsp(java.util.ArrayList<BEC.STConsult> vtConsult)
    {
        this.vtConsult = vtConsult;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtConsult) {
            ostream.writeList(0, vtConsult);
        }
    }

    static java.util.ArrayList<BEC.STConsult> VAR_TYPE_4_VTCONSULT = new java.util.ArrayList<BEC.STConsult>();
    static {
        VAR_TYPE_4_VTCONSULT.add(new BEC.STConsult());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtConsult = (java.util.ArrayList<BEC.STConsult>)istream.readList(0, false, VAR_TYPE_4_VTCONSULT);
    }

}

