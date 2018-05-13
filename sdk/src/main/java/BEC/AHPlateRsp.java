package BEC;

public final class AHPlateRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.AHPlateDesc> vAHPlateDesc = null;

    public java.util.ArrayList<BEC.AHPlateDesc> getVAHPlateDesc()
    {
        return vAHPlateDesc;
    }

    public void  setVAHPlateDesc(java.util.ArrayList<BEC.AHPlateDesc> vAHPlateDesc)
    {
        this.vAHPlateDesc = vAHPlateDesc;
    }

    public AHPlateRsp()
    {
    }

    public AHPlateRsp(java.util.ArrayList<BEC.AHPlateDesc> vAHPlateDesc)
    {
        this.vAHPlateDesc = vAHPlateDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vAHPlateDesc) {
            ostream.writeList(1, vAHPlateDesc);
        }
    }

    static java.util.ArrayList<BEC.AHPlateDesc> VAR_TYPE_4_VAHPLATEDESC = new java.util.ArrayList<BEC.AHPlateDesc>();
    static {
        VAR_TYPE_4_VAHPLATEDESC.add(new BEC.AHPlateDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vAHPlateDesc = (java.util.ArrayList<BEC.AHPlateDesc>)istream.readList(1, false, VAR_TYPE_4_VAHPLATEDESC);
    }

}

