package BEC;

public final class SearchCondition extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public int eConditionType = 0;

    public String sTopValue = "";

    public String sBottomValue = "";

    public java.util.ArrayList<String> vValues = null;

    public int getEConditionType()
    {
        return eConditionType;
    }

    public void  setEConditionType(int eConditionType)
    {
        this.eConditionType = eConditionType;
    }

    public String getSTopValue()
    {
        return sTopValue;
    }

    public void  setSTopValue(String sTopValue)
    {
        this.sTopValue = sTopValue;
    }

    public String getSBottomValue()
    {
        return sBottomValue;
    }

    public void  setSBottomValue(String sBottomValue)
    {
        this.sBottomValue = sBottomValue;
    }

    public java.util.ArrayList<String> getVValues()
    {
        return vValues;
    }

    public void  setVValues(java.util.ArrayList<String> vValues)
    {
        this.vValues = vValues;
    }

    public SearchCondition()
    {
    }

    public SearchCondition(int eConditionType, String sTopValue, String sBottomValue, java.util.ArrayList<String> vValues)
    {
        this.eConditionType = eConditionType;
        this.sTopValue = sTopValue;
        this.sBottomValue = sBottomValue;
        this.vValues = vValues;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        ostream.writeInt32(0, eConditionType);
        if (null != sTopValue) {
            ostream.writeString(1, sTopValue);
        }
        if (null != sBottomValue) {
            ostream.writeString(2, sBottomValue);
        }
        if (null != vValues) {
            ostream.writeList(3, vValues);
        }
    }

    static java.util.ArrayList<String> VAR_TYPE_4_VVALUES = new java.util.ArrayList<String>();
    static {
        VAR_TYPE_4_VVALUES.add("");
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.eConditionType = (int)istream.readInt32(0, false, this.eConditionType);
        this.sTopValue = (String)istream.readString(1, false, this.sTopValue);
        this.sBottomValue = (String)istream.readString(2, false, this.sBottomValue);
        this.vValues = (java.util.ArrayList<String>)istream.readList(3, false, VAR_TYPE_4_VVALUES);
    }

}

