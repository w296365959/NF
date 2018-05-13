package BEC;

public final class DongmiQaListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.DongmiQaDetail> vDongmiQaDetail = null;

    public String sNextId = "";

    public java.util.ArrayList<BEC.DongmiQaDetail> getVDongmiQaDetail()
    {
        return vDongmiQaDetail;
    }

    public void  setVDongmiQaDetail(java.util.ArrayList<BEC.DongmiQaDetail> vDongmiQaDetail)
    {
        this.vDongmiQaDetail = vDongmiQaDetail;
    }

    public String getSNextId()
    {
        return sNextId;
    }

    public void  setSNextId(String sNextId)
    {
        this.sNextId = sNextId;
    }

    public DongmiQaListRsp()
    {
    }

    public DongmiQaListRsp(java.util.ArrayList<BEC.DongmiQaDetail> vDongmiQaDetail, String sNextId)
    {
        this.vDongmiQaDetail = vDongmiQaDetail;
        this.sNextId = sNextId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vDongmiQaDetail) {
            ostream.writeList(0, vDongmiQaDetail);
        }
        if (null != sNextId) {
            ostream.writeString(1, sNextId);
        }
    }

    static java.util.ArrayList<BEC.DongmiQaDetail> VAR_TYPE_4_VDONGMIQADETAIL = new java.util.ArrayList<BEC.DongmiQaDetail>();
    static {
        VAR_TYPE_4_VDONGMIQADETAIL.add(new BEC.DongmiQaDetail());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vDongmiQaDetail = (java.util.ArrayList<BEC.DongmiQaDetail>)istream.readList(0, false, VAR_TYPE_4_VDONGMIQADETAIL);
        this.sNextId = (String)istream.readString(1, false, this.sNextId);
    }

}

