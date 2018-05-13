package BEC;

public final class ShapeChooseStockRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.ShapeChooseStockRst> vtShapeRst = null;

    public java.util.ArrayList<BEC.ShapeChooseStockRst> getVtShapeRst()
    {
        return vtShapeRst;
    }

    public void  setVtShapeRst(java.util.ArrayList<BEC.ShapeChooseStockRst> vtShapeRst)
    {
        this.vtShapeRst = vtShapeRst;
    }

    public ShapeChooseStockRsp()
    {
    }

    public ShapeChooseStockRsp(java.util.ArrayList<BEC.ShapeChooseStockRst> vtShapeRst)
    {
        this.vtShapeRst = vtShapeRst;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtShapeRst) {
            ostream.writeList(0, vtShapeRst);
        }
    }

    static java.util.ArrayList<BEC.ShapeChooseStockRst> VAR_TYPE_4_VTSHAPERST = new java.util.ArrayList<BEC.ShapeChooseStockRst>();
    static {
        VAR_TYPE_4_VTSHAPERST.add(new BEC.ShapeChooseStockRst());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtShapeRst = (java.util.ArrayList<BEC.ShapeChooseStockRst>)istream.readList(0, false, VAR_TYPE_4_VTSHAPERST);
    }

}

