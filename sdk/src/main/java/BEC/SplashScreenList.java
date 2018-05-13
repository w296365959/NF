package BEC;

public final class SplashScreenList extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.SplashScreenInfo> vList = null;

    public java.util.ArrayList<BEC.SplashScreenInfo> getVList()
    {
        return vList;
    }

    public void  setVList(java.util.ArrayList<BEC.SplashScreenInfo> vList)
    {
        this.vList = vList;
    }

    public SplashScreenList()
    {
    }

    public SplashScreenList(java.util.ArrayList<BEC.SplashScreenInfo> vList)
    {
        this.vList = vList;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vList) {
            ostream.writeList(0, vList);
        }
    }

    static java.util.ArrayList<BEC.SplashScreenInfo> VAR_TYPE_4_VLIST = new java.util.ArrayList<BEC.SplashScreenInfo>();
    static {
        VAR_TYPE_4_VLIST.add(new BEC.SplashScreenInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vList = (java.util.ArrayList<BEC.SplashScreenInfo>)istream.readList(0, false, VAR_TYPE_4_VLIST);
    }

}

