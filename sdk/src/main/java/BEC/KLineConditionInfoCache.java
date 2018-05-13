package BEC;

public final class KLineConditionInfoCache extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<String, java.util.ArrayList<BEC.KLineConditionInfo>> mpKeyVtCondition = null;

    public java.util.Map<String, java.util.ArrayList<BEC.KLineConditionInfo>> getMpKeyVtCondition()
    {
        return mpKeyVtCondition;
    }

    public void  setMpKeyVtCondition(java.util.Map<String, java.util.ArrayList<BEC.KLineConditionInfo>> mpKeyVtCondition)
    {
        this.mpKeyVtCondition = mpKeyVtCondition;
    }

    public KLineConditionInfoCache()
    {
    }

    public KLineConditionInfoCache(java.util.Map<String, java.util.ArrayList<BEC.KLineConditionInfo>> mpKeyVtCondition)
    {
        this.mpKeyVtCondition = mpKeyVtCondition;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != mpKeyVtCondition) {
            ostream.writeMap(0, mpKeyVtCondition);
        }
    }

    static java.util.Map<String, java.util.ArrayList<BEC.KLineConditionInfo>> VAR_TYPE_4_MPKEYVTCONDITION = new java.util.HashMap<String, java.util.ArrayList<BEC.KLineConditionInfo>>();
    static {
        java.util.ArrayList<BEC.KLineConditionInfo> VAR_TYPE_4_MPKEYVTCONDITION_V_C = new java.util.ArrayList<BEC.KLineConditionInfo>();
        VAR_TYPE_4_MPKEYVTCONDITION_V_C.add(new BEC.KLineConditionInfo());
        VAR_TYPE_4_MPKEYVTCONDITION.put("", VAR_TYPE_4_MPKEYVTCONDITION_V_C);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.mpKeyVtCondition = (java.util.Map<String, java.util.ArrayList<BEC.KLineConditionInfo>>)istream.readMap(0, false, VAR_TYPE_4_MPKEYVTCONDITION);
    }

}

