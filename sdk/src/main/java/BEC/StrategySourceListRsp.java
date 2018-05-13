package BEC;

public final class StrategySourceListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<String> vSource = null;

    public java.util.ArrayList<String> getVSource()
    {
        return vSource;
    }

    public void  setVSource(java.util.ArrayList<String> vSource)
    {
        this.vSource = vSource;
    }

    public StrategySourceListRsp()
    {
    }

    public StrategySourceListRsp(java.util.ArrayList<String> vSource)
    {
        this.vSource = vSource;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vSource) {
            ostream.writeList(0, vSource);
        }
    }

    static java.util.ArrayList<String> VAR_TYPE_4_VSOURCE = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VSOURCE.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vSource = (java.util.ArrayList<String>)istream.readList(0, false, VAR_TYPE_4_VSOURCE);
    }

}

