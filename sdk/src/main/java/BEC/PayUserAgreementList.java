package BEC;

public final class PayUserAgreementList extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<Integer, BEC.PayUserAgreement> mSubjectUserAgreement = null;

    public java.util.Map<Integer, BEC.PayUserAgreement> getMSubjectUserAgreement()
    {
        return mSubjectUserAgreement;
    }

    public void  setMSubjectUserAgreement(java.util.Map<Integer, BEC.PayUserAgreement> mSubjectUserAgreement)
    {
        this.mSubjectUserAgreement = mSubjectUserAgreement;
    }

    public PayUserAgreementList()
    {
    }

    public PayUserAgreementList(java.util.Map<Integer, BEC.PayUserAgreement> mSubjectUserAgreement)
    {
        this.mSubjectUserAgreement = mSubjectUserAgreement;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != mSubjectUserAgreement) {
            ostream.writeMap(0, mSubjectUserAgreement);
        }
    }

    static java.util.Map<Integer, BEC.PayUserAgreement> VAR_TYPE_4_MSUBJECTUSERAGREEMENT = new java.util.HashMap<Integer, BEC.PayUserAgreement>();
    static {
        VAR_TYPE_4_MSUBJECTUSERAGREEMENT.put(0, new BEC.PayUserAgreement());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.mSubjectUserAgreement = (java.util.Map<Integer, BEC.PayUserAgreement>)istream.readMap(0, false, VAR_TYPE_4_MSUBJECTUSERAGREEMENT);
    }

}

