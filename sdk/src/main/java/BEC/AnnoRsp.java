package BEC;

public final class AnnoRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.STAnno> vtAnno = null;

    public java.util.ArrayList<BEC.STAnno> getVtAnno()
    {
        return vtAnno;
    }

    public void  setVtAnno(java.util.ArrayList<BEC.STAnno> vtAnno)
    {
        this.vtAnno = vtAnno;
    }

    public AnnoRsp()
    {
    }

    public AnnoRsp(java.util.ArrayList<BEC.STAnno> vtAnno)
    {
        this.vtAnno = vtAnno;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtAnno) {
            ostream.writeList(0, vtAnno);
        }
    }

    static java.util.ArrayList<BEC.STAnno> VAR_TYPE_4_VTANNO = new java.util.ArrayList<BEC.STAnno>();
    static {
        VAR_TYPE_4_VTANNO.add(new BEC.STAnno());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtAnno = (java.util.ArrayList<BEC.STAnno>)istream.readList(0, false, VAR_TYPE_4_VTANNO);
    }

}

