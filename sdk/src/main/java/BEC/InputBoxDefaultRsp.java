package BEC;

public final class InputBoxDefaultRsp extends com.dengtacj.thoth.Message implements Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    public java.util.ArrayList<BEC.InputDefaultItem> vtInputDefaultItem = null;

    public java.util.ArrayList<BEC.InputDefaultItem> getVtInputDefaultItem()
    {
        return vtInputDefaultItem;
    }

    public void  setVtInputDefaultItem(java.util.ArrayList<BEC.InputDefaultItem> vtInputDefaultItem)
    {
        this.vtInputDefaultItem = vtInputDefaultItem;
    }

    public InputBoxDefaultRsp()
    {
    }

    public InputBoxDefaultRsp(java.util.ArrayList<BEC.InputDefaultItem> vtInputDefaultItem)
    {
        this.vtInputDefaultItem = vtInputDefaultItem;
    }

    public void write(com.dengtacj.thoth.BaseEncodeStream eos)
    {
        com.dengtacj.thoth.BaseEncodeStream ostream = new com.dengtacj.thoth.BaseEncodeStream(eos);
        ostream.setCharset(eos.getCharset());

        if (null != vtInputDefaultItem) {
            ostream.writeList(0, vtInputDefaultItem);
        }
    }

    static java.util.ArrayList<BEC.InputDefaultItem> VAR_TYPE_4_VTINPUTDEFAULTITEM = new java.util.ArrayList<BEC.InputDefaultItem>();
    static {
        VAR_TYPE_4_VTINPUTDEFAULTITEM.add(new BEC.InputDefaultItem());
    }


    public void read(com.dengtacj.thoth.BaseDecodeStream dos)
    {
        com.dengtacj.thoth.BaseDecodeStream istream = new com.dengtacj.thoth.BaseDecodeStream(dos);
        istream.setCharset(dos.getCharset());

        this.vtInputDefaultItem = (java.util.ArrayList<BEC.InputDefaultItem>)istream.readList(0, false, VAR_TYPE_4_VTINPUTDEFAULTITEM);
    }

}

