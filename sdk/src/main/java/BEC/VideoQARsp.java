package BEC;

public final class VideoQARsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.VideoQA> vtVideoQA = null;

    public String sNextQuestionId = "";

    public java.util.ArrayList<BEC.VideoQA> getVtVideoQA()
    {
        return vtVideoQA;
    }

    public void  setVtVideoQA(java.util.ArrayList<BEC.VideoQA> vtVideoQA)
    {
        this.vtVideoQA = vtVideoQA;
    }

    public String getSNextQuestionId()
    {
        return sNextQuestionId;
    }

    public void  setSNextQuestionId(String sNextQuestionId)
    {
        this.sNextQuestionId = sNextQuestionId;
    }

    public VideoQARsp()
    {
    }

    public VideoQARsp(java.util.ArrayList<BEC.VideoQA> vtVideoQA, String sNextQuestionId)
    {
        this.vtVideoQA = vtVideoQA;
        this.sNextQuestionId = sNextQuestionId;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtVideoQA) {
            ostream.writeList(0, vtVideoQA);
        }
        if (null != sNextQuestionId) {
            ostream.writeString(1, sNextQuestionId);
        }
    }

    static java.util.ArrayList<BEC.VideoQA> VAR_TYPE_4_VTVIDEOQA = new java.util.ArrayList<BEC.VideoQA>();
    static {
        VAR_TYPE_4_VTVIDEOQA.add(new BEC.VideoQA());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtVideoQA = (java.util.ArrayList<BEC.VideoQA>)istream.readList(0, false, VAR_TYPE_4_VTVIDEOQA);
        this.sNextQuestionId = (String)istream.readString(1, false, this.sNextQuestionId);
    }

}

