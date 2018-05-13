package BEC;

public final class ConsultScoreRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.ConsultScore> vtConsultScore = null;

    public java.util.ArrayList<BEC.ConsultScore> getVtConsultScore()
    {
        return vtConsultScore;
    }

    public void  setVtConsultScore(java.util.ArrayList<BEC.ConsultScore> vtConsultScore)
    {
        this.vtConsultScore = vtConsultScore;
    }

    public ConsultScoreRsp()
    {
    }

    public ConsultScoreRsp(java.util.ArrayList<BEC.ConsultScore> vtConsultScore)
    {
        this.vtConsultScore = vtConsultScore;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtConsultScore) {
            ostream.writeList(0, vtConsultScore);
        }
    }

    static java.util.ArrayList<BEC.ConsultScore> VAR_TYPE_4_VTCONSULTSCORE = new java.util.ArrayList<BEC.ConsultScore>();
    static {
        VAR_TYPE_4_VTCONSULTSCORE.add(new BEC.ConsultScore());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtConsultScore = (java.util.ArrayList<BEC.ConsultScore>)istream.readList(0, false, VAR_TYPE_4_VTCONSULTSCORE);
    }

}

