package BEC;

public final class SimilarKLineTopInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.SecInfo stSrcSecInfo = null;

    public BEC.SecInfo stTargetSecInfo = null;

    public float fSimilarValue = 0;

    public BEC.SecInfo getStSrcSecInfo()
    {
        return stSrcSecInfo;
    }

    public void  setStSrcSecInfo(BEC.SecInfo stSrcSecInfo)
    {
        this.stSrcSecInfo = stSrcSecInfo;
    }

    public BEC.SecInfo getStTargetSecInfo()
    {
        return stTargetSecInfo;
    }

    public void  setStTargetSecInfo(BEC.SecInfo stTargetSecInfo)
    {
        this.stTargetSecInfo = stTargetSecInfo;
    }

    public float getFSimilarValue()
    {
        return fSimilarValue;
    }

    public void  setFSimilarValue(float fSimilarValue)
    {
        this.fSimilarValue = fSimilarValue;
    }

    public SimilarKLineTopInfo()
    {
    }

    public SimilarKLineTopInfo(BEC.SecInfo stSrcSecInfo, BEC.SecInfo stTargetSecInfo, float fSimilarValue)
    {
        this.stSrcSecInfo = stSrcSecInfo;
        this.stTargetSecInfo = stTargetSecInfo;
        this.fSimilarValue = fSimilarValue;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stSrcSecInfo) {
            ostream.writeMessage(0, stSrcSecInfo);
        }
        if (null != stTargetSecInfo) {
            ostream.writeMessage(1, stTargetSecInfo);
        }
        ostream.writeFloat(2, fSimilarValue);
    }

    static BEC.SecInfo VAR_TYPE_4_STSRCSECINFO = new BEC.SecInfo();

    static BEC.SecInfo VAR_TYPE_4_STTARGETSECINFO = new BEC.SecInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stSrcSecInfo = (BEC.SecInfo)istream.readMessage(0, false, VAR_TYPE_4_STSRCSECINFO);
        this.stTargetSecInfo = (BEC.SecInfo)istream.readMessage(1, false, VAR_TYPE_4_STTARGETSECINFO);
        this.fSimilarValue = (float)istream.readFloat(2, false, this.fSimilarValue);
    }

}

