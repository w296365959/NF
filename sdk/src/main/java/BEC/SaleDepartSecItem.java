package BEC;

public final class SaleDepartSecItem extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public LHBReasonDetail stDetail = null;

    public SecCoordsInfo stCoords = null;

    public int iSaleDepartNum = 0;

    public LHBReasonDetail getStDetail()
    {
        return stDetail;
    }

    public void  setStDetail(LHBReasonDetail stDetail)
    {
        this.stDetail = stDetail;
    }

    public SecCoordsInfo getStCoords()
    {
        return stCoords;
    }

    public void  setStCoords(SecCoordsInfo stCoords)
    {
        this.stCoords = stCoords;
    }

    public int getISaleDepartNum()
    {
        return iSaleDepartNum;
    }

    public void  setISaleDepartNum(int iSaleDepartNum)
    {
        this.iSaleDepartNum = iSaleDepartNum;
    }

    public SaleDepartSecItem()
    {
    }

    public SaleDepartSecItem(LHBReasonDetail stDetail, SecCoordsInfo stCoords, int iSaleDepartNum)
    {
        this.stDetail = stDetail;
        this.stCoords = stCoords;
        this.iSaleDepartNum = iSaleDepartNum;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stDetail) {
            ostream.writeMessage(0, stDetail);
        }
        if (null != stCoords) {
            ostream.writeMessage(1, stCoords);
        }
        ostream.writeInt32(2, iSaleDepartNum);
    }

    static LHBReasonDetail VAR_TYPE_4_STDETAIL = new LHBReasonDetail();

    static SecCoordsInfo VAR_TYPE_4_STCOORDS = new SecCoordsInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stDetail = (LHBReasonDetail)istream.readMessage(0, false, VAR_TYPE_4_STDETAIL);
        this.stCoords = (SecCoordsInfo)istream.readMessage(1, false, VAR_TYPE_4_STCOORDS);
        this.iSaleDepartNum = (int)istream.readInt32(2, false, this.iSaleDepartNum);
    }

}

