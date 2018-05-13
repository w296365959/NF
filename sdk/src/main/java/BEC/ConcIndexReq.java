package BEC;

import com.dengtacj.json.JSON;
import com.dengtacj.json.JSONException;

public final class ConcIndexReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sDtSecCode = "";

    public int iStartxh = 0;

    public int iWantnum = 0;

    public byte [] vGuid = null;

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

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

    public ConcIndexReq()
    {
    }

    public ConcIndexReq(String sDtSecCode, int iStartxh, int iWantnum, byte [] vGuid)
    {
        this.sDtSecCode = sDtSecCode;
        this.iStartxh = iStartxh;
        this.iWantnum = iWantnum;
        this.vGuid = vGuid;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sDtSecCode) {
            ostream.writeString(0, sDtSecCode);
        }
        ostream.writeInt32(1, iStartxh);
        ostream.writeInt32(2, iWantnum);
        if (null != vGuid) {
            ostream.writeBytes(3, vGuid);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sDtSecCode = (String)istream.readString(0, false, this.sDtSecCode);
        this.iStartxh = (int)istream.readInt32(1, false, this.iStartxh);
        this.iWantnum = (int)istream.readInt32(2, false, this.iWantnum);
        this.vGuid = (byte [])istream.readBytes(3, false, this.vGuid);
    }

    public String writeToJsonString() throws JSONException
    {
        return JSON.toJSONString(this);
    }

    public void readFromJsonString(String text) throws JSONException
    {
        ConcIndexReq temp = JSON.parseObject(text, ConcIndexReq.class);
        this.sDtSecCode = temp.sDtSecCode;
        this.iStartxh = temp.iStartxh;
        this.iWantnum = temp.iWantnum;
        this.vGuid = temp.vGuid;
    }

}

