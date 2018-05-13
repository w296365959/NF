package BEC;

public final class ReportScanRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sTicket = "";

    public String sTipMsg = "";

    public String sTargetMsg = "";

    public String getSTicket()
    {
        return sTicket;
    }

    public void  setSTicket(String sTicket)
    {
        this.sTicket = sTicket;
    }

    public String getSTipMsg()
    {
        return sTipMsg;
    }

    public void  setSTipMsg(String sTipMsg)
    {
        this.sTipMsg = sTipMsg;
    }

    public String getSTargetMsg()
    {
        return sTargetMsg;
    }

    public void  setSTargetMsg(String sTargetMsg)
    {
        this.sTargetMsg = sTargetMsg;
    }

    public ReportScanRsp()
    {
    }

    public ReportScanRsp(String sTicket, String sTipMsg, String sTargetMsg)
    {
        this.sTicket = sTicket;
        this.sTipMsg = sTipMsg;
        this.sTargetMsg = sTargetMsg;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sTicket) {
            ostream.writeString(0, sTicket);
        }
        if (null != sTipMsg) {
            ostream.writeString(1, sTipMsg);
        }
        if (null != sTargetMsg) {
            ostream.writeString(2, sTargetMsg);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sTicket = (String)istream.readString(0, false, this.sTicket);
        this.sTipMsg = (String)istream.readString(1, false, this.sTipMsg);
        this.sTargetMsg = (String)istream.readString(2, false, this.sTargetMsg);
    }

}

