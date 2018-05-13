package BEC;

public final class VideoChannelDesc extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int eChannel = 0;

    public String sName = "";

    public String sIcon = "";

    public String sDesc = "";

    public String sTags = "";

    public int getEChannel()
    {
        return eChannel;
    }

    public void  setEChannel(int eChannel)
    {
        this.eChannel = eChannel;
    }

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public String getSIcon()
    {
        return sIcon;
    }

    public void  setSIcon(String sIcon)
    {
        this.sIcon = sIcon;
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

    public VideoChannelDesc()
    {
    }

    public VideoChannelDesc(int eChannel, String sName, String sIcon, String sDesc, String sTags)
    {
        this.eChannel = eChannel;
        this.sName = sName;
        this.sIcon = sIcon;
        this.sDesc = sDesc;
        this.sTags = sTags;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, eChannel);
        if (null != sName) {
            ostream.writeString(1, sName);
        }
        if (null != sIcon) {
            ostream.writeString(2, sIcon);
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

        this.eChannel = (int)istream.readInt32(0, false, this.eChannel);
        this.sName = (String)istream.readString(1, false, this.sName);
        this.sIcon = (String)istream.readString(2, false, this.sIcon);
        this.sDesc = (String)istream.readString(3, false, this.sDesc);
        this.sTags = (String)istream.readString(4, false, this.sTags);
    }

}

