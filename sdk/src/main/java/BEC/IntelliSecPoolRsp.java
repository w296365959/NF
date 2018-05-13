package BEC;

public final class IntelliSecPoolRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<IntelliDaySec> vtDaySec = null;

    public java.util.ArrayList<IntelliDaySec> getVtDaySec()
    {
        return vtDaySec;
    }

    public void  setVtDaySec(java.util.ArrayList<IntelliDaySec> vtDaySec)
    {
        this.vtDaySec = vtDaySec;
    }

    public IntelliSecPoolRsp()
    {
    }

    public IntelliSecPoolRsp(java.util.ArrayList<IntelliDaySec> vtDaySec)
    {
        this.vtDaySec = vtDaySec;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtDaySec) {
            ostream.writeList(0, vtDaySec);
        }
    }

    static java.util.ArrayList<IntelliDaySec> VAR_TYPE_4_VTDAYSEC = new java.util.ArrayList<IntelliDaySec>();
    static {
        VAR_TYPE_4_VTDAYSEC.add(new IntelliDaySec());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtDaySec = (java.util.ArrayList<IntelliDaySec>)istream.readList(0, false, VAR_TYPE_4_VTDAYSEC);
    }

}

