package BEC;

public final class SecLiveMsgRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<SecLiveMsg> vSecLiveMsg = null;

    public int iLiveMsgStatus = 0;

    public java.util.ArrayList<SecLiveMsg> getVSecLiveMsg()
    {
        return vSecLiveMsg;
    }

    public void  setVSecLiveMsg(java.util.ArrayList<SecLiveMsg> vSecLiveMsg)
    {
        this.vSecLiveMsg = vSecLiveMsg;
    }

    public int getILiveMsgStatus()
    {
        return iLiveMsgStatus;
    }

    public void  setILiveMsgStatus(int iLiveMsgStatus)
    {
        this.iLiveMsgStatus = iLiveMsgStatus;
    }

    public SecLiveMsgRsp()
    {
    }

    public SecLiveMsgRsp(java.util.ArrayList<SecLiveMsg> vSecLiveMsg, int iLiveMsgStatus)
    {
        this.vSecLiveMsg = vSecLiveMsg;
        this.iLiveMsgStatus = iLiveMsgStatus;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vSecLiveMsg) {
            ostream.writeList(0, vSecLiveMsg);
        }
        ostream.writeInt32(1, iLiveMsgStatus);
    }

    static java.util.ArrayList<SecLiveMsg> VAR_TYPE_4_VSECLIVEMSG = new java.util.ArrayList<SecLiveMsg>();
    static {
        VAR_TYPE_4_VSECLIVEMSG.add(new SecLiveMsg());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vSecLiveMsg = (java.util.ArrayList<SecLiveMsg>)istream.readList(0, false, VAR_TYPE_4_VSECLIVEMSG);
        this.iLiveMsgStatus = (int)istream.readInt32(1, false, this.iLiveMsgStatus);
    }

}

