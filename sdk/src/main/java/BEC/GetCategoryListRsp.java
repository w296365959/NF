package BEC;

public final class GetCategoryListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<CategoryInfo> vList = null;

    public java.util.ArrayList<CategoryInfo> getVList()
    {
        return vList;
    }

    public void  setVList(java.util.ArrayList<CategoryInfo> vList)
    {
        this.vList = vList;
    }

    public GetCategoryListRsp()
    {
    }

    public GetCategoryListRsp(java.util.ArrayList<CategoryInfo> vList)
    {
        this.vList = vList;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vList) {
            ostream.writeList(0, vList);
        }
    }

    static java.util.ArrayList<CategoryInfo> VAR_TYPE_4_VLIST = new java.util.ArrayList<CategoryInfo>();
    static {
        VAR_TYPE_4_VLIST.add(new CategoryInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vList = (java.util.ArrayList<CategoryInfo>)istream.readList(0, false, VAR_TYPE_4_VLIST);
    }

}

