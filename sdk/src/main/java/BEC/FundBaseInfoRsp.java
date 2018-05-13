package BEC;

public final class FundBaseInfoRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.FundInfo stFundInfo = null;

    public BEC.FundInfo getStFundInfo()
    {
        return stFundInfo;
    }

    public void  setStFundInfo(BEC.FundInfo stFundInfo)
    {
        this.stFundInfo = stFundInfo;
    }

    public FundBaseInfoRsp()
    {
    }

    public FundBaseInfoRsp(BEC.FundInfo stFundInfo)
    {
        this.stFundInfo = stFundInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stFundInfo) {
            ostream.writeMessage(0, stFundInfo);
        }
    }

    static BEC.FundInfo VAR_TYPE_4_STFUNDINFO = new BEC.FundInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stFundInfo = (BEC.FundInfo)istream.readMessage(0, false, VAR_TYPE_4_STFUNDINFO);
    }

}

