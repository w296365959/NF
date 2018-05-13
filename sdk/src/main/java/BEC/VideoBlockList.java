package BEC;

public final class VideoBlockList extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.VideoBlock> vVideoBlock = null;

    public String sNextVideoKey = "";

    public java.util.ArrayList<BEC.VideoBlock> getVVideoBlock()
    {
        return vVideoBlock;
    }

    public void  setVVideoBlock(java.util.ArrayList<BEC.VideoBlock> vVideoBlock)
    {
        this.vVideoBlock = vVideoBlock;
    }

    public String getSNextVideoKey()
    {
        return sNextVideoKey;
    }

    public void  setSNextVideoKey(String sNextVideoKey)
    {
        this.sNextVideoKey = sNextVideoKey;
    }

    public VideoBlockList()
    {
    }

    public VideoBlockList(java.util.ArrayList<BEC.VideoBlock> vVideoBlock, String sNextVideoKey)
    {
        this.vVideoBlock = vVideoBlock;
        this.sNextVideoKey = sNextVideoKey;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vVideoBlock) {
            ostream.writeList(0, vVideoBlock);
        }
        if (null != sNextVideoKey) {
            ostream.writeString(1, sNextVideoKey);
        }
    }

    static java.util.ArrayList<BEC.VideoBlock> VAR_TYPE_4_VVIDEOBLOCK = new java.util.ArrayList<BEC.VideoBlock>();
    static {
        VAR_TYPE_4_VVIDEOBLOCK.add(new BEC.VideoBlock());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vVideoBlock = (java.util.ArrayList<BEC.VideoBlock>)istream.readList(0, false, VAR_TYPE_4_VVIDEOBLOCK);
        this.sNextVideoKey = (String)istream.readString(1, false, this.sNextVideoKey);
    }

}

