package BEC;

public final class UpInfoIdList extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public int eNewsType = 0;

    public java.util.Map<String, BEC.NewsDesc> mNewsDesc = null;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public int getENewsType()
    {
        return eNewsType;
    }

    public void  setENewsType(int eNewsType)
    {
        this.eNewsType = eNewsType;
    }

    public java.util.Map<String, BEC.NewsDesc> getMNewsDesc()
    {
        return mNewsDesc;
    }

    public void  setMNewsDesc(java.util.Map<String, BEC.NewsDesc> mNewsDesc)
    {
        this.mNewsDesc = mNewsDesc;
    }

    public UpInfoIdList()
    {
    }

    public UpInfoIdList(String sDtSecCode, int eNewsType, java.util.Map<String, BEC.NewsDesc> mNewsDesc)
    {
        this.sDtSecCode = sDtSecCode;
        this.eNewsType = eNewsType;
        this.mNewsDesc = mNewsDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        ostream.writeInt32(1, eNewsType);
        if (null != mNewsDesc) {
            ostream.writeMap(2, mNewsDesc);
        }
    }

    static java.util.Map<String, BEC.NewsDesc> VAR_TYPE_4_MNEWSDESC = new java.util.HashMap<String, BEC.NewsDesc>();
    static {
        VAR_TYPE_4_MNEWSDESC.put("", new BEC.NewsDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.eNewsType = (int)istream.readInt32(1, false, this.eNewsType);
        this.mNewsDesc = (java.util.Map<String, BEC.NewsDesc>)istream.readMap(2, false, VAR_TYPE_4_MNEWSDESC);
    }

}

