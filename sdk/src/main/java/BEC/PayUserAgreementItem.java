package BEC;

public final class PayUserAgreementItem extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public boolean bCheckBox = false;

    public boolean bDefaultCheck = true;

    public java.util.ArrayList<BEC.PayUserAgreementDesc> vDesc = null;

    public boolean getBCheckBox()
    {
        return bCheckBox;
    }

    public void  setBCheckBox(boolean bCheckBox)
    {
        this.bCheckBox = bCheckBox;
    }

    public boolean getBDefaultCheck()
    {
        return bDefaultCheck;
    }

    public void  setBDefaultCheck(boolean bDefaultCheck)
    {
        this.bDefaultCheck = bDefaultCheck;
    }

    public java.util.ArrayList<BEC.PayUserAgreementDesc> getVDesc()
    {
        return vDesc;
    }

    public void  setVDesc(java.util.ArrayList<BEC.PayUserAgreementDesc> vDesc)
    {
        this.vDesc = vDesc;
    }

    public PayUserAgreementItem()
    {
    }

    public PayUserAgreementItem(boolean bCheckBox, boolean bDefaultCheck, java.util.ArrayList<BEC.PayUserAgreementDesc> vDesc)
    {
        this.bCheckBox = bCheckBox;
        this.bDefaultCheck = bDefaultCheck;
        this.vDesc = vDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeBoolean(0, bCheckBox);
        ostream.writeBoolean(1, bDefaultCheck);
        if (null != vDesc) {
            ostream.writeList(2, vDesc);
        }
    }

    static java.util.ArrayList<BEC.PayUserAgreementDesc> VAR_TYPE_4_VDESC = new java.util.ArrayList<BEC.PayUserAgreementDesc>();
    static {
        VAR_TYPE_4_VDESC.add(new BEC.PayUserAgreementDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.bCheckBox = (boolean)istream.readBoolean(0, false, this.bCheckBox);
        this.bDefaultCheck = (boolean)istream.readBoolean(1, false, this.bDefaultCheck);
        this.vDesc = (java.util.ArrayList<BEC.PayUserAgreementDesc>)istream.readList(2, false, VAR_TYPE_4_VDESC);
    }

}

