package BEC;

public final class GetInvestAdvisorListRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.InvestAdviseInfoList stInvestAdviseInfoList = null;

    public BEC.InvestAdviseInfoList getStInvestAdviseInfoList()
    {
        return stInvestAdviseInfoList;
    }

    public void  setStInvestAdviseInfoList(BEC.InvestAdviseInfoList stInvestAdviseInfoList)
    {
        this.stInvestAdviseInfoList = stInvestAdviseInfoList;
    }

    public GetInvestAdvisorListRsp()
    {
    }

    public GetInvestAdvisorListRsp(BEC.InvestAdviseInfoList stInvestAdviseInfoList)
    {
        this.stInvestAdviseInfoList = stInvestAdviseInfoList;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stInvestAdviseInfoList) {
            ostream.writeMessage(0, stInvestAdviseInfoList);
        }
    }

    static BEC.InvestAdviseInfoList VAR_TYPE_4_STINVESTADVISEINFOLIST = new BEC.InvestAdviseInfoList();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stInvestAdviseInfoList = (BEC.InvestAdviseInfoList)istream.readMessage(0, false, VAR_TYPE_4_STINVESTADVISEINFOLIST);
    }

}

