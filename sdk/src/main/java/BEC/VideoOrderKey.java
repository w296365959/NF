package BEC;

public final class VideoOrderKey extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iOrder = 0;

    public int eType = 0;

    public int iChannelPri = 0;

    public int iTimeStamp = 0;

    public String sVideoId = "";

    public int getIOrder()
    {
        return iOrder;
    }

    public void  setIOrder(int iOrder)
    {
        this.iOrder = iOrder;
    }

    public int getEType()
    {
        return eType;
    }

    public void  setEType(int eType)
    {
        this.eType = eType;
    }

    public int getIChannelPri()
    {
        return iChannelPri;
    }

    public void  setIChannelPri(int iChannelPri)
    {
        this.iChannelPri = iChannelPri;
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

    public VideoOrderKey()
    {
    }

    public VideoOrderKey(int iOrder, int eType, int iChannelPri, int iTimeStamp, String sVideoId)
    {
        this.iOrder = iOrder;
        this.eType = eType;
        this.iChannelPri = iChannelPri;
        this.iTimeStamp = iTimeStamp;
        this.sVideoId = sVideoId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iOrder);
        ostream.writeInt32(1, eType);
        ostream.writeInt32(2, iChannelPri);
        ostream.writeInt32(3, iTimeStamp);
        if (null != sVideoId) {
            ostream.writeString(4, sVideoId);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iOrder = (int)istream.readInt32(0, false, this.iOrder);
        this.eType = (int)istream.readInt32(1, false, this.eType);
        this.iChannelPri = (int)istream.readInt32(2, false, this.iChannelPri);
        this.iTimeStamp = (int)istream.readInt32(3, false, this.iTimeStamp);
        this.sVideoId = (String)istream.readString(4, false, this.sVideoId);
    }

}

