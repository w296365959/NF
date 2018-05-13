package BEC;

public final class GenAccountIdRspItem extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iRetCode = 0;

    public long iAccountId = 0;

    public boolean bAdopted = false;

    public int getIRetCode()
    {
        return iRetCode;
    }

    public void  setIRetCode(int iRetCode)
    {
        this.iRetCode = iRetCode;
    }

    public long getIAccountId()
    {
        return iAccountId;
    }

    public void  setIAccountId(long iAccountId)
    {
        this.iAccountId = iAccountId;
    }

    public boolean getBAdopted()
    {
        return bAdopted;
    }

    public void  setBAdopted(boolean bAdopted)
    {
        this.bAdopted = bAdopted;
    }

    public GenAccountIdRspItem()
    {
    }

    public GenAccountIdRspItem(int iRetCode, long iAccountId, boolean bAdopted)
    {
        this.iRetCode = iRetCode;
        this.iAccountId = iAccountId;
        this.bAdopted = bAdopted;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iRetCode);
        ostream.writeUInt32(1, iAccountId);
        ostream.writeBoolean(2, bAdopted);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iRetCode = (int)istream.readInt32(0, false, this.iRetCode);
        this.iAccountId = (long)istream.readUInt32(1, false, this.iAccountId);
        this.bAdopted = (boolean)istream.readBoolean(2, false, this.bAdopted);
    }

}

