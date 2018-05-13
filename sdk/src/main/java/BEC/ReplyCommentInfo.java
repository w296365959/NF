package BEC;

public final class ReplyCommentInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sReplyCommentId = "";

    public long iReplyAccountId = 0;

    public String sReplyNickName = "";

    public String getSReplyCommentId()
    {
        return sReplyCommentId;
    }

    public void  setSReplyCommentId(String sReplyCommentId)
    {
        this.sReplyCommentId = sReplyCommentId;
    }

    public long getIReplyAccountId()
    {
        return iReplyAccountId;
    }

    public void  setIReplyAccountId(long iReplyAccountId)
    {
        this.iReplyAccountId = iReplyAccountId;
    }

    public String getSReplyNickName()
    {
        return sReplyNickName;
    }

    public void  setSReplyNickName(String sReplyNickName)
    {
        this.sReplyNickName = sReplyNickName;
    }

    public ReplyCommentInfo()
    {
    }

    public ReplyCommentInfo(String sReplyCommentId, long iReplyAccountId, String sReplyNickName)
    {
        this.sReplyCommentId = sReplyCommentId;
        this.iReplyAccountId = iReplyAccountId;
        this.sReplyNickName = sReplyNickName;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sReplyCommentId) {
            ostream.writeString(0, sReplyCommentId);
        }
        ostream.writeUInt32(1, iReplyAccountId);
        if (null != sReplyNickName) {
            ostream.writeString(2, sReplyNickName);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sReplyCommentId = (String)istream.readString(0, false, this.sReplyCommentId);
        this.iReplyAccountId = (long)istream.readUInt32(1, false, this.iReplyAccountId);
        this.sReplyNickName = (String)istream.readString(2, false, this.sReplyNickName);
    }

}

