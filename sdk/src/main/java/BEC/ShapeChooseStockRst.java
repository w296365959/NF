package BEC;

public final class ShapeChooseStockRst extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public int iSimilarVal = 0;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public int getISimilarVal()
    {
        return iSimilarVal;
    }

    public void  setISimilarVal(int iSimilarVal)
    {
        this.iSimilarVal = iSimilarVal;
    }

    public ShapeChooseStockRst()
    {
    }

    public ShapeChooseStockRst(String sDtSecCode, int iSimilarVal)
    {
        this.sDtSecCode = sDtSecCode;
        this.iSimilarVal = iSimilarVal;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        ostream.writeInt32(1, iSimilarVal);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.iSimilarVal = (int)istream.readInt32(1, false, this.iSimilarVal);
    }

}

