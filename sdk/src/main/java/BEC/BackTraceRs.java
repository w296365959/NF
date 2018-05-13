package BEC;

public final class BackTraceRs extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public float fAvgInc5 = 0;

    public float fProbaInc5 = 0;

    public float fAvgInc10 = 0;

    public float fProbaInc10 = 0;

    public float fAvgInc15 = 0;

    public float fProbaInc15 = 0;

    public float getFAvgInc5()
    {
        return fAvgInc5;
    }

    public void  setFAvgInc5(float fAvgInc5)
    {
        this.fAvgInc5 = fAvgInc5;
    }

    public float getFProbaInc5()
    {
        return fProbaInc5;
    }

    public void  setFProbaInc5(float fProbaInc5)
    {
        this.fProbaInc5 = fProbaInc5;
    }

    public float getFAvgInc10()
    {
        return fAvgInc10;
    }

    public void  setFAvgInc10(float fAvgInc10)
    {
        this.fAvgInc10 = fAvgInc10;
    }

    public float getFProbaInc10()
    {
        return fProbaInc10;
    }

    public void  setFProbaInc10(float fProbaInc10)
    {
        this.fProbaInc10 = fProbaInc10;
    }

    public float getFAvgInc15()
    {
        return fAvgInc15;
    }

    public void  setFAvgInc15(float fAvgInc15)
    {
        this.fAvgInc15 = fAvgInc15;
    }

    public float getFProbaInc15()
    {
        return fProbaInc15;
    }

    public void  setFProbaInc15(float fProbaInc15)
    {
        this.fProbaInc15 = fProbaInc15;
    }

    public BackTraceRs()
    {
    }

    public BackTraceRs(float fAvgInc5, float fProbaInc5, float fAvgInc10, float fProbaInc10, float fAvgInc15, float fProbaInc15)
    {
        this.fAvgInc5 = fAvgInc5;
        this.fProbaInc5 = fProbaInc5;
        this.fAvgInc10 = fAvgInc10;
        this.fProbaInc10 = fProbaInc10;
        this.fAvgInc15 = fAvgInc15;
        this.fProbaInc15 = fProbaInc15;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeFloat(0, fAvgInc5);
        ostream.writeFloat(1, fProbaInc5);
        ostream.writeFloat(2, fAvgInc10);
        ostream.writeFloat(3, fProbaInc10);
        ostream.writeFloat(4, fAvgInc15);
        ostream.writeFloat(5, fProbaInc15);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.fAvgInc5 = (float)istream.readFloat(0, false, this.fAvgInc5);
        this.fProbaInc5 = (float)istream.readFloat(1, false, this.fProbaInc5);
        this.fAvgInc10 = (float)istream.readFloat(2, false, this.fAvgInc10);
        this.fProbaInc10 = (float)istream.readFloat(3, false, this.fProbaInc10);
        this.fAvgInc15 = (float)istream.readFloat(4, false, this.fAvgInc15);
        this.fProbaInc15 = (float)istream.readFloat(5, false, this.fProbaInc15);
    }

}

