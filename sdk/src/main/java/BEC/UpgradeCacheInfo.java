package BEC;

public final class UpgradeCacheInfo extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iLastUpgradeTime = 0;

    public int iCount = 0;

    public int getILastUpgradeTime()
    {
        return iLastUpgradeTime;
    }

    public void  setILastUpgradeTime(int iLastUpgradeTime)
    {
        this.iLastUpgradeTime = iLastUpgradeTime;
    }

    public int getICount()
    {
        return iCount;
    }

    public void  setICount(int iCount)
    {
        this.iCount = iCount;
    }

    public UpgradeCacheInfo()
    {
    }

    public UpgradeCacheInfo(int iLastUpgradeTime, int iCount)
    {
        this.iLastUpgradeTime = iLastUpgradeTime;
        this.iCount = iCount;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iLastUpgradeTime);
        ostream.writeInt32(1, iCount);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iLastUpgradeTime = (int)istream.readInt32(0, false, this.iLastUpgradeTime);
        this.iCount = (int)istream.readInt32(1, false, this.iCount);
    }

}

