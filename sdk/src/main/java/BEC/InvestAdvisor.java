package BEC;

public final class InvestAdvisor extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sName = "";

    public String sOrgName = "";

    public String sFaceUrl = "";

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public String getSOrgName()
    {
        return sOrgName;
    }

    public void  setSOrgName(String sOrgName)
    {
        this.sOrgName = sOrgName;
    }

    public String getSFaceUrl()
    {
        return sFaceUrl;
    }

    public void  setSFaceUrl(String sFaceUrl)
    {
        this.sFaceUrl = sFaceUrl;
    }

    public InvestAdvisor()
    {
    }

    public InvestAdvisor(String sName, String sOrgName, String sFaceUrl)
    {
        this.sName = sName;
        this.sOrgName = sOrgName;
        this.sFaceUrl = sFaceUrl;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sName) {
            ostream.writeString(0, sName);
        }
        if (null != sOrgName) {
            ostream.writeString(1, sOrgName);
        }
        if (null != sFaceUrl) {
            ostream.writeString(2, sFaceUrl);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sName = (String)istream.readString(0, false, this.sName);
        this.sOrgName = (String)istream.readString(1, false, this.sOrgName);
        this.sFaceUrl = (String)istream.readString(2, false, this.sFaceUrl);
    }

}

