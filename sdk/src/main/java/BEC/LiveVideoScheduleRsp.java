package BEC;

public final class LiveVideoScheduleRsp extends com.dengtacj.thoth.Message implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.Map<String, java.util.ArrayList<BEC.VideoSchedule>> mvVideoSchedule = null;

    public java.util.Map<String, java.util.ArrayList<BEC.VideoSchedule>> getMvVideoSchedule()
    {
        return mvVideoSchedule;
    }

    public void  setMvVideoSchedule(java.util.Map<String, java.util.ArrayList<BEC.VideoSchedule>> mvVideoSchedule)
    {
        this.mvVideoSchedule = mvVideoSchedule;
    }

    public LiveVideoScheduleRsp()
    {
    }

    public LiveVideoScheduleRsp(java.util.Map<String, java.util.ArrayList<BEC.VideoSchedule>> mvVideoSchedule)
    {
        this.mvVideoSchedule = mvVideoSchedule;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != mvVideoSchedule) {
            ostream.writeMap(0, mvVideoSchedule);
        }
    }

    static java.util.Map<String, java.util.ArrayList<BEC.VideoSchedule>> VAR_TYPE_4_MVVIDEOSCHEDULE = new java.util.HashMap<String, java.util.ArrayList<BEC.VideoSchedule>>();
    static {
        java.util.ArrayList<BEC.VideoSchedule> VAR_TYPE_4_MVVIDEOSCHEDULE_V_C = new java.util.ArrayList<BEC.VideoSchedule>();
        VAR_TYPE_4_MVVIDEOSCHEDULE_V_C.add(new BEC.VideoSchedule());
        VAR_TYPE_4_MVVIDEOSCHEDULE.put("", VAR_TYPE_4_MVVIDEOSCHEDULE_V_C);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.mvVideoSchedule = (java.util.Map<String, java.util.ArrayList<BEC.VideoSchedule>>)istream.readMap(0, false, VAR_TYPE_4_MVVIDEOSCHEDULE);
    }

}

