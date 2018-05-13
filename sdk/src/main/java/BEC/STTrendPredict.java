package BEC;

public final class STTrendPredict extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public String sDate = "";

    public java.util.ArrayList<Float> vtClose = null;

    public float fScore = 0;

    public String sDesc = "";

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public String getSDate()
    {
        return sDate;
    }

    public void  setSDate(String sDate)
    {
        this.sDate = sDate;
    }

    public java.util.ArrayList<Float> getVtClose()
    {
        return vtClose;
    }

    public void  setVtClose(java.util.ArrayList<Float> vtClose)
    {
        this.vtClose = vtClose;
    }

    public float getFScore()
    {
        return fScore;
    }

    public void  setFScore(float fScore)
    {
        this.fScore = fScore;
    }

    public String getSDesc()
    {
        return sDesc;
    }

    public void  setSDesc(String sDesc)
    {
        this.sDesc = sDesc;
    }

    public STTrendPredict()
    {
    }

    public STTrendPredict(String sDtSecCode, String sDate, java.util.ArrayList<Float> vtClose, float fScore, String sDesc)
    {
        this.sDtSecCode = sDtSecCode;
        this.sDate = sDate;
        this.vtClose = vtClose;
        this.fScore = fScore;
        this.sDesc = sDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        if (null != sDate) {
            ostream.writeString(1, sDate);
        }
        if (null != vtClose) {
            ostream.writeList(2, vtClose);
        }
        ostream.writeFloat(3, fScore);
        if (null != sDesc) {
            ostream.writeString(4, sDesc);
        }
    }

    static java.util.ArrayList<Float> VAR_TYPE_4_VTCLOSE = new java.util.ArrayList<Float>();
    static {
        VAR_TYPE_4_VTCLOSE.add(0.0f);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.sDate = (String)istream.readString(1, false, this.sDate);
        this.vtClose = (java.util.ArrayList<Float>)istream.readList(2, false, VAR_TYPE_4_VTCLOSE);
        this.fScore = (float)istream.readFloat(3, false, this.fScore);
        this.sDesc = (String)istream.readString(4, false, this.sDesc);
    }

}

