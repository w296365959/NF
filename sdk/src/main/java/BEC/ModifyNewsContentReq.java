package BEC;

public final class ModifyNewsContentReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public BEC.NewsId stNewsId = null;

    public boolean bDelete = false;

    public java.util.ArrayList<String> vtDtSecCode = null;

    public BEC.NewsId getStNewsId()
    {
        return stNewsId;
    }

    public void  setStNewsId(BEC.NewsId stNewsId)
    {
        this.stNewsId = stNewsId;
    }

    public boolean getBDelete()
    {
        return bDelete;
    }

    public void  setBDelete(boolean bDelete)
    {
        this.bDelete = bDelete;
    }

    public java.util.ArrayList<String> getVtDtSecCode()
    {
        return vtDtSecCode;
    }

    public void  setVtDtSecCode(java.util.ArrayList<String> vtDtSecCode)
    {
        this.vtDtSecCode = vtDtSecCode;
    }

    public ModifyNewsContentReq()
    {
    }

    public ModifyNewsContentReq(BEC.NewsId stNewsId, boolean bDelete, java.util.ArrayList<String> vtDtSecCode)
    {
        this.stNewsId = stNewsId;
        this.bDelete = bDelete;
        this.vtDtSecCode = vtDtSecCode;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stNewsId) {
            ostream.writeMessage(0, stNewsId);
        }
        ostream.writeBoolean(1, bDelete);
        if (null != vtDtSecCode) {
            ostream.writeList(2, vtDtSecCode);
        }
    }

    static BEC.NewsId VAR_TYPE_4_STNEWSID = new BEC.NewsId();

    static java.util.ArrayList<String> VAR_TYPE_4_VTDTSECCODE = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VTDTSECCODE.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stNewsId = (BEC.NewsId)istream.readMessage(0, false, VAR_TYPE_4_STNEWSID);
        this.bDelete = (boolean)istream.readBoolean(1, false, this.bDelete);
        this.vtDtSecCode = (java.util.ArrayList<String>)istream.readList(2, false, VAR_TYPE_4_VTDTSECCODE);
    }

}

