package jcifs.smb;

public abstract interface SmbFilenameFilter
{
  public abstract boolean accept(SmbFile paramSmbFile, String paramString)
    throws SmbException;
}