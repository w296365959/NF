package BEC;

public final class SendSmsReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<String> vtPhone = null;

    public String sContent = "";

    public java.util.ArrayList<String> getVtPhone()
    {
        return vtPhone;
    }

    public void  setVtPhone(java.util.ArrayList<String> vtPhone)
    {
        this.vtPhone = vtPhone;
    }

    public String getSContent()
    {
        return sContent;
    }

    public void  setSContent(String sContent)
    {
        this.sContent = sContent;
    }

    public SendSmsReq()
    {
    }

    public SendSmsReq(java.util.ArrayList<String> vtPhone, String sContent)
    {
        this.vtPhone = vtPhone;
        this.sContent = sContent;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtPhone) {
            ostream.writeList(0, vtPhone);
        }
        if (null != sContent) {
            ostream.writeString(1, sContent);
        }
    }

    static java.util.ArrayList<String> VAR_TYPE_4_VTPHONE = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VTPHONE.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtPhone = (java.util.ArrayList<String>)istream.readList(0, false, VAR_TYPE_4_VTPHONE);
        this.sContent = (String)istream.readString(1, false, this.sContent);
    }

}

