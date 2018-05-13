package BEC;

public final class GetUserEvalResultRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iRiskType = 0;

    public String sRiskType = "";

    public int getIRiskType()
    {
        return iRiskType;
    }

    public void  setIRiskType(int iRiskType)
    {
        this.iRiskType = iRiskType;
    }

    public String getSRiskType()
    {
        return sRiskType;
    }

    public void  setSRiskType(String sRiskType)
    {
        this.sRiskType = sRiskType;
    }

    public GetUserEvalResultRsp()
    {
    }

    public GetUserEvalResultRsp(int iRiskType, String sRiskType)
    {
        this.iRiskType = iRiskType;
        this.sRiskType = sRiskType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iRiskType);
        if (null != sRiskType) {
            ostream.writeString(1, sRiskType);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iRiskType = (int)istream.readInt32(0, false, this.iRiskType);
        this.sRiskType = (String)istream.readString(1, false, this.sRiskType);
    }

}

