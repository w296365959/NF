package BEC;

public final class CityResult extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iRet = 0;

    public java.util.ArrayList<BEC.CityInfo> vtCityInfo = null;

    public int getIRet()
    {
        return iRet;
    }

    public void  setIRet(int iRet)
    {
        this.iRet = iRet;
    }

    public java.util.ArrayList<BEC.CityInfo> getVtCityInfo()
    {
        return vtCityInfo;
    }

    public void  setVtCityInfo(java.util.ArrayList<BEC.CityInfo> vtCityInfo)
    {
        this.vtCityInfo = vtCityInfo;
    }

    public CityResult()
    {
    }

    public CityResult(int iRet, java.util.ArrayList<BEC.CityInfo> vtCityInfo)
    {
        this.iRet = iRet;
        this.vtCityInfo = vtCityInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iRet);
        if (null != vtCityInfo) {
            ostream.writeList(1, vtCityInfo);
        }
    }

    static java.util.ArrayList<BEC.CityInfo> VAR_TYPE_4_VTCITYINFO = new java.util.ArrayList<BEC.CityInfo>();
    static {
        VAR_TYPE_4_VTCITYINFO.add(new BEC.CityInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iRet = (int)istream.readInt32(0, false, this.iRet);
        this.vtCityInfo = (java.util.ArrayList<BEC.CityInfo>)istream.readList(1, false, VAR_TYPE_4_VTCITYINFO);
    }

}

