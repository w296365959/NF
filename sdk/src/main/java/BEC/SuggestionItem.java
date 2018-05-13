package BEC;

public final class SuggestionItem extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sSentence = "";

    public String getSSentence()
    {
        return sSentence;
    }

    public void  setSSentence(String sSentence)
    {
        this.sSentence = sSentence;
    }

    public SuggestionItem()
    {
    }

    public SuggestionItem(String sSentence)
    {
        this.sSentence = sSentence;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sSentence) {
            ostream.writeString(0, sSentence);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sSentence = (String)istream.readString(0, false, this.sSentence);
    }

}

