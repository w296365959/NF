package BEC;

public final class SecPortraitMarket extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public SecPortraitIndustry stStockUpdowns = null;

    public SecPortraitIndustry stIndexUpdowns = null;

    public SecPortraitIndustry stPlateUpdowns = null;

    public String sDesc = "";

    public SecPortraitIndustry getStStockUpdowns()
    {
        return stStockUpdowns;
    }

    public void  setStStockUpdowns(SecPortraitIndustry stStockUpdowns)
    {
        this.stStockUpdowns = stStockUpdowns;
    }

    public SecPortraitIndustry getStIndexUpdowns()
    {
        return stIndexUpdowns;
    }

    public void  setStIndexUpdowns(SecPortraitIndustry stIndexUpdowns)
    {
        this.stIndexUpdowns = stIndexUpdowns;
    }

    public SecPortraitIndustry getStPlateUpdowns()
    {
        return stPlateUpdowns;
    }

    public void  setStPlateUpdowns(SecPortraitIndustry stPlateUpdowns)
    {
        this.stPlateUpdowns = stPlateUpdowns;
    }

    public String getSDesc()
    {
        return sDesc;
    }

    public void  setSDesc(String sDesc)
    {
        this.sDesc = sDesc;
    }

    public SecPortraitMarket()
    {
    }

    public SecPortraitMarket(SecPortraitIndustry stStockUpdowns, SecPortraitIndustry stIndexUpdowns, SecPortraitIndustry stPlateUpdowns, String sDesc)
    {
        this.stStockUpdowns = stStockUpdowns;
        this.stIndexUpdowns = stIndexUpdowns;
        this.stPlateUpdowns = stPlateUpdowns;
        this.sDesc = sDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stStockUpdowns) {
            ostream.writeMessage(0, stStockUpdowns);
        }
        if (null != stIndexUpdowns) {
            ostream.writeMessage(1, stIndexUpdowns);
        }
        if (null != stPlateUpdowns) {
            ostream.writeMessage(2, stPlateUpdowns);
        }
        if (null != sDesc) {
            ostream.writeString(3, sDesc);
        }
    }

    static SecPortraitIndustry VAR_TYPE_4_STSTOCKUPDOWNS = new SecPortraitIndustry();

    static SecPortraitIndustry VAR_TYPE_4_STINDEXUPDOWNS = new SecPortraitIndustry();

    static SecPortraitIndustry VAR_TYPE_4_STPLATEUPDOWNS = new SecPortraitIndustry();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stStockUpdowns = (SecPortraitIndustry)istream.readMessage(0, false, VAR_TYPE_4_STSTOCKUPDOWNS);
        this.stIndexUpdowns = (SecPortraitIndustry)istream.readMessage(1, false, VAR_TYPE_4_STINDEXUPDOWNS);
        this.stPlateUpdowns = (SecPortraitIndustry)istream.readMessage(2, false, VAR_TYPE_4_STPLATEUPDOWNS);
        this.sDesc = (String)istream.readString(3, false, this.sDesc);
    }

}

