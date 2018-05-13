package BEC;

public final class CheckUserCouponRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iCouponNum = 0;

    public BEC.UserRiskEvalResult stRiskResult = null;

    public int iNeedSign = 1;

    public int getICouponNum()
    {
        return iCouponNum;
    }

    public void  setICouponNum(int iCouponNum)
    {
        this.iCouponNum = iCouponNum;
    }

    public BEC.UserRiskEvalResult getStRiskResult()
    {
        return stRiskResult;
    }

    public void  setStRiskResult(BEC.UserRiskEvalResult stRiskResult)
    {
        this.stRiskResult = stRiskResult;
    }

    public int getINeedSign()
    {
        return iNeedSign;
    }

    public void  setINeedSign(int iNeedSign)
    {
        this.iNeedSign = iNeedSign;
    }

    public CheckUserCouponRsp()
    {
    }

    public CheckUserCouponRsp(int iCouponNum, BEC.UserRiskEvalResult stRiskResult, int iNeedSign)
    {
        this.iCouponNum = iCouponNum;
        this.stRiskResult = stRiskResult;
        this.iNeedSign = iNeedSign;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iCouponNum);
        if (null != stRiskResult) {
            ostream.writeMessage(1, stRiskResult);
        }
        ostream.writeInt32(2, iNeedSign);
    }

    static BEC.UserRiskEvalResult VAR_TYPE_4_STRISKRESULT = new BEC.UserRiskEvalResult();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iCouponNum = (int)istream.readInt32(0, false, this.iCouponNum);
        this.stRiskResult = (BEC.UserRiskEvalResult)istream.readMessage(1, false, VAR_TYPE_4_STRISKRESULT);
        this.iNeedSign = (int)istream.readInt32(2, false, this.iNeedSign);
    }

}

