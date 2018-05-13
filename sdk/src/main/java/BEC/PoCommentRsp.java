package BEC;

public final class PoCommentRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sCommentId = "";

    public String sClientCommentId = "";

    public String getSCommentId()
    {
        return sCommentId;
    }

    public void  setSCommentId(String sCommentId)
    {
        this.sCommentId = sCommentId;
    }

    public String getSClientCommentId()
    {
        return sClientCommentId;
    }

    public void  setSClientCommentId(String sClientCommentId)
    {
        this.sClientCommentId = sClientCommentId;
    }

    public PoCommentRsp()
    {
    }

    public PoCommentRsp(String sCommentId, String sClientCommentId)
    {
        this.sCommentId = sCommentId;
        this.sClientCommentId = sClientCommentId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sCommentId) {
            ostream.writeString(1, sCommentId);
        }
        if (null != sClientCommentId) {
            ostream.writeString(2, sClientCommentId);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sCommentId = (String)istream.readString(1, false, this.sCommentId);
        this.sClientCommentId = (String)istream.readString(2, false, this.sClientCommentId);
    }

}

