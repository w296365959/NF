package BEC;

public final class VideoFilter extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iFilterType = 0;

    public String sFilterCondition = "";

    public int getIFilterType()
    {
        return iFilterType;
    }

    public void  setIFilterType(int iFilterType)
    {
        this.iFilterType = iFilterType;
    }

    public String getSFilterCondition()
    {
        return sFilterCondition;
    }

    public void  setSFilterCondition(String sFilterCondition)
    {
        this.sFilterCondition = sFilterCondition;
    }

    public VideoFilter()
    {
    }

    public VideoFilter(int iFilterType, String sFilterCondition)
    {
        this.iFilterType = iFilterType;
        this.sFilterCondition = sFilterCondition;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iFilterType);
        if (null != sFilterCondition) {
            ostream.writeString(1, sFilterCondition);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iFilterType = (int)istream.readInt32(0, false, this.iFilterType);
        this.sFilterCondition = (String)istream.readString(1, false, this.sFilterCondition);
    }

}

