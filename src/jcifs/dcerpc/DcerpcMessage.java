package jcifs.dcerpc;

import jcifs.dcerpc.ndr.NdrBuffer;
import jcifs.dcerpc.ndr.NdrException;
import jcifs.dcerpc.ndr.NdrObject;

public abstract class DcerpcMessage extends NdrObject
  implements DcerpcConstants
{
  protected int alloc_hint = 0;
  protected int call_id = 0;
  protected int flags = 0;
  protected int length = 0;
  protected int ptype = -1;
  protected int result = 0;

  public void decode(NdrBuffer paramNdrBuffer)
    throws NdrException
  {
    decode_header(paramNdrBuffer);
    if ((this.ptype != 12) && (this.ptype != 2) && (this.ptype != 3))
      throw new NdrException("Unexpected ptype: " + this.ptype);
    if ((this.ptype == 2) || (this.ptype == 3))
    {
      this.alloc_hint = paramNdrBuffer.dec_ndr_long();
      paramNdrBuffer.dec_ndr_short();
      paramNdrBuffer.dec_ndr_short();
    }
    if (this.ptype == 3)
    {
      this.result = paramNdrBuffer.dec_ndr_long();
      return;
    }
    decode_out(paramNdrBuffer);
  }

  void decode_header(NdrBuffer paramNdrBuffer)
    throws NdrException
  {
    paramNdrBuffer.dec_ndr_small();
    paramNdrBuffer.dec_ndr_small();
    this.ptype = paramNdrBuffer.dec_ndr_small();
    this.flags = paramNdrBuffer.dec_ndr_small();
    if (paramNdrBuffer.dec_ndr_long() != 16)
      throw new NdrException("Data representation not supported");
    this.length = paramNdrBuffer.dec_ndr_short();
    if (paramNdrBuffer.dec_ndr_short() != 0)
      throw new NdrException("DCERPC authentication not supported");
    this.call_id = paramNdrBuffer.dec_ndr_long();
  }

  public abstract void decode_out(NdrBuffer paramNdrBuffer)
    throws NdrException;

  public void encode(NdrBuffer paramNdrBuffer)
    throws NdrException
  {
    int i = paramNdrBuffer.getIndex();
    paramNdrBuffer.advance(16);
    int j = this.ptype;
    int k = 0;
    if (j == 0)
    {
      k = paramNdrBuffer.getIndex();
      paramNdrBuffer.enc_ndr_long(0);
      paramNdrBuffer.enc_ndr_short(0);
      paramNdrBuffer.enc_ndr_short(getOpnum());
    }
    encode_in(paramNdrBuffer);
    this.length = (paramNdrBuffer.getIndex() - i);
    if (this.ptype == 0)
    {
      paramNdrBuffer.setIndex(k);
      this.alloc_hint = (this.length - k);
      paramNdrBuffer.enc_ndr_long(this.alloc_hint);
    }
    paramNdrBuffer.setIndex(i);
    encode_header(paramNdrBuffer);
    paramNdrBuffer.setIndex(i + this.length);
  }

  void encode_header(NdrBuffer paramNdrBuffer)
  {
    paramNdrBuffer.enc_ndr_small(5);
    paramNdrBuffer.enc_ndr_small(0);
    paramNdrBuffer.enc_ndr_small(this.ptype);
    paramNdrBuffer.enc_ndr_small(this.flags);
    paramNdrBuffer.enc_ndr_long(16);
    paramNdrBuffer.enc_ndr_short(this.length);
    paramNdrBuffer.enc_ndr_short(0);
    paramNdrBuffer.enc_ndr_long(this.call_id);
  }

  public abstract void encode_in(NdrBuffer paramNdrBuffer)
    throws NdrException;

  public abstract int getOpnum();

  public DcerpcException getResult()
  {
    if (this.result != 0)
      return new DcerpcException(this.result);
    return null;
  }

  public boolean isFlagSet(int paramInt)
  {
    return (paramInt & this.flags) == paramInt;
  }

  public void setFlag(int paramInt)
  {
    this.flags = (paramInt | this.flags);
  }

  public void unsetFlag(int paramInt)
  {
    this.flags = (paramInt | this.flags);
  }
}