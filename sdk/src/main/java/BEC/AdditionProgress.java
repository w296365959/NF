package BEC;

public final class AdditionProgress extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDay = "";

    public String sProgress = "";

    public String getSDay()
    {
        return sDay;
    }

    public void  setSDay(String sDay)
    {
        this.sDay = sDay;
    }

    public String getSProgress()
    {
        return sProgress;
    }

    public void  setSProgress(String sProgress)
    {
        this.sProgress = sProgress;
    }

    public AdditionProgress()
    {
    }

    public AdditionProgress(String sDay, String sProgress)
    {
        this.sDay = sDay;
        this.sProgress = sProgress;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDay) {
            ostream.writeString(0, sDay);
        }
        if (null != sProgress) {
            ostream.writeString(1, sProgress);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDay = (String)istream.readString(0, false, this.sDay);
        this.sProgress = (String)istream.readString(1, false, this.sProgress);
    }

}

