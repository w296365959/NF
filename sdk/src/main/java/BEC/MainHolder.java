package BEC;

public final class MainHolder extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iHolders = 0;

    public float fTotalCount = 0;

    public float fChange = 0;

    public float fRate = 0;

    public java.util.ArrayList<BEC.MainHolderDetail> vtMainHolderDetail = null;

    public int getIHolders()
    {
        return iHolders;
    }

    public void  setIHolders(int iHolders)
    {
        this.iHolders = iHolders;
    }

    public float getFTotalCount()
    {
        return fTotalCount;
    }

    public void  setFTotalCount(float fTotalCount)
    {
        this.fTotalCount = fTotalCount;
    }

    public float getFChange()
    {
        return fChange;
    }

    public void  setFChange(float fChange)
    {
        this.fChange = fChange;
    }

    public float getFRate()
    {
        return fRate;
    }

    public void  setFRate(float fRate)
    {
        this.fRate = fRate;
    }

    public java.util.ArrayList<BEC.MainHolderDetail> getVtMainHolderDetail()
    {
        return vtMainHolderDetail;
    }

    public void  setVtMainHolderDetail(java.util.ArrayList<BEC.MainHolderDetail> vtMainHolderDetail)
    {
        this.vtMainHolderDetail = vtMainHolderDetail;
    }

    public MainHolder()
    {
    }

    public MainHolder(int iHolders, float fTotalCount, float fChange, float fRate, java.util.ArrayList<BEC.MainHolderDetail> vtMainHolderDetail)
    {
        this.iHolders = iHolders;
        this.fTotalCount = fTotalCount;
        this.fChange = fChange;
        this.fRate = fRate;
        this.vtMainHolderDetail = vtMainHolderDetail;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iHolders);
        ostream.writeFloat(1, fTotalCount);
        ostream.writeFloat(2, fChange);
        ostream.writeFloat(3, fRate);
        if (null != vtMainHolderDetail) {
            ostream.writeList(4, vtMainHolderDetail);
        }
    }

    static java.util.ArrayList<BEC.MainHolderDetail> VAR_TYPE_4_VTMAINHOLDERDETAIL = new java.util.ArrayList<BEC.MainHolderDetail>();
    static {
        VAR_TYPE_4_VTMAINHOLDERDETAIL.add(new BEC.MainHolderDetail());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iHolders = (int)istream.readInt32(0, false, this.iHolders);
        this.fTotalCount = (float)istream.readFloat(1, false, this.fTotalCount);
        this.fChange = (float)istream.readFloat(2, false, this.fChange);
        this.fRate = (float)istream.readFloat(3, false, this.fRate);
        this.vtMainHolderDetail = (java.util.ArrayList<BEC.MainHolderDetail>)istream.readList(4, false, VAR_TYPE_4_VTMAINHOLDERDETAIL);
    }

}

