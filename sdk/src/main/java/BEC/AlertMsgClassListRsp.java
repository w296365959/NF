package BEC;

public final class AlertMsgClassListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<AlertMsgClassDesc> vAlertMsgClassDesc = null;

    public java.util.ArrayList<AlertMsgClassDesc> getVAlertMsgClassDesc()
    {
        return vAlertMsgClassDesc;
    }

    public void  setVAlertMsgClassDesc(java.util.ArrayList<AlertMsgClassDesc> vAlertMsgClassDesc)
    {
        this.vAlertMsgClassDesc = vAlertMsgClassDesc;
    }

    public AlertMsgClassListRsp()
    {
    }

    public AlertMsgClassListRsp(java.util.ArrayList<AlertMsgClassDesc> vAlertMsgClassDesc)
    {
        this.vAlertMsgClassDesc = vAlertMsgClassDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vAlertMsgClassDesc) {
            ostream.writeList(0, vAlertMsgClassDesc);
        }
    }

    static java.util.ArrayList<AlertMsgClassDesc> VAR_TYPE_4_VALERTMSGCLASSDESC = new java.util.ArrayList<AlertMsgClassDesc>();
    static {
        VAR_TYPE_4_VALERTMSGCLASSDESC.add(new AlertMsgClassDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vAlertMsgClassDesc = (java.util.ArrayList<AlertMsgClassDesc>)istream.readList(0, false, VAR_TYPE_4_VALERTMSGCLASSDESC);
    }

}

