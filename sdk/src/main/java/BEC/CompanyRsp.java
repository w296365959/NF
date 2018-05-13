package BEC;

public final class CompanyRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.CompanyDesc stCompanyDesc = null;

    public BEC.CompanyDesc getStCompanyDesc()
    {
        return stCompanyDesc;
    }

    public void  setStCompanyDesc(BEC.CompanyDesc stCompanyDesc)
    {
        this.stCompanyDesc = stCompanyDesc;
    }

    public CompanyRsp()
    {
    }

    public CompanyRsp(BEC.CompanyDesc stCompanyDesc)
    {
        this.stCompanyDesc = stCompanyDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stCompanyDesc) {
            ostream.writeMessage(0, stCompanyDesc);
        }
    }

    static BEC.CompanyDesc VAR_TYPE_4_STCOMPANYDESC = new BEC.CompanyDesc();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stCompanyDesc = (BEC.CompanyDesc)istream.readMessage(0, false, VAR_TYPE_4_STCOMPANYDESC);
    }

}

