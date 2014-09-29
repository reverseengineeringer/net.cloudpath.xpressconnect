package jcifs.smb;

public abstract interface SmbFileFilter
{
  public abstract boolean accept(SmbFile paramSmbFile)
    throws SmbException;
}