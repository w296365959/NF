package BEC;

public final class HisChipDistRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.ChipDistRsp> vtChipDistRsp = null;

    public String sChangeDesc = "";

    public BEC.BackTraceRs stBackTraceRs = null;

    public int iShowMaxDay = 0;

    public String sMainChgDesc = "";

    public java.util.ArrayList<BEC.ChipDistRsp> getVtChipDistRsp()
    {
        return vtChipDistRsp;
    }

    public void  setVtChipDistRsp(java.util.ArrayList<BEC.ChipDistRsp> vtChipDistRsp)
    {
        this.vtChipDistRsp = vtChipDistRsp;
    }

    public String getSChangeDesc()
    {
        return sChangeDesc;
    }

    public void  setSChangeDesc(String sChangeDesc)
    {
        this.sChangeDesc = sChangeDesc;
    }

    public BEC.BackTraceRs getStBackTraceRs()
    {
        return stBackTraceRs;
    }

    public void  setStBackTraceRs(BEC.BackTraceRs stBackTraceRs)
    {
        this.stBackTraceRs = stBackTraceRs;
    }

    public int getIShowMaxDay()
    {
        return iShowMaxDay;
    }

    public void  setIShowMaxDay(int iShowMaxDay)
    {
        this.iShowMaxDay = iShowMaxDay;
    }

    public String getSMainChgDesc()
    {
        return sMainChgDesc;
    }

    public void  setSMainChgDesc(String sMainChgDesc)
    {
        this.sMainChgDesc = sMainChgDesc;
    }

    public HisChipDistRsp()
    {
    }

    public HisChipDistRsp(java.util.ArrayList<BEC.ChipDistRsp> vtChipDistRsp, String sChangeDesc, BEC.BackTraceRs stBackTraceRs, int iShowMaxDay, String sMainChgDesc)
    {
        this.vtChipDistRsp = vtChipDistRsp;
        this.sChangeDesc = sChangeDesc;
        this.stBackTraceRs = stBackTraceRs;
        this.iShowMaxDay = iShowMaxDay;
        this.sMainChgDesc = sMainChgDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtChipDistRsp) {
            ostream.writeList(0, vtChipDistRsp);
        }
        if (null != sChangeDesc) {
            ostream.writeString(1, sChangeDesc);
        }
        if (null != stBackTraceRs) {
            ostream.writeMessage(2, stBackTraceRs);
        }
        ostream.writeInt32(3, iShowMaxDay);
        if (null != sMainChgDesc) {
            ostream.writeString(4, sMainChgDesc);
        }
    }

    static java.util.ArrayList<BEC.ChipDistRsp> VAR_TYPE_4_VTCHIPDISTRSP = new java.util.ArrayList<BEC.ChipDistRsp>();
    static {
        VAR_TYPE_4_VTCHIPDISTRSP.add(new BEC.ChipDistRsp());
    }

    static BEC.BackTraceRs VAR_TYPE_4_STBACKTRACERS = new BEC.BackTraceRs();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtChipDistRsp = (java.util.ArrayList<BEC.ChipDistRsp>)istream.readList(0, false, VAR_TYPE_4_VTCHIPDISTRSP);
        this.sChangeDesc = (String)istream.readString(1, false, this.sChangeDesc);
        this.stBackTraceRs = (BEC.BackTraceRs)istream.readMessage(2, false, VAR_TYPE_4_STBACKTRACERS);
        this.iShowMaxDay = (int)istream.readInt32(3, false, this.iShowMaxDay);
        this.sMainChgDesc = (String)istream.readString(4, false, this.sMainChgDesc);
    }

}

