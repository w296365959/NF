package BEC;

public final class SingalConfigRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<SingalConfigDesc> vSingalConfigDesc = null;

    public java.util.ArrayList<SingalConfigDesc> getVSingalConfigDesc()
    {
        return vSingalConfigDesc;
    }

    public void  setVSingalConfigDesc(java.util.ArrayList<SingalConfigDesc> vSingalConfigDesc)
    {
        this.vSingalConfigDesc = vSingalConfigDesc;
    }

    public SingalConfigRsp()
    {
    }

    public SingalConfigRsp(java.util.ArrayList<SingalConfigDesc> vSingalConfigDesc)
    {
        this.vSingalConfigDesc = vSingalConfigDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vSingalConfigDesc) {
            ostream.writeList(0, vSingalConfigDesc);
        }
    }

    static java.util.ArrayList<SingalConfigDesc> VAR_TYPE_4_VSINGALCONFIGDESC = new java.util.ArrayList<SingalConfigDesc>();
    static {
        VAR_TYPE_4_VSINGALCONFIGDESC.add(new SingalConfigDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vSingalConfigDesc = (java.util.ArrayList<SingalConfigDesc>)istream.readList(0, false, VAR_TYPE_4_VSINGALCONFIGDESC);
    }

}

