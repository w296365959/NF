package BEC;

public final class ShortLineStrategyList extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.ShortLineStrategy> vShortLineStrategy = null;

    public java.util.ArrayList<BEC.ShortLineStrategy> getVShortLineStrategy()
    {
        return vShortLineStrategy;
    }

    public void  setVShortLineStrategy(java.util.ArrayList<BEC.ShortLineStrategy> vShortLineStrategy)
    {
        this.vShortLineStrategy = vShortLineStrategy;
    }

    public ShortLineStrategyList()
    {
    }

    public ShortLineStrategyList(java.util.ArrayList<BEC.ShortLineStrategy> vShortLineStrategy)
    {
        this.vShortLineStrategy = vShortLineStrategy;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vShortLineStrategy) {
            ostream.writeList(0, vShortLineStrategy);
        }
    }

    static java.util.ArrayList<BEC.ShortLineStrategy> VAR_TYPE_4_VSHORTLINESTRATEGY = new java.util.ArrayList<BEC.ShortLineStrategy>();
    static {
        VAR_TYPE_4_VSHORTLINESTRATEGY.add(new BEC.ShortLineStrategy());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vShortLineStrategy = (java.util.ArrayList<BEC.ShortLineStrategy>)istream.readList(0, false, VAR_TYPE_4_VSHORTLINESTRATEGY);
    }

}

