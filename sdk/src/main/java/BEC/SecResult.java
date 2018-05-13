package BEC;

public final class SecResult extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iRet = 0;

    public java.util.ArrayList<SecInfo> vtSecInfoResultItem = null;

    public int getIRet()
    {
        return iRet;
    }

    public void  setIRet(int iRet)
    {
        this.iRet = iRet;
    }

    public java.util.ArrayList<SecInfo> getVtSecInfoResultItem()
    {
        return vtSecInfoResultItem;
    }

    public void  setVtSecInfoResultItem(java.util.ArrayList<SecInfo> vtSecInfoResultItem)
    {
        this.vtSecInfoResultItem = vtSecInfoResultItem;
    }

    public SecResult()
    {
    }

    public SecResult(int iRet, java.util.ArrayList<SecInfo> vtSecInfoResultItem)
    {
        this.iRet = iRet;
        this.vtSecInfoResultItem = vtSecInfoResultItem;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iRet);
        if (null != vtSecInfoResultItem) {
            ostream.writeList(1, vtSecInfoResultItem);
        }
    }

    static java.util.ArrayList<SecInfo> VAR_TYPE_4_VTSECINFORESULTITEM = new java.util.ArrayList<SecInfo>();
    static {
        VAR_TYPE_4_VTSECINFORESULTITEM.add(new SecInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iRet = (int)istream.readInt32(0, false, this.iRet);
        this.vtSecInfoResultItem = (java.util.ArrayList<SecInfo>)istream.readList(1, false, VAR_TYPE_4_VTSECINFORESULTITEM);
    }

}

