package BEC;

import com.dengtacj.json.JSON;
import com.dengtacj.json.JSONException;

public final class MarginTradeReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public byte [] vGuid = null;

    public String sDtSecCode = "";

    public byte [] getVGuid()
    {
        return vGuid;
    }

    public void  setVGuid(byte [] vGuid)
    {
        this.vGuid = vGuid;
    }

    public String getSDtSecCode()
    {
        return sDtSecCode;
    }

    public void  setSDtSecCode(String sDtSecCode)
    {
        this.sDtSecCode = sDtSecCode;
    }

    public MarginTradeReq()
    {
    }

    public MarginTradeReq(byte [] vGuid, String sDtSecCode)
    {
        this.vGuid = vGuid;
        this.sDtSecCode = sDtSecCode;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vGuid) {
            ostream.writeBytes(0, vGuid);
        }
        if (null != sDtSecCode) {
            ostream.writeString(1, sDtSecCode);
        }
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vGuid = (byte [])istream.readBytes(0, false, this.vGuid);
        this.sDtSecCode = (String)istream.readString(1, false, this.sDtSecCode);
    }

    public String writeToJsonString() throws JSONException
    {
        return JSON.toJSONString(this);
    }

    public void readFromJsonString(String text) throws JSONException
    {
        MarginTradeReq temp = JSON.parseObject(text, MarginTradeReq.class);
        this.vGuid = temp.vGuid;
        this.sDtSecCode = temp.sDtSecCode;
    }

}

