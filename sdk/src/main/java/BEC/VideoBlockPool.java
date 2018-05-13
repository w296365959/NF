package BEC;

public final class VideoBlockPool extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<String, BEC.VideoBlock> mVideoBlock = null;

    public java.util.Map<String, BEC.VideoBlock> getMVideoBlock()
    {
        return mVideoBlock;
    }

    public void  setMVideoBlock(java.util.Map<String, BEC.VideoBlock> mVideoBlock)
    {
        this.mVideoBlock = mVideoBlock;
    }

    public VideoBlockPool()
    {
    }

    public VideoBlockPool(java.util.Map<String, BEC.VideoBlock> mVideoBlock)
    {
        this.mVideoBlock = mVideoBlock;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != mVideoBlock) {
            ostream.writeMap(0, mVideoBlock);
        }
    }

    static java.util.Map<String, BEC.VideoBlock> VAR_TYPE_4_MVIDEOBLOCK = new java.util.HashMap<String, BEC.VideoBlock>();
    static {
        VAR_TYPE_4_MVIDEOBLOCK.put("", new BEC.VideoBlock());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.mVideoBlock = (java.util.Map<String, BEC.VideoBlock>)istream.readMap(0, false, VAR_TYPE_4_MVIDEOBLOCK);
    }

}

