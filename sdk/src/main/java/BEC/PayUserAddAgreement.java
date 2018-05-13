package BEC;

public final class PayUserAddAgreement extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iType = 0;

    public int iIndex = 0;

    public BEC.PayUserAgreementDesc stDynamicAgreement = null;

    public int getIType()
    {
        return iType;
    }

    public void  setIType(int iType)
    {
        this.iType = iType;
    }

    public int getIIndex()
    {
        return iIndex;
    }

    public void  setIIndex(int iIndex)
    {
        this.iIndex = iIndex;
    }

    public BEC.PayUserAgreementDesc getStDynamicAgreement()
    {
        return stDynamicAgreement;
    }

    public void  setStDynamicAgreement(BEC.PayUserAgreementDesc stDynamicAgreement)
    {
        this.stDynamicAgreement = stDynamicAgreement;
    }

    public PayUserAddAgreement()
    {
    }

    public PayUserAddAgreement(int iType, int iIndex, BEC.PayUserAgreementDesc stDynamicAgreement)
    {
        this.iType = iType;
        this.iIndex = iIndex;
        this.stDynamicAgreement = stDynamicAgreement;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iType);
        ostream.writeInt32(1, iIndex);
        if (null != stDynamicAgreement) {
            ostream.writeMessage(2, stDynamicAgreement);
        }
    }

    static BEC.PayUserAgreementDesc VAR_TYPE_4_STDYNAMICAGREEMENT = new BEC.PayUserAgreementDesc();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iType = (int)istream.readInt32(0, false, this.iType);
        this.iIndex = (int)istream.readInt32(1, false, this.iIndex);
        this.stDynamicAgreement = (BEC.PayUserAgreementDesc)istream.readMessage(2, false, VAR_TYPE_4_STDYNAMICAGREEMENT);
    }

}

