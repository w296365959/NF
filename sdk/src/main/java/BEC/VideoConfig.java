package BEC;

public final class VideoConfig extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sVideoName = "";

    public java.util.Map<Integer, String> mChannelConfig = null;

    public String sImgUrl = "";

    public String getSVideoName()
    {
        return sVideoName;
    }

    public void  setSVideoName(String sVideoName)
    {
        this.sVideoName = sVideoName;
    }

    public java.util.Map<Integer, String> getMChannelConfig()
    {
        return mChannelConfig;
    }

    public void  setMChannelConfig(java.util.Map<Integer, String> mChannelConfig)
    {
        this.mChannelConfig = mChannelConfig;
    }

    public String getSImgUrl()
    {
        return sImgUrl;
    }

    public void  setSImgUrl(String sImgUrl)
    {
        this.sImgUrl = sImgUrl;
    }

    public VideoConfig()
    {
    }

    public VideoConfig(String sVideoName, java.util.Map<Integer, String> mChannelConfig, String sImgUrl)
    {
        this.sVideoName = sVideoName;
        this.mChannelConfig = mChannelConfig;
        this.sImgUrl = sImgUrl;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sVideoName) {
            ostream.writeString(0, sVideoName);
        }
        if (null != mChannelConfig) {
            ostream.writeMap(1, mChannelConfig);
        }
        if (null != sImgUrl) {
            ostream.writeString(2, sImgUrl);
        }
    }

    static java.util.Map<Integer, String> VAR_TYPE_4_MCHANNELCONFIG = new java.util.HashMap<Integer, String>();
    static {
        VAR_TYPE_4_MCHANNELCONFIG.put(0, "");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sVideoName = (String)istream.readString(0, false, this.sVideoName);
        this.mChannelConfig = (java.util.Map<Integer, String>)istream.readMap(1, false, VAR_TYPE_4_MCHANNELCONFIG);
        this.sImgUrl = (String)istream.readString(2, false, this.sImgUrl);
    }

}

