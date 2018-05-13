package BEC;

public final class AdditionLiftedListRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.AdditionStockLifted> vAdditionStockLifted = null;

    public String sNextId = "";

    public int iTotalCount = 0;

    public java.util.ArrayList<BEC.AdditionStockLifted> getVAdditionStockLifted()
    {
        return vAdditionStockLifted;
    }

    public void  setVAdditionStockLifted(java.util.ArrayList<BEC.AdditionStockLifted> vAdditionStockLifted)
    {
        this.vAdditionStockLifted = vAdditionStockLifted;
    }

    public String getSNextId()
    {
        return sNextId;
    }

    public void  setSNextId(String sNextId)
    {
        this.sNextId = sNextId;
    }

    public int getITotalCount()
    {
        return iTotalCount;
    }

    public void  setITotalCount(int iTotalCount)
    {
        this.iTotalCount = iTotalCount;
    }

    public AdditionLiftedListRsp()
    {
    }

    public AdditionLiftedListRsp(java.util.ArrayList<BEC.AdditionStockLifted> vAdditionStockLifted, String sNextId, int iTotalCount)
    {
        this.vAdditionStockLifted = vAdditionStockLifted;
        this.sNextId = sNextId;
        this.iTotalCount = iTotalCount;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vAdditionStockLifted) {
            ostream.writeList(0, vAdditionStockLifted);
        }
        if (null != sNextId) {
            ostream.writeString(1, sNextId);
        }
        ostream.writeInt32(2, iTotalCount);
    }

    static java.util.ArrayList<BEC.AdditionStockLifted> VAR_TYPE_4_VADDITIONSTOCKLIFTED = new java.util.ArrayList<BEC.AdditionStockLifted>();
    static {
        VAR_TYPE_4_VADDITIONSTOCKLIFTED.add(new BEC.AdditionStockLifted());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vAdditionStockLifted = (java.util.ArrayList<BEC.AdditionStockLifted>)istream.readList(0, false, VAR_TYPE_4_VADDITIONSTOCKLIFTED);
        this.sNextId = (String)istream.readString(1, false, this.sNextId);
        this.iTotalCount = (int)istream.readInt32(2, false, this.iTotalCount);
    }

}

