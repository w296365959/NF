package BEC;

public final class CommonSearchRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sShowOrder = "2,1,3";

    public SecResult stSecResult = null;

    public PlateResult stPlateResult = null;

    public ConceptResult stConceptResult = null;

    public CityResult stCityResult = null;

    public CityResult stSaleDepartResult = null;

    public String getSShowOrder()
    {
        return sShowOrder;
    }

    public void  setSShowOrder(String sShowOrder)
    {
        this.sShowOrder = sShowOrder;
    }

    public SecResult getStSecResult()
    {
        return stSecResult;
    }

    public void  setStSecResult(SecResult stSecResult)
    {
        this.stSecResult = stSecResult;
    }

    public PlateResult getStPlateResult()
    {
        return stPlateResult;
    }

    public void  setStPlateResult(PlateResult stPlateResult)
    {
        this.stPlateResult = stPlateResult;
    }

    public ConceptResult getStConceptResult()
    {
        return stConceptResult;
    }

    public void  setStConceptResult(ConceptResult stConceptResult)
    {
        this.stConceptResult = stConceptResult;
    }

    public CityResult getStCityResult()
    {
        return stCityResult;
    }

    public void  setStCityResult(CityResult stCityResult)
    {
        this.stCityResult = stCityResult;
    }

    public CityResult getStSaleDepartResult()
    {
        return stSaleDepartResult;
    }

    public void  setStSaleDepartResult(CityResult stSaleDepartResult)
    {
        this.stSaleDepartResult = stSaleDepartResult;
    }

    public CommonSearchRsp()
    {
    }

    public CommonSearchRsp(String sShowOrder, SecResult stSecResult, PlateResult stPlateResult, ConceptResult stConceptResult, CityResult stCityResult, CityResult stSaleDepartResult)
    {
        this.sShowOrder = sShowOrder;
        this.stSecResult = stSecResult;
        this.stPlateResult = stPlateResult;
        this.stConceptResult = stConceptResult;
        this.stCityResult = stCityResult;
        this.stSaleDepartResult = stSaleDepartResult;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sShowOrder) {
            ostream.writeString(0, sShowOrder);
        }
        if (null != stSecResult) {
            ostream.writeMessage(1, stSecResult);
        }
        if (null != stPlateResult) {
            ostream.writeMessage(2, stPlateResult);
        }
        if (null != stConceptResult) {
            ostream.writeMessage(3, stConceptResult);
        }
        if (null != stCityResult) {
            ostream.writeMessage(4, stCityResult);
        }
        if (null != stSaleDepartResult) {
            ostream.writeMessage(5, stSaleDepartResult);
        }
    }

    static SecResult VAR_TYPE_4_STSECRESULT = new SecResult();

    static PlateResult VAR_TYPE_4_STPLATERESULT = new PlateResult();

    static ConceptResult VAR_TYPE_4_STCONCEPTRESULT = new ConceptResult();

    static CityResult VAR_TYPE_4_STCITYRESULT = new CityResult();

    static CityResult VAR_TYPE_4_STSALEDEPARTRESULT = new CityResult();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sShowOrder = (String)istream.readString(0, false, this.sShowOrder);
        this.stSecResult = (SecResult)istream.readMessage(1, false, VAR_TYPE_4_STSECRESULT);
        this.stPlateResult = (PlateResult)istream.readMessage(2, false, VAR_TYPE_4_STPLATERESULT);
        this.stConceptResult = (ConceptResult)istream.readMessage(3, false, VAR_TYPE_4_STCONCEPTRESULT);
        this.stCityResult = (CityResult)istream.readMessage(4, false, VAR_TYPE_4_STCITYRESULT);
        this.stSaleDepartResult = (CityResult)istream.readMessage(5, false, VAR_TYPE_4_STSALEDEPARTRESULT);
    }

}

