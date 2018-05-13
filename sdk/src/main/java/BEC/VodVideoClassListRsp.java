package BEC;

public final class VodVideoClassListRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.VideoClass> vtVideoClass = null;

    public java.util.ArrayList<BEC.VideoClass> getVtVideoClass()
    {
        return vtVideoClass;
    }

    public void  setVtVideoClass(java.util.ArrayList<BEC.VideoClass> vtVideoClass)
    {
        this.vtVideoClass = vtVideoClass;
    }

    public VodVideoClassListRsp()
    {
    }

    public VodVideoClassListRsp(java.util.ArrayList<BEC.VideoClass> vtVideoClass)
    {
        this.vtVideoClass = vtVideoClass;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtVideoClass) {
            ostream.writeList(0, vtVideoClass);
        }
    }

    static java.util.ArrayList<BEC.VideoClass> VAR_TYPE_4_VTVIDEOCLASS = new java.util.ArrayList<BEC.VideoClass>();
    static {
        VAR_TYPE_4_VTVIDEOCLASS.add(new BEC.VideoClass());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtVideoClass = (java.util.ArrayList<BEC.VideoClass>)istream.readList(0, false, VAR_TYPE_4_VTVIDEOCLASS);
    }

}

