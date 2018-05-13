package BEC;

public final class InputSuggestionRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.SuggestionItem> vtSuggestionItem = null;

    public java.util.ArrayList<BEC.SuggestionItem> getVtSuggestionItem()
    {
        return vtSuggestionItem;
    }

    public void  setVtSuggestionItem(java.util.ArrayList<BEC.SuggestionItem> vtSuggestionItem)
    {
        this.vtSuggestionItem = vtSuggestionItem;
    }

    public InputSuggestionRsp()
    {
    }

    public InputSuggestionRsp(java.util.ArrayList<BEC.SuggestionItem> vtSuggestionItem)
    {
        this.vtSuggestionItem = vtSuggestionItem;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtSuggestionItem) {
            ostream.writeList(0, vtSuggestionItem);
        }
    }

    static java.util.ArrayList<BEC.SuggestionItem> VAR_TYPE_4_VTSUGGESTIONITEM = new java.util.ArrayList<BEC.SuggestionItem>();
    static {
        VAR_TYPE_4_VTSUGGESTIONITEM.add(new BEC.SuggestionItem());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtSuggestionItem = (java.util.ArrayList<BEC.SuggestionItem>)istream.readList(0, false, VAR_TYPE_4_VTSUGGESTIONITEM);
    }

}

