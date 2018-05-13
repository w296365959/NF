package BEC;

public final class QRDes extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iMinute = 0;

    public float fPoint = 0;

    public String sPoint = "";

    public int getIMinute()
    {
        return iMinute;
    }

    public void  setIMinute(int iMinute)
    {
        this.iMinute = iMinute;
    }

    public float getFPoint()
    {
        return fPoint;
    }

    public void  setFPoint(float fPoint)
    {
        this.fPoint = fPoint;
    }

    public String getSPoint()
    {
        return sPoint;
    }

    public void  setSPoint(String sPoint)
    {
        this.sPoint = sPoint;
    }

    public QRDes()
    {
    }

    public QRDes(int iMinute, float fPoint, String sPoint)
    {
        this.iMinute = iMinute;
        this.fPoint = fPoint;
        this.sPoint = sPoint;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iMinute);
        ostream.writeFloat(1, fPoint);
        if (null != sPoint) {
            ostream.writeString(2, sPoint);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iMinute = (int)istream.readInt32(0, false, this.iMinute);
        this.fPoint = (float)istream.readFloat(1, false, this.fPoint);
        this.sPoint = (String)istream.readString(2, false, this.sPoint);
    }

}

