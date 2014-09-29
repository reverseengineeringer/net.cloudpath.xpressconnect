package jcifs.dcerpc.ndr;

public class NdrShort extends NdrObject
{
  public int value;

  public NdrShort(int paramInt)
  {
    this.value = (paramInt & 0xFF);
  }

  public void decode(NdrBuffer paramNdrBuffer)
    throws NdrException
  {
    this.value = paramNdrBuffer.dec_ndr_short();
  }

  public void encode(NdrBuffer paramNdrBuffer)
    throws NdrException
  {
    paramNdrBuffer.enc_ndr_short(this.value);
  }
}