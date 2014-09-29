package jcifs.dcerpc.ndr;

public abstract class NdrObject
{
  public abstract void decode(NdrBuffer paramNdrBuffer)
    throws NdrException;

  public abstract void encode(NdrBuffer paramNdrBuffer)
    throws NdrException;
}