package BEC;

public final class UserReq extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public UserInfo stUser = null;

    public int type = 0;

    public String sMD5 = "";

    public UserInfo getStUser()
    {
        return stUser;
    }

    public void  setStUser(UserInfo stUser)
    {
        this.stUser = stUser;
    }

    public int getType()
    {
        return type;
    }

    public void  setType(int type)
    {
        this.type = type;
    }

    public String getSMD5()
    {
        return sMD5;
    }

    public void  setSMD5(String sMD5)
    {
        this.sMD5 = sMD5;
    }

    public UserReq()
    {
    }

    public UserReq(UserInfo stUser, int type, String sMD5)
    {
        this.stUser = stUser;
        this.type = type;
        this.sMD5 = sMD5;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != stUser) {
            ostream.writeMessage(0, stUser);
        }
        ostream.writeInt32(1, type);
        if (null != sMD5) {
            ostream.writeString(2, sMD5);
        }
    }

    static UserInfo VAR_TYPE_4_STUSER = new UserInfo();


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.stUser = (UserInfo)istream.readMessage(0, false, VAR_TYPE_4_STUSER);
        this.type = (int)istream.readInt32(1, false, this.type);
        this.sMD5 = (String)istream.readString(2, false, this.sMD5);
    }

}

