package BEC;

public final class TodaySecPolicyNXInfoRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iDate = 0;

    public java.util.ArrayList<SecPolicyNXInfo> vNSecPolicy = null;

    public java.util.ArrayList<SecPolicyNXInfo> vXSecPolicy = null;

    public java.util.ArrayList<SecPolicyNXInfo> vDurNSecPolicy = null;

    public int getIDate()
    {
        return iDate;
    }

    public void  setIDate(int iDate)
    {
        this.iDate = iDate;
    }

    public java.util.ArrayList<SecPolicyNXInfo> getVNSecPolicy()
    {
        return vNSecPolicy;
    }

    public void  setVNSecPolicy(java.util.ArrayList<SecPolicyNXInfo> vNSecPolicy)
    {
        this.vNSecPolicy = vNSecPolicy;
    }

    public java.util.ArrayList<SecPolicyNXInfo> getVXSecPolicy()
    {
        return vXSecPolicy;
    }

    public void  setVXSecPolicy(java.util.ArrayList<SecPolicyNXInfo> vXSecPolicy)
    {
        this.vXSecPolicy = vXSecPolicy;
    }

    public java.util.ArrayList<SecPolicyNXInfo> getVDurNSecPolicy()
    {
        return vDurNSecPolicy;
    }

    public void  setVDurNSecPolicy(java.util.ArrayList<SecPolicyNXInfo> vDurNSecPolicy)
    {
        this.vDurNSecPolicy = vDurNSecPolicy;
    }

    public TodaySecPolicyNXInfoRsp()
    {
    }

    public TodaySecPolicyNXInfoRsp(int iDate, java.util.ArrayList<SecPolicyNXInfo> vNSecPolicy, java.util.ArrayList<SecPolicyNXInfo> vXSecPolicy, java.util.ArrayList<SecPolicyNXInfo> vDurNSecPolicy)
    {
        this.iDate = iDate;
        this.vNSecPolicy = vNSecPolicy;
        this.vXSecPolicy = vXSecPolicy;
        this.vDurNSecPolicy = vDurNSecPolicy;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iDate);
        if (null != vNSecPolicy) {
            ostream.writeList(1, vNSecPolicy);
        }
        if (null != vXSecPolicy) {
            ostream.writeList(2, vXSecPolicy);
        }
        if (null != vDurNSecPolicy) {
            ostream.writeList(3, vDurNSecPolicy);
        }
    }

    static java.util.ArrayList<SecPolicyNXInfo> VAR_TYPE_4_VNSECPOLICY = new java.util.ArrayList<SecPolicyNXInfo>();
    static {
        VAR_TYPE_4_VNSECPOLICY.add(new SecPolicyNXInfo());
    }

    static java.util.ArrayList<SecPolicyNXInfo> VAR_TYPE_4_VXSECPOLICY = new java.util.ArrayList<SecPolicyNXInfo>();
    static {
        VAR_TYPE_4_VXSECPOLICY.add(new SecPolicyNXInfo());
    }

    static java.util.ArrayList<SecPolicyNXInfo> VAR_TYPE_4_VDURNSECPOLICY = new java.util.ArrayList<SecPolicyNXInfo>();
    static {
        VAR_TYPE_4_VDURNSECPOLICY.add(new SecPolicyNXInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iDate = (int)istream.readInt32(0, false, this.iDate);
        this.vNSecPolicy = (java.util.ArrayList<SecPolicyNXInfo>)istream.readList(1, false, VAR_TYPE_4_VNSECPOLICY);
        this.vXSecPolicy = (java.util.ArrayList<SecPolicyNXInfo>)istream.readList(2, false, VAR_TYPE_4_VXSECPOLICY);
        this.vDurNSecPolicy = (java.util.ArrayList<SecPolicyNXInfo>)istream.readList(3, false, VAR_TYPE_4_VDURNSECPOLICY);
    }

}

