package BEC;

public final class GetCalendarEventsRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sStartDate = "";

    public String sEndDate = "";

    public java.util.ArrayList<CalendarEventDetail> vtCalendarEventDetail = null;

    public String getSStartDate()
    {
        return sStartDate;
    }

    public void  setSStartDate(String sStartDate)
    {
        this.sStartDate = sStartDate;
    }

    public String getSEndDate()
    {
        return sEndDate;
    }

    public void  setSEndDate(String sEndDate)
    {
        this.sEndDate = sEndDate;
    }

    public java.util.ArrayList<CalendarEventDetail> getVtCalendarEventDetail()
    {
        return vtCalendarEventDetail;
    }

    public void  setVtCalendarEventDetail(java.util.ArrayList<CalendarEventDetail> vtCalendarEventDetail)
    {
        this.vtCalendarEventDetail = vtCalendarEventDetail;
    }

    public GetCalendarEventsRsp()
    {
    }

    public GetCalendarEventsRsp(String sStartDate, String sEndDate, java.util.ArrayList<CalendarEventDetail> vtCalendarEventDetail)
    {
        this.sStartDate = sStartDate;
        this.sEndDate = sEndDate;
        this.vtCalendarEventDetail = vtCalendarEventDetail;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sStartDate) {
            ostream.writeString(0, sStartDate);
        }
        if (null != sEndDate) {
            ostream.writeString(1, sEndDate);
        }
        if (null != vtCalendarEventDetail) {
            ostream.writeList(2, vtCalendarEventDetail);
        }
    }

    static java.util.ArrayList<CalendarEventDetail> VAR_TYPE_4_VTCALENDAREVENTDETAIL = new java.util.ArrayList<CalendarEventDetail>();
    static {
        VAR_TYPE_4_VTCALENDAREVENTDETAIL.add(new CalendarEventDetail());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sStartDate = (String)istream.readString(0, false, this.sStartDate);
        this.sEndDate = (String)istream.readString(1, false, this.sEndDate);
        this.vtCalendarEventDetail = (java.util.ArrayList<CalendarEventDetail>)istream.readList(2, false, VAR_TYPE_4_VTCALENDAREVENTDETAIL);
    }

}

