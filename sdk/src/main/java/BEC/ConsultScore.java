package BEC;

public final class ConsultScore extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public BEC.ScoreDesc stRiseScoreDesc = null;

    public BEC.ScoreDesc stMktHotScoreDesc = null;

    public BEC.ScoreDesc stMainScoreDesc = null;

    public BEC.ScoreDesc stTrendScoreDesc = null;

    public BEC.ScoreDesc stValueScoreDesc = null;

    public BEC.ScoreDesc stConsultScoreDesc = null;

    public float fVal = 0;

    public String sScoreDesc = "";

    public BEC.ScoreDesc stRiseNewScoreDesc = null;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public BEC.ScoreDesc getStRiseScoreDesc()
    {
        return stRiseScoreDesc;
    }

    public void  setStRiseScoreDesc(BEC.ScoreDesc stRiseScoreDesc)
    {
        this.stRiseScoreDesc = stRiseScoreDesc;
    }

    public BEC.ScoreDesc getStMktHotScoreDesc()
    {
        return stMktHotScoreDesc;
    }

    public void  setStMktHotScoreDesc(BEC.ScoreDesc stMktHotScoreDesc)
    {
        this.stMktHotScoreDesc = stMktHotScoreDesc;
    }

    public BEC.ScoreDesc getStMainScoreDesc()
    {
        return stMainScoreDesc;
    }

    public void  setStMainScoreDesc(BEC.ScoreDesc stMainScoreDesc)
    {
        this.stMainScoreDesc = stMainScoreDesc;
    }

    public BEC.ScoreDesc getStTrendScoreDesc()
    {
        return stTrendScoreDesc;
    }

    public void  setStTrendScoreDesc(BEC.ScoreDesc stTrendScoreDesc)
    {
        this.stTrendScoreDesc = stTrendScoreDesc;
    }

    public BEC.ScoreDesc getStValueScoreDesc()
    {
        return stValueScoreDesc;
    }

    public void  setStValueScoreDesc(BEC.ScoreDesc stValueScoreDesc)
    {
        this.stValueScoreDesc = stValueScoreDesc;
    }

    public BEC.ScoreDesc getStConsultScoreDesc()
    {
        return stConsultScoreDesc;
    }

    public void  setStConsultScoreDesc(BEC.ScoreDesc stConsultScoreDesc)
    {
        this.stConsultScoreDesc = stConsultScoreDesc;
    }

    public float getFVal()
    {
        return fVal;
    }

    public void  setFVal(float fVal)
    {
        this.fVal = fVal;
    }

    public String getSScoreDesc()
    {
        return sScoreDesc;
    }

    public void  setSScoreDesc(String sScoreDesc)
    {
        this.sScoreDesc = sScoreDesc;
    }

    public BEC.ScoreDesc getStRiseNewScoreDesc()
    {
        return stRiseNewScoreDesc;
    }

    public void  setStRiseNewScoreDesc(BEC.ScoreDesc stRiseNewScoreDesc)
    {
        this.stRiseNewScoreDesc = stRiseNewScoreDesc;
    }

    public ConsultScore()
    {
    }

    public ConsultScore(String sDtSecCode, BEC.ScoreDesc stRiseScoreDesc, BEC.ScoreDesc stMktHotScoreDesc, BEC.ScoreDesc stMainScoreDesc, BEC.ScoreDesc stTrendScoreDesc, BEC.ScoreDesc stValueScoreDesc, BEC.ScoreDesc stConsultScoreDesc, float fVal, String sScoreDesc, BEC.ScoreDesc stRiseNewScoreDesc)
    {
        this.sDtSecCode = sDtSecCode;
        this.stRiseScoreDesc = stRiseScoreDesc;
        this.stMktHotScoreDesc = stMktHotScoreDesc;
        this.stMainScoreDesc = stMainScoreDesc;
        this.stTrendScoreDesc = stTrendScoreDesc;
        this.stValueScoreDesc = stValueScoreDesc;
        this.stConsultScoreDesc = stConsultScoreDesc;
        this.fVal = fVal;
        this.sScoreDesc = sScoreDesc;
        this.stRiseNewScoreDesc = stRiseNewScoreDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        if (null != stRiseScoreDesc) {
            ostream.writeMessage(1, stRiseScoreDesc);
        }
        if (null != stMktHotScoreDesc) {
            ostream.writeMessage(2, stMktHotScoreDesc);
        }
        if (null != stMainScoreDesc) {
            ostream.writeMessage(3, stMainScoreDesc);
        }
        if (null != stTrendScoreDesc) {
            ostream.writeMessage(4, stTrendScoreDesc);
        }
        if (null != stValueScoreDesc) {
            ostream.writeMessage(5, stValueScoreDesc);
        }
        if (null != stConsultScoreDesc) {
            ostream.writeMessage(6, stConsultScoreDesc);
        }
        ostream.writeFloat(7, fVal);
        if (null != sScoreDesc) {
            ostream.writeString(8, sScoreDesc);
        }
        if (null != stRiseNewScoreDesc) {
            ostream.writeMessage(9, stRiseNewScoreDesc);
        }
    }

    static BEC.ScoreDesc VAR_TYPE_4_STRISESCOREDESC = new BEC.ScoreDesc();

    static BEC.ScoreDesc VAR_TYPE_4_STMKTHOTSCOREDESC = new BEC.ScoreDesc();

    static BEC.ScoreDesc VAR_TYPE_4_STMAINSCOREDESC = new BEC.ScoreDesc();

    static BEC.ScoreDesc VAR_TYPE_4_STTRENDSCOREDESC = new BEC.ScoreDesc();

    static BEC.ScoreDesc VAR_TYPE_4_STVALUESCOREDESC = new BEC.ScoreDesc();

    static BEC.ScoreDesc VAR_TYPE_4_STCONSULTSCOREDESC = new BEC.ScoreDesc();

    static BEC.ScoreDesc VAR_TYPE_4_STRISENEWSCOREDESC = new BEC.ScoreDesc();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.stRiseScoreDesc = (BEC.ScoreDesc)istream.readMessage(1, false, VAR_TYPE_4_STRISESCOREDESC);
        this.stMktHotScoreDesc = (BEC.ScoreDesc)istream.readMessage(2, false, VAR_TYPE_4_STMKTHOTSCOREDESC);
        this.stMainScoreDesc = (BEC.ScoreDesc)istream.readMessage(3, false, VAR_TYPE_4_STMAINSCOREDESC);
        this.stTrendScoreDesc = (BEC.ScoreDesc)istream.readMessage(4, false, VAR_TYPE_4_STTRENDSCOREDESC);
        this.stValueScoreDesc = (BEC.ScoreDesc)istream.readMessage(5, false, VAR_TYPE_4_STVALUESCOREDESC);
        this.stConsultScoreDesc = (BEC.ScoreDesc)istream.readMessage(6, false, VAR_TYPE_4_STCONSULTSCOREDESC);
        this.fVal = (float)istream.readFloat(7, false, this.fVal);
        this.sScoreDesc = (String)istream.readString(8, false, this.sScoreDesc);
        this.stRiseNewScoreDesc = (BEC.ScoreDesc)istream.readMessage(9, false, VAR_TYPE_4_STRISENEWSCOREDESC);
    }

}

