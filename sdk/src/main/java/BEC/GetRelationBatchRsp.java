package BEC;

public final class GetRelationBatchRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<Long, Integer> mRelation = null;

    public java.util.Map<Long, Integer> getMRelation()
    {
        return mRelation;
    }

    public void  setMRelation(java.util.Map<Long, Integer> mRelation)
    {
        this.mRelation = mRelation;
    }

    public GetRelationBatchRsp()
    {
    }

    public GetRelationBatchRsp(java.util.Map<Long, Integer> mRelation)
    {
        this.mRelation = mRelation;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != mRelation) {
            ostream.writeMap(0, mRelation);
        }
    }

    static java.util.Map<Long, Integer> VAR_TYPE_4_MRELATION = new java.util.HashMap<Long, Integer>();
    static {
        VAR_TYPE_4_MRELATION.put(0L, 0);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.mRelation = (java.util.Map<Long, Integer>)istream.readMap(0, false, VAR_TYPE_4_MRELATION);
    }

}

