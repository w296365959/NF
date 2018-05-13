package BEC;

public final class FeedCommentInfoList extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.FeedCommentInfo> vCommentList = null;

    public java.util.ArrayList<BEC.FeedCommentInfo> getVCommentList()
    {
        return vCommentList;
    }

    public void  setVCommentList(java.util.ArrayList<BEC.FeedCommentInfo> vCommentList)
    {
        this.vCommentList = vCommentList;
    }

    public FeedCommentInfoList()
    {
    }

    public FeedCommentInfoList(java.util.ArrayList<BEC.FeedCommentInfo> vCommentList)
    {
        this.vCommentList = vCommentList;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vCommentList) {
            ostream.writeList(0, vCommentList);
        }
    }

    static java.util.ArrayList<BEC.FeedCommentInfo> VAR_TYPE_4_VCOMMENTLIST = new java.util.ArrayList<BEC.FeedCommentInfo>();
    static {
        VAR_TYPE_4_VCOMMENTLIST.add(new BEC.FeedCommentInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vCommentList = (java.util.ArrayList<BEC.FeedCommentInfo>)istream.readList(0, false, VAR_TYPE_4_VCOMMENTLIST);
    }

}

