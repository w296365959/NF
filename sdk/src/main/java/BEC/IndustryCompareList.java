package BEC;

public final class IndustryCompareList extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.IndustryCompare> vIndustryCompare = null;

    public java.util.ArrayList<BEC.IndustryCompare> getVIndustryCompare()
    {
        return vIndustryCompare;
    }

    public void  setVIndustryCompare(java.util.ArrayList<BEC.IndustryCompare> vIndustryCompare)
    {
        this.vIndustryCompare = vIndustryCompare;
    }

    public IndustryCompareList()
    {
    }

    public IndustryCompareList(java.util.ArrayList<BEC.IndustryCompare> vIndustryCompare)
    {
        this.vIndustryCompare = vIndustryCompare;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vIndustryCompare) {
            ostream.writeList(0, vIndustryCompare);
        }
    }

    static java.util.ArrayList<BEC.IndustryCompare> VAR_TYPE_4_VINDUSTRYCOMPARE = new java.util.ArrayList<BEC.IndustryCompare>();
    static {
        VAR_TYPE_4_VINDUSTRYCOMPARE.add(new BEC.IndustryCompare());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vIndustryCompare = (java.util.ArrayList<BEC.IndustryCompare>)istream.readList(0, false, VAR_TYPE_4_VINDUSTRYCOMPARE);
    }

}

