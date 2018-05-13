package BEC;

public final class IntelliBroker extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sBrokerName = "";

    public String sBrokerIcon = "";

    public String sBrokerUrl = "";

    public java.util.ArrayList<String> vTag = null;

    public String getSBrokerName()
    {
        return sBrokerName;
    }

    public void  setSBrokerName(String sBrokerName)
    {
        this.sBrokerName = sBrokerName;
    }

    public String getSBrokerIcon()
    {
        return sBrokerIcon;
    }

    public void  setSBrokerIcon(String sBrokerIcon)
    {
        this.sBrokerIcon = sBrokerIcon;
    }

    public String getSBrokerUrl()
    {
        return sBrokerUrl;
    }

    public void  setSBrokerUrl(String sBrokerUrl)
    {
        this.sBrokerUrl = sBrokerUrl;
    }

    public java.util.ArrayList<String> getVTag()
    {
        return vTag;
    }

    public void  setVTag(java.util.ArrayList<String> vTag)
    {
        this.vTag = vTag;
    }

    public IntelliBroker()
    {
    }

    public IntelliBroker(String sBrokerName, String sBrokerIcon, String sBrokerUrl, java.util.ArrayList<String> vTag)
    {
        this.sBrokerName = sBrokerName;
        this.sBrokerIcon = sBrokerIcon;
        this.sBrokerUrl = sBrokerUrl;
        this.vTag = vTag;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sBrokerName) {
            ostream.writeString(0, sBrokerName);
        }
        if (null != sBrokerIcon) {
            ostream.writeString(1, sBrokerIcon);
        }
        if (null != sBrokerUrl) {
            ostream.writeString(2, sBrokerUrl);
        }
        if (null != vTag) {
            ostream.writeList(3, vTag);
        }
    }

    static java.util.ArrayList<String> VAR_TYPE_4_VTAG = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VTAG.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sBrokerName = (String)istream.readString(0, false, this.sBrokerName);
        this.sBrokerIcon = (String)istream.readString(1, false, this.sBrokerIcon);
        this.sBrokerUrl = (String)istream.readString(2, false, this.sBrokerUrl);
        this.vTag = (java.util.ArrayList<String>)istream.readList(3, false, VAR_TYPE_4_VTAG);
    }

}

