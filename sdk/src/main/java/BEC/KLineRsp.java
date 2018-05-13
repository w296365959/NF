package BEC;

public final class KLineRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.KLineDesc> vKLineDesc = null;

    public int eKLineType = 0;

    public java.util.ArrayList<BEC.KLineDesc> getVKLineDesc()
    {
        return vKLineDesc;
    }

    public void  setVKLineDesc(java.util.ArrayList<BEC.KLineDesc> vKLineDesc)
    {
        this.vKLineDesc = vKLineDesc;
    }

    public int getEKLineType()
    {
        return eKLineType;
    }

    public void  setEKLineType(int eKLineType)
    {
        this.eKLineType = eKLineType;
    }

    public KLineRsp()
    {
    }

    public KLineRsp(java.util.ArrayList<BEC.KLineDesc> vKLineDesc, int eKLineType)
    {
        this.vKLineDesc = vKLineDesc;
        this.eKLineType = eKLineType;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vKLineDesc) {
            ostream.writeList(0, vKLineDesc);
        }
        ostream.writeInt32(2, eKLineType);
    }

    static java.util.ArrayList<BEC.KLineDesc> VAR_TYPE_4_VKLINEDESC = new java.util.ArrayList<BEC.KLineDesc>();
    static {
        VAR_TYPE_4_VKLINEDESC.add(new BEC.KLineDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vKLineDesc = (java.util.ArrayList<BEC.KLineDesc>)istream.readList(0, false, VAR_TYPE_4_VKLINEDESC);
        this.eKLineType = (int)istream.readInt32(2, false, this.eKLineType);
    }

}

