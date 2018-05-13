package BEC;

public final class GetLHBDetailRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public java.util.ArrayList<LHBReason> vReason = null;

    public String sDay = "";

    public String sWeekDay = "";

    public java.util.ArrayList<String> vDay = null;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public java.util.ArrayList<LHBReason> getVReason()
    {
        return vReason;
    }

    public void  setVReason(java.util.ArrayList<LHBReason> vReason)
    {
        this.vReason = vReason;
    }

    public String getSDay()
    {
        return sDay;
    }

    public void  setSDay(String sDay)
    {
        this.sDay = sDay;
    }

    public String getSWeekDay()
    {
        return sWeekDay;
    }

    public void  setSWeekDay(String sWeekDay)
    {
        this.sWeekDay = sWeekDay;
    }

    public java.util.ArrayList<String> getVDay()
    {
        return vDay;
    }

    public void  setVDay(java.util.ArrayList<String> vDay)
    {
        this.vDay = vDay;
    }

    public GetLHBDetailRsp()
    {
    }

    public GetLHBDetailRsp(String sDtSecCode, java.util.ArrayList<LHBReason> vReason, String sDay, String sWeekDay, java.util.ArrayList<String> vDay)
    {
        this.sDtSecCode = sDtSecCode;
        this.vReason = vReason;
        this.sDay = sDay;
        this.sWeekDay = sWeekDay;
        this.vDay = vDay;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        if (null != vReason) {
            ostream.writeList(1, vReason);
        }
        if (null != sDay) {
            ostream.writeString(2, sDay);
        }
        if (null != sWeekDay) {
            ostream.writeString(3, sWeekDay);
        }
        if (null != vDay) {
            ostream.writeList(4, vDay);
        }
    }

    static java.util.ArrayList<LHBReason> VAR_TYPE_4_VREASON = new java.util.ArrayList<LHBReason>();
    static {
        VAR_TYPE_4_VREASON.add(new LHBReason());
    }

    static java.util.ArrayList<String> VAR_TYPE_4_VDAY = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VDAY.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.vReason = (java.util.ArrayList<LHBReason>)istream.readList(1, false, VAR_TYPE_4_VREASON);
        this.sDay = (String)istream.readString(2, false, this.sDay);
        this.sWeekDay = (String)istream.readString(3, false, this.sWeekDay);
        this.vDay = (java.util.ArrayList<String>)istream.readList(4, false, VAR_TYPE_4_VDAY);
    }

}

