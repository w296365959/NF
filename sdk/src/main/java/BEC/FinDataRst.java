package BEC;

public final class FinDataRst extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.STFinDate stFinDate = null;

    public int eFinType = 0;

    public BEC.STFinDate getStFinDate()
    {
        return stFinDate;
    }

    public void  setStFinDate(BEC.STFinDate stFinDate)
    {
        this.stFinDate = stFinDate;
    }

    public int getEFinType()
    {
        return eFinType;
    }

    public void  setEFinType(int eFinType)
    {
        this.eFinType = eFinType;
    }

    public FinDataRst()
    {
    }

    public FinDataRst(BEC.STFinDate stFinDate, int eFinType)
    {
        this.stFinDate = stFinDate;
        this.eFinType = eFinType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stFinDate) {
            ostream.writeMessage(0, stFinDate);
        }
        ostream.writeInt32(1, eFinType);
    }

    static BEC.STFinDate VAR_TYPE_4_STFINDATE = new BEC.STFinDate();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stFinDate = (BEC.STFinDate)istream.readMessage(0, false, VAR_TYPE_4_STFINDATE);
        this.eFinType = (int)istream.readInt32(1, false, this.eFinType);
    }

}

