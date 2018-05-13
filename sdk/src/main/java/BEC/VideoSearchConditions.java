package BEC;

public final class VideoSearchConditions extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<Integer> vType = null;

    public java.util.ArrayList<BEC.VideoChannelDesc> vChannel = null;

    public java.util.ArrayList<BEC.Professor> vProfessor = null;

    public java.util.ArrayList<Integer> getVType()
    {
        return vType;
    }

    public void  setVType(java.util.ArrayList<Integer> vType)
    {
        this.vType = vType;
    }

    public java.util.ArrayList<BEC.VideoChannelDesc> getVChannel()
    {
        return vChannel;
    }

    public void  setVChannel(java.util.ArrayList<BEC.VideoChannelDesc> vChannel)
    {
        this.vChannel = vChannel;
    }

    public java.util.ArrayList<BEC.Professor> getVProfessor()
    {
        return vProfessor;
    }

    public void  setVProfessor(java.util.ArrayList<BEC.Professor> vProfessor)
    {
        this.vProfessor = vProfessor;
    }

    public VideoSearchConditions()
    {
    }

    public VideoSearchConditions(java.util.ArrayList<Integer> vType, java.util.ArrayList<BEC.VideoChannelDesc> vChannel, java.util.ArrayList<BEC.Professor> vProfessor)
    {
        this.vType = vType;
        this.vChannel = vChannel;
        this.vProfessor = vProfessor;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vType) {
            ostream.writeList(0, vType);
        }
        if (null != vChannel) {
            ostream.writeList(1, vChannel);
        }
        if (null != vProfessor) {
            ostream.writeList(2, vProfessor);
        }
    }

    static java.util.ArrayList<Integer> VAR_TYPE_4_VTYPE = new java.util.ArrayList<Integer>();
    static {
        VAR_TYPE_4_VTYPE.add(0);
    }

    static java.util.ArrayList<BEC.VideoChannelDesc> VAR_TYPE_4_VCHANNEL = new java.util.ArrayList<BEC.VideoChannelDesc>();
    static {
        VAR_TYPE_4_VCHANNEL.add(new BEC.VideoChannelDesc());
    }

    static java.util.ArrayList<BEC.Professor> VAR_TYPE_4_VPROFESSOR = new java.util.ArrayList<BEC.Professor>();
    static {
        VAR_TYPE_4_VPROFESSOR.add(new BEC.Professor());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vType = (java.util.ArrayList<Integer>)istream.readList(0, false, VAR_TYPE_4_VTYPE);
        this.vChannel = (java.util.ArrayList<BEC.VideoChannelDesc>)istream.readList(1, false, VAR_TYPE_4_VCHANNEL);
        this.vProfessor = (java.util.ArrayList<BEC.Professor>)istream.readList(2, false, VAR_TYPE_4_VPROFESSOR);
    }

}

