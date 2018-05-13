package BEC;

public final class RecommValueAddedListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<RecommValueAdded> vRecommValueAdded = null;

    public java.util.ArrayList<RecommValueAdded> getVRecommValueAdded()
    {
        return vRecommValueAdded;
    }

    public void  setVRecommValueAdded(java.util.ArrayList<RecommValueAdded> vRecommValueAdded)
    {
        this.vRecommValueAdded = vRecommValueAdded;
    }

    public RecommValueAddedListRsp()
    {
    }

    public RecommValueAddedListRsp(java.util.ArrayList<RecommValueAdded> vRecommValueAdded)
    {
        this.vRecommValueAdded = vRecommValueAdded;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vRecommValueAdded) {
            ostream.writeList(0, vRecommValueAdded);
        }
    }

    static java.util.ArrayList<RecommValueAdded> VAR_TYPE_4_VRECOMMVALUEADDED = new java.util.ArrayList<RecommValueAdded>();
    static {
        VAR_TYPE_4_VRECOMMVALUEADDED.add(new RecommValueAdded());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vRecommValueAdded = (java.util.ArrayList<RecommValueAdded>)istream.readList(0, false, VAR_TYPE_4_VRECOMMVALUEADDED);
    }

}

