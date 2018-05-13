package BEC;

import com.dengtacj.json.JSON;
import com.dengtacj.json.JSONException;

public final class CapitalMainFlowReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<String> vDtSecCode = null;

    public int iStartxh = 0;

    public int iWantnum = 0;

    public byte [] vGuid = null;

    public java.util.ArrayList<String> getVDtSecCode()
    {
        return vDtSecCode;
    }

    public void  setVDtSecCode(java.util.ArrayList<String> vDtSecCode)
    {
        this.vDtSecCode = vDtSecCode;
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

    public CapitalMainFlowReq()
    {
    }

    public CapitalMainFlowReq(java.util.ArrayList<String> vDtSecCode, int iStartxh, int iWantnum, byte [] vGuid)
    {
        this.vDtSecCode = vDtSecCode;
        this.iStartxh = iStartxh;
        this.iWantnum = iWantnum;
        this.vGuid = vGuid;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vDtSecCode) {
            ostream.writeList(0, vDtSecCode);
        }
        ostream.writeInt32(1, iStartxh);
        ostream.writeInt32(2, iWantnum);
        if (null != vGuid) {
            ostream.writeBytes(3, vGuid);
        }
    }

    static java.util.ArrayList<String> VAR_TYPE_4_VDTSECCODE = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VDTSECCODE.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vDtSecCode = (java.util.ArrayList<String>)istream.readList(0, false, VAR_TYPE_4_VDTSECCODE);
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
        CapitalMainFlowReq temp = JSON.parseObject(text, CapitalMainFlowReq.class);
        this.vDtSecCode = temp.vDtSecCode;
        this.iStartxh = temp.iStartxh;
        this.iWantnum = temp.iWantnum;
        this.vGuid = temp.vGuid;
    }

}

