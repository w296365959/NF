package BEC;

public final class StareStrategyClass extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sName = "";

    public java.util.ArrayList<BEC.StareStrategySubClass> vSubClass = null;

    public int iStraType = 0;

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public java.util.ArrayList<BEC.StareStrategySubClass> getVSubClass()
    {
        return vSubClass;
    }

    public void  setVSubClass(java.util.ArrayList<BEC.StareStrategySubClass> vSubClass)
    {
        this.vSubClass = vSubClass;
    }

    public int getIStraType()
    {
        return iStraType;
    }

    public void  setIStraType(int iStraType)
    {
        this.iStraType = iStraType;
    }

    public StareStrategyClass()
    {
    }

    public StareStrategyClass(String sName, java.util.ArrayList<BEC.StareStrategySubClass> vSubClass, int iStraType)
    {
        this.sName = sName;
        this.vSubClass = vSubClass;
        this.iStraType = iStraType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sName) {
            ostream.writeString(0, sName);
        }
        if (null != vSubClass) {
            ostream.writeList(1, vSubClass);
        }
        ostream.writeInt32(2, iStraType);
    }

    static java.util.ArrayList<BEC.StareStrategySubClass> VAR_TYPE_4_VSUBCLASS = new java.util.ArrayList<BEC.StareStrategySubClass>();
    static {
        VAR_TYPE_4_VSUBCLASS.add(new BEC.StareStrategySubClass());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sName = (String)istream.readString(0, false, this.sName);
        this.vSubClass = (java.util.ArrayList<BEC.StareStrategySubClass>)istream.readList(1, false, VAR_TYPE_4_VSUBCLASS);
        this.iStraType = (int)istream.readInt32(2, false, this.iStraType);
    }

}

