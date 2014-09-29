package jcifs.dcerpc;

import jcifs.dcerpc.ndr.NdrBuffer;
import jcifs.dcerpc.ndr.NdrException;
import jcifs.util.Hexdump;

public class DcerpcBind extends DcerpcMessage
{
  static final String[] result_message = { "0", "DCERPC_BIND_ERR_ABSTRACT_SYNTAX_NOT_SUPPORTED", "DCERPC_BIND_ERR_PROPOSED_TRANSFER_SYNTAXES_NOT_SUPPORTED", "DCERPC_BIND_ERR_LOCAL_LIMIT_EXCEEDED" };
  DcerpcBinding binding;
  int max_recv;
  int max_xmit;

  DcerpcBind(DcerpcBinding paramDcerpcBinding, DcerpcHandle paramDcerpcHandle)
  {
    this.binding = paramDcerpcBinding;
    this.max_xmit = paramDcerpcHandle.max_xmit;
    this.max_recv = paramDcerpcHandle.max_recv;
    this.ptype = 11;
    this.flags = 3;
  }

  static String getResultMessage(int paramInt)
  {
    if (paramInt < 4)
      return result_message[paramInt];
    return "0x" + Hexdump.toHexString(paramInt, 4);
  }

  public void decode_out(NdrBuffer paramNdrBuffer)
    throws NdrException
  {
    paramNdrBuffer.dec_ndr_short();
    paramNdrBuffer.dec_ndr_short();
    paramNdrBuffer.dec_ndr_long();
    paramNdrBuffer.advance(paramNdrBuffer.dec_ndr_short());
    paramNdrBuffer.align(4);
    paramNdrBuffer.dec_ndr_small();
    paramNdrBuffer.align(4);
    this.result = paramNdrBuffer.dec_ndr_short();
    paramNdrBuffer.dec_ndr_short();
    paramNdrBuffer.advance(20);
  }

  public void encode_in(NdrBuffer paramNdrBuffer)
    throws NdrException
  {
    paramNdrBuffer.enc_ndr_short(this.max_xmit);
    paramNdrBuffer.enc_ndr_short(this.max_recv);
    paramNdrBuffer.enc_ndr_long(0);
    paramNdrBuffer.enc_ndr_small(1);
    paramNdrBuffer.enc_ndr_small(0);
    paramNdrBuffer.enc_ndr_short(0);
    paramNdrBuffer.enc_ndr_short(0);
    paramNdrBuffer.enc_ndr_small(1);
    paramNdrBuffer.enc_ndr_small(0);
    this.binding.uuid.encode(paramNdrBuffer);
    paramNdrBuffer.enc_ndr_short(this.binding.major);
    paramNdrBuffer.enc_ndr_short(this.binding.minor);
    DcerpcConstants.DCERPC_UUID_SYNTAX_NDR.encode(paramNdrBuffer);
    paramNdrBuffer.enc_ndr_long(2);
  }

  public int getOpnum()
  {
    return 0;
  }

  public DcerpcException getResult()
  {
    if (this.result != 0)
      return new DcerpcException(getResultMessage(this.result));
    return null;
  }
}