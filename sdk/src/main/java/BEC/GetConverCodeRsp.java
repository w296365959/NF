package BEC;

public final class GetConverCodeRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<String> vConversionCode = null;

    public java.util.ArrayList<String> getVConversionCode()
    {
        return vConversionCode;
    }

    public void  setVConversionCode(java.util.ArrayList<String> vConversionCode)
    {
        this.vConversionCode = vConversionCode;
    }

    public GetConverCodeRsp()
    {
    }

    public GetConverCodeRsp(java.util.ArrayList<String> vConversionCode)
    {
        this.vConversionCode = vConversionCode;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vConversionCode) {
            ostream.writeList(0, vConversionCode);
        }
    }

    static java.util.ArrayList<String> VAR_TYPE_4_VCONVERSIONCODE = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VCONVERSIONCODE.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vConversionCode = (java.util.ArrayList<String>)istream.readList(0, false, VAR_TYPE_4_VCONVERSIONCODE);
    }

}

