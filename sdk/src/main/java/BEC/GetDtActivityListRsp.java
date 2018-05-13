package BEC;

public final class GetDtActivityListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<DtActivityDetail> vList = null;

    public java.util.ArrayList<DtActivityDetail> getVList()
    {
        return vList;
    }

    public void  setVList(java.util.ArrayList<DtActivityDetail> vList)
    {
        this.vList = vList;
    }

    public GetDtActivityListRsp()
    {
    }

    public GetDtActivityListRsp(java.util.ArrayList<DtActivityDetail> vList)
    {
        this.vList = vList;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vList) {
            ostream.writeList(0, vList);
        }
    }

    static java.util.ArrayList<DtActivityDetail> VAR_TYPE_4_VLIST = new java.util.ArrayList<DtActivityDetail>();
    static {
        VAR_TYPE_4_VLIST.add(new DtActivityDetail());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vList = (java.util.ArrayList<DtActivityDetail>)istream.readList(0, false, VAR_TYPE_4_VLIST);
    }

}

