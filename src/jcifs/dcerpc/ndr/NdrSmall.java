package jcifs.dcerpc.ndr;

public class NdrSmall extends NdrObject
{
  public int value;

  public NdrSmall(int paramInt)
  {
    this.value = (paramInt & 0xFF);
  }

  public void decode(NdrBuffer paramNdrBuffer)
    throws NdrException
  {
    this.value = paramNdrBuffer.dec_ndr_small();
  }

  public void encode(NdrBuffer paramNdrBuffer)
    throws NdrException
  {
    paramNdrBuffer.enc_ndr_small(this.value);
  }
}