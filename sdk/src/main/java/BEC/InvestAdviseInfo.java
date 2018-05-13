package BEC;

public final class InvestAdviseInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sId = "";

    public String sQuestion = "";

    public String sAnwser = "";

    public BEC.InvestAdvisor stInvestAdvisor = null;

    public int iUpdateTime = 0;

    public String getSId()
    {
        return sId;
    }

    public void  setSId(String sId)
    {
        this.sId = sId;
    }

    public String getSQuestion()
    {
        return sQuestion;
    }

    public void  setSQuestion(String sQuestion)
    {
        this.sQuestion = sQuestion;
    }

    public String getSAnwser()
    {
        return sAnwser;
    }

    public void  setSAnwser(String sAnwser)
    {
        this.sAnwser = sAnwser;
    }

    public BEC.InvestAdvisor getStInvestAdvisor()
    {
        return stInvestAdvisor;
    }

    public void  setStInvestAdvisor(BEC.InvestAdvisor stInvestAdvisor)
    {
        this.stInvestAdvisor = stInvestAdvisor;
    }

    public int getIUpdateTime()
    {
        return iUpdateTime;
    }

    public void  setIUpdateTime(int iUpdateTime)
    {
        this.iUpdateTime = iUpdateTime;
    }

    public InvestAdviseInfo()
    {
    }

    public InvestAdviseInfo(String sId, String sQuestion, String sAnwser, BEC.InvestAdvisor stInvestAdvisor, int iUpdateTime)
    {
        this.sId = sId;
        this.sQuestion = sQuestion;
        this.sAnwser = sAnwser;
        this.stInvestAdvisor = stInvestAdvisor;
        this.iUpdateTime = iUpdateTime;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sId) {
            ostream.writeString(0, sId);
        }
        if (null != sQuestion) {
            ostream.writeString(1, sQuestion);
        }
        if (null != sAnwser) {
            ostream.writeString(2, sAnwser);
        }
        if (null != stInvestAdvisor) {
            ostream.writeMessage(3, stInvestAdvisor);
        }
        ostream.writeInt32(4, iUpdateTime);
    }

    static BEC.InvestAdvisor VAR_TYPE_4_STINVESTADVISOR = new BEC.InvestAdvisor();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sId = (String)istream.readString(0, false, this.sId);
        this.sQuestion = (String)istream.readString(1, false, this.sQuestion);
        this.sAnwser = (String)istream.readString(2, false, this.sAnwser);
        this.stInvestAdvisor = (BEC.InvestAdvisor)istream.readMessage(3, false, VAR_TYPE_4_STINVESTADVISOR);
        this.iUpdateTime = (int)istream.readInt32(4, false, this.iUpdateTime);
    }

}

