package BEC;

public final class PushData extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public byte [] vtData = null;

    public PushType stPushType = null;

    public int iPushTime = 0;

    public int iExpireTime = 0;

    public int iStartTime = 0;

    public String sTitle = "";

    public int eDeviceType = PUSH_DEVICE_TYPE.E_PDT_ALL;

    public String sDescription = "";

    public int iNotifyEffect = 0;

    public PushControlData stControl = null;

    public int iClassID = 0;

    public byte [] getVtData()
    {
        return vtData;
    }

    public void  setVtData(byte [] vtData)
    {
        this.vtData = vtData;
    }

    public PushType getStPushType()
    {
        return stPushType;
    }

    public void  setStPushType(PushType stPushType)
    {
        this.stPushType = stPushType;
    }

    public int getIPushTime()
    {
        return iPushTime;
    }

    public void  setIPushTime(int iPushTime)
    {
        this.iPushTime = iPushTime;
    }

    public int getIExpireTime()
    {
        return iExpireTime;
    }

    public void  setIExpireTime(int iExpireTime)
    {
        this.iExpireTime = iExpireTime;
    }

    public int getIStartTime()
    {
        return iStartTime;
    }

    public void  setIStartTime(int iStartTime)
    {
        this.iStartTime = iStartTime;
    }

    public String getSTitle()
    {
        return sTitle;
    }

    public void  setSTitle(String sTitle)
    {
        this.sTitle = sTitle;
    }

    public int getEDeviceType()
    {
        return eDeviceType;
    }

    public void  setEDeviceType(int eDeviceType)
    {
        this.eDeviceType = eDeviceType;
    }

    public String getSDescription()
    {
        return sDescription;
    }

    public void  setSDescription(String sDescription)
    {
        this.sDescription = sDescription;
    }

    public int getINotifyEffect()
    {
        return iNotifyEffect;
    }

    public void  setINotifyEffect(int iNotifyEffect)
    {
        this.iNotifyEffect = iNotifyEffect;
    }

    public PushControlData getStControl()
    {
        return stControl;
    }

    public void  setStControl(PushControlData stControl)
    {
        this.stControl = stControl;
    }

    public int getIClassID()
    {
        return iClassID;
    }

    public void  setIClassID(int iClassID)
    {
        this.iClassID = iClassID;
    }

    public PushData()
    {
    }

    public PushData(byte [] vtData, PushType stPushType, int iPushTime, int iExpireTime, int iStartTime, String sTitle, int eDeviceType, String sDescription, int iNotifyEffect, PushControlData stControl, int iClassID)
    {
        this.vtData = vtData;
        this.stPushType = stPushType;
        this.iPushTime = iPushTime;
        this.iExpireTime = iExpireTime;
        this.iStartTime = iStartTime;
        this.sTitle = sTitle;
        this.eDeviceType = eDeviceType;
        this.sDescription = sDescription;
        this.iNotifyEffect = iNotifyEffect;
        this.stControl = stControl;
        this.iClassID = iClassID;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeBytes(0, vtData);
        ostream.writeMessage(1, stPushType);
        ostream.writeInt32(2, iPushTime);
        ostream.writeInt32(3, iExpireTime);
        ostream.writeInt32(4, iStartTime);
        if (null != sTitle) {
            ostream.writeString(5, sTitle);
        }
        ostream.writeInt32(6, eDeviceType);
        if (null != sDescription) {
            ostream.writeString(7, sDescription);
        }
        ostream.writeInt32(9, iNotifyEffect);
        if (null != stControl) {
            ostream.writeMessage(10, stControl);
        }
        ostream.writeInt32(11, iClassID);
    }

    static PushType VAR_TYPE_4_STPUSHTYPE = new PushType();

    static PushControlData VAR_TYPE_4_STCONTROL = new PushControlData();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtData = (byte [])istream.readBytes(0, true, this.vtData);
        this.stPushType = (PushType)istream.readMessage(1, true, VAR_TYPE_4_STPUSHTYPE);
        this.iPushTime = (int)istream.readInt32(2, false, this.iPushTime);
        this.iExpireTime = (int)istream.readInt32(3, false, this.iExpireTime);
        this.iStartTime = (int)istream.readInt32(4, false, this.iStartTime);
        this.sTitle = (String)istream.readString(5, false, this.sTitle);
        this.eDeviceType = (int)istream.readInt32(6, false, this.eDeviceType);
        this.sDescription = (String)istream.readString(7, false, this.sDescription);
        this.iNotifyEffect = (int)istream.readInt32(9, false, this.iNotifyEffect);
        this.stControl = (PushControlData)istream.readMessage(10, false, VAR_TYPE_4_STCONTROL);
        this.iClassID = (int)istream.readInt32(11, false, this.iClassID);
    }

}

