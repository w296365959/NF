package BEC;

public final class DtMemberInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iMemberType = E_DT_MEMBER_TYPE.E_DT_NO_MEMBER;

    public long lMemberStartTime = 0;

    public long lMemberEndTime = 0;

    public int getIMemberType()
    {
        return iMemberType;
    }

    public void  setIMemberType(int iMemberType)
    {
        this.iMemberType = iMemberType;
    }

    public long getLMemberStartTime()
    {
        return lMemberStartTime;
    }

    public void  setLMemberStartTime(long lMemberStartTime)
    {
        this.lMemberStartTime = lMemberStartTime;
    }

    public long getLMemberEndTime()
    {
        return lMemberEndTime;
    }

    public void  setLMemberEndTime(long lMemberEndTime)
    {
        this.lMemberEndTime = lMemberEndTime;
    }

    public DtMemberInfo()
    {
    }

    public DtMemberInfo(int iMemberType, long lMemberStartTime, long lMemberEndTime)
    {
        this.iMemberType = iMemberType;
        this.lMemberStartTime = lMemberStartTime;
        this.lMemberEndTime = lMemberEndTime;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iMemberType);
        ostream.writeInt64(1, lMemberStartTime);
        ostream.writeInt64(2, lMemberEndTime);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iMemberType = (int)istream.readInt32(0, false, this.iMemberType);
        this.lMemberStartTime = (long)istream.readInt64(1, false, this.lMemberStartTime);
        this.lMemberEndTime = (long)istream.readInt64(2, false, this.lMemberEndTime);
    }

}

