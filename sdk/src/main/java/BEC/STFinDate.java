package BEC;

public final class STFinDate extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public float fVal = 0;

    public java.util.ArrayList<BEC.STDateValue> vtDateValue = null;

    public java.util.ArrayList<BEC.STDateValue> vtDateValueAvg = null;

    public BEC.STRank stIndSTRank = null;

    public String sFinanceDesc = "";

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public float getFVal()
    {
        return fVal;
    }

    public void  setFVal(float fVal)
    {
        this.fVal = fVal;
    }

    public java.util.ArrayList<BEC.STDateValue> getVtDateValue()
    {
        return vtDateValue;
    }

    public void  setVtDateValue(java.util.ArrayList<BEC.STDateValue> vtDateValue)
    {
        this.vtDateValue = vtDateValue;
    }

    public java.util.ArrayList<BEC.STDateValue> getVtDateValueAvg()
    {
        return vtDateValueAvg;
    }

    public void  setVtDateValueAvg(java.util.ArrayList<BEC.STDateValue> vtDateValueAvg)
    {
        this.vtDateValueAvg = vtDateValueAvg;
    }

    public BEC.STRank getStIndSTRank()
    {
        return stIndSTRank;
    }

    public void  setStIndSTRank(BEC.STRank stIndSTRank)
    {
        this.stIndSTRank = stIndSTRank;
    }

    public String getSFinanceDesc()
    {
        return sFinanceDesc;
    }

    public void  setSFinanceDesc(String sFinanceDesc)
    {
        this.sFinanceDesc = sFinanceDesc;
    }

    public STFinDate()
    {
    }

    public STFinDate(String sDtSecCode, float fVal, java.util.ArrayList<BEC.STDateValue> vtDateValue, java.util.ArrayList<BEC.STDateValue> vtDateValueAvg, BEC.STRank stIndSTRank, String sFinanceDesc)
    {
        this.sDtSecCode = sDtSecCode;
        this.fVal = fVal;
        this.vtDateValue = vtDateValue;
        this.vtDateValueAvg = vtDateValueAvg;
        this.stIndSTRank = stIndSTRank;
        this.sFinanceDesc = sFinanceDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        ostream.writeFloat(1, fVal);
        if (null != vtDateValue) {
            ostream.writeList(2, vtDateValue);
        }
        if (null != vtDateValueAvg) {
            ostream.writeList(3, vtDateValueAvg);
        }
        if (null != stIndSTRank) {
            ostream.writeMessage(4, stIndSTRank);
        }
        if (null != sFinanceDesc) {
            ostream.writeString(5, sFinanceDesc);
        }
    }

    static java.util.ArrayList<BEC.STDateValue> VAR_TYPE_4_VTDATEVALUE = new java.util.ArrayList<BEC.STDateValue>();
    static {
        VAR_TYPE_4_VTDATEVALUE.add(new BEC.STDateValue());
    }

    static java.util.ArrayList<BEC.STDateValue> VAR_TYPE_4_VTDATEVALUEAVG = new java.util.ArrayList<BEC.STDateValue>();
    static {
        VAR_TYPE_4_VTDATEVALUEAVG.add(new BEC.STDateValue());
    }

    static BEC.STRank VAR_TYPE_4_STINDSTRANK = new BEC.STRank();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.fVal = (float)istream.readFloat(1, false, this.fVal);
        this.vtDateValue = (java.util.ArrayList<BEC.STDateValue>)istream.readList(2, false, VAR_TYPE_4_VTDATEVALUE);
        this.vtDateValueAvg = (java.util.ArrayList<BEC.STDateValue>)istream.readList(3, false, VAR_TYPE_4_VTDATEVALUEAVG);
        this.stIndSTRank = (BEC.STRank)istream.readMessage(4, false, VAR_TYPE_4_STINDSTRANK);
        this.sFinanceDesc = (String)istream.readString(5, false, this.sFinanceDesc);
    }

}

