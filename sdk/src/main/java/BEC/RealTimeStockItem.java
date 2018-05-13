package BEC;

public final class RealTimeStockItem extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public String sSecName = "";

    public int iAttenDegree = 0;

    public int iCurrPos = 0;

    public int iChangePos = 0;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public String getSSecName()
    {
        return sSecName;
    }

    public void  setSSecName(String sSecName)
    {
        this.sSecName = sSecName;
    }

    public int getIAttenDegree()
    {
        return iAttenDegree;
    }

    public void  setIAttenDegree(int iAttenDegree)
    {
        this.iAttenDegree = iAttenDegree;
    }

    public int getICurrPos()
    {
        return iCurrPos;
    }

    public void  setICurrPos(int iCurrPos)
    {
        this.iCurrPos = iCurrPos;
    }

    public int getIChangePos()
    {
        return iChangePos;
    }

    public void  setIChangePos(int iChangePos)
    {
        this.iChangePos = iChangePos;
    }

    public RealTimeStockItem()
    {
    }

    public RealTimeStockItem(String sDtSecCode, String sSecName, int iAttenDegree, int iCurrPos, int iChangePos)
    {
        this.sDtSecCode = sDtSecCode;
        this.sSecName = sSecName;
        this.iAttenDegree = iAttenDegree;
        this.iCurrPos = iCurrPos;
        this.iChangePos = iChangePos;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        if (null != sSecName) {
            ostream.writeString(1, sSecName);
        }
        ostream.writeInt32(2, iAttenDegree);
        ostream.writeInt32(3, iCurrPos);
        ostream.writeInt32(4, iChangePos);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.sSecName = (String)istream.readString(1, false, this.sSecName);
        this.iAttenDegree = (int)istream.readInt32(2, false, this.iAttenDegree);
        this.iCurrPos = (int)istream.readInt32(3, false, this.iCurrPos);
        this.iChangePos = (int)istream.readInt32(4, false, this.iChangePos);
    }

}

