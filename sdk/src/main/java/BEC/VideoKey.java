package BEC;

public final class VideoKey extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int eType = 0;

    public int eChannel = 0;

    public int iTimeStamp = 0;

    public String sVideoId = "";

    public int getEType()
    {
        return eType;
    }

    public void  setEType(int eType)
    {
        this.eType = eType;
    }

    public int getEChannel()
    {
        return eChannel;
    }

    public void  setEChannel(int eChannel)
    {
        this.eChannel = eChannel;
    }

    public int getITimeStamp()
    {
        return iTimeStamp;
    }

    public void  setITimeStamp(int iTimeStamp)
    {
        this.iTimeStamp = iTimeStamp;
    }

    public String getSVideoId()
    {
        return sVideoId;
    }

    public void  setSVideoId(String sVideoId)
    {
        this.sVideoId = sVideoId;
    }

    public VideoKey()
    {
    }

    public VideoKey(int eType, int eChannel, int iTimeStamp, String sVideoId)
    {
        this.eType = eType;
        this.eChannel = eChannel;
        this.iTimeStamp = iTimeStamp;
        this.sVideoId = sVideoId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, eType);
        ostream.writeInt32(1, eChannel);
        ostream.writeInt32(2, iTimeStamp);
        if (null != sVideoId) {
            ostream.writeString(3, sVideoId);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.eType = (int)istream.readInt32(0, false, this.eType);
        this.eChannel = (int)istream.readInt32(1, false, this.eChannel);
        this.iTimeStamp = (int)istream.readInt32(2, false, this.iTimeStamp);
        this.sVideoId = (String)istream.readString(3, false, this.sVideoId);
    }

}

