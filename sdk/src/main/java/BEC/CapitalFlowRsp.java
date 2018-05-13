package BEC;

public final class CapitalFlowRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.CapitalFlow> vCapitalFlow = null;

    public java.util.ArrayList<BEC.CapitalFlow> getVCapitalFlow()
    {
        return vCapitalFlow;
    }

    public void  setVCapitalFlow(java.util.ArrayList<BEC.CapitalFlow> vCapitalFlow)
    {
        this.vCapitalFlow = vCapitalFlow;
    }

    public CapitalFlowRsp()
    {
    }

    public CapitalFlowRsp(java.util.ArrayList<BEC.CapitalFlow> vCapitalFlow)
    {
        this.vCapitalFlow = vCapitalFlow;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vCapitalFlow) {
            ostream.writeList(0, vCapitalFlow);
        }
    }

    static java.util.ArrayList<BEC.CapitalFlow> VAR_TYPE_4_VCAPITALFLOW = new java.util.ArrayList<BEC.CapitalFlow>();
    static {
        VAR_TYPE_4_VCAPITALFLOW.add(new BEC.CapitalFlow());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vCapitalFlow = (java.util.ArrayList<BEC.CapitalFlow>)istream.readList(0, false, VAR_TYPE_4_VCAPITALFLOW);
    }

}

