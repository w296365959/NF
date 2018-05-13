package BEC;

public final class TrendPredictRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.STTrendPredict> vtTrendPredict = null;

    public java.util.ArrayList<BEC.STTrendPredict> vtTrendPredictNew = null;

    public java.util.ArrayList<BEC.STTrendPredict> getVtTrendPredict()
    {
        return vtTrendPredict;
    }

    public void  setVtTrendPredict(java.util.ArrayList<BEC.STTrendPredict> vtTrendPredict)
    {
        this.vtTrendPredict = vtTrendPredict;
    }

    public java.util.ArrayList<BEC.STTrendPredict> getVtTrendPredictNew()
    {
        return vtTrendPredictNew;
    }

    public void  setVtTrendPredictNew(java.util.ArrayList<BEC.STTrendPredict> vtTrendPredictNew)
    {
        this.vtTrendPredictNew = vtTrendPredictNew;
    }

    public TrendPredictRsp()
    {
    }

    public TrendPredictRsp(java.util.ArrayList<BEC.STTrendPredict> vtTrendPredict, java.util.ArrayList<BEC.STTrendPredict> vtTrendPredictNew)
    {
        this.vtTrendPredict = vtTrendPredict;
        this.vtTrendPredictNew = vtTrendPredictNew;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtTrendPredict) {
            ostream.writeList(0, vtTrendPredict);
        }
        if (null != vtTrendPredictNew) {
            ostream.writeList(1, vtTrendPredictNew);
        }
    }

    static java.util.ArrayList<BEC.STTrendPredict> VAR_TYPE_4_VTTRENDPREDICT = new java.util.ArrayList<BEC.STTrendPredict>();
    static {
        VAR_TYPE_4_VTTRENDPREDICT.add(new BEC.STTrendPredict());
    }

    static java.util.ArrayList<BEC.STTrendPredict> VAR_TYPE_4_VTTRENDPREDICTNEW = new java.util.ArrayList<BEC.STTrendPredict>();
    static {
        VAR_TYPE_4_VTTRENDPREDICTNEW.add(new BEC.STTrendPredict());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtTrendPredict = (java.util.ArrayList<BEC.STTrendPredict>)istream.readList(0, false, VAR_TYPE_4_VTTRENDPREDICT);
        this.vtTrendPredictNew = (java.util.ArrayList<BEC.STTrendPredict>)istream.readList(1, false, VAR_TYPE_4_VTTRENDPREDICTNEW);
    }

}

