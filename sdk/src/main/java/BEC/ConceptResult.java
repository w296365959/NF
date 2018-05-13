package BEC;

public final class ConceptResult extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iRet = 0;

    public java.util.ArrayList<ConcInfo> vtConcInfo = null;

    public int getIRet()
    {
        return iRet;
    }

    public void  setIRet(int iRet)
    {
        this.iRet = iRet;
    }

    public java.util.ArrayList<ConcInfo> getVtConcInfo()
    {
        return vtConcInfo;
    }

    public void  setVtConcInfo(java.util.ArrayList<ConcInfo> vtConcInfo)
    {
        this.vtConcInfo = vtConcInfo;
    }

    public ConceptResult()
    {
    }

    public ConceptResult(int iRet, java.util.ArrayList<ConcInfo> vtConcInfo)
    {
        this.iRet = iRet;
        this.vtConcInfo = vtConcInfo;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iRet);
        if (null != vtConcInfo) {
            ostream.writeList(1, vtConcInfo);
        }
    }

    static java.util.ArrayList<ConcInfo> VAR_TYPE_4_VTCONCINFO = new java.util.ArrayList<ConcInfo>();
    static {
        VAR_TYPE_4_VTCONCINFO.add(new ConcInfo());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iRet = (int)istream.readInt32(0, false, this.iRet);
        this.vtConcInfo = (java.util.ArrayList<ConcInfo>)istream.readList(1, false, VAR_TYPE_4_VTCONCINFO);
    }

}

