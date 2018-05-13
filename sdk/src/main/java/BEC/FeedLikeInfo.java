package BEC;

public final class FeedLikeInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<Long, Integer> mAccountLike = null;

    public java.util.Map<Long, Integer> getMAccountLike()
    {
        return mAccountLike;
    }

    public void  setMAccountLike(java.util.Map<Long, Integer> mAccountLike)
    {
        this.mAccountLike = mAccountLike;
    }

    public FeedLikeInfo()
    {
    }

    public FeedLikeInfo(java.util.Map<Long, Integer> mAccountLike)
    {
        this.mAccountLike = mAccountLike;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != mAccountLike) {
            ostream.writeMap(0, mAccountLike);
        }
    }

    static java.util.Map<Long, Integer> VAR_TYPE_4_MACCOUNTLIKE = new java.util.HashMap<Long, Integer>();
    static {
        VAR_TYPE_4_MACCOUNTLIKE.put(0L, 0);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.mAccountLike = (java.util.Map<Long, Integer>)istream.readMap(0, false, VAR_TYPE_4_MACCOUNTLIKE);
    }

}

