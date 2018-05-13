package BEC;

public final class OpenApiRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int eOpenApiRet = 0;

    public byte [] vBuffer = null;

    public int getEOpenApiRet()
    {
        return eOpenApiRet;
    }

    public void  setEOpenApiRet(int eOpenApiRet)
    {
        this.eOpenApiRet = eOpenApiRet;
    }

    public byte [] getVBuffer()
    {
        return vBuffer;
    }

    public void  setVBuffer(byte [] vBuffer)
    {
        this.vBuffer = vBuffer;
    }

    public OpenApiRsp()
    {
    }

    public OpenApiRsp(int eOpenApiRet, byte [] vBuffer)
    {
        this.eOpenApiRet = eOpenApiRet;
        this.vBuffer = vBuffer;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, eOpenApiRet);
        if (null != vBuffer) {
            ostream.writeBytes(2, vBuffer);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.eOpenApiRet = (int)istream.readInt32(0, false, this.eOpenApiRet);
        this.vBuffer = (byte [])istream.readBytes(2, false, this.vBuffer);
    }

}

