package BEC;

public final class ChargePointInfo extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int iChargePoint = 0;

    public int iSwitch = 0;

    public int getIChargePoint()
    {
        return iChargePoint;
    }

    public void  setIChargePoint(int iChargePoint)
    {
        this.iChargePoint = iChargePoint;
    }

    public int getISwitch()
    {
        return iSwitch;
    }

    public void  setISwitch(int iSwitch)
    {
        this.iSwitch = iSwitch;
    }

    public ChargePointInfo()
    {
    }

    public ChargePointInfo(int iChargePoint, int iSwitch)
    {
        this.iChargePoint = iChargePoint;
        this.iSwitch = iSwitch;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, iChargePoint);
        ostream.writeInt32(1, iSwitch);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.iChargePoint = (int)istream.readInt32(0, false, this.iChargePoint);
        this.iSwitch = (int)istream.readInt32(1, false, this.iSwitch);
    }

}

