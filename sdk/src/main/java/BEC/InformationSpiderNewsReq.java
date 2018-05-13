package BEC;

public final class InformationSpiderNewsReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iID = 0;

    public int iKind = 0;

    public int getIID()
    {
        return iID;
    }

    public void  setIID(int iID)
    {
        this.iID = iID;
    }

    public int getIKind()
    {
        return iKind;
    }

    public void  setIKind(int iKind)
    {
        this.iKind = iKind;
    }

    public InformationSpiderNewsReq()
    {
    }

    public InformationSpiderNewsReq(int iID, int iKind)
    {
        this.iID = iID;
        this.iKind = iKind;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iID);
        ostream.writeInt32(1, iKind);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iID = (int)istream.readInt32(0, false, this.iID);
        this.iKind = (int)istream.readInt32(1, false, this.iKind);
    }

}

