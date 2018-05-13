package BEC;

public final class DongmiQaDetail extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sQaKey = "";

    public String sDtSecCode = "";

    public String sQuestioner = "";

    public int iQuestionTime = 0;

    public String sQuestion = "";

    public String sAnswerer = "";

    public int iAnswerTime = 0;

    public String sAnswer = "";

    public String sFrom = "";

    public String getSQaKey()
    {
        return sQaKey;
    }

    public void  setSQaKey(String sQaKey)
    {
        this.sQaKey = sQaKey;
    }

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public String getSQuestioner()
    {
        return sQuestioner;
    }

    public void  setSQuestioner(String sQuestioner)
    {
        this.sQuestioner = sQuestioner;
    }

    public int getIQuestionTime()
    {
        return iQuestionTime;
    }

    public void  setIQuestionTime(int iQuestionTime)
    {
        this.iQuestionTime = iQuestionTime;
    }

    public String getSQuestion()
    {
        return sQuestion;
    }

    public void  setSQuestion(String sQuestion)
    {
        this.sQuestion = sQuestion;
    }

    public String getSAnswerer()
    {
        return sAnswerer;
    }

    public void  setSAnswerer(String sAnswerer)
    {
        this.sAnswerer = sAnswerer;
    }

    public int getIAnswerTime()
    {
        return iAnswerTime;
    }

    public void  setIAnswerTime(int iAnswerTime)
    {
        this.iAnswerTime = iAnswerTime;
    }

    public String getSAnswer()
    {
        return sAnswer;
    }

    public void  setSAnswer(String sAnswer)
    {
        this.sAnswer = sAnswer;
    }

    public String getSFrom()
    {
        return sFrom;
    }

    public void  setSFrom(String sFrom)
    {
        this.sFrom = sFrom;
    }

    public DongmiQaDetail()
    {
    }

    public DongmiQaDetail(String sQaKey, String sDtSecCode, String sQuestioner, int iQuestionTime, String sQuestion, String sAnswerer, int iAnswerTime, String sAnswer, String sFrom)
    {
        this.sQaKey = sQaKey;
        this.sDtSecCode = sDtSecCode;
        this.sQuestioner = sQuestioner;
        this.iQuestionTime = iQuestionTime;
        this.sQuestion = sQuestion;
        this.sAnswerer = sAnswerer;
        this.iAnswerTime = iAnswerTime;
        this.sAnswer = sAnswer;
        this.sFrom = sFrom;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sQaKey) {
            ostream.writeString(0, sQaKey);
        }
        if (null != sDtSecCode) {
            ostream.writeString(1, sDtSecCode);
        }
        if (null != sQuestioner) {
            ostream.writeString(2, sQuestioner);
        }
        ostream.writeInt32(3, iQuestionTime);
        if (null != sQuestion) {
            ostream.writeString(4, sQuestion);
        }
        if (null != sAnswerer) {
            ostream.writeString(5, sAnswerer);
        }
        ostream.writeInt32(6, iAnswerTime);
        if (null != sAnswer) {
            ostream.writeString(7, sAnswer);
        }
        if (null != sFrom) {
            ostream.writeString(8, sFrom);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sQaKey = (String)istream.readString(0, false, this.sQaKey);
        this.sDtSecCode = (String)istream.readString(1, false, this.sDtSecCode);
        this.sQuestioner = (String)istream.readString(2, false, this.sQuestioner);
        this.iQuestionTime = (int)istream.readInt32(3, false, this.iQuestionTime);
        this.sQuestion = (String)istream.readString(4, false, this.sQuestion);
        this.sAnswerer = (String)istream.readString(5, false, this.sAnswerer);
        this.iAnswerTime = (int)istream.readInt32(6, false, this.iAnswerTime);
        this.sAnswer = (String)istream.readString(7, false, this.sAnswer);
        this.sFrom = (String)istream.readString(8, false, this.sFrom);
    }

}

