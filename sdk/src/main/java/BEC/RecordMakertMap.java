package BEC;

public final class RecordMakertMap extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<String, RecordMakertDesc> mRecordMakertDesc = null;

    public java.util.Map<String, RecordMakertDesc> getMRecordMakertDesc()
    {
        return mRecordMakertDesc;
    }

    public void  setMRecordMakertDesc(java.util.Map<String, RecordMakertDesc> mRecordMakertDesc)
    {
        this.mRecordMakertDesc = mRecordMakertDesc;
    }

    public RecordMakertMap()
    {
    }

    public RecordMakertMap(java.util.Map<String, RecordMakertDesc> mRecordMakertDesc)
    {
        this.mRecordMakertDesc = mRecordMakertDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != mRecordMakertDesc) {
            ostream.writeMap(0, mRecordMakertDesc);
        }
    }

    static java.util.Map<String, RecordMakertDesc> VAR_TYPE_4_MRECORDMAKERTDESC = new java.util.HashMap<String, RecordMakertDesc>();
    static {
        VAR_TYPE_4_MRECORDMAKERTDESC.put("", new RecordMakertDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.mRecordMakertDesc = (java.util.Map<String, RecordMakertDesc>)istream.readMap(0, false, VAR_TYPE_4_MRECORDMAKERTDESC);
    }

}

