package BEC;

public final class LiveVideoStateRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<String, Integer> mVideoState = null;

    public java.util.Map<String, Integer> getMVideoState()
    {
        return mVideoState;
    }

    public void  setMVideoState(java.util.Map<String, Integer> mVideoState)
    {
        this.mVideoState = mVideoState;
    }

    public LiveVideoStateRsp()
    {
    }

    public LiveVideoStateRsp(java.util.Map<String, Integer> mVideoState)
    {
        this.mVideoState = mVideoState;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != mVideoState) {
            ostream.writeMap(0, mVideoState);
        }
    }

    static java.util.Map<String, Integer> VAR_TYPE_4_MVIDEOSTATE = new java.util.HashMap<String, Integer>();
    static {
        VAR_TYPE_4_MVIDEOSTATE.put("", 0);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.mVideoState = (java.util.Map<String, Integer>)istream.readMap(0, false, VAR_TYPE_4_MVIDEOSTATE);
    }

}

