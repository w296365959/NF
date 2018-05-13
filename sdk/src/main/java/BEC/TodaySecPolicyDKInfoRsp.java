package BEC;

public final class TodaySecPolicyDKInfoRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iDate = 0;

    public java.util.ArrayList<SecPolicyDKInfoByCode> vDSecPolicy = null;

    public java.util.ArrayList<SecPolicyDKInfoByCode> vKSecPolicy = null;

    public int getIDate()
    {
        return iDate;
    }

    public void  setIDate(int iDate)
    {
        this.iDate = iDate;
    }

    public java.util.ArrayList<SecPolicyDKInfoByCode> getVDSecPolicy()
    {
        return vDSecPolicy;
    }

    public void  setVDSecPolicy(java.util.ArrayList<SecPolicyDKInfoByCode> vDSecPolicy)
    {
        this.vDSecPolicy = vDSecPolicy;
    }

    public java.util.ArrayList<SecPolicyDKInfoByCode> getVKSecPolicy()
    {
        return vKSecPolicy;
    }

    public void  setVKSecPolicy(java.util.ArrayList<SecPolicyDKInfoByCode> vKSecPolicy)
    {
        this.vKSecPolicy = vKSecPolicy;
    }

    public TodaySecPolicyDKInfoRsp()
    {
    }

    public TodaySecPolicyDKInfoRsp(int iDate, java.util.ArrayList<SecPolicyDKInfoByCode> vDSecPolicy, java.util.ArrayList<SecPolicyDKInfoByCode> vKSecPolicy)
    {
        this.iDate = iDate;
        this.vDSecPolicy = vDSecPolicy;
        this.vKSecPolicy = vKSecPolicy;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iDate);
        if (null != vDSecPolicy) {
            ostream.writeList(1, vDSecPolicy);
        }
        if (null != vKSecPolicy) {
            ostream.writeList(2, vKSecPolicy);
        }
    }

    static java.util.ArrayList<SecPolicyDKInfoByCode> VAR_TYPE_4_VDSECPOLICY = new java.util.ArrayList<SecPolicyDKInfoByCode>();
    static {
        VAR_TYPE_4_VDSECPOLICY.add(new SecPolicyDKInfoByCode());
    }

    static java.util.ArrayList<SecPolicyDKInfoByCode> VAR_TYPE_4_VKSECPOLICY = new java.util.ArrayList<SecPolicyDKInfoByCode>();
    static {
        VAR_TYPE_4_VKSECPOLICY.add(new SecPolicyDKInfoByCode());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iDate = (int)istream.readInt32(0, false, this.iDate);
        this.vDSecPolicy = (java.util.ArrayList<SecPolicyDKInfoByCode>)istream.readList(1, false, VAR_TYPE_4_VDSECPOLICY);
        this.vKSecPolicy = (java.util.ArrayList<SecPolicyDKInfoByCode>)istream.readList(2, false, VAR_TYPE_4_VKSECPOLICY);
    }

}

