package BEC;

public final class LHBSaleDepInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sName = "";

    public int sStockNum = 0;

    public float fIncome = 0;

    public java.util.ArrayList<LHBReasonDetail> vLHBReasonDetail = null;

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public int getSStockNum()
    {
        return sStockNum;
    }

    public void  setSStockNum(int sStockNum)
    {
        this.sStockNum = sStockNum;
    }

    public float getFIncome()
    {
        return fIncome;
    }

    public void  setFIncome(float fIncome)
    {
        this.fIncome = fIncome;
    }

    public java.util.ArrayList<LHBReasonDetail> getVLHBReasonDetail()
    {
        return vLHBReasonDetail;
    }

    public void  setVLHBReasonDetail(java.util.ArrayList<LHBReasonDetail> vLHBReasonDetail)
    {
        this.vLHBReasonDetail = vLHBReasonDetail;
    }

    public LHBSaleDepInfo()
    {
    }

    public LHBSaleDepInfo(String sName, int sStockNum, float fIncome, java.util.ArrayList<LHBReasonDetail> vLHBReasonDetail)
    {
        this.sName = sName;
        this.sStockNum = sStockNum;
        this.fIncome = fIncome;
        this.vLHBReasonDetail = vLHBReasonDetail;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sName) {
            ostream.writeString(0, sName);
        }
        ostream.writeInt32(1, sStockNum);
        ostream.writeFloat(2, fIncome);
        if (null != vLHBReasonDetail) {
            ostream.writeList(3, vLHBReasonDetail);
        }
    }

    static java.util.ArrayList<LHBReasonDetail> VAR_TYPE_4_VLHBREASONDETAIL = new java.util.ArrayList<LHBReasonDetail>();
    static {
        VAR_TYPE_4_VLHBREASONDETAIL.add(new LHBReasonDetail());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sName = (String)istream.readString(0, false, this.sName);
        this.sStockNum = (int)istream.readInt32(1, false, this.sStockNum);
        this.fIncome = (float)istream.readFloat(2, false, this.fIncome);
        this.vLHBReasonDetail = (java.util.ArrayList<LHBReasonDetail>)istream.readList(3, false, VAR_TYPE_4_VLHBREASONDETAIL);
    }

}

