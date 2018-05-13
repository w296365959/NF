package BEC;

public final class RiskMatchResult extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sMatchName = "";

    public String sUserAnswers = "";

    public String sProductEval = "";

    public int iMatchResult = 0;

    public String getSMatchName()
    {
        return sMatchName;
    }

    public void  setSMatchName(String sMatchName)
    {
        this.sMatchName = sMatchName;
    }

    public String getSUserAnswers()
    {
        return sUserAnswers;
    }

    public void  setSUserAnswers(String sUserAnswers)
    {
        this.sUserAnswers = sUserAnswers;
    }

    public String getSProductEval()
    {
        return sProductEval;
    }

    public void  setSProductEval(String sProductEval)
    {
        this.sProductEval = sProductEval;
    }

    public int getIMatchResult()
    {
        return iMatchResult;
    }

    public void  setIMatchResult(int iMatchResult)
    {
        this.iMatchResult = iMatchResult;
    }

    public RiskMatchResult()
    {
    }

    public RiskMatchResult(String sMatchName, String sUserAnswers, String sProductEval, int iMatchResult)
    {
        this.sMatchName = sMatchName;
        this.sUserAnswers = sUserAnswers;
        this.sProductEval = sProductEval;
        this.iMatchResult = iMatchResult;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sMatchName) {
            ostream.writeString(0, sMatchName);
        }
        if (null != sUserAnswers) {
            ostream.writeString(1, sUserAnswers);
        }
        if (null != sProductEval) {
            ostream.writeString(2, sProductEval);
        }
        ostream.writeInt32(3, iMatchResult);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sMatchName = (String)istream.readString(0, false, this.sMatchName);
        this.sUserAnswers = (String)istream.readString(1, false, this.sUserAnswers);
        this.sProductEval = (String)istream.readString(2, false, this.sProductEval);
        this.iMatchResult = (int)istream.readInt32(3, false, this.iMatchResult);
    }

}

