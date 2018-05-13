package BEC;

public final class RealMarketQRRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public float fPoint = 0;

    public String sTypeText = "";

    public String sDescText = "";

    public float getFPoint()
    {
        return fPoint;
    }

    public void  setFPoint(float fPoint)
    {
        this.fPoint = fPoint;
    }

    public String getSTypeText()
    {
        return sTypeText;
    }

    public void  setSTypeText(String sTypeText)
    {
        this.sTypeText = sTypeText;
    }

    public String getSDescText()
    {
        return sDescText;
    }

    public void  setSDescText(String sDescText)
    {
        this.sDescText = sDescText;
    }

    public RealMarketQRRsp()
    {
    }

    public RealMarketQRRsp(float fPoint, String sTypeText, String sDescText)
    {
        this.fPoint = fPoint;
        this.sTypeText = sTypeText;
        this.sDescText = sDescText;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeFloat(0, fPoint);
        if (null != sTypeText) {
            ostream.writeString(1, sTypeText);
        }
        if (null != sDescText) {
            ostream.writeString(2, sDescText);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.fPoint = (float)istream.readFloat(0, false, this.fPoint);
        this.sTypeText = (String)istream.readString(1, false, this.sTypeText);
        this.sDescText = (String)istream.readString(2, false, this.sDescText);
    }

}

