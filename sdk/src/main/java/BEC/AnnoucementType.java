package BEC;

public final class AnnoucementType extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sLabel = "";

    public String sId = "";

    public String getSLabel()
    {
        return sLabel;
    }

    public void  setSLabel(String sLabel)
    {
        this.sLabel = sLabel;
    }

    public String getSId()
    {
        return sId;
    }

    public void  setSId(String sId)
    {
        this.sId = sId;
    }

    public AnnoucementType()
    {
    }

    public AnnoucementType(String sLabel, String sId)
    {
        this.sLabel = sLabel;
        this.sId = sId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sLabel) {
            ostream.writeString(0, sLabel);
        }
        if (null != sId) {
            ostream.writeString(1, sId);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sLabel = (String)istream.readString(0, false, this.sLabel);
        this.sId = (String)istream.readString(1, false, this.sId);
    }

}

