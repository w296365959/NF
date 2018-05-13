package BEC;

public final class ZipStoreRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<ZipResponse> vZipResponse = null;

    public java.util.ArrayList<ZipResponse> getVZipResponse()
    {
        return vZipResponse;
    }

    public void  setVZipResponse(java.util.ArrayList<ZipResponse> vZipResponse)
    {
        this.vZipResponse = vZipResponse;
    }

    public ZipStoreRsp()
    {
    }

    public ZipStoreRsp(java.util.ArrayList<ZipResponse> vZipResponse)
    {
        this.vZipResponse = vZipResponse;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vZipResponse) {
            ostream.writeList(0, vZipResponse);
        }
    }

    static java.util.ArrayList<ZipResponse> VAR_TYPE_4_VZIPRESPONSE = new java.util.ArrayList<ZipResponse>();
    static {
        VAR_TYPE_4_VZIPRESPONSE.add(new ZipResponse());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vZipResponse = (java.util.ArrayList<ZipResponse>)istream.readList(0, false, VAR_TYPE_4_VZIPRESPONSE);
    }

}

