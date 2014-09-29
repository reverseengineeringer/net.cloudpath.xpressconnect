package jcifs.dcerpc;

import jcifs.dcerpc.ndr.NdrBuffer;
import jcifs.dcerpc.ndr.NdrException;
import jcifs.dcerpc.ndr.NdrObject;

public class rpc
{
  public static class policy_handle extends NdrObject
  {
    public int type;
    public rpc.uuid_t uuid;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      this.type = paramNdrBuffer.dec_ndr_long();
      paramNdrBuffer.align(4);
      if (this.uuid == null)
        this.uuid = new rpc.uuid_t();
      this.uuid.time_low = paramNdrBuffer.dec_ndr_long();
      this.uuid.time_mid = ((short)paramNdrBuffer.dec_ndr_short());
      this.uuid.time_hi_and_version = ((short)paramNdrBuffer.dec_ndr_short());
      this.uuid.clock_seq_hi_and_reserved = ((byte)paramNdrBuffer.dec_ndr_small());
      this.uuid.clock_seq_low = ((byte)paramNdrBuffer.dec_ndr_small());
      int i = paramNdrBuffer.index;
      paramNdrBuffer.advance(6);
      if (this.uuid.node == null)
      {
        if ((6 < 0) || (6 > 65535))
          throw new NdrException("invalid array conformance");
        this.uuid.node = new byte[6];
      }
      NdrBuffer localNdrBuffer = paramNdrBuffer.derive(i);
      for (int j = 0; j < 6; j++)
        this.uuid.node[j] = ((byte)localNdrBuffer.dec_ndr_small());
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_long(this.type);
      paramNdrBuffer.enc_ndr_long(this.uuid.time_low);
      paramNdrBuffer.enc_ndr_short(this.uuid.time_mid);
      paramNdrBuffer.enc_ndr_short(this.uuid.time_hi_and_version);
      paramNdrBuffer.enc_ndr_small(this.uuid.clock_seq_hi_and_reserved);
      paramNdrBuffer.enc_ndr_small(this.uuid.clock_seq_low);
      int i = paramNdrBuffer.index;
      paramNdrBuffer.advance(6);
      NdrBuffer localNdrBuffer = paramNdrBuffer.derive(i);
      for (int j = 0; j < 6; j++)
        localNdrBuffer.enc_ndr_small(this.uuid.node[j]);
    }
  }

  public static class sid_t extends NdrObject
  {
    public byte[] identifier_authority;
    public byte revision;
    public int[] sub_authority;
    public byte sub_authority_count;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      int i = paramNdrBuffer.dec_ndr_long();
      this.revision = ((byte)paramNdrBuffer.dec_ndr_small());
      this.sub_authority_count = ((byte)paramNdrBuffer.dec_ndr_small());
      int j = paramNdrBuffer.index;
      paramNdrBuffer.advance(6);
      int k = paramNdrBuffer.index;
      paramNdrBuffer.advance(i * 4);
      if (this.identifier_authority == null)
      {
        if ((6 < 0) || (6 > 65535))
          throw new NdrException("invalid array conformance");
        this.identifier_authority = new byte[6];
      }
      NdrBuffer localNdrBuffer1 = paramNdrBuffer.derive(j);
      for (int m = 0; m < 6; m++)
        this.identifier_authority[m] = ((byte)localNdrBuffer1.dec_ndr_small());
      if (this.sub_authority == null)
      {
        if ((i < 0) || (i > 65535))
          throw new NdrException("invalid array conformance");
        this.sub_authority = new int[i];
      }
      NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(k);
      for (int n = 0; n < i; n++)
        this.sub_authority[n] = localNdrBuffer2.dec_ndr_long();
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      int i = this.sub_authority_count;
      paramNdrBuffer.enc_ndr_long(i);
      paramNdrBuffer.enc_ndr_small(this.revision);
      paramNdrBuffer.enc_ndr_small(this.sub_authority_count);
      int j = paramNdrBuffer.index;
      paramNdrBuffer.advance(6);
      int k = paramNdrBuffer.index;
      paramNdrBuffer.advance(i * 4);
      NdrBuffer localNdrBuffer1 = paramNdrBuffer.derive(j);
      for (int m = 0; m < 6; m++)
        localNdrBuffer1.enc_ndr_small(this.identifier_authority[m]);
      NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(k);
      for (int n = 0; n < i; n++)
        localNdrBuffer2.enc_ndr_long(this.sub_authority[n]);
    }
  }

  public static class unicode_string extends NdrObject
  {
    public short[] buffer;
    public short length;
    public short maximum_length;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      this.length = ((short)paramNdrBuffer.dec_ndr_short());
      this.maximum_length = ((short)paramNdrBuffer.dec_ndr_short());
      if (paramNdrBuffer.dec_ndr_long() != 0)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        int i = localNdrBuffer1.dec_ndr_long();
        localNdrBuffer1.dec_ndr_long();
        int j = localNdrBuffer1.dec_ndr_long();
        int k = localNdrBuffer1.index;
        localNdrBuffer1.advance(j * 2);
        if (this.buffer == null)
        {
          if ((i < 0) || (i > 65535))
            throw new NdrException("invalid array conformance");
          this.buffer = new short[i];
        }
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(k);
        for (int m = 0; m < j; m++)
          this.buffer[m] = ((short)localNdrBuffer2.dec_ndr_short());
      }
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_short(this.length);
      paramNdrBuffer.enc_ndr_short(this.maximum_length);
      paramNdrBuffer.enc_ndr_referent(this.buffer, 1);
      if (this.buffer != null)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        int i = this.length / 2;
        localNdrBuffer1.enc_ndr_long(this.maximum_length / 2);
        localNdrBuffer1.enc_ndr_long(0);
        localNdrBuffer1.enc_ndr_long(i);
        int j = localNdrBuffer1.index;
        localNdrBuffer1.advance(i * 2);
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
          localNdrBuffer2.enc_ndr_short(this.buffer[k]);
      }
    }
  }

  public static class uuid_t extends NdrObject
  {
    public byte clock_seq_hi_and_reserved;
    public byte clock_seq_low;
    public byte[] node;
    public short time_hi_and_version;
    public int time_low;
    public short time_mid;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      this.time_low = paramNdrBuffer.dec_ndr_long();
      this.time_mid = ((short)paramNdrBuffer.dec_ndr_short());
      this.time_hi_and_version = ((short)paramNdrBuffer.dec_ndr_short());
      this.clock_seq_hi_and_reserved = ((byte)paramNdrBuffer.dec_ndr_small());
      this.clock_seq_low = ((byte)paramNdrBuffer.dec_ndr_small());
      int i = paramNdrBuffer.index;
      paramNdrBuffer.advance(6);
      if (this.node == null)
      {
        if ((6 < 0) || (6 > 65535))
          throw new NdrException("invalid array conformance");
        this.node = new byte[6];
      }
      NdrBuffer localNdrBuffer = paramNdrBuffer.derive(i);
      for (int j = 0; j < 6; j++)
        this.node[j] = ((byte)localNdrBuffer.dec_ndr_small());
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_long(this.time_low);
      paramNdrBuffer.enc_ndr_short(this.time_mid);
      paramNdrBuffer.enc_ndr_short(this.time_hi_and_version);
      paramNdrBuffer.enc_ndr_small(this.clock_seq_hi_and_reserved);
      paramNdrBuffer.enc_ndr_small(this.clock_seq_low);
      int i = paramNdrBuffer.index;
      paramNdrBuffer.advance(6);
      NdrBuffer localNdrBuffer = paramNdrBuffer.derive(i);
      for (int j = 0; j < 6; j++)
        localNdrBuffer.enc_ndr_small(this.node[j]);
    }
  }
}