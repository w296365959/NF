package BEC;

public final class SecRelaLastUpDowns extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<SecUpdowns> vtSecUpdowns = null;

    public String sDesc = "";

    public int iUpdateTime = 0;

    public java.util.ArrayList<SecUpdowns> getVtSecUpdowns()
    {
        return vtSecUpdowns;
    }

    public void  setVtSecUpdowns(java.util.ArrayList<SecUpdowns> vtSecUpdowns)
    {
        this.vtSecUpdowns = vtSecUpdowns;
    }

    public String getSDesc()
    {
        return sDesc;
    }

    public void  setSDesc(String sDesc)
    {
        this.sDesc = sDesc;
    }

    public int getIUpdateTime()
    {
        return iUpdateTime;
    }

    public void  setIUpdateTime(int iUpdateTime)
    {
        this.iUpdateTime = iUpdateTime;
    }

    public SecRelaLastUpDowns()
    {
    }

    public SecRelaLastUpDowns(java.util.ArrayList<SecUpdowns> vtSecUpdowns, String sDesc, int iUpdateTime)
    {
        this.vtSecUpdowns = vtSecUpdowns;
        this.sDesc = sDesc;
        this.iUpdateTime = iUpdateTime;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtSecUpdowns) {
            ostream.writeList(0, vtSecUpdowns);
        }
        if (null != sDesc) {
            ostream.writeString(1, sDesc);
        }
        ostream.writeInt32(2, iUpdateTime);
    }

    static java.util.ArrayList<SecUpdowns> VAR_TYPE_4_VTSECUPDOWNS = new java.util.ArrayList<SecUpdowns>();
    static {
        VAR_TYPE_4_VTSECUPDOWNS.add(new SecUpdowns());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtSecUpdowns = (java.util.ArrayList<SecUpdowns>)istream.readList(0, false, VAR_TYPE_4_VTSECUPDOWNS);
        this.sDesc = (String)istream.readString(1, false, this.sDesc);
        this.iUpdateTime = (int)istream.readInt32(2, false, this.iUpdateTime);
    }

}

