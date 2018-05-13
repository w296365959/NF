package BEC;

public final class VideoList extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.VideoDesc> vtVideoDesc = null;

    public String sOpeningVideoID = "";

    public String sOpeningVideoPassword = "";

    public java.util.ArrayList<BEC.VideoDesc> getVtVideoDesc()
    {
        return vtVideoDesc;
    }

    public void  setVtVideoDesc(java.util.ArrayList<BEC.VideoDesc> vtVideoDesc)
    {
        this.vtVideoDesc = vtVideoDesc;
    }

    public String getSOpeningVideoID()
    {
        return sOpeningVideoID;
    }

    public void  setSOpeningVideoID(String sOpeningVideoID)
    {
        this.sOpeningVideoID = sOpeningVideoID;
    }

    public String getSOpeningVideoPassword()
    {
        return sOpeningVideoPassword;
    }

    public void  setSOpeningVideoPassword(String sOpeningVideoPassword)
    {
        this.sOpeningVideoPassword = sOpeningVideoPassword;
    }

    public VideoList()
    {
    }

    public VideoList(java.util.ArrayList<BEC.VideoDesc> vtVideoDesc, String sOpeningVideoID, String sOpeningVideoPassword)
    {
        this.vtVideoDesc = vtVideoDesc;
        this.sOpeningVideoID = sOpeningVideoID;
        this.sOpeningVideoPassword = sOpeningVideoPassword;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtVideoDesc) {
            ostream.writeList(0, vtVideoDesc);
        }
        if (null != sOpeningVideoID) {
            ostream.writeString(1, sOpeningVideoID);
        }
        if (null != sOpeningVideoPassword) {
            ostream.writeString(2, sOpeningVideoPassword);
        }
    }

    static java.util.ArrayList<BEC.VideoDesc> VAR_TYPE_4_VTVIDEODESC = new java.util.ArrayList<BEC.VideoDesc>();
    static {
        VAR_TYPE_4_VTVIDEODESC.add(new BEC.VideoDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtVideoDesc = (java.util.ArrayList<BEC.VideoDesc>)istream.readList(0, false, VAR_TYPE_4_VTVIDEODESC);
        this.sOpeningVideoID = (String)istream.readString(1, false, this.sOpeningVideoID);
        this.sOpeningVideoPassword = (String)istream.readString(2, false, this.sOpeningVideoPassword);
    }

}

