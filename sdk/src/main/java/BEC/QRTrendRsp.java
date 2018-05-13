package BEC;

public final class QRTrendRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<QRDes> vMarketQRTrend = null;

    public int iMarketDate = 0;

    public java.util.ArrayList<QRDes> getVMarketQRTrend()
    {
        return vMarketQRTrend;
    }

    public void  setVMarketQRTrend(java.util.ArrayList<QRDes> vMarketQRTrend)
    {
        this.vMarketQRTrend = vMarketQRTrend;
    }

    public int getIMarketDate()
    {
        return iMarketDate;
    }

    public void  setIMarketDate(int iMarketDate)
    {
        this.iMarketDate = iMarketDate;
    }

    public QRTrendRsp()
    {
    }

    public QRTrendRsp(java.util.ArrayList<QRDes> vMarketQRTrend, int iMarketDate)
    {
        this.vMarketQRTrend = vMarketQRTrend;
        this.iMarketDate = iMarketDate;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vMarketQRTrend) {
            ostream.writeList(0, vMarketQRTrend);
        }
        ostream.writeInt32(1, iMarketDate);
    }

    static java.util.ArrayList<QRDes> VAR_TYPE_4_VMARKETQRTREND = new java.util.ArrayList<QRDes>();
    static {
        VAR_TYPE_4_VMARKETQRTREND.add(new QRDes());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vMarketQRTrend = (java.util.ArrayList<QRDes>)istream.readList(0, false, VAR_TYPE_4_VMARKETQRTREND);
        this.iMarketDate = (int)istream.readInt32(1, false, this.iMarketDate);
    }

}

