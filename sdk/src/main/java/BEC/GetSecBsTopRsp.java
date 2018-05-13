package BEC;

public final class GetSecBsTopRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<SecBsInfo> vSecBsInfoBuy = null;

    public java.util.ArrayList<SecBsInfo> vSecBsInfoSell = null;

    public java.util.ArrayList<SecBsInfo> getVSecBsInfoBuy()
    {
        return vSecBsInfoBuy;
    }

    public void  setVSecBsInfoBuy(java.util.ArrayList<SecBsInfo> vSecBsInfoBuy)
    {
        this.vSecBsInfoBuy = vSecBsInfoBuy;
    }

    public java.util.ArrayList<SecBsInfo> getVSecBsInfoSell()
    {
        return vSecBsInfoSell;
    }

    public void  setVSecBsInfoSell(java.util.ArrayList<SecBsInfo> vSecBsInfoSell)
    {
        this.vSecBsInfoSell = vSecBsInfoSell;
    }

    public GetSecBsTopRsp()
    {
    }

    public GetSecBsTopRsp(java.util.ArrayList<SecBsInfo> vSecBsInfoBuy, java.util.ArrayList<SecBsInfo> vSecBsInfoSell)
    {
        this.vSecBsInfoBuy = vSecBsInfoBuy;
        this.vSecBsInfoSell = vSecBsInfoSell;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vSecBsInfoBuy) {
            ostream.writeList(0, vSecBsInfoBuy);
        }
        if (null != vSecBsInfoSell) {
            ostream.writeList(1, vSecBsInfoSell);
        }
    }

    static java.util.ArrayList<SecBsInfo> VAR_TYPE_4_VSECBSINFOBUY = new java.util.ArrayList<SecBsInfo>();
    static {
        VAR_TYPE_4_VSECBSINFOBUY.add(new SecBsInfo());
    }

    static java.util.ArrayList<SecBsInfo> VAR_TYPE_4_VSECBSINFOSELL = new java.util.ArrayList<SecBsInfo>();
    static {
        VAR_TYPE_4_VSECBSINFOSELL.add(new SecBsInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vSecBsInfoBuy = (java.util.ArrayList<SecBsInfo>)istream.readList(0, false, VAR_TYPE_4_VSECBSINFOBUY);
        this.vSecBsInfoSell = (java.util.ArrayList<SecBsInfo>)istream.readList(1, false, VAR_TYPE_4_VSECBSINFOSELL);
    }

}

