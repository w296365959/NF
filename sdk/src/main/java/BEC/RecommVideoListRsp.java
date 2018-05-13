package BEC;

public final class RecommVideoListRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int eChannel = 0;

    public java.util.ArrayList<BEC.VideoBlock> vVideoBlock = null;

    public int getEChannel()
    {
        return eChannel;
    }

    public void  setEChannel(int eChannel)
    {
        this.eChannel = eChannel;
    }

    public java.util.ArrayList<BEC.VideoBlock> getVVideoBlock()
    {
        return vVideoBlock;
    }

    public void  setVVideoBlock(java.util.ArrayList<BEC.VideoBlock> vVideoBlock)
    {
        this.vVideoBlock = vVideoBlock;
    }

    public RecommVideoListRsp()
    {
    }

    public RecommVideoListRsp(int eChannel, java.util.ArrayList<BEC.VideoBlock> vVideoBlock)
    {
        this.eChannel = eChannel;
        this.vVideoBlock = vVideoBlock;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, eChannel);
        if (null != vVideoBlock) {
            ostream.writeList(1, vVideoBlock);
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

        this.eChannel = (int)istream.readInt32(0, false, this.eChannel);
        this.vVideoBlock = (java.util.ArrayList<BEC.VideoBlock>)istream.readList(1, false, VAR_TYPE_4_VVIDEOBLOCK);
    }

}

