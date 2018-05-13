package BEC;

public final class VideoTeacher extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sTeacherId = "";

    public String sTeacherName = "";

    public String sTeacherImg = "";

    public String sTeacherDesc = "";

    public String sTeacherTags = "";

    public String getSTeacherId()
    {
        return sTeacherId;
    }

    public void  setSTeacherId(String sTeacherId)
    {
        this.sTeacherId = sTeacherId;
    }

    public String getSTeacherName()
    {
        return sTeacherName;
    }

    public void  setSTeacherName(String sTeacherName)
    {
        this.sTeacherName = sTeacherName;
    }

    public String getSTeacherImg()
    {
        return sTeacherImg;
    }

    public void  setSTeacherImg(String sTeacherImg)
    {
        this.sTeacherImg = sTeacherImg;
    }

    public String getSTeacherDesc()
    {
        return sTeacherDesc;
    }

    public void  setSTeacherDesc(String sTeacherDesc)
    {
        this.sTeacherDesc = sTeacherDesc;
    }

    public String getSTeacherTags()
    {
        return sTeacherTags;
    }

    public void  setSTeacherTags(String sTeacherTags)
    {
        this.sTeacherTags = sTeacherTags;
    }

    public VideoTeacher()
    {
    }

    public VideoTeacher(String sTeacherId, String sTeacherName, String sTeacherImg, String sTeacherDesc, String sTeacherTags)
    {
        this.sTeacherId = sTeacherId;
        this.sTeacherName = sTeacherName;
        this.sTeacherImg = sTeacherImg;
        this.sTeacherDesc = sTeacherDesc;
        this.sTeacherTags = sTeacherTags;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sTeacherId) {
            ostream.writeString(0, sTeacherId);
        }
        if (null != sTeacherName) {
            ostream.writeString(1, sTeacherName);
        }
        if (null != sTeacherImg) {
            ostream.writeString(2, sTeacherImg);
        }
        if (null != sTeacherDesc) {
            ostream.writeString(3, sTeacherDesc);
        }
        if (null != sTeacherTags) {
            ostream.writeString(4, sTeacherTags);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sTeacherId = (String)istream.readString(0, false, this.sTeacherId);
        this.sTeacherName = (String)istream.readString(1, false, this.sTeacherName);
        this.sTeacherImg = (String)istream.readString(2, false, this.sTeacherImg);
        this.sTeacherDesc = (String)istream.readString(3, false, this.sTeacherDesc);
        this.sTeacherTags = (String)istream.readString(4, false, this.sTeacherTags);
    }

}

