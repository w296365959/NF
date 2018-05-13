package BEC;

public final class PlateResult extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iRet = 0;

    public java.util.ArrayList<PlateInfo> vtPlateResultItem = null;

    public int getIRet()
    {
        return iRet;
    }

    public void  setIRet(int iRet)
    {
        this.iRet = iRet;
    }

    public java.util.ArrayList<PlateInfo> getVtPlateResultItem()
    {
        return vtPlateResultItem;
    }

    public void  setVtPlateResultItem(java.util.ArrayList<PlateInfo> vtPlateResultItem)
    {
        this.vtPlateResultItem = vtPlateResultItem;
    }

    public PlateResult()
    {
    }

    public PlateResult(int iRet, java.util.ArrayList<PlateInfo> vtPlateResultItem)
    {
        this.iRet = iRet;
        this.vtPlateResultItem = vtPlateResultItem;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iRet);
        if (null != vtPlateResultItem) {
            ostream.writeList(1, vtPlateResultItem);
        }
    }

    static java.util.ArrayList<PlateInfo> VAR_TYPE_4_VTPLATERESULTITEM = new java.util.ArrayList<PlateInfo>();
    static {
        VAR_TYPE_4_VTPLATERESULTITEM.add(new PlateInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iRet = (int)istream.readInt32(0, false, this.iRet);
        this.vtPlateResultItem = (java.util.ArrayList<PlateInfo>)istream.readList(1, false, VAR_TYPE_4_VTPLATERESULTITEM);
    }

}

