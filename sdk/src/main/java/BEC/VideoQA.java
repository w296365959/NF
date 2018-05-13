package BEC;

public final class VideoQA extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sQuestionId = "";

    public String sQuestion = "";

    public int iTime = 0;

    public String sUserName = "";

    public java.util.ArrayList<BEC.VideoAnswer> vtVideoAnswer = null;

    public String getSQuestionId()
    {
        return sQuestionId;
    }

    public void  setSQuestionId(String sQuestionId)
    {
        this.sQuestionId = sQuestionId;
    }

    public String getSQuestion()
    {
        return sQuestion;
    }

    public void  setSQuestion(String sQuestion)
    {
        this.sQuestion = sQuestion;
    }

    public int getITime()
    {
        return iTime;
    }

    public void  setITime(int iTime)
    {
        this.iTime = iTime;
    }

    public String getSUserName()
    {
        return sUserName;
    }

    public void  setSUserName(String sUserName)
    {
        this.sUserName = sUserName;
    }

    public java.util.ArrayList<BEC.VideoAnswer> getVtVideoAnswer()
    {
        return vtVideoAnswer;
    }

    public void  setVtVideoAnswer(java.util.ArrayList<BEC.VideoAnswer> vtVideoAnswer)
    {
        this.vtVideoAnswer = vtVideoAnswer;
    }

    public VideoQA()
    {
    }

    public VideoQA(String sQuestionId, String sQuestion, int iTime, String sUserName, java.util.ArrayList<BEC.VideoAnswer> vtVideoAnswer)
    {
        this.sQuestionId = sQuestionId;
        this.sQuestion = sQuestion;
        this.iTime = iTime;
        this.sUserName = sUserName;
        this.vtVideoAnswer = vtVideoAnswer;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sQuestionId) {
            ostream.writeString(0, sQuestionId);
        }
        if (null != sQuestion) {
            ostream.writeString(1, sQuestion);
        }
        ostream.writeInt32(2, iTime);
        if (null != sUserName) {
            ostream.writeString(3, sUserName);
        }
        if (null != vtVideoAnswer) {
            ostream.writeList(4, vtVideoAnswer);
        }
    }

    static java.util.ArrayList<BEC.VideoAnswer> VAR_TYPE_4_VTVIDEOANSWER = new java.util.ArrayList<BEC.VideoAnswer>();
    static {
        VAR_TYPE_4_VTVIDEOANSWER.add(new BEC.VideoAnswer());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sQuestionId = (String)istream.readString(0, false, this.sQuestionId);
        this.sQuestion = (String)istream.readString(1, false, this.sQuestion);
        this.iTime = (int)istream.readInt32(2, false, this.iTime);
        this.sUserName = (String)istream.readString(3, false, this.sUserName);
        this.vtVideoAnswer = (java.util.ArrayList<BEC.VideoAnswer>)istream.readList(4, false, VAR_TYPE_4_VTVIDEOANSWER);
    }

}

