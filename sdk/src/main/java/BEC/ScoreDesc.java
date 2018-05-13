package BEC;

public final class ScoreDesc extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iScore = 0;

    public String sScoreDesc = "";

    public int getIScore()
    {
        return iScore;
    }

    public void  setIScore(int iScore)
    {
        this.iScore = iScore;
    }

    public String getSScoreDesc()
    {
        return sScoreDesc;
    }

    public void  setSScoreDesc(String sScoreDesc)
    {
        this.sScoreDesc = sScoreDesc;
    }

    public ScoreDesc()
    {
    }

    public ScoreDesc(int iScore, String sScoreDesc)
    {
        this.iScore = iScore;
        this.sScoreDesc = sScoreDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iScore);
        if (null != sScoreDesc) {
            ostream.writeString(1, sScoreDesc);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iScore = (int)istream.readInt32(0, false, this.iScore);
        this.sScoreDesc = (String)istream.readString(1, false, this.sScoreDesc);
    }

}

