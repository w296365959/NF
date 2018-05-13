package BEC;

public final class VodSchedule extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<String, java.util.Map<Integer, BEC.VideoSchedule>> mmVideoSchedule = null;

    public java.util.Map<String, java.util.Map<Integer, BEC.VideoSchedule>> getMmVideoSchedule()
    {
        return mmVideoSchedule;
    }

    public void  setMmVideoSchedule(java.util.Map<String, java.util.Map<Integer, BEC.VideoSchedule>> mmVideoSchedule)
    {
        this.mmVideoSchedule = mmVideoSchedule;
    }

    public VodSchedule()
    {
    }

    public VodSchedule(java.util.Map<String, java.util.Map<Integer, BEC.VideoSchedule>> mmVideoSchedule)
    {
        this.mmVideoSchedule = mmVideoSchedule;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != mmVideoSchedule) {
            ostream.writeMap(0, mmVideoSchedule);
        }
    }

    static java.util.Map<String, java.util.Map<Integer, BEC.VideoSchedule>> VAR_TYPE_4_MMVIDEOSCHEDULE = new java.util.HashMap<String, java.util.Map<Integer, BEC.VideoSchedule>>();
    static {
        java.util.Map<Integer, BEC.VideoSchedule> VAR_TYPE_4_MMVIDEOSCHEDULE_V_C = new java.util.HashMap<Integer, BEC.VideoSchedule>();
        VAR_TYPE_4_MMVIDEOSCHEDULE_V_C.put(0, new BEC.VideoSchedule());
        VAR_TYPE_4_MMVIDEOSCHEDULE.put("", VAR_TYPE_4_MMVIDEOSCHEDULE_V_C);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.mmVideoSchedule = (java.util.Map<String, java.util.Map<Integer, BEC.VideoSchedule>>)istream.readMap(0, false, VAR_TYPE_4_MMVIDEOSCHEDULE);
    }

}

