package BEC;

public final class DtMemberFeeItem extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iMonthNum = 0;

    public int iAvgMoney = 0;

    public int iTotalMoney = 0;

    public int iUnit = E_DT_PAY_TIME_UNIT.E_DT_PAY_TIME_MONTH;

    public String sDesc = "";

    public String sTag = "";

    public boolean bLineCrossed = false;

    public boolean bDefaultChoosed = false;

    public int getIMonthNum()
    {
        return iMonthNum;
    }

    public void  setIMonthNum(int iMonthNum)
    {
        this.iMonthNum = iMonthNum;
    }

    public int getIAvgMoney()
    {
        return iAvgMoney;
    }

    public void  setIAvgMoney(int iAvgMoney)
    {
        this.iAvgMoney = iAvgMoney;
    }

    public int getITotalMoney()
    {
        return iTotalMoney;
    }

    public void  setITotalMoney(int iTotalMoney)
    {
        this.iTotalMoney = iTotalMoney;
    }

    public int getIUnit()
    {
        return iUnit;
    }

    public void  setIUnit(int iUnit)
    {
        this.iUnit = iUnit;
    }

    public String getSDesc()
    {
        return sDesc;
    }

    public void  setSDesc(String sDesc)
    {
        this.sDesc = sDesc;
    }

    public String getSTag()
    {
        return sTag;
    }

    public void  setSTag(String sTag)
    {
        this.sTag = sTag;
    }

    public boolean getBLineCrossed()
    {
        return bLineCrossed;
    }

    public void  setBLineCrossed(boolean bLineCrossed)
    {
        this.bLineCrossed = bLineCrossed;
    }

    public boolean getBDefaultChoosed()
    {
        return bDefaultChoosed;
    }

    public void  setBDefaultChoosed(boolean bDefaultChoosed)
    {
        this.bDefaultChoosed = bDefaultChoosed;
    }

    public DtMemberFeeItem()
    {
    }

    public DtMemberFeeItem(int iMonthNum, int iAvgMoney, int iTotalMoney, int iUnit, String sDesc, String sTag, boolean bLineCrossed, boolean bDefaultChoosed)
    {
        this.iMonthNum = iMonthNum;
        this.iAvgMoney = iAvgMoney;
        this.iTotalMoney = iTotalMoney;
        this.iUnit = iUnit;
        this.sDesc = sDesc;
        this.sTag = sTag;
        this.bLineCrossed = bLineCrossed;
        this.bDefaultChoosed = bDefaultChoosed;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iMonthNum);
        ostream.writeInt32(1, iAvgMoney);
        ostream.writeInt32(2, iTotalMoney);
        ostream.writeInt32(3, iUnit);
        if (null != sDesc) {
            ostream.writeString(4, sDesc);
        }
        if (null != sTag) {
            ostream.writeString(5, sTag);
        }
        ostream.writeBoolean(6, bLineCrossed);
        ostream.writeBoolean(7, bDefaultChoosed);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iMonthNum = (int)istream.readInt32(0, false, this.iMonthNum);
        this.iAvgMoney = (int)istream.readInt32(1, false, this.iAvgMoney);
        this.iTotalMoney = (int)istream.readInt32(2, false, this.iTotalMoney);
        this.iUnit = (int)istream.readInt32(3, false, this.iUnit);
        this.sDesc = (String)istream.readString(4, false, this.sDesc);
        this.sTag = (String)istream.readString(5, false, this.sTag);
        this.bLineCrossed = (boolean)istream.readBoolean(6, false, this.bLineCrossed);
        this.bDefaultChoosed = (boolean)istream.readBoolean(7, false, this.bDefaultChoosed);
    }

}

