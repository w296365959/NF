package BEC;

public final class IncomeResult extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iDay = 0;

    public float fAvgIncrease = 0;

    public int getIDay()
    {
        return iDay;
    }

    public void  setIDay(int iDay)
    {
        this.iDay = iDay;
    }

    public float getFAvgIncrease()
    {
        return fAvgIncrease;
    }

    public void  setFAvgIncrease(float fAvgIncrease)
    {
        this.fAvgIncrease = fAvgIncrease;
    }

    public IncomeResult()
    {
    }

    public IncomeResult(int iDay, float fAvgIncrease)
    {
        this.iDay = iDay;
        this.fAvgIncrease = fAvgIncrease;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iDay);
        ostream.writeFloat(1, fAvgIncrease);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iDay = (int)istream.readInt32(0, false, this.iDay);
        this.fAvgIncrease = (float)istream.readFloat(1, false, this.fAvgIncrease);
    }

}

