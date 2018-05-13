package BEC;

public final class GetSecByCoordsRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<SecMapSecItem> vSec = null;

    public java.util.ArrayList<SecMapSecItem> getVSec()
    {
        return vSec;
    }

    public void  setVSec(java.util.ArrayList<SecMapSecItem> vSec)
    {
        this.vSec = vSec;
    }

    public GetSecByCoordsRsp()
    {
    }

    public GetSecByCoordsRsp(java.util.ArrayList<SecMapSecItem> vSec)
    {
        this.vSec = vSec;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vSec) {
            ostream.writeList(0, vSec);
        }
    }

    static java.util.ArrayList<SecMapSecItem> VAR_TYPE_4_VSEC = new java.util.ArrayList<SecMapSecItem>();
    static {
        VAR_TYPE_4_VSEC.add(new SecMapSecItem());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vSec = (java.util.ArrayList<SecMapSecItem>)istream.readList(0, false, VAR_TYPE_4_VSEC);
    }

}

