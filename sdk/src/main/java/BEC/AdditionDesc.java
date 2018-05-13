package BEC;

public final class AdditionDesc extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sId = "";

    public String sDtSecCode = "";

    public String sSecCode = "";

    public String sSecName = "";

    public String sProcess = "";

    public String sProcessDate = "";

    public float fFund = 0;

    public float fCount = 0;

    public float fPrice = 0;

    public String sObject = "";

    public String sProject = "";

    public float fLatestPrice = 0;

    public float fDiffRate = 0;

    public String sIndustry = "";

    public java.util.ArrayList<BEC.AdditionProgress> vProgress = null;

    public java.util.ArrayList<BEC.NewsDesc> vRelAnnc = null;

    public float fHairCuts = 0;

    public float fFundRate = 0;

    public float fPERatio = 0;

    public float fScore = 0;

    public float fIncrease = 0;

    public float fDeclPrice = 0;

    public String getSId()
    {
        return sId;
    }

    public void  setSId(String sId)
    {
        this.sId = sId;
    }

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public String getSSecCode()
    {
        return sSecCode;
    }

    public void  setSSecCode(String sSecCode)
    {
        this.sSecCode = sSecCode;
    }

    public String getSSecName()
    {
        return sSecName;
    }

    public void  setSSecName(String sSecName)
    {
        this.sSecName = sSecName;
    }

    public String getSProcess()
    {
        return sProcess;
    }

    public void  setSProcess(String sProcess)
    {
        this.sProcess = sProcess;
    }

    public String getSProcessDate()
    {
        return sProcessDate;
    }

    public void  setSProcessDate(String sProcessDate)
    {
        this.sProcessDate = sProcessDate;
    }

    public float getFFund()
    {
        return fFund;
    }

    public void  setFFund(float fFund)
    {
        this.fFund = fFund;
    }

    public float getFCount()
    {
        return fCount;
    }

    public void  setFCount(float fCount)
    {
        this.fCount = fCount;
    }

    public float getFPrice()
    {
        return fPrice;
    }

    public void  setFPrice(float fPrice)
    {
        this.fPrice = fPrice;
    }

    public String getSObject()
    {
        return sObject;
    }

    public void  setSObject(String sObject)
    {
        this.sObject = sObject;
    }

    public String getSProject()
    {
        return sProject;
    }

    public void  setSProject(String sProject)
    {
        this.sProject = sProject;
    }

    public float getFLatestPrice()
    {
        return fLatestPrice;
    }

    public void  setFLatestPrice(float fLatestPrice)
    {
        this.fLatestPrice = fLatestPrice;
    }

    public float getFDiffRate()
    {
        return fDiffRate;
    }

    public void  setFDiffRate(float fDiffRate)
    {
        this.fDiffRate = fDiffRate;
    }

    public String getSIndustry()
    {
        return sIndustry;
    }

    public void  setSIndustry(String sIndustry)
    {
        this.sIndustry = sIndustry;
    }

    public java.util.ArrayList<BEC.AdditionProgress> getVProgress()
    {
        return vProgress;
    }

    public void  setVProgress(java.util.ArrayList<BEC.AdditionProgress> vProgress)
    {
        this.vProgress = vProgress;
    }

    public java.util.ArrayList<BEC.NewsDesc> getVRelAnnc()
    {
        return vRelAnnc;
    }

    public void  setVRelAnnc(java.util.ArrayList<BEC.NewsDesc> vRelAnnc)
    {
        this.vRelAnnc = vRelAnnc;
    }

    public float getFHairCuts()
    {
        return fHairCuts;
    }

    public void  setFHairCuts(float fHairCuts)
    {
        this.fHairCuts = fHairCuts;
    }

    public float getFFundRate()
    {
        return fFundRate;
    }

    public void  setFFundRate(float fFundRate)
    {
        this.fFundRate = fFundRate;
    }

    public float getFPERatio()
    {
        return fPERatio;
    }

    public void  setFPERatio(float fPERatio)
    {
        this.fPERatio = fPERatio;
    }

    public float getFScore()
    {
        return fScore;
    }

    public void  setFScore(float fScore)
    {
        this.fScore = fScore;
    }

    public float getFIncrease()
    {
        return fIncrease;
    }

    public void  setFIncrease(float fIncrease)
    {
        this.fIncrease = fIncrease;
    }

    public float getFDeclPrice()
    {
        return fDeclPrice;
    }

    public void  setFDeclPrice(float fDeclPrice)
    {
        this.fDeclPrice = fDeclPrice;
    }

    public AdditionDesc()
    {
    }

    public AdditionDesc(String sId, String sDtSecCode, String sSecCode, String sSecName, String sProcess, String sProcessDate, float fFund, float fCount, float fPrice, String sObject, String sProject, float fLatestPrice, float fDiffRate, String sIndustry, java.util.ArrayList<BEC.AdditionProgress> vProgress, java.util.ArrayList<BEC.NewsDesc> vRelAnnc, float fHairCuts, float fFundRate, float fPERatio, float fScore, float fIncrease, float fDeclPrice)
    {
        this.sId = sId;
        this.sDtSecCode = sDtSecCode;
        this.sSecCode = sSecCode;
        this.sSecName = sSecName;
        this.sProcess = sProcess;
        this.sProcessDate = sProcessDate;
        this.fFund = fFund;
        this.fCount = fCount;
        this.fPrice = fPrice;
        this.sObject = sObject;
        this.sProject = sProject;
        this.fLatestPrice = fLatestPrice;
        this.fDiffRate = fDiffRate;
        this.sIndustry = sIndustry;
        this.vProgress = vProgress;
        this.vRelAnnc = vRelAnnc;
        this.fHairCuts = fHairCuts;
        this.fFundRate = fFundRate;
        this.fPERatio = fPERatio;
        this.fScore = fScore;
        this.fIncrease = fIncrease;
        this.fDeclPrice = fDeclPrice;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sId) {
            ostream.writeString(0, sId);
        }
        if (null != sDtSecCode) {
            ostream.writeString(1, sDtSecCode);
        }
        if (null != sSecCode) {
            ostream.writeString(2, sSecCode);
        }
        if (null != sSecName) {
            ostream.writeString(3, sSecName);
        }
        if (null != sProcess) {
            ostream.writeString(4, sProcess);
        }
        if (null != sProcessDate) {
            ostream.writeString(5, sProcessDate);
        }
        ostream.writeFloat(6, fFund);
        ostream.writeFloat(7, fCount);
        ostream.writeFloat(8, fPrice);
        if (null != sObject) {
            ostream.writeString(9, sObject);
        }
        if (null != sProject) {
            ostream.writeString(10, sProject);
        }
        ostream.writeFloat(11, fLatestPrice);
        ostream.writeFloat(12, fDiffRate);
        if (null != sIndustry) {
            ostream.writeString(13, sIndustry);
        }
        if (null != vProgress) {
            ostream.writeList(14, vProgress);
        }
        if (null != vRelAnnc) {
            ostream.writeList(15, vRelAnnc);
        }
        ostream.writeFloat(16, fHairCuts);
        ostream.writeFloat(17, fFundRate);
        ostream.writeFloat(18, fPERatio);
        ostream.writeFloat(19, fScore);
        ostream.writeFloat(20, fIncrease);
        ostream.writeFloat(21, fDeclPrice);
    }

    static java.util.ArrayList<BEC.AdditionProgress> VAR_TYPE_4_VPROGRESS = new java.util.ArrayList<BEC.AdditionProgress>();
    static {
        VAR_TYPE_4_VPROGRESS.add(new BEC.AdditionProgress());
    }

    static java.util.ArrayList<BEC.NewsDesc> VAR_TYPE_4_VRELANNC = new java.util.ArrayList<BEC.NewsDesc>();
    static {
        VAR_TYPE_4_VRELANNC.add(new BEC.NewsDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sId = (String)istream.readString(0, false, this.sId);
        this.sDtSecCode = (String)istream.readString(1, false, this.sDtSecCode);
        this.sSecCode = (String)istream.readString(2, false, this.sSecCode);
        this.sSecName = (String)istream.readString(3, false, this.sSecName);
        this.sProcess = (String)istream.readString(4, false, this.sProcess);
        this.sProcessDate = (String)istream.readString(5, false, this.sProcessDate);
        this.fFund = (float)istream.readFloat(6, false, this.fFund);
        this.fCount = (float)istream.readFloat(7, false, this.fCount);
        this.fPrice = (float)istream.readFloat(8, false, this.fPrice);
        this.sObject = (String)istream.readString(9, false, this.sObject);
        this.sProject = (String)istream.readString(10, false, this.sProject);
        this.fLatestPrice = (float)istream.readFloat(11, false, this.fLatestPrice);
        this.fDiffRate = (float)istream.readFloat(12, false, this.fDiffRate);
        this.sIndustry = (String)istream.readString(13, false, this.sIndustry);
        this.vProgress = (java.util.ArrayList<BEC.AdditionProgress>)istream.readList(14, false, VAR_TYPE_4_VPROGRESS);
        this.vRelAnnc = (java.util.ArrayList<BEC.NewsDesc>)istream.readList(15, false, VAR_TYPE_4_VRELANNC);
        this.fHairCuts = (float)istream.readFloat(16, false, this.fHairCuts);
        this.fFundRate = (float)istream.readFloat(17, false, this.fFundRate);
        this.fPERatio = (float)istream.readFloat(18, false, this.fPERatio);
        this.fScore = (float)istream.readFloat(19, false, this.fScore);
        this.fIncrease = (float)istream.readFloat(20, false, this.fIncrease);
        this.fDeclPrice = (float)istream.readFloat(21, false, this.fDeclPrice);
    }

}

