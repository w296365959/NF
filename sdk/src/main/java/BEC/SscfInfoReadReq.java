package BEC;

public final class SscfInfoReadReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sReadCount = "";

    public int iSscfInfoID = 0;

    public int eInfoType = E_INFO_TYPE.E_IT_TEACH;

    public String getSReadCount()
    {
        return sReadCount;
    }

    public void  setSReadCount(String sReadCount)
    {
        this.sReadCount = sReadCount;
    }

    public int getISscfInfoID()
    {
        return iSscfInfoID;
    }

    public void  setISscfInfoID(int iSscfInfoID)
    {
        this.iSscfInfoID = iSscfInfoID;
    }

    public int getEInfoType()
    {
        return eInfoType;
    }

    public void  setEInfoType(int eInfoType)
    {
        this.eInfoType = eInfoType;
    }

    public SscfInfoReadReq()
    {
    }

    public SscfInfoReadReq(String sReadCount, int iSscfInfoID, int eInfoType)
    {
        this.sReadCount = sReadCount;
        this.iSscfInfoID = iSscfInfoID;
        this.eInfoType = eInfoType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sReadCount) {
            ostream.writeString(0, sReadCount);
        }
        ostream.writeInt32(1, iSscfInfoID);
        ostream.writeInt32(2, eInfoType);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sReadCount = (String)istream.readString(0, false, this.sReadCount);
        this.iSscfInfoID = (int)istream.readInt32(1, false, this.iSscfInfoID);
        this.eInfoType = (int)istream.readInt32(2, false, this.eInfoType);
    }

}

