package BEC;

public final class TopicListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<TopicListItem> stTopicList = null;

    public boolean bHasMore = true;

    public java.util.ArrayList<TopicListItem> getStTopicList()
    {
        return stTopicList;
    }

    public void  setStTopicList(java.util.ArrayList<TopicListItem> stTopicList)
    {
        this.stTopicList = stTopicList;
    }

    public boolean getBHasMore()
    {
        return bHasMore;
    }

    public void  setBHasMore(boolean bHasMore)
    {
        this.bHasMore = bHasMore;
    }

    public TopicListRsp()
    {
    }

    public TopicListRsp(java.util.ArrayList<TopicListItem> stTopicList, boolean bHasMore)
    {
        this.stTopicList = stTopicList;
        this.bHasMore = bHasMore;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stTopicList) {
            ostream.writeList(0, stTopicList);
        }
        ostream.writeBoolean(1, bHasMore);
    }

    static java.util.ArrayList<TopicListItem> VAR_TYPE_4_STTOPICLIST = new java.util.ArrayList<TopicListItem>();
    static {
        VAR_TYPE_4_STTOPICLIST.add(new TopicListItem());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stTopicList = (java.util.ArrayList<TopicListItem>)istream.readList(0, false, VAR_TYPE_4_STTOPICLIST);
        this.bHasMore = (boolean)istream.readBoolean(1, false, this.bHasMore);
    }

}

