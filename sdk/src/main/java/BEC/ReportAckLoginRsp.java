package BEC;

public final class ReportAckLoginRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sTipMsg = "";

    public String getSTipMsg()
    {
        return sTipMsg;
    }

    public void  setSTipMsg(String sTipMsg)
    {
        this.sTipMsg = sTipMsg;
    }

    public ReportAckLoginRsp()
    {
    }

    public ReportAckLoginRsp(String sTipMsg)
    {
        this.sTipMsg = sTipMsg;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sTipMsg) {
            ostream.writeString(0, sTipMsg);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sTipMsg = (String)istream.readString(0, false, this.sTipMsg);
    }

}

