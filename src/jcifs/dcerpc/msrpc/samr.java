package jcifs.dcerpc.msrpc;

import jcifs.dcerpc.DcerpcMessage;
import jcifs.dcerpc.ndr.NdrBuffer;
import jcifs.dcerpc.ndr.NdrException;
import jcifs.dcerpc.ndr.NdrObject;
import jcifs.dcerpc.rpc.policy_handle;
import jcifs.dcerpc.rpc.sid_t;
import jcifs.dcerpc.rpc.unicode_string;

public class samr
{
  public static String getSyntax()
  {
    return "12345778-1234-abcd-ef00-0123456789ac:1.0";
  }

  public static class SamrCloseHandle extends DcerpcMessage
  {
    public rpc.policy_handle handle;
    public int retval;

    public SamrCloseHandle(rpc.policy_handle parampolicy_handle)
    {
      this.handle = parampolicy_handle;
    }

    public void decode_out(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      this.retval = paramNdrBuffer.dec_ndr_long();
    }

    public void encode_in(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      this.handle.encode(paramNdrBuffer);
    }

    public int getOpnum()
    {
      return 1;
    }
  }

  public static class SamrConnect2 extends DcerpcMessage
  {
    public int access_mask;
    public rpc.policy_handle handle;
    public int retval;
    public String system_name;

    public SamrConnect2(String paramString, int paramInt, rpc.policy_handle parampolicy_handle)
    {
      this.system_name = paramString;
      this.access_mask = paramInt;
      this.handle = parampolicy_handle;
    }

    public void decode_out(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      this.handle.decode(paramNdrBuffer);
      this.retval = paramNdrBuffer.dec_ndr_long();
    }

    public void encode_in(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.enc_ndr_referent(this.system_name, 1);
      if (this.system_name != null)
        paramNdrBuffer.enc_ndr_string(this.system_name);
      paramNdrBuffer.enc_ndr_long(this.access_mask);
    }

    public int getOpnum()
    {
      return 57;
    }
  }

  public static class SamrConnect4 extends DcerpcMessage
  {
    public int access_mask;
    public rpc.policy_handle handle;
    public int retval;
    public String system_name;
    public int unknown;

    public SamrConnect4(String paramString, int paramInt1, int paramInt2, rpc.policy_handle parampolicy_handle)
    {
      this.system_name = paramString;
      this.unknown = paramInt1;
      this.access_mask = paramInt2;
      this.handle = parampolicy_handle;
    }

    public void decode_out(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      this.handle.decode(paramNdrBuffer);
      this.retval = paramNdrBuffer.dec_ndr_long();
    }

    public void encode_in(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.enc_ndr_referent(this.system_name, 1);
      if (this.system_name != null)
        paramNdrBuffer.enc_ndr_string(this.system_name);
      paramNdrBuffer.enc_ndr_long(this.unknown);
      paramNdrBuffer.enc_ndr_long(this.access_mask);
    }

    public int getOpnum()
    {
      return 62;
    }
  }

  public static class SamrEnumerateAliasesInDomain extends DcerpcMessage
  {
    public int acct_flags;
    public rpc.policy_handle domain_handle;
    public int num_entries;
    public int resume_handle;
    public int retval;
    public samr.SamrSamArray sam;

    public SamrEnumerateAliasesInDomain(rpc.policy_handle parampolicy_handle, int paramInt1, int paramInt2, samr.SamrSamArray paramSamrSamArray, int paramInt3)
    {
      this.domain_handle = parampolicy_handle;
      this.resume_handle = paramInt1;
      this.acct_flags = paramInt2;
      this.sam = paramSamrSamArray;
      this.num_entries = paramInt3;
    }

    public void decode_out(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      this.resume_handle = paramNdrBuffer.dec_ndr_long();
      if (paramNdrBuffer.dec_ndr_long() != 0)
      {
        if (this.sam == null)
          this.sam = new samr.SamrSamArray();
        this.sam.decode(paramNdrBuffer);
      }
      this.num_entries = paramNdrBuffer.dec_ndr_long();
      this.retval = paramNdrBuffer.dec_ndr_long();
    }

    public void encode_in(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      this.domain_handle.encode(paramNdrBuffer);
      paramNdrBuffer.enc_ndr_long(this.resume_handle);
      paramNdrBuffer.enc_ndr_long(this.acct_flags);
    }

    public int getOpnum()
    {
      return 15;
    }
  }

  public static class SamrGetMembersInAlias extends DcerpcMessage
  {
    public rpc.policy_handle alias_handle;
    public int retval;
    public lsarpc.LsarSidArray sids;

    public SamrGetMembersInAlias(rpc.policy_handle parampolicy_handle, lsarpc.LsarSidArray paramLsarSidArray)
    {
      this.alias_handle = parampolicy_handle;
      this.sids = paramLsarSidArray;
    }

    public void decode_out(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      this.sids.decode(paramNdrBuffer);
      this.retval = paramNdrBuffer.dec_ndr_long();
    }

    public void encode_in(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      this.alias_handle.encode(paramNdrBuffer);
    }

    public int getOpnum()
    {
      return 33;
    }
  }

  public static class SamrOpenAlias extends DcerpcMessage
  {
    public int access_mask;
    public rpc.policy_handle alias_handle;
    public rpc.policy_handle domain_handle;
    public int retval;
    public int rid;

    public SamrOpenAlias(rpc.policy_handle parampolicy_handle1, int paramInt1, int paramInt2, rpc.policy_handle parampolicy_handle2)
    {
      this.domain_handle = parampolicy_handle1;
      this.access_mask = paramInt1;
      this.rid = paramInt2;
      this.alias_handle = parampolicy_handle2;
    }

    public void decode_out(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      this.alias_handle.decode(paramNdrBuffer);
      this.retval = paramNdrBuffer.dec_ndr_long();
    }

    public void encode_in(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      this.domain_handle.encode(paramNdrBuffer);
      paramNdrBuffer.enc_ndr_long(this.access_mask);
      paramNdrBuffer.enc_ndr_long(this.rid);
    }

    public int getOpnum()
    {
      return 27;
    }
  }

  public static class SamrOpenDomain extends DcerpcMessage
  {
    public int access_mask;
    public rpc.policy_handle domain_handle;
    public rpc.policy_handle handle;
    public int retval;
    public rpc.sid_t sid;

    public SamrOpenDomain(rpc.policy_handle parampolicy_handle1, int paramInt, rpc.sid_t paramsid_t, rpc.policy_handle parampolicy_handle2)
    {
      this.handle = parampolicy_handle1;
      this.access_mask = paramInt;
      this.sid = paramsid_t;
      this.domain_handle = parampolicy_handle2;
    }

    public void decode_out(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      this.domain_handle.decode(paramNdrBuffer);
      this.retval = paramNdrBuffer.dec_ndr_long();
    }

    public void encode_in(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      this.handle.encode(paramNdrBuffer);
      paramNdrBuffer.enc_ndr_long(this.access_mask);
      this.sid.encode(paramNdrBuffer);
    }

    public int getOpnum()
    {
      return 7;
    }
  }

  public static class SamrSamArray extends NdrObject
  {
    public int count;
    public samr.SamrSamEntry[] entries;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      this.count = paramNdrBuffer.dec_ndr_long();
      if (paramNdrBuffer.dec_ndr_long() != 0)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        int i = localNdrBuffer1.dec_ndr_long();
        int j = localNdrBuffer1.index;
        localNdrBuffer1.advance(i * 12);
        if (this.entries == null)
        {
          if ((i < 0) || (i > 65535))
            throw new NdrException("invalid array conformance");
          this.entries = new samr.SamrSamEntry[i];
        }
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
        {
          if (this.entries[k] == null)
            this.entries[k] = new samr.SamrSamEntry();
          this.entries[k].decode(localNdrBuffer2);
        }
      }
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_long(this.count);
      paramNdrBuffer.enc_ndr_referent(this.entries, 1);
      if (this.entries != null)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        int i = this.count;
        localNdrBuffer1.enc_ndr_long(i);
        int j = localNdrBuffer1.index;
        localNdrBuffer1.advance(i * 12);
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
          this.entries[k].encode(localNdrBuffer2);
      }
    }
  }

  public static class SamrSamEntry extends NdrObject
  {
    public int idx;
    public rpc.unicode_string name;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      this.idx = paramNdrBuffer.dec_ndr_long();
      paramNdrBuffer.align(4);
      if (this.name == null)
        this.name = new rpc.unicode_string();
      this.name.length = ((short)paramNdrBuffer.dec_ndr_short());
      this.name.maximum_length = ((short)paramNdrBuffer.dec_ndr_short());
      if (paramNdrBuffer.dec_ndr_long() != 0)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        int i = localNdrBuffer1.dec_ndr_long();
        localNdrBuffer1.dec_ndr_long();
        int j = localNdrBuffer1.dec_ndr_long();
        int k = localNdrBuffer1.index;
        localNdrBuffer1.advance(j * 2);
        if (this.name.buffer == null)
        {
          if ((i < 0) || (i > 65535))
            throw new NdrException("invalid array conformance");
          this.name.buffer = new short[i];
        }
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(k);
        for (int m = 0; m < j; m++)
          this.name.buffer[m] = ((short)localNdrBuffer2.dec_ndr_short());
      }
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_long(this.idx);
      paramNdrBuffer.enc_ndr_short(this.name.length);
      paramNdrBuffer.enc_ndr_short(this.name.maximum_length);
      paramNdrBuffer.enc_ndr_referent(this.name.buffer, 1);
      if (this.name.buffer != null)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        int i = this.name.length / 2;
        localNdrBuffer1.enc_ndr_long(this.name.maximum_length / 2);
        localNdrBuffer1.enc_ndr_long(0);
        localNdrBuffer1.enc_ndr_long(i);
        int j = localNdrBuffer1.index;
        localNdrBuffer1.advance(i * 2);
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
          localNdrBuffer2.enc_ndr_short(this.name.buffer[k]);
      }
    }
  }
}