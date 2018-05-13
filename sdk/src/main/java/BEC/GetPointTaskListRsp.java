package BEC;

public final class GetPointTaskListRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<AccuPointTaskItem> vTask = null;

    public java.util.ArrayList<Integer> vFinished = null;

    public java.util.ArrayList<AccuPointTaskItem> getVTask()
    {
        return vTask;
    }

    public void  setVTask(java.util.ArrayList<AccuPointTaskItem> vTask)
    {
        this.vTask = vTask;
    }

    public java.util.ArrayList<Integer> getVFinished()
    {
        return vFinished;
    }

    public void  setVFinished(java.util.ArrayList<Integer> vFinished)
    {
        this.vFinished = vFinished;
    }

    public GetPointTaskListRsp()
    {
    }

    public GetPointTaskListRsp(java.util.ArrayList<AccuPointTaskItem> vTask, java.util.ArrayList<Integer> vFinished)
    {
        this.vTask = vTask;
        this.vFinished = vFinished;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vTask) {
            ostream.writeList(0, vTask);
        }
        if (null != vFinished) {
            ostream.writeList(1, vFinished);
        }
    }

    static java.util.ArrayList<AccuPointTaskItem> VAR_TYPE_4_VTASK = new java.util.ArrayList<AccuPointTaskItem>();
    static {
        VAR_TYPE_4_VTASK.add(new AccuPointTaskItem());
    }

    static java.util.ArrayList<Integer> VAR_TYPE_4_VFINISHED = new java.util.ArrayList<Integer>();
    static {
        VAR_TYPE_4_VFINISHED.add(0);
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vTask = (java.util.ArrayList<AccuPointTaskItem>)istream.readList(0, false, VAR_TYPE_4_VTASK);
        this.vFinished = (java.util.ArrayList<Integer>)istream.readList(1, false, VAR_TYPE_4_VFINISHED);
    }

}

