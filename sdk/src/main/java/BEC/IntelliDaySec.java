package BEC;

public final class IntelliDaySec extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public String sOptime = "";

    public java.util.ArrayList<IntelliSec> vtSec = null;

    public String getSOptime()
    {
        return sOptime;
    }

    public void  setSOptime(String sOptime)
    {
        this.sOptime = sOptime;
    }

    public java.util.ArrayList<IntelliSec> getVtSec()
    {
        return vtSec;
    }

    public void  setVtSec(java.util.ArrayList<IntelliSec> vtSec)
    {
        this.vtSec = vtSec;
    }

    public IntelliDaySec()
    {
    }

    public IntelliDaySec(String sOptime, java.util.ArrayList<IntelliSec> vtSec)
    {
        this.sOptime = sOptime;
        this.vtSec = vtSec;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != sOptime) {
            ostream.writeString(0, sOptime);
        }
        if (null != vtSec) {
            ostream.writeList(1, vtSec);
        }
    }

    static java.util.ArrayList<IntelliSec> VAR_TYPE_4_VTSEC = new java.util.ArrayList<IntelliSec>();
    static {
        VAR_TYPE_4_VTSEC.add(new IntelliSec());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.sOptime = (String)istream.readString(0, false, this.sOptime);
        this.vtSec = (java.util.ArrayList<IntelliSec>)istream.readList(1, false, VAR_TYPE_4_VTSEC);
    }

}

