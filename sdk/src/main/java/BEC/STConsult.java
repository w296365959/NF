package BEC;

public final class STConsult extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public float fVal = 0;

    public BEC.STRank stIndSTRank = null;

    public BEC.STRank stAllDtSTRank = null;

    public String sConsultDesc = "";

    public java.util.Map<String, Integer> mpDateNum = null;

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

    public BEC.STRank getStIndSTRank()
    {
        return stIndSTRank;
    }

    public void  setStIndSTRank(BEC.STRank stIndSTRank)
    {
        this.stIndSTRank = stIndSTRank;
    }

    public BEC.STRank getStAllDtSTRank()
    {
        return stAllDtSTRank;
    }

    public void  setStAllDtSTRank(BEC.STRank stAllDtSTRank)
    {
        this.stAllDtSTRank = stAllDtSTRank;
    }

    public String getSConsultDesc()
    {
        return sConsultDesc;
    }

    public void  setSConsultDesc(String sConsultDesc)
    {
        this.sConsultDesc = sConsultDesc;
    }

    public java.util.Map<String, Integer> getMpDateNum()
    {
        return mpDateNum;
    }

    public void  setMpDateNum(java.util.Map<String, Integer> mpDateNum)
    {
        this.mpDateNum = mpDateNum;
    }

    public STConsult()
    {
    }

    public STConsult(String sDtSecCode, float fVal, BEC.STRank stIndSTRank, BEC.STRank stAllDtSTRank, String sConsultDesc, java.util.Map<String, Integer> mpDateNum)
    {
        this.sDtSecCode = sDtSecCode;
        this.fVal = fVal;
        this.stIndSTRank = stIndSTRank;
        this.stAllDtSTRank = stAllDtSTRank;
        this.sConsultDesc = sConsultDesc;
        this.mpDateNum = mpDateNum;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        ostream.writeFloat(1, fVal);
        if (null != stIndSTRank) {
            ostream.writeMessage(2, stIndSTRank);
        }
        if (null != stAllDtSTRank) {
            ostream.writeMessage(3, stAllDtSTRank);
        }
        if (null != sConsultDesc) {
            ostream.writeString(4, sConsultDesc);
        }
        if (null != mpDateNum) {
            ostream.writeMap(5, mpDateNum);
        }
    }

    static BEC.STRank VAR_TYPE_4_STINDSTRANK = new BEC.STRank();

    static BEC.STRank VAR_TYPE_4_STALLDTSTRANK = new BEC.STRank();

    static java.util.Map<String, Integer> VAR_TYPE_4_MPDATENUM = new java.util.HashMap<String, Integer>();
    static {
        VAR_TYPE_4_MPDATENUM.put("", 0);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.fVal = (float)istream.readFloat(1, false, this.fVal);
        this.stIndSTRank = (BEC.STRank)istream.readMessage(2, false, VAR_TYPE_4_STINDSTRANK);
        this.stAllDtSTRank = (BEC.STRank)istream.readMessage(3, false, VAR_TYPE_4_STALLDTSTRANK);
        this.sConsultDesc = (String)istream.readString(4, false, this.sConsultDesc);
        this.mpDateNum = (java.util.Map<String, Integer>)istream.readMap(5, false, VAR_TYPE_4_MPDATENUM);
    }

}

