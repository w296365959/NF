package BEC;

public final class GetLHBSaleDepDetailRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDay = "";

    public String sSaleDepName = "";

    public float fIncome = 0;

    public int sStockNum = 0;

    public java.util.ArrayList<LHBReasonDetail> vLHBReasonDetail = null;

    public java.util.ArrayList<String> vDay = null;

    public String getSDay()
    {
        return sDay;
    }

    public void  setSDay(String sDay)
    {
        this.sDay = sDay;
    }

    public String getSSaleDepName()
    {
        return sSaleDepName;
    }

    public void  setSSaleDepName(String sSaleDepName)
    {
        this.sSaleDepName = sSaleDepName;
    }

    public float getFIncome()
    {
        return fIncome;
    }

    public void  setFIncome(float fIncome)
    {
        this.fIncome = fIncome;
    }

    public int getSStockNum()
    {
        return sStockNum;
    }

    public void  setSStockNum(int sStockNum)
    {
        this.sStockNum = sStockNum;
    }

    public java.util.ArrayList<LHBReasonDetail> getVLHBReasonDetail()
    {
        return vLHBReasonDetail;
    }

    public void  setVLHBReasonDetail(java.util.ArrayList<LHBReasonDetail> vLHBReasonDetail)
    {
        this.vLHBReasonDetail = vLHBReasonDetail;
    }

    public java.util.ArrayList<String> getVDay()
    {
        return vDay;
    }

    public void  setVDay(java.util.ArrayList<String> vDay)
    {
        this.vDay = vDay;
    }

    public GetLHBSaleDepDetailRsp()
    {
    }

    public GetLHBSaleDepDetailRsp(String sDay, String sSaleDepName, float fIncome, int sStockNum, java.util.ArrayList<LHBReasonDetail> vLHBReasonDetail, java.util.ArrayList<String> vDay)
    {
        this.sDay = sDay;
        this.sSaleDepName = sSaleDepName;
        this.fIncome = fIncome;
        this.sStockNum = sStockNum;
        this.vLHBReasonDetail = vLHBReasonDetail;
        this.vDay = vDay;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDay) {
            ostream.writeString(0, sDay);
        }
        if (null != sSaleDepName) {
            ostream.writeString(1, sSaleDepName);
        }
        ostream.writeFloat(2, fIncome);
        ostream.writeInt32(3, sStockNum);
        if (null != vLHBReasonDetail) {
            ostream.writeList(4, vLHBReasonDetail);
        }
        if (null != vDay) {
            ostream.writeList(5, vDay);
        }
    }

    static java.util.ArrayList<LHBReasonDetail> VAR_TYPE_4_VLHBREASONDETAIL = new java.util.ArrayList<LHBReasonDetail>();
    static {
        VAR_TYPE_4_VLHBREASONDETAIL.add(new LHBReasonDetail());
    }

    static java.util.ArrayList<String> VAR_TYPE_4_VDAY = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VDAY.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDay = (String)istream.readString(0, false, this.sDay);
        this.sSaleDepName = (String)istream.readString(1, false, this.sSaleDepName);
        this.fIncome = (float)istream.readFloat(2, false, this.fIncome);
        this.sStockNum = (int)istream.readInt32(3, false, this.sStockNum);
        this.vLHBReasonDetail = (java.util.ArrayList<LHBReasonDetail>)istream.readList(4, false, VAR_TYPE_4_VLHBREASONDETAIL);
        this.vDay = (java.util.ArrayList<String>)istream.readList(5, false, VAR_TYPE_4_VDAY);
    }

}

