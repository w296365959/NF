package BEC;

public final class GetPriviUserInviteInfoRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iRetCode = 0;

    public int iPriviSwitch = 0;

    public int iInviteFriendNum = 0;

    public int iInviteOpenDays = 0;

    public int iAccuUseDays = 0;

    public int iAccuOpenDays = 0;

    public int iLoginOpenDays = 0;

    public int iFriendOpenDays = 0;

    public int iSuccInviteFriendNum = 0;

    public int iMonthAccuUseDays = 0;

    public int iFirstLoginTime = 0;

    public String sPriviEndDay = "";

    public String sInvitePass = "";

    public int iPriviStatus = 0;

    public int iTotalOpenUserNum = 0;

    public int iAlarmDaysOfExpire = 0;

    public int iPriviLeftDays = 0;

    public int getIRetCode()
    {
        return iRetCode;
    }

    public void  setIRetCode(int iRetCode)
    {
        this.iRetCode = iRetCode;
    }

    public int getIPriviSwitch()
    {
        return iPriviSwitch;
    }

    public void  setIPriviSwitch(int iPriviSwitch)
    {
        this.iPriviSwitch = iPriviSwitch;
    }

    public int getIInviteFriendNum()
    {
        return iInviteFriendNum;
    }

    public void  setIInviteFriendNum(int iInviteFriendNum)
    {
        this.iInviteFriendNum = iInviteFriendNum;
    }

    public int getIInviteOpenDays()
    {
        return iInviteOpenDays;
    }

    public void  setIInviteOpenDays(int iInviteOpenDays)
    {
        this.iInviteOpenDays = iInviteOpenDays;
    }

    public int getIAccuUseDays()
    {
        return iAccuUseDays;
    }

    public void  setIAccuUseDays(int iAccuUseDays)
    {
        this.iAccuUseDays = iAccuUseDays;
    }

    public int getIAccuOpenDays()
    {
        return iAccuOpenDays;
    }

    public void  setIAccuOpenDays(int iAccuOpenDays)
    {
        this.iAccuOpenDays = iAccuOpenDays;
    }

    public int getILoginOpenDays()
    {
        return iLoginOpenDays;
    }

    public void  setILoginOpenDays(int iLoginOpenDays)
    {
        this.iLoginOpenDays = iLoginOpenDays;
    }

    public int getIFriendOpenDays()
    {
        return iFriendOpenDays;
    }

    public void  setIFriendOpenDays(int iFriendOpenDays)
    {
        this.iFriendOpenDays = iFriendOpenDays;
    }

    public int getISuccInviteFriendNum()
    {
        return iSuccInviteFriendNum;
    }

    public void  setISuccInviteFriendNum(int iSuccInviteFriendNum)
    {
        this.iSuccInviteFriendNum = iSuccInviteFriendNum;
    }

    public int getIMonthAccuUseDays()
    {
        return iMonthAccuUseDays;
    }

    public void  setIMonthAccuUseDays(int iMonthAccuUseDays)
    {
        this.iMonthAccuUseDays = iMonthAccuUseDays;
    }

    public int getIFirstLoginTime()
    {
        return iFirstLoginTime;
    }

    public void  setIFirstLoginTime(int iFirstLoginTime)
    {
        this.iFirstLoginTime = iFirstLoginTime;
    }

    public String getSPriviEndDay()
    {
        return sPriviEndDay;
    }

    public void  setSPriviEndDay(String sPriviEndDay)
    {
        this.sPriviEndDay = sPriviEndDay;
    }

    public String getSInvitePass()
    {
        return sInvitePass;
    }

    public void  setSInvitePass(String sInvitePass)
    {
        this.sInvitePass = sInvitePass;
    }

    public int getIPriviStatus()
    {
        return iPriviStatus;
    }

    public void  setIPriviStatus(int iPriviStatus)
    {
        this.iPriviStatus = iPriviStatus;
    }

    public int getITotalOpenUserNum()
    {
        return iTotalOpenUserNum;
    }

    public void  setITotalOpenUserNum(int iTotalOpenUserNum)
    {
        this.iTotalOpenUserNum = iTotalOpenUserNum;
    }

    public int getIAlarmDaysOfExpire()
    {
        return iAlarmDaysOfExpire;
    }

    public void  setIAlarmDaysOfExpire(int iAlarmDaysOfExpire)
    {
        this.iAlarmDaysOfExpire = iAlarmDaysOfExpire;
    }

    public int getIPriviLeftDays()
    {
        return iPriviLeftDays;
    }

    public void  setIPriviLeftDays(int iPriviLeftDays)
    {
        this.iPriviLeftDays = iPriviLeftDays;
    }

    public GetPriviUserInviteInfoRsp()
    {
    }

    public GetPriviUserInviteInfoRsp(int iRetCode, int iPriviSwitch, int iInviteFriendNum, int iInviteOpenDays, int iAccuUseDays, int iAccuOpenDays, int iLoginOpenDays, int iFriendOpenDays, int iSuccInviteFriendNum, int iMonthAccuUseDays, int iFirstLoginTime, String sPriviEndDay, String sInvitePass, int iPriviStatus, int iTotalOpenUserNum, int iAlarmDaysOfExpire, int iPriviLeftDays)
    {
        this.iRetCode = iRetCode;
        this.iPriviSwitch = iPriviSwitch;
        this.iInviteFriendNum = iInviteFriendNum;
        this.iInviteOpenDays = iInviteOpenDays;
        this.iAccuUseDays = iAccuUseDays;
        this.iAccuOpenDays = iAccuOpenDays;
        this.iLoginOpenDays = iLoginOpenDays;
        this.iFriendOpenDays = iFriendOpenDays;
        this.iSuccInviteFriendNum = iSuccInviteFriendNum;
        this.iMonthAccuUseDays = iMonthAccuUseDays;
        this.iFirstLoginTime = iFirstLoginTime;
        this.sPriviEndDay = sPriviEndDay;
        this.sInvitePass = sInvitePass;
        this.iPriviStatus = iPriviStatus;
        this.iTotalOpenUserNum = iTotalOpenUserNum;
        this.iAlarmDaysOfExpire = iAlarmDaysOfExpire;
        this.iPriviLeftDays = iPriviLeftDays;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iRetCode);
        ostream.writeInt32(1, iPriviSwitch);
        ostream.writeInt32(2, iInviteFriendNum);
        ostream.writeInt32(3, iInviteOpenDays);
        ostream.writeInt32(4, iAccuUseDays);
        ostream.writeInt32(5, iAccuOpenDays);
        ostream.writeInt32(6, iLoginOpenDays);
        ostream.writeInt32(7, iFriendOpenDays);
        ostream.writeInt32(8, iSuccInviteFriendNum);
        ostream.writeInt32(9, iMonthAccuUseDays);
        ostream.writeInt32(10, iFirstLoginTime);
        if (null != sPriviEndDay) {
            ostream.writeString(11, sPriviEndDay);
        }
        if (null != sInvitePass) {
            ostream.writeString(12, sInvitePass);
        }
        ostream.writeInt32(13, iPriviStatus);
        ostream.writeInt32(14, iTotalOpenUserNum);
        ostream.writeInt32(15, iAlarmDaysOfExpire);
        ostream.writeInt32(16, iPriviLeftDays);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iRetCode = (int)istream.readInt32(0, false, this.iRetCode);
        this.iPriviSwitch = (int)istream.readInt32(1, false, this.iPriviSwitch);
        this.iInviteFriendNum = (int)istream.readInt32(2, false, this.iInviteFriendNum);
        this.iInviteOpenDays = (int)istream.readInt32(3, false, this.iInviteOpenDays);
        this.iAccuUseDays = (int)istream.readInt32(4, false, this.iAccuUseDays);
        this.iAccuOpenDays = (int)istream.readInt32(5, false, this.iAccuOpenDays);
        this.iLoginOpenDays = (int)istream.readInt32(6, false, this.iLoginOpenDays);
        this.iFriendOpenDays = (int)istream.readInt32(7, false, this.iFriendOpenDays);
        this.iSuccInviteFriendNum = (int)istream.readInt32(8, false, this.iSuccInviteFriendNum);
        this.iMonthAccuUseDays = (int)istream.readInt32(9, false, this.iMonthAccuUseDays);
        this.iFirstLoginTime = (int)istream.readInt32(10, false, this.iFirstLoginTime);
        this.sPriviEndDay = (String)istream.readString(11, false, this.sPriviEndDay);
        this.sInvitePass = (String)istream.readString(12, false, this.sInvitePass);
        this.iPriviStatus = (int)istream.readInt32(13, false, this.iPriviStatus);
        this.iTotalOpenUserNum = (int)istream.readInt32(14, false, this.iTotalOpenUserNum);
        this.iAlarmDaysOfExpire = (int)istream.readInt32(15, false, this.iAlarmDaysOfExpire);
        this.iPriviLeftDays = (int)istream.readInt32(16, false, this.iPriviLeftDays);
    }

}

