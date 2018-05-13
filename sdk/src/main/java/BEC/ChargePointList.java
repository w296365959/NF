package BEC;

public final class ChargePointList extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.ChargePointInfo> vChargeList = null;

    public java.util.ArrayList<BEC.ChargePointInfo> getVChargeList()
    {
        return vChargeList;
    }

    public void  setVChargeList(java.util.ArrayList<BEC.ChargePointInfo> vChargeList)
    {
        this.vChargeList = vChargeList;
    }

    public ChargePointList()
    {
    }

    public ChargePointList(java.util.ArrayList<BEC.ChargePointInfo> vChargeList)
    {
        this.vChargeList = vChargeList;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vChargeList) {
            ostream.writeList(0, vChargeList);
        }
    }

    static java.util.ArrayList<BEC.ChargePointInfo> VAR_TYPE_4_VCHARGELIST = new java.util.ArrayList<BEC.ChargePointInfo>();
    static {
        VAR_TYPE_4_VCHARGELIST.add(new BEC.ChargePointInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vChargeList = (java.util.ArrayList<BEC.ChargePointInfo>)istream.readList(0, false, VAR_TYPE_4_VCHARGELIST);
    }

}

