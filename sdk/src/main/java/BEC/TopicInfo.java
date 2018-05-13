package BEC;

public final class TopicInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sName = "";

    public String sImg = "";

    public String sDescription = "";

    public int iCreatetime = 0;

    public BEC.NewsDesc stFaceNews = null;

    public java.util.ArrayList<BEC.TopicSubDir> vTopicSubDir = null;

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public String getSImg()
    {
        return sImg;
    }

    public void  setSImg(String sImg)
    {
        this.sImg = sImg;
    }

    public String getSDescription()
    {
        return sDescription;
    }

    public void  setSDescription(String sDescription)
    {
        this.sDescription = sDescription;
    }

    public int getICreatetime()
    {
        return iCreatetime;
    }

    public void  setICreatetime(int iCreatetime)
    {
        this.iCreatetime = iCreatetime;
    }

    public BEC.NewsDesc getStFaceNews()
    {
        return stFaceNews;
    }

    public void  setStFaceNews(BEC.NewsDesc stFaceNews)
    {
        this.stFaceNews = stFaceNews;
    }

    public java.util.ArrayList<BEC.TopicSubDir> getVTopicSubDir()
    {
        return vTopicSubDir;
    }

    public void  setVTopicSubDir(java.util.ArrayList<BEC.TopicSubDir> vTopicSubDir)
    {
        this.vTopicSubDir = vTopicSubDir;
    }

    public TopicInfo()
    {
    }

    public TopicInfo(String sName, String sImg, String sDescription, int iCreatetime, BEC.NewsDesc stFaceNews, java.util.ArrayList<BEC.TopicSubDir> vTopicSubDir)
    {
        this.sName = sName;
        this.sImg = sImg;
        this.sDescription = sDescription;
        this.iCreatetime = iCreatetime;
        this.stFaceNews = stFaceNews;
        this.vTopicSubDir = vTopicSubDir;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sName) {
            ostream.writeString(0, sName);
        }
        if (null != sImg) {
            ostream.writeString(1, sImg);
        }
        if (null != sDescription) {
            ostream.writeString(2, sDescription);
        }
        ostream.writeInt32(3, iCreatetime);
        if (null != stFaceNews) {
            ostream.writeMessage(4, stFaceNews);
        }
        if (null != vTopicSubDir) {
            ostream.writeList(5, vTopicSubDir);
        }
    }

    static BEC.NewsDesc VAR_TYPE_4_STFACENEWS = new BEC.NewsDesc();

    static java.util.ArrayList<BEC.TopicSubDir> VAR_TYPE_4_VTOPICSUBDIR = new java.util.ArrayList<BEC.TopicSubDir>();
    static {
        VAR_TYPE_4_VTOPICSUBDIR.add(new BEC.TopicSubDir());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sName = (String)istream.readString(0, false, this.sName);
        this.sImg = (String)istream.readString(1, false, this.sImg);
        this.sDescription = (String)istream.readString(2, false, this.sDescription);
        this.iCreatetime = (int)istream.readInt32(3, false, this.iCreatetime);
        this.stFaceNews = (BEC.NewsDesc)istream.readMessage(4, false, VAR_TYPE_4_STFACENEWS);
        this.vTopicSubDir = (java.util.ArrayList<BEC.TopicSubDir>)istream.readList(5, false, VAR_TYPE_4_VTOPICSUBDIR);
    }

}

