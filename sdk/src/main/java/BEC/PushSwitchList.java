package BEC;

public final class PushSwitchList extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.PushSwitchInfo> vSwitchList = null;

    public java.util.ArrayList<BEC.PushSwitchInfo> getVSwitchList()
    {
        return vSwitchList;
    }

    public void  setVSwitchList(java.util.ArrayList<BEC.PushSwitchInfo> vSwitchList)
    {
        this.vSwitchList = vSwitchList;
    }

    public PushSwitchList()
    {
    }

    public PushSwitchList(java.util.ArrayList<BEC.PushSwitchInfo> vSwitchList)
    {
        this.vSwitchList = vSwitchList;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vSwitchList) {
            ostream.writeList(0, vSwitchList);
        }
    }

    static java.util.ArrayList<BEC.PushSwitchInfo> VAR_TYPE_4_VSWITCHLIST = new java.util.ArrayList<BEC.PushSwitchInfo>();
    static {
        VAR_TYPE_4_VSWITCHLIST.add(new BEC.PushSwitchInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vSwitchList = (java.util.ArrayList<BEC.PushSwitchInfo>)istream.readList(0, false, VAR_TYPE_4_VSWITCHLIST);
    }

}

