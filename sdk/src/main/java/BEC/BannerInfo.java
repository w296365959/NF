package BEC;

public final class BannerInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iID = 0;

    public String sTitle = "";

    public String sImgUrl = "";

    public String sSkippUrl = "";

    public java.util.ArrayList<Integer> vSceneType = null;

    public int getIID()
    {
        return iID;
    }

    public void  setIID(int iID)
    {
        this.iID = iID;
    }

    public String getSTitle()
    {
        return sTitle;
    }

    public void  setSTitle(String sTitle)
    {
        this.sTitle = sTitle;
    }

    public String getSImgUrl()
    {
        return sImgUrl;
    }

    public void  setSImgUrl(String sImgUrl)
    {
        this.sImgUrl = sImgUrl;
    }

    public String getSSkippUrl()
    {
        return sSkippUrl;
    }

    public void  setSSkippUrl(String sSkippUrl)
    {
        this.sSkippUrl = sSkippUrl;
    }

    public java.util.ArrayList<Integer> getVSceneType()
    {
        return vSceneType;
    }

    public void  setVSceneType(java.util.ArrayList<Integer> vSceneType)
    {
        this.vSceneType = vSceneType;
    }

    public BannerInfo()
    {
    }

    public BannerInfo(int iID, String sTitle, String sImgUrl, String sSkippUrl, java.util.ArrayList<Integer> vSceneType)
    {
        this.iID = iID;
        this.sTitle = sTitle;
        this.sImgUrl = sImgUrl;
        this.sSkippUrl = sSkippUrl;
        this.vSceneType = vSceneType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iID);
        if (null != sTitle) {
            ostream.writeString(1, sTitle);
        }
        if (null != sImgUrl) {
            ostream.writeString(2, sImgUrl);
        }
        if (null != sSkippUrl) {
            ostream.writeString(3, sSkippUrl);
        }
        if (null != vSceneType) {
            ostream.writeList(4, vSceneType);
        }
    }

    static java.util.ArrayList<Integer> VAR_TYPE_4_VSCENETYPE = new java.util.ArrayList<Integer>();
    static {
        VAR_TYPE_4_VSCENETYPE.add(0);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iID = (int)istream.readInt32(0, false, this.iID);
        this.sTitle = (String)istream.readString(1, false, this.sTitle);
        this.sImgUrl = (String)istream.readString(2, false, this.sImgUrl);
        this.sSkippUrl = (String)istream.readString(3, false, this.sSkippUrl);
        this.vSceneType = (java.util.ArrayList<Integer>)istream.readList(4, false, VAR_TYPE_4_VSCENETYPE);
    }

}

