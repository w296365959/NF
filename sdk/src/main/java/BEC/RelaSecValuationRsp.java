package BEC;

public final class RelaSecValuationRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.SecValuation> vtSecValuation = null;

    public String sPeDesc = "";

    public String sPbDesc = "";

    public int iUpdateTime = 0;

    public String sPlatePb = "";

    public String sPlatePe = "";

    public java.util.ArrayList<BEC.SecValuation> getVtSecValuation()
    {
        return vtSecValuation;
    }

    public void  setVtSecValuation(java.util.ArrayList<BEC.SecValuation> vtSecValuation)
    {
        this.vtSecValuation = vtSecValuation;
    }

    public String getSPeDesc()
    {
        return sPeDesc;
    }

    public void  setSPeDesc(String sPeDesc)
    {
        this.sPeDesc = sPeDesc;
    }

    public String getSPbDesc()
    {
        return sPbDesc;
    }

    public void  setSPbDesc(String sPbDesc)
    {
        this.sPbDesc = sPbDesc;
    }

    public int getIUpdateTime()
    {
        return iUpdateTime;
    }

    public void  setIUpdateTime(int iUpdateTime)
    {
        this.iUpdateTime = iUpdateTime;
    }

    public String getSPlatePb()
    {
        return sPlatePb;
    }

    public void  setSPlatePb(String sPlatePb)
    {
        this.sPlatePb = sPlatePb;
    }

    public String getSPlatePe()
    {
        return sPlatePe;
    }

    public void  setSPlatePe(String sPlatePe)
    {
        this.sPlatePe = sPlatePe;
    }

    public RelaSecValuationRsp()
    {
    }

    public RelaSecValuationRsp(java.util.ArrayList<BEC.SecValuation> vtSecValuation, String sPeDesc, String sPbDesc, int iUpdateTime, String sPlatePb, String sPlatePe)
    {
        this.vtSecValuation = vtSecValuation;
        this.sPeDesc = sPeDesc;
        this.sPbDesc = sPbDesc;
        this.iUpdateTime = iUpdateTime;
        this.sPlatePb = sPlatePb;
        this.sPlatePe = sPlatePe;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtSecValuation) {
            ostream.writeList(0, vtSecValuation);
        }
        if (null != sPeDesc) {
            ostream.writeString(1, sPeDesc);
        }
        if (null != sPbDesc) {
            ostream.writeString(2, sPbDesc);
        }
        ostream.writeInt32(3, iUpdateTime);
        if (null != sPlatePb) {
            ostream.writeString(4, sPlatePb);
        }
        if (null != sPlatePe) {
            ostream.writeString(5, sPlatePe);
        }
    }

    static java.util.ArrayList<BEC.SecValuation> VAR_TYPE_4_VTSECVALUATION = new java.util.ArrayList<BEC.SecValuation>();
    static {
        VAR_TYPE_4_VTSECVALUATION.add(new BEC.SecValuation());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtSecValuation = (java.util.ArrayList<BEC.SecValuation>)istream.readList(0, false, VAR_TYPE_4_VTSECVALUATION);
        this.sPeDesc = (String)istream.readString(1, false, this.sPeDesc);
        this.sPbDesc = (String)istream.readString(2, false, this.sPbDesc);
        this.iUpdateTime = (int)istream.readInt32(3, false, this.iUpdateTime);
        this.sPlatePb = (String)istream.readString(4, false, this.sPlatePb);
        this.sPlatePe = (String)istream.readString(5, false, this.sPlatePe);
    }

}

