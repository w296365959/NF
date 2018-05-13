package BEC;

public final class SingalConfigDesc extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sTypeName = "";

    public String sTypeNameChinese = "";

    public String sDesc = "";

    public java.util.ArrayList<SingalModels> vSingalModels = null;

    public String getSTypeName()
    {
        return sTypeName;
    }

    public void  setSTypeName(String sTypeName)
    {
        this.sTypeName = sTypeName;
    }

    public String getSTypeNameChinese()
    {
        return sTypeNameChinese;
    }

    public void  setSTypeNameChinese(String sTypeNameChinese)
    {
        this.sTypeNameChinese = sTypeNameChinese;
    }

    public String getSDesc()
    {
        return sDesc;
    }

    public void  setSDesc(String sDesc)
    {
        this.sDesc = sDesc;
    }

    public java.util.ArrayList<SingalModels> getVSingalModels()
    {
        return vSingalModels;
    }

    public void  setVSingalModels(java.util.ArrayList<SingalModels> vSingalModels)
    {
        this.vSingalModels = vSingalModels;
    }

    public SingalConfigDesc()
    {
    }

    public SingalConfigDesc(String sTypeName, String sTypeNameChinese, String sDesc, java.util.ArrayList<SingalModels> vSingalModels)
    {
        this.sTypeName = sTypeName;
        this.sTypeNameChinese = sTypeNameChinese;
        this.sDesc = sDesc;
        this.vSingalModels = vSingalModels;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sTypeName) {
            ostream.writeString(0, sTypeName);
        }
        if (null != sTypeNameChinese) {
            ostream.writeString(1, sTypeNameChinese);
        }
        if (null != sDesc) {
            ostream.writeString(2, sDesc);
        }
        if (null != vSingalModels) {
            ostream.writeList(3, vSingalModels);
        }
    }

    static java.util.ArrayList<SingalModels> VAR_TYPE_4_VSINGALMODELS = new java.util.ArrayList<SingalModels>();
    static {
        VAR_TYPE_4_VSINGALMODELS.add(new SingalModels());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sTypeName = (String)istream.readString(0, false, this.sTypeName);
        this.sTypeNameChinese = (String)istream.readString(1, false, this.sTypeNameChinese);
        this.sDesc = (String)istream.readString(2, false, this.sDesc);
        this.vSingalModels = (java.util.ArrayList<SingalModels>)istream.readList(3, false, VAR_TYPE_4_VSINGALMODELS);
    }

}

