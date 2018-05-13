package BEC;

public final class GetUserCouponNumRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iCouponNum = 0;

    public int getICouponNum()
    {
        return iCouponNum;
    }

    public void  setICouponNum(int iCouponNum)
    {
        this.iCouponNum = iCouponNum;
    }

    public GetUserCouponNumRsp()
    {
    }

    public GetUserCouponNumRsp(int iCouponNum)
    {
        this.iCouponNum = iCouponNum;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iCouponNum);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iCouponNum = (int)istream.readInt32(0, false, this.iCouponNum);
    }

}

