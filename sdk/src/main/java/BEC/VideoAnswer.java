package BEC;

public final class VideoAnswer extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sTeacherName = "";

    public String sAnswer = "";

    public int iTime = 0;

    public String getSTeacherName()
    {
        return sTeacherName;
    }

    public void  setSTeacherName(String sTeacherName)
    {
        this.sTeacherName = sTeacherName;
    }

    public String getSAnswer()
    {
        return sAnswer;
    }

    public void  setSAnswer(String sAnswer)
    {
        this.sAnswer = sAnswer;
    }

    public int getITime()
    {
        return iTime;
    }

    public void  setITime(int iTime)
    {
        this.iTime = iTime;
    }

    public VideoAnswer()
    {
    }

    public VideoAnswer(String sTeacherName, String sAnswer, int iTime)
    {
        this.sTeacherName = sTeacherName;
        this.sAnswer = sAnswer;
        this.iTime = iTime;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sTeacherName) {
            ostream.writeString(0, sTeacherName);
        }
        if (null != sAnswer) {
            ostream.writeString(1, sAnswer);
        }
        ostream.writeInt32(2, iTime);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sTeacherName = (String)istream.readString(0, false, this.sTeacherName);
        this.sAnswer = (String)istream.readString(1, false, this.sAnswer);
        this.iTime = (int)istream.readInt32(2, false, this.iTime);
    }

}

