package BEC;

public final class UserRiskEvalResult extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iSubjectRiskLevel = E_DT_SUBJECT_RISK_LEVEL.E_DT_SUBJECT_RISK_NO;

    public String sSubjectRiskDesc = "";

    public int iUserRiskType = AccuPointUserRiskType.E_USER_RISK_NO_EVAL;

    public String sUserRiskType = "";

    public String sEvalResult = "";

    public BEC.PayUserAddAgreement stAddAgreement = null;

    public java.util.ArrayList<BEC.RiskMatchResult> vMatchResult = null;

    public int getISubjectRiskLevel()
    {
        return iSubjectRiskLevel;
    }

    public void  setISubjectRiskLevel(int iSubjectRiskLevel)
    {
        this.iSubjectRiskLevel = iSubjectRiskLevel;
    }

    public String getSSubjectRiskDesc()
    {
        return sSubjectRiskDesc;
    }

    public void  setSSubjectRiskDesc(String sSubjectRiskDesc)
    {
        this.sSubjectRiskDesc = sSubjectRiskDesc;
    }

    public int getIUserRiskType()
    {
        return iUserRiskType;
    }

    public void  setIUserRiskType(int iUserRiskType)
    {
        this.iUserRiskType = iUserRiskType;
    }

    public String getSUserRiskType()
    {
        return sUserRiskType;
    }

    public void  setSUserRiskType(String sUserRiskType)
    {
        this.sUserRiskType = sUserRiskType;
    }

    public String getSEvalResult()
    {
        return sEvalResult;
    }

    public void  setSEvalResult(String sEvalResult)
    {
        this.sEvalResult = sEvalResult;
    }

    public BEC.PayUserAddAgreement getStAddAgreement()
    {
        return stAddAgreement;
    }

    public void  setStAddAgreement(BEC.PayUserAddAgreement stAddAgreement)
    {
        this.stAddAgreement = stAddAgreement;
    }

    public java.util.ArrayList<BEC.RiskMatchResult> getVMatchResult()
    {
        return vMatchResult;
    }

    public void  setVMatchResult(java.util.ArrayList<BEC.RiskMatchResult> vMatchResult)
    {
        this.vMatchResult = vMatchResult;
    }

    public UserRiskEvalResult()
    {
    }

    public UserRiskEvalResult(int iSubjectRiskLevel, String sSubjectRiskDesc, int iUserRiskType, String sUserRiskType, String sEvalResult, BEC.PayUserAddAgreement stAddAgreement, java.util.ArrayList<BEC.RiskMatchResult> vMatchResult)
    {
        this.iSubjectRiskLevel = iSubjectRiskLevel;
        this.sSubjectRiskDesc = sSubjectRiskDesc;
        this.iUserRiskType = iUserRiskType;
        this.sUserRiskType = sUserRiskType;
        this.sEvalResult = sEvalResult;
        this.stAddAgreement = stAddAgreement;
        this.vMatchResult = vMatchResult;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iSubjectRiskLevel);
        if (null != sSubjectRiskDesc) {
            ostream.writeString(1, sSubjectRiskDesc);
        }
        ostream.writeInt32(2, iUserRiskType);
        if (null != sUserRiskType) {
            ostream.writeString(3, sUserRiskType);
        }
        if (null != sEvalResult) {
            ostream.writeString(4, sEvalResult);
        }
        if (null != stAddAgreement) {
            ostream.writeMessage(5, stAddAgreement);
        }
        if (null != vMatchResult) {
            ostream.writeList(6, vMatchResult);
        }
    }

    static BEC.PayUserAddAgreement VAR_TYPE_4_STADDAGREEMENT = new BEC.PayUserAddAgreement();

    static java.util.ArrayList<BEC.RiskMatchResult> VAR_TYPE_4_VMATCHRESULT = new java.util.ArrayList<BEC.RiskMatchResult>();
    static {
        VAR_TYPE_4_VMATCHRESULT.add(new BEC.RiskMatchResult());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iSubjectRiskLevel = (int)istream.readInt32(0, false, this.iSubjectRiskLevel);
        this.sSubjectRiskDesc = (String)istream.readString(1, false, this.sSubjectRiskDesc);
        this.iUserRiskType = (int)istream.readInt32(2, false, this.iUserRiskType);
        this.sUserRiskType = (String)istream.readString(3, false, this.sUserRiskType);
        this.sEvalResult = (String)istream.readString(4, false, this.sEvalResult);
        this.stAddAgreement = (BEC.PayUserAddAgreement)istream.readMessage(5, false, VAR_TYPE_4_STADDAGREEMENT);
        this.vMatchResult = (java.util.ArrayList<BEC.RiskMatchResult>)istream.readList(6, false, VAR_TYPE_4_VMATCHRESULT);
    }

}

