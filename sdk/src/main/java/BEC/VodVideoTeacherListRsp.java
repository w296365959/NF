package BEC;

public final class VodVideoTeacherListRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.VideoTeacher> vtVideoTeacher = null;

    public java.util.ArrayList<BEC.VideoTeacher> getVtVideoTeacher()
    {
        return vtVideoTeacher;
    }

    public void  setVtVideoTeacher(java.util.ArrayList<BEC.VideoTeacher> vtVideoTeacher)
    {
        this.vtVideoTeacher = vtVideoTeacher;
    }

    public VodVideoTeacherListRsp()
    {
    }

    public VodVideoTeacherListRsp(java.util.ArrayList<BEC.VideoTeacher> vtVideoTeacher)
    {
        this.vtVideoTeacher = vtVideoTeacher;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtVideoTeacher) {
            ostream.writeList(0, vtVideoTeacher);
        }
    }

    static java.util.ArrayList<BEC.VideoTeacher> VAR_TYPE_4_VTVIDEOTEACHER = new java.util.ArrayList<BEC.VideoTeacher>();
    static {
        VAR_TYPE_4_VTVIDEOTEACHER.add(new BEC.VideoTeacher());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtVideoTeacher = (java.util.ArrayList<BEC.VideoTeacher>)istream.readList(0, false, VAR_TYPE_4_VTVIDEOTEACHER);
    }

}

