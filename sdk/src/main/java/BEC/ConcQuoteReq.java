package BEC;

import com.dengtacj.json.JSON;
import com.dengtacj.json.JSONException;

public final class ConcQuoteReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iStartxh = 0;

    public int iWantnum = 0;

    public byte [] vGuid = null;

    public int getIStartxh()
    {
        return iStartxh;
    }

    public void  setIStartxh(int iStartxh)
    {
        this.iStartxh = iStartxh;
    }

    public int getIWantnum()
    {
        return iWantnum;
    }

    public void  setIWantnum(int iWantnum)
    {
        this.iWantnum = iWantnum;
    }

    public byte [] getVGuid()
    {
        return vGuid;
    }

    public void  setVGuid(byte [] vGuid)
    {
        this.vGuid = vGuid;
    }

    public ConcQuoteReq()
    {
    }

    public ConcQuoteReq(int iStartxh, int iWantnum, byte [] vGuid)
    {
        this.iStartxh = iStartxh;
        this.iWantnum = iWantnum;
        this.vGuid = vGuid;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iStartxh);
        ostream.writeInt32(1, iWantnum);
        if (null != vGuid) {
            ostream.writeBytes(2, vGuid);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iStartxh = (int)istream.readInt32(0, false, this.iStartxh);
        this.iWantnum = (int)istream.readInt32(1, false, this.iWantnum);
        this.vGuid = (byte [])istream.readBytes(2, false, this.vGuid);
    }

    public String writeToJsonString() throws JSONException
    {
        return JSON.toJSONString(this);
    }

    public void readFromJsonString(String text) throws JSONException
    {
        ConcQuoteReq temp = JSON.parseObject(text, ConcQuoteReq.class);
        this.iStartxh = temp.iStartxh;
        this.iWantnum = temp.iWantnum;
        this.vGuid = temp.vGuid;
    }

}

