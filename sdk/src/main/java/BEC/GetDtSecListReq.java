package BEC;

public final class GetDtSecListReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sComeFrom = "changzheng";

    public String getSComeFrom()
    {
        return sComeFrom;
    }

    public void  setSComeFrom(String sComeFrom)
    {
        this.sComeFrom = sComeFrom;
    }

    public GetDtSecListReq()
    {
    }

    public GetDtSecListReq(String sComeFrom)
    {
        this.sComeFrom = sComeFrom;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sComeFrom) {
            ostream.writeString(0, sComeFrom);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sComeFrom = (String)istream.readString(0, false, this.sComeFrom);
    }

}

