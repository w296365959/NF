package BEC;

public final class AdditionDetailRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iRet = 0;

    public java.util.ArrayList<BEC.AdditionDesc> vAdditionDesc = null;

    public int getIRet()
    {
        return iRet;
    }

    public void  setIRet(int iRet)
    {
        this.iRet = iRet;
    }

    public java.util.ArrayList<BEC.AdditionDesc> getVAdditionDesc()
    {
        return vAdditionDesc;
    }

    public void  setVAdditionDesc(java.util.ArrayList<BEC.AdditionDesc> vAdditionDesc)
    {
        this.vAdditionDesc = vAdditionDesc;
    }

    public AdditionDetailRsp()
    {
    }

    public AdditionDetailRsp(int iRet, java.util.ArrayList<BEC.AdditionDesc> vAdditionDesc)
    {
        this.iRet = iRet;
        this.vAdditionDesc = vAdditionDesc;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iRet);
        if (null != vAdditionDesc) {
            ostream.writeList(1, vAdditionDesc);
        }
    }

    static java.util.ArrayList<BEC.AdditionDesc> VAR_TYPE_4_VADDITIONDESC = new java.util.ArrayList<BEC.AdditionDesc>();
    static {
        VAR_TYPE_4_VADDITIONDESC.add(new BEC.AdditionDesc());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iRet = (int)istream.readInt32(0, false, this.iRet);
        this.vAdditionDesc = (java.util.ArrayList<BEC.AdditionDesc>)istream.readList(1, false, VAR_TYPE_4_VADDITIONDESC);
    }

}

