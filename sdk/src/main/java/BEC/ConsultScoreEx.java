package BEC;

public final class ConsultScoreEx extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public java.util.Map<String, BEC.ScoreDesc> mpTypeScoreDesc = null;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public java.util.Map<String, BEC.ScoreDesc> getMpTypeScoreDesc()
    {
        return mpTypeScoreDesc;
    }

    public void  setMpTypeScoreDesc(java.util.Map<String, BEC.ScoreDesc> mpTypeScoreDesc)
    {
        this.mpTypeScoreDesc = mpTypeScoreDesc;
    }

    public ConsultScoreEx()
    {
    }

    public ConsultScoreEx(String sDtSecCode, java.util.Map<String, BEC.ScoreDesc> mpTypeScoreDesc)
    {
        this.sDtSecCode = sDtSecCode;
        this.mpTypeScoreDesc = mpTypeScoreDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        if (null != mpTypeScoreDesc) {
            ostream.writeMap(1, mpTypeScoreDesc);
        }
    }

    static java.util.Map<String, BEC.ScoreDesc> VAR_TYPE_4_MPTYPESCOREDESC = new java.util.HashMap<String, BEC.ScoreDesc>();
    static {
        VAR_TYPE_4_MPTYPESCOREDESC.put("", new BEC.ScoreDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.mpTypeScoreDesc = (java.util.Map<String, BEC.ScoreDesc>)istream.readMap(1, false, VAR_TYPE_4_MPTYPESCOREDESC);
    }

}

