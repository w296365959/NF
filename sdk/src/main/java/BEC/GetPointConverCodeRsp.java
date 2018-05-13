package BEC;

public final class GetPointConverCodeRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<String> vCode = null;

    public java.util.ArrayList<String> getVCode()
    {
        return vCode;
    }

    public void  setVCode(java.util.ArrayList<String> vCode)
    {
        this.vCode = vCode;
    }

    public GetPointConverCodeRsp()
    {
    }

    public GetPointConverCodeRsp(java.util.ArrayList<String> vCode)
    {
        this.vCode = vCode;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vCode) {
            ostream.writeList(0, vCode);
        }
    }

    static java.util.ArrayList<String> VAR_TYPE_4_VCODE = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VCODE.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vCode = (java.util.ArrayList<String>)istream.readList(0, false, VAR_TYPE_4_VCODE);
    }

}

