package BEC;

public final class STPopuIndex extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDate = "";

    public int iIndexVal = 0;

    public String getSDate()
    {
        return sDate;
    }

    public void  setSDate(String sDate)
    {
        this.sDate = sDate;
    }

    public int getIIndexVal()
    {
        return iIndexVal;
    }

    public void  setIIndexVal(int iIndexVal)
    {
        this.iIndexVal = iIndexVal;
    }

    public STPopuIndex()
    {
    }

    public STPopuIndex(String sDate, int iIndexVal)
    {
        this.sDate = sDate;
        this.iIndexVal = iIndexVal;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDate) {
            ostream.writeString(0, sDate);
        }
        ostream.writeInt32(1, iIndexVal);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDate = (String)istream.readString(0, false, this.sDate);
        this.iIndexVal = (int)istream.readInt32(1, false, this.iIndexVal);
    }

}

