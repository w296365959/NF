package BEC;

public final class HisSecPolicyNXInfoRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iDate = 0;

    public java.util.ArrayList<BEC.SecPolicyNXInfo> vDSecPolicy = null;

    public int getIDate()
    {
        return iDate;
    }

    public void  setIDate(int iDate)
    {
        this.iDate = iDate;
    }

    public java.util.ArrayList<BEC.SecPolicyNXInfo> getVDSecPolicy()
    {
        return vDSecPolicy;
    }

    public void  setVDSecPolicy(java.util.ArrayList<BEC.SecPolicyNXInfo> vDSecPolicy)
    {
        this.vDSecPolicy = vDSecPolicy;
    }

    public HisSecPolicyNXInfoRsp()
    {
    }

    public HisSecPolicyNXInfoRsp(int iDate, java.util.ArrayList<BEC.SecPolicyNXInfo> vDSecPolicy)
    {
        this.iDate = iDate;
        this.vDSecPolicy = vDSecPolicy;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iDate);
        if (null != vDSecPolicy) {
            ostream.writeList(1, vDSecPolicy);
        }
    }

    static java.util.ArrayList<BEC.SecPolicyNXInfo> VAR_TYPE_4_VDSECPOLICY = new java.util.ArrayList<BEC.SecPolicyNXInfo>();
    static {
        VAR_TYPE_4_VDSECPOLICY.add(new BEC.SecPolicyNXInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iDate = (int)istream.readInt32(0, false, this.iDate);
        this.vDSecPolicy = (java.util.ArrayList<BEC.SecPolicyNXInfo>)istream.readList(1, false, VAR_TYPE_4_VDSECPOLICY);
    }

}

