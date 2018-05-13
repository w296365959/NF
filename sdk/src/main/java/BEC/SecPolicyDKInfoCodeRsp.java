package BEC;

public final class SecPolicyDKInfoCodeRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public SecCodeDetail stSecCodeDetail = null;

    public java.util.ArrayList<SecPolicyDKInfoByDate> vPolicyDKInfo = null;

    public SecCodeDetail getStSecCodeDetail()
    {
        return stSecCodeDetail;
    }

    public void  setStSecCodeDetail(SecCodeDetail stSecCodeDetail)
    {
        this.stSecCodeDetail = stSecCodeDetail;
    }

    public java.util.ArrayList<SecPolicyDKInfoByDate> getVPolicyDKInfo()
    {
        return vPolicyDKInfo;
    }

    public void  setVPolicyDKInfo(java.util.ArrayList<SecPolicyDKInfoByDate> vPolicyDKInfo)
    {
        this.vPolicyDKInfo = vPolicyDKInfo;
    }

    public SecPolicyDKInfoCodeRsp()
    {
    }

    public SecPolicyDKInfoCodeRsp(SecCodeDetail stSecCodeDetail, java.util.ArrayList<SecPolicyDKInfoByDate> vPolicyDKInfo)
    {
        this.stSecCodeDetail = stSecCodeDetail;
        this.vPolicyDKInfo = vPolicyDKInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stSecCodeDetail) {
            ostream.writeMessage(0, stSecCodeDetail);
        }
        if (null != vPolicyDKInfo) {
            ostream.writeList(1, vPolicyDKInfo);
        }
    }

    static SecCodeDetail VAR_TYPE_4_STSECCODEDETAIL = new SecCodeDetail();

    static java.util.ArrayList<SecPolicyDKInfoByDate> VAR_TYPE_4_VPOLICYDKINFO = new java.util.ArrayList<SecPolicyDKInfoByDate>();
    static {
        VAR_TYPE_4_VPOLICYDKINFO.add(new SecPolicyDKInfoByDate());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stSecCodeDetail = (SecCodeDetail)istream.readMessage(0, false, VAR_TYPE_4_STSECCODEDETAIL);
        this.vPolicyDKInfo = (java.util.ArrayList<SecPolicyDKInfoByDate>)istream.readList(1, false, VAR_TYPE_4_VPOLICYDKINFO);
    }

}

