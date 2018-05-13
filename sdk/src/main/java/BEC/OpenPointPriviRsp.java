package BEC;

public final class OpenPointPriviRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<AccuPointPriviInfo> vPrivi = null;

    public java.util.ArrayList<AccuPointPriviInfo> getVPrivi()
    {
        return vPrivi;
    }

    public void  setVPrivi(java.util.ArrayList<AccuPointPriviInfo> vPrivi)
    {
        this.vPrivi = vPrivi;
    }

    public OpenPointPriviRsp()
    {
    }

    public OpenPointPriviRsp(java.util.ArrayList<AccuPointPriviInfo> vPrivi)
    {
        this.vPrivi = vPrivi;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vPrivi) {
            ostream.writeList(0, vPrivi);
        }
    }

    static java.util.ArrayList<AccuPointPriviInfo> VAR_TYPE_4_VPRIVI = new java.util.ArrayList<AccuPointPriviInfo>();
    static {
        VAR_TYPE_4_VPRIVI.add(new AccuPointPriviInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vPrivi = (java.util.ArrayList<AccuPointPriviInfo>)istream.readList(0, false, VAR_TYPE_4_VPRIVI);
    }

}

