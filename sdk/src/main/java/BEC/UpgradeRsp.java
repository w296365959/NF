package BEC;

public final class UpgradeRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UpgradeInfo stUpgradeInfo = null;

    public UpgradeInfo getStUpgradeInfo()
    {
        return stUpgradeInfo;
    }

    public void  setStUpgradeInfo(UpgradeInfo stUpgradeInfo)
    {
        this.stUpgradeInfo = stUpgradeInfo;
    }

    public UpgradeRsp()
    {
    }

    public UpgradeRsp(UpgradeInfo stUpgradeInfo)
    {
        this.stUpgradeInfo = stUpgradeInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUpgradeInfo) {
            ostream.writeMessage(0, stUpgradeInfo);
        }
    }

    static UpgradeInfo VAR_TYPE_4_STUPGRADEINFO = new UpgradeInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUpgradeInfo = (UpgradeInfo)istream.readMessage(0, false, VAR_TYPE_4_STUPGRADEINFO);
    }

}

