package BEC;

public final class SecPortraitIndustry extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sName = "";

    public java.util.ArrayList<SecPortraitUpdowns> vtUpdowns = null;

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public java.util.ArrayList<SecPortraitUpdowns> getVtUpdowns()
    {
        return vtUpdowns;
    }

    public void  setVtUpdowns(java.util.ArrayList<SecPortraitUpdowns> vtUpdowns)
    {
        this.vtUpdowns = vtUpdowns;
    }

    public SecPortraitIndustry()
    {
    }

    public SecPortraitIndustry(String sName, java.util.ArrayList<SecPortraitUpdowns> vtUpdowns)
    {
        this.sName = sName;
        this.vtUpdowns = vtUpdowns;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sName) {
            ostream.writeString(0, sName);
        }
        if (null != vtUpdowns) {
            ostream.writeList(1, vtUpdowns);
        }
    }

    static java.util.ArrayList<SecPortraitUpdowns> VAR_TYPE_4_VTUPDOWNS = new java.util.ArrayList<SecPortraitUpdowns>();
    static {
        VAR_TYPE_4_VTUPDOWNS.add(new SecPortraitUpdowns());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sName = (String)istream.readString(0, false, this.sName);
        this.vtUpdowns = (java.util.ArrayList<SecPortraitUpdowns>)istream.readList(1, false, VAR_TYPE_4_VTUPDOWNS);
    }

}

