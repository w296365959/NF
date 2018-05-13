package BEC;

public final class SecRateRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.OrgRate> vtOrgRate = null;

    public String sDesc = "";

    public float fRate = 0;

    public java.util.ArrayList<BEC.OrgRate> getVtOrgRate()
    {
        return vtOrgRate;
    }

    public void  setVtOrgRate(java.util.ArrayList<BEC.OrgRate> vtOrgRate)
    {
        this.vtOrgRate = vtOrgRate;
    }

    public String getSDesc()
    {
        return sDesc;
    }

    public void  setSDesc(String sDesc)
    {
        this.sDesc = sDesc;
    }

    public float getFRate()
    {
        return fRate;
    }

    public void  setFRate(float fRate)
    {
        this.fRate = fRate;
    }

    public SecRateRsp()
    {
    }

    public SecRateRsp(java.util.ArrayList<BEC.OrgRate> vtOrgRate, String sDesc, float fRate)
    {
        this.vtOrgRate = vtOrgRate;
        this.sDesc = sDesc;
        this.fRate = fRate;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtOrgRate) {
            ostream.writeList(0, vtOrgRate);
        }
        if (null != sDesc) {
            ostream.writeString(1, sDesc);
        }
        ostream.writeFloat(2, fRate);
    }

    static java.util.ArrayList<BEC.OrgRate> VAR_TYPE_4_VTORGRATE = new java.util.ArrayList<BEC.OrgRate>();
    static {
        VAR_TYPE_4_VTORGRATE.add(new BEC.OrgRate());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtOrgRate = (java.util.ArrayList<BEC.OrgRate>)istream.readList(0, false, VAR_TYPE_4_VTORGRATE);
        this.sDesc = (String)istream.readString(1, false, this.sDesc);
        this.fRate = (float)istream.readFloat(2, false, this.fRate);
    }

}

