package BEC;

public final class GetPayOrderIdRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public DtPayItem stDtPayItem = null;

    public DtPayItem getStDtPayItem()
    {
        return stDtPayItem;
    }

    public void  setStDtPayItem(DtPayItem stDtPayItem)
    {
        this.stDtPayItem = stDtPayItem;
    }

    public GetPayOrderIdRsp()
    {
    }

    public GetPayOrderIdRsp(DtPayItem stDtPayItem)
    {
        this.stDtPayItem = stDtPayItem;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stDtPayItem) {
            ostream.writeMessage(0, stDtPayItem);
        }
    }

    static DtPayItem VAR_TYPE_4_STDTPAYITEM = new DtPayItem();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stDtPayItem = (DtPayItem)istream.readMessage(0, false, VAR_TYPE_4_STDTPAYITEM);
    }

}

