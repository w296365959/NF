package org.vudroid.pdfdroid.codec;


public class PdfDocument {
    private long docHandle;
    private static final int FITZMEMORY = 512 * 1024;

    private PdfDocument(long docHandle) {
        this.docHandle = docHandle;
    }

    public PdfPage getPage(int pageNumber) throws Exception {
        return PdfPage.createPage(docHandle, pageNumber + 1);
    }

    public int getPageCount() {
        return getPageCount(docHandle);
    }

    public static PdfDocument openDocument(String fname, String pwd) {
        return new PdfDocument(open(FITZMEMORY, fname, pwd));
    }

    private static native long open(int fitzmemory, String fname, String pwd);

    private static native void free(long handle);

    private static native int getPageCount(long handle);

    @Override
    protected void finalize() throws Throwable {
        recycle();
        super.finalize();
    }

    public synchronized void recycle() {
        if (docHandle != 0) {
            free(docHandle);
            docHandle = 0;
        }
    }
}
