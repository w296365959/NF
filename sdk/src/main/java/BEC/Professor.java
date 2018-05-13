package BEC;

public final class Professor extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sProfessorKey = "";

    public String sName = "";

    public String sImg = "";

    public String sDesc = "";

    public String sTags = "";

    public String getSProfessorKey()
    {
        return sProfessorKey;
    }

    public void  setSProfessorKey(String sProfessorKey)
    {
        this.sProfessorKey = sProfessorKey;
    }

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

    public String getSDesc()
    {
        return sDesc;
    }

    public void  setSDesc(String sDesc)
    {
        this.sDesc = sDesc;
    }

    public String getSTags()
    {
        return sTags;
    }

    public void  setSTags(String sTags)
    {
        this.sTags = sTags;
    }

    public Professor()
    {
    }

    public Professor(String sProfessorKey, String sName, String sImg, String sDesc, String sTags)
    {
        this.sProfessorKey = sProfessorKey;
        this.sName = sName;
        this.sImg = sImg;
        this.sDesc = sDesc;
        this.sTags = sTags;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sProfessorKey) {
            ostream.writeString(0, sProfessorKey);
        }
        if (null != sName) {
            ostream.writeString(1, sName);
        }
        if (null != sImg) {
            ostream.writeString(2, sImg);
        }
        if (null != sDesc) {
            ostream.writeString(3, sDesc);
        }
        if (null != sTags) {
            ostream.writeString(4, sTags);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sProfessorKey = (String)istream.readString(0, false, this.sProfessorKey);
        this.sName = (String)istream.readString(1, false, this.sName);
        this.sImg = (String)istream.readString(2, false, this.sImg);
        this.sDesc = (String)istream.readString(3, false, this.sDesc);
        this.sTags = (String)istream.readString(4, false, this.sTags);
    }

}

