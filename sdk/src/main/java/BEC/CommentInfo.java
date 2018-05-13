package BEC;

public final class CommentInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sComment = "";

    public int iCreateTime = -1;

    public int iUpdateTime = -1;

    public String getSComment()
    {
        return sComment;
    }

    public void  setSComment(String sComment)
    {
        this.sComment = sComment;
    }

    public int getICreateTime()
    {
        return iCreateTime;
    }

    public void  setICreateTime(int iCreateTime)
    {
        this.iCreateTime = iCreateTime;
    }

    public int getIUpdateTime()
    {
        return iUpdateTime;
    }

    public void  setIUpdateTime(int iUpdateTime)
    {
        this.iUpdateTime = iUpdateTime;
    }

    public CommentInfo()
    {
    }

    public CommentInfo(String sComment, int iCreateTime, int iUpdateTime)
    {
        this.sComment = sComment;
        this.iCreateTime = iCreateTime;
        this.iUpdateTime = iUpdateTime;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sComment) {
            ostream.writeString(0, sComment);
        }
        ostream.writeInt32(1, iCreateTime);
        ostream.writeInt32(2, iUpdateTime);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sComment = (String)istream.readString(0, false, this.sComment);
        this.iCreateTime = (int)istream.readInt32(1, false, this.iCreateTime);
        this.iUpdateTime = (int)istream.readInt32(2, false, this.iUpdateTime);
    }

}

