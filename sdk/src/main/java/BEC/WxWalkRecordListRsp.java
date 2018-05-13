package BEC;

public final class WxWalkRecordListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<WxWalkRecord> vtWxWalkRecord = null;

    public int iTotalCount = 0;

    public java.util.ArrayList<WxWalkRecord> getVtWxWalkRecord()
    {
        return vtWxWalkRecord;
    }

    public void  setVtWxWalkRecord(java.util.ArrayList<WxWalkRecord> vtWxWalkRecord)
    {
        this.vtWxWalkRecord = vtWxWalkRecord;
    }

    public int getITotalCount()
    {
        return iTotalCount;
    }

    public void  setITotalCount(int iTotalCount)
    {
        this.iTotalCount = iTotalCount;
    }

    public WxWalkRecordListRsp()
    {
    }

    public WxWalkRecordListRsp(java.util.ArrayList<WxWalkRecord> vtWxWalkRecord, int iTotalCount)
    {
        this.vtWxWalkRecord = vtWxWalkRecord;
        this.iTotalCount = iTotalCount;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtWxWalkRecord) {
            ostream.writeList(0, vtWxWalkRecord);
        }
        ostream.writeInt32(1, iTotalCount);
    }

    static java.util.ArrayList<WxWalkRecord> VAR_TYPE_4_VTWXWALKRECORD = new java.util.ArrayList<WxWalkRecord>();
    static {
        VAR_TYPE_4_VTWXWALKRECORD.add(new WxWalkRecord());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtWxWalkRecord = (java.util.ArrayList<WxWalkRecord>)istream.readList(0, false, VAR_TYPE_4_VTWXWALKRECORD);
        this.iTotalCount = (int)istream.readInt32(1, false, this.iTotalCount);
    }

}

