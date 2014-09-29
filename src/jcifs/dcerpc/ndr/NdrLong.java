package jcifs.dcerpc.ndr;

public class NdrLong extends NdrObject
{
  public int value;

  public NdrLong(int paramInt)
  {
    this.value = paramInt;
  }

  public void decode(NdrBuffer paramNdrBuffer)
    throws NdrException
  {
    this.value = paramNdrBuffer.dec_ndr_long();
  }

  public void encode(NdrBuffer paramNdrBuffer)
    throws NdrException
  {
    paramNdrBuffer.enc_ndr_long(this.value);
  }
}