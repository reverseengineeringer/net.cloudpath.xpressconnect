package jcifs.dcerpc.ndr;

public class NdrHyper extends NdrObject
{
  public long value;

  public NdrHyper(long paramLong)
  {
    this.value = paramLong;
  }

  public void decode(NdrBuffer paramNdrBuffer)
    throws NdrException
  {
    this.value = paramNdrBuffer.dec_ndr_hyper();
  }

  public void encode(NdrBuffer paramNdrBuffer)
    throws NdrException
  {
    paramNdrBuffer.enc_ndr_hyper(this.value);
  }
}