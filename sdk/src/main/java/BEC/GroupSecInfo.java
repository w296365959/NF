package BEC;

public final class GroupSecInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public int iUpdateTime = -1;

    public boolean isDel = false;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public int getIUpdateTime()
    {
        return iUpdateTime;
    }

    public void  setIUpdateTime(int iUpdateTime)
    {
        this.iUpdateTime = iUpdateTime;
    }

    public boolean getIsDel()
    {
        return isDel;
    }

    public void  setIsDel(boolean isDel)
    {
        this.isDel = isDel;
    }

    public GroupSecInfo()
    {
    }

    public GroupSecInfo(String sDtSecCode, int iUpdateTime, boolean isDel)
    {
        this.sDtSecCode = sDtSecCode;
        this.iUpdateTime = iUpdateTime;
        this.isDel = isDel;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        ostream.writeInt32(1, iUpdateTime);
        ostream.writeBoolean(2, isDel);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.iUpdateTime = (int)istream.readInt32(1, false, this.iUpdateTime);
        this.isDel = (boolean)istream.readBoolean(2, false, this.isDel);
    }

}

