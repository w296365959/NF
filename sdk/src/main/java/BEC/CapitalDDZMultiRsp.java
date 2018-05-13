package BEC;

public final class CapitalDDZMultiRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<java.util.ArrayList<CapitalDDZDesc>> vvCapitalDDZ = null;

    public java.util.ArrayList<java.util.ArrayList<CapitalDDZDesc>> getVvCapitalDDZ()
    {
        return vvCapitalDDZ;
    }

    public void  setVvCapitalDDZ(java.util.ArrayList<java.util.ArrayList<CapitalDDZDesc>> vvCapitalDDZ)
    {
        this.vvCapitalDDZ = vvCapitalDDZ;
    }

    public CapitalDDZMultiRsp()
    {
    }

    public CapitalDDZMultiRsp(java.util.ArrayList<java.util.ArrayList<CapitalDDZDesc>> vvCapitalDDZ)
    {
        this.vvCapitalDDZ = vvCapitalDDZ;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vvCapitalDDZ) {
            ostream.writeList(0, vvCapitalDDZ);
        }
    }

    static java.util.ArrayList<java.util.ArrayList<CapitalDDZDesc>> VAR_TYPE_4_VVCAPITALDDZ = new java.util.ArrayList<java.util.ArrayList<CapitalDDZDesc>>();
    static {
        java.util.ArrayList<CapitalDDZDesc> VAR_TYPE_4_VVCAPITALDDZ_C = new java.util.ArrayList<CapitalDDZDesc>();
        VAR_TYPE_4_VVCAPITALDDZ_C.add(new CapitalDDZDesc());
        VAR_TYPE_4_VVCAPITALDDZ.add(VAR_TYPE_4_VVCAPITALDDZ_C);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vvCapitalDDZ = (java.util.ArrayList<java.util.ArrayList<CapitalDDZDesc>>)istream.readList(0, false, VAR_TYPE_4_VVCAPITALDDZ);
    }

}

