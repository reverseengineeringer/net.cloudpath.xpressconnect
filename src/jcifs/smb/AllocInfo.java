package jcifs.smb;

abstract interface AllocInfo
{
  public abstract long getCapacity();

  public abstract long getFree();
}