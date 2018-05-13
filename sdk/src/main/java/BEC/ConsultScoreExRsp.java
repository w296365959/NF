package BEC;

public final class ConsultScoreExRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.ConsultScoreEx> vtConsultScoreEx = null;

    public java.util.ArrayList<BEC.ConsultScoreEx> getVtConsultScoreEx()
    {
        return vtConsultScoreEx;
    }

    public void  setVtConsultScoreEx(java.util.ArrayList<BEC.ConsultScoreEx> vtConsultScoreEx)
    {
        this.vtConsultScoreEx = vtConsultScoreEx;
    }

    public ConsultScoreExRsp()
    {
    }

    public ConsultScoreExRsp(java.util.ArrayList<BEC.ConsultScoreEx> vtConsultScoreEx)
    {
        this.vtConsultScoreEx = vtConsultScoreEx;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtConsultScoreEx) {
            ostream.writeList(0, vtConsultScoreEx);
        }
    }

    static java.util.ArrayList<BEC.ConsultScoreEx> VAR_TYPE_4_VTCONSULTSCOREEX = new java.util.ArrayList<BEC.ConsultScoreEx>();
    static {
        VAR_TYPE_4_VTCONSULTSCOREEX.add(new BEC.ConsultScoreEx());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtConsultScoreEx = (java.util.ArrayList<BEC.ConsultScoreEx>)istream.readList(0, false, VAR_TYPE_4_VTCONSULTSCOREEX);
    }

}

