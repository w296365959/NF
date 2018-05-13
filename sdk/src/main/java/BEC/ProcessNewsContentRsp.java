package BEC;

public final class ProcessNewsContentRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sResult = "";

    public String getSResult()
    {
        return sResult;
    }

    public void  setSResult(String sResult)
    {
        this.sResult = sResult;
    }

    public ProcessNewsContentRsp()
    {
    }

    public ProcessNewsContentRsp(String sResult)
    {
        this.sResult = sResult;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sResult) {
            ostream.writeString(0, sResult);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sResult = (String)istream.readString(0, false, this.sResult);
    }

}

