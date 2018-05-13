package BEC;

public final class LHBReason extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sReasonMsg = "";

    public java.util.ArrayList<LHBReasonDetail> vDetail = null;

    public String getSReasonMsg()
    {
        return sReasonMsg;
    }

    public void  setSReasonMsg(String sReasonMsg)
    {
        this.sReasonMsg = sReasonMsg;
    }

    public java.util.ArrayList<LHBReasonDetail> getVDetail()
    {
        return vDetail;
    }

    public void  setVDetail(java.util.ArrayList<LHBReasonDetail> vDetail)
    {
        this.vDetail = vDetail;
    }

    public LHBReason()
    {
    }

    public LHBReason(String sReasonMsg, java.util.ArrayList<LHBReasonDetail> vDetail)
    {
        this.sReasonMsg = sReasonMsg;
        this.vDetail = vDetail;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sReasonMsg) {
            ostream.writeString(0, sReasonMsg);
        }
        if (null != vDetail) {
            ostream.writeList(1, vDetail);
        }
    }

    static java.util.ArrayList<LHBReasonDetail> VAR_TYPE_4_VDETAIL = new java.util.ArrayList<LHBReasonDetail>();
    static {
        VAR_TYPE_4_VDETAIL.add(new LHBReasonDetail());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sReasonMsg = (String)istream.readString(0, false, this.sReasonMsg);
        this.vDetail = (java.util.ArrayList<LHBReasonDetail>)istream.readList(1, false, VAR_TYPE_4_VDETAIL);
    }

}

