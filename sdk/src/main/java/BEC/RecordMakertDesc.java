package BEC;

public final class RecordMakertDesc extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public float fOpen = 0;

    public float fNow = 0;

    public float fClose = 0;

    public long lVolume = 0;

    public long lAmount = 0;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public float getFOpen()
    {
        return fOpen;
    }

    public void  setFOpen(float fOpen)
    {
        this.fOpen = fOpen;
    }

    public float getFNow()
    {
        return fNow;
    }

    public void  setFNow(float fNow)
    {
        this.fNow = fNow;
    }

    public float getFClose()
    {
        return fClose;
    }

    public void  setFClose(float fClose)
    {
        this.fClose = fClose;
    }

    public long getLVolume()
    {
        return lVolume;
    }

    public void  setLVolume(long lVolume)
    {
        this.lVolume = lVolume;
    }

    public long getLAmount()
    {
        return lAmount;
    }

    public void  setLAmount(long lAmount)
    {
        this.lAmount = lAmount;
    }

    public RecordMakertDesc()
    {
    }

    public RecordMakertDesc(String sDtSecCode, float fOpen, float fNow, float fClose, long lVolume, long lAmount)
    {
        this.sDtSecCode = sDtSecCode;
        this.fOpen = fOpen;
        this.fNow = fNow;
        this.fClose = fClose;
        this.lVolume = lVolume;
        this.lAmount = lAmount;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        ostream.writeFloat(1, fOpen);
        ostream.writeFloat(2, fNow);
        ostream.writeFloat(3, fClose);
        ostream.writeInt64(4, lVolume);
        ostream.writeInt64(5, lAmount);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.fOpen = (float)istream.readFloat(1, false, this.fOpen);
        this.fNow = (float)istream.readFloat(2, false, this.fNow);
        this.fClose = (float)istream.readFloat(3, false, this.fClose);
        this.lVolume = (long)istream.readInt64(4, false, this.lVolume);
        this.lAmount = (long)istream.readInt64(5, false, this.lAmount);
    }

}

