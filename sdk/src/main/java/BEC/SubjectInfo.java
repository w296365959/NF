package BEC;

public final class SubjectInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public boolean isDel = false;

    public int iCreateTime = -1;

    public int iUpdateTime = -1;

    public String sName = "";

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public boolean getIsDel()
    {
        return isDel;
    }

    public void  setIsDel(boolean isDel)
    {
        this.isDel = isDel;
    }

    public int getICreateTime()
    {
        return iCreateTime;
    }

    public void  setICreateTime(int iCreateTime)
    {
        this.iCreateTime = iCreateTime;
    }

    public int getIUpdateTime()
    {
        return iUpdateTime;
    }

    public void  setIUpdateTime(int iUpdateTime)
    {
        this.iUpdateTime = iUpdateTime;
    }

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public SubjectInfo()
    {
    }

    public SubjectInfo(String sDtSecCode, boolean isDel, int iCreateTime, int iUpdateTime, String sName)
    {
        this.sDtSecCode = sDtSecCode;
        this.isDel = isDel;
        this.iCreateTime = iCreateTime;
        this.iUpdateTime = iUpdateTime;
        this.sName = sName;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        ostream.writeBoolean(1, isDel);
        ostream.writeInt32(2, iCreateTime);
        ostream.writeInt32(3, iUpdateTime);
        if (null != sName) {
            ostream.writeString(4, sName);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.isDel = (boolean)istream.readBoolean(1, false, this.isDel);
        this.iCreateTime = (int)istream.readInt32(2, false, this.iCreateTime);
        this.iUpdateTime = (int)istream.readInt32(3, false, this.iUpdateTime);
        this.sName = (String)istream.readString(4, false, this.sName);
    }

}

