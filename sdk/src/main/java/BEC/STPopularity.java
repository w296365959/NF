package BEC;

public final class STPopularity extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public java.util.ArrayList<BEC.STPopuIndex> vtPopuIndex = null;

    public String sPopularityDesc = "";

    public float fVal = 0;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public java.util.ArrayList<BEC.STPopuIndex> getVtPopuIndex()
    {
        return vtPopuIndex;
    }

    public void  setVtPopuIndex(java.util.ArrayList<BEC.STPopuIndex> vtPopuIndex)
    {
        this.vtPopuIndex = vtPopuIndex;
    }

    public String getSPopularityDesc()
    {
        return sPopularityDesc;
    }

    public void  setSPopularityDesc(String sPopularityDesc)
    {
        this.sPopularityDesc = sPopularityDesc;
    }

    public float getFVal()
    {
        return fVal;
    }

    public void  setFVal(float fVal)
    {
        this.fVal = fVal;
    }

    public STPopularity()
    {
    }

    public STPopularity(String sDtSecCode, java.util.ArrayList<BEC.STPopuIndex> vtPopuIndex, String sPopularityDesc, float fVal)
    {
        this.sDtSecCode = sDtSecCode;
        this.vtPopuIndex = vtPopuIndex;
        this.sPopularityDesc = sPopularityDesc;
        this.fVal = fVal;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        if (null != vtPopuIndex) {
            ostream.writeList(1, vtPopuIndex);
        }
        if (null != sPopularityDesc) {
            ostream.writeString(2, sPopularityDesc);
        }
        ostream.writeFloat(3, fVal);
    }

    static java.util.ArrayList<BEC.STPopuIndex> VAR_TYPE_4_VTPOPUINDEX = new java.util.ArrayList<BEC.STPopuIndex>();
    static {
        VAR_TYPE_4_VTPOPUINDEX.add(new BEC.STPopuIndex());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.vtPopuIndex = (java.util.ArrayList<BEC.STPopuIndex>)istream.readList(1, false, VAR_TYPE_4_VTPOPUINDEX);
        this.sPopularityDesc = (String)istream.readString(2, false, this.sPopularityDesc);
        this.fVal = (float)istream.readFloat(3, false, this.fVal);
    }

}

