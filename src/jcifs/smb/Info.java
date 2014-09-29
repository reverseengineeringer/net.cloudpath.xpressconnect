package jcifs.smb;

abstract interface Info
{
  public abstract int getAttributes();

  public abstract long getCreateTime();

  public abstract long getLastWriteTime();

  public abstract long getSize();
}