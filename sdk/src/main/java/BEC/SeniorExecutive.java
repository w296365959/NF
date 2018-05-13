package BEC;

public final class SeniorExecutive extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sName = "";

    public int iAge = 0;

    public String sEdu = "";

    public String sBusiness = "";

    public String sTimeofOffice = "";

    public float fHoldNum = 0;

    public float fPay = 0;

    public String sIntroduce = "";

    public String sUniCode = "";

    public String getSName()
    {
        return sName;
    }

    public void  setSName(String sName)
    {
        this.sName = sName;
    }

    public int getIAge()
    {
        return iAge;
    }

    public void  setIAge(int iAge)
    {
        this.iAge = iAge;
    }

    public String getSEdu()
    {
        return sEdu;
    }

    public void  setSEdu(String sEdu)
    {
        this.sEdu = sEdu;
    }

    public String getSBusiness()
    {
        return sBusiness;
    }

    public void  setSBusiness(String sBusiness)
    {
        this.sBusiness = sBusiness;
    }

    public String getSTimeofOffice()
    {
        return sTimeofOffice;
    }

    public void  setSTimeofOffice(String sTimeofOffice)
    {
        this.sTimeofOffice = sTimeofOffice;
    }

    public float getFHoldNum()
    {
        return fHoldNum;
    }

    public void  setFHoldNum(float fHoldNum)
    {
        this.fHoldNum = fHoldNum;
    }

    public float getFPay()
    {
        return fPay;
    }

    public void  setFPay(float fPay)
    {
        this.fPay = fPay;
    }

    public String getSIntroduce()
    {
        return sIntroduce;
    }

    public void  setSIntroduce(String sIntroduce)
    {
        this.sIntroduce = sIntroduce;
    }

    public String getSUniCode()
    {
        return sUniCode;
    }

    public void  setSUniCode(String sUniCode)
    {
        this.sUniCode = sUniCode;
    }

    public SeniorExecutive()
    {
    }

    public SeniorExecutive(String sName, int iAge, String sEdu, String sBusiness, String sTimeofOffice, float fHoldNum, float fPay, String sIntroduce, String sUniCode)
    {
        this.sName = sName;
        this.iAge = iAge;
        this.sEdu = sEdu;
        this.sBusiness = sBusiness;
        this.sTimeofOffice = sTimeofOffice;
        this.fHoldNum = fHoldNum;
        this.fPay = fPay;
        this.sIntroduce = sIntroduce;
        this.sUniCode = sUniCode;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sName) {
            ostream.writeString(0, sName);
        }
        ostream.writeInt32(1, iAge);
        if (null != sEdu) {
            ostream.writeString(2, sEdu);
        }
        if (null != sBusiness) {
            ostream.writeString(3, sBusiness);
        }
        if (null != sTimeofOffice) {
            ostream.writeString(4, sTimeofOffice);
        }
        ostream.writeFloat(5, fHoldNum);
        ostream.writeFloat(6, fPay);
        if (null != sIntroduce) {
            ostream.writeString(7, sIntroduce);
        }
        if (null != sUniCode) {
            ostream.writeString(8, sUniCode);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sName = (String)istream.readString(0, false, this.sName);
        this.iAge = (int)istream.readInt32(1, false, this.iAge);
        this.sEdu = (String)istream.readString(2, false, this.sEdu);
        this.sBusiness = (String)istream.readString(3, false, this.sBusiness);
        this.sTimeofOffice = (String)istream.readString(4, false, this.sTimeofOffice);
        this.fHoldNum = (float)istream.readFloat(5, false, this.fHoldNum);
        this.fPay = (float)istream.readFloat(6, false, this.fPay);
        this.sIntroduce = (String)istream.readString(7, false, this.sIntroduce);
        this.sUniCode = (String)istream.readString(8, false, this.sUniCode);
    }

}

