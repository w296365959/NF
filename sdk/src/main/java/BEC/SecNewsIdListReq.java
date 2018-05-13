package BEC;

public final class SecNewsIdListReq extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public int eNewsType = 0;

    public String sCurNewestId = "";

    public boolean bForceUpdate = false;

    public boolean bGetIncrementRsp = false;

    public boolean bGetFromDb = true;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public int getENewsType()
    {
        return eNewsType;
    }

    public void  setENewsType(int eNewsType)
    {
        this.eNewsType = eNewsType;
    }

    public String getSCurNewestId()
    {
        return sCurNewestId;
    }

    public void  setSCurNewestId(String sCurNewestId)
    {
        this.sCurNewestId = sCurNewestId;
    }

    public boolean getBForceUpdate()
    {
        return bForceUpdate;
    }

    public void  setBForceUpdate(boolean bForceUpdate)
    {
        this.bForceUpdate = bForceUpdate;
    }

    public boolean getBGetIncrementRsp()
    {
        return bGetIncrementRsp;
    }

    public void  setBGetIncrementRsp(boolean bGetIncrementRsp)
    {
        this.bGetIncrementRsp = bGetIncrementRsp;
    }

    public boolean getBGetFromDb()
    {
        return bGetFromDb;
    }

    public void  setBGetFromDb(boolean bGetFromDb)
    {
        this.bGetFromDb = bGetFromDb;
    }

    public SecNewsIdListReq()
    {
    }

    public SecNewsIdListReq(String sDtSecCode, int eNewsType, String sCurNewestId, boolean bForceUpdate, boolean bGetIncrementRsp, boolean bGetFromDb)
    {
        this.sDtSecCode = sDtSecCode;
        this.eNewsType = eNewsType;
        this.sCurNewestId = sCurNewestId;
        this.bForceUpdate = bForceUpdate;
        this.bGetIncrementRsp = bGetIncrementRsp;
        this.bGetFromDb = bGetFromDb;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        ostream.writeInt32(1, eNewsType);
        if (null != sCurNewestId) {
            ostream.writeString(2, sCurNewestId);
        }
        ostream.writeBoolean(3, bForceUpdate);
        ostream.writeBoolean(4, bGetIncrementRsp);
        ostream.writeBoolean(5, bGetFromDb);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.eNewsType = (int)istream.readInt32(1, false, this.eNewsType);
        this.sCurNewestId = (String)istream.readString(2, false, this.sCurNewestId);
        this.bForceUpdate = (boolean)istream.readBoolean(3, false, this.bForceUpdate);
        this.bGetIncrementRsp = (boolean)istream.readBoolean(4, false, this.bGetIncrementRsp);
        this.bGetFromDb = (boolean)istream.readBoolean(5, false, this.bGetFromDb);
    }

}

