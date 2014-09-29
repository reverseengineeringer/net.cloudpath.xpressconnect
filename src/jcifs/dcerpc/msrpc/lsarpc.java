package jcifs.dcerpc.msrpc;

import jcifs.dcerpc.DcerpcMessage;
import jcifs.dcerpc.ndr.NdrBuffer;
import jcifs.dcerpc.ndr.NdrException;
import jcifs.dcerpc.ndr.NdrObject;
import jcifs.dcerpc.ndr.NdrSmall;
import jcifs.dcerpc.rpc.policy_handle;
import jcifs.dcerpc.rpc.sid_t;
import jcifs.dcerpc.rpc.unicode_string;
import jcifs.dcerpc.rpc.uuid_t;

public class lsarpc
{
  public static final int POLICY_INFO_ACCOUNT_DOMAIN = 5;
  public static final int POLICY_INFO_AUDIT_EVENTS = 2;
  public static final int POLICY_INFO_DNS_DOMAIN = 12;
  public static final int POLICY_INFO_MODIFICATION = 9;
  public static final int POLICY_INFO_PRIMARY_DOMAIN = 3;
  public static final int POLICY_INFO_SERVER_ROLE = 6;
  public static final int SID_NAME_ALIAS = 4;
  public static final int SID_NAME_DELETED = 6;
  public static final int SID_NAME_DOMAIN = 3;
  public static final int SID_NAME_DOM_GRP = 2;
  public static final int SID_NAME_INVALID = 7;
  public static final int SID_NAME_UNKNOWN = 8;
  public static final int SID_NAME_USER = 1;
  public static final int SID_NAME_USE_NONE = 0;
  public static final int SID_NAME_WKN_GRP = 5;

  public static String getSyntax()
  {
    return "12345778-1234-abcd-ef00-0123456789ab:0.0";
  }

  public static class LsarClose extends DcerpcMessage
  {
    public rpc.policy_handle handle;
    public int retval;

    public LsarClose(rpc.policy_handle parampolicy_handle)
    {
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
      this.handle.encode(paramNdrBuffer);
    }

    public int getOpnum()
    {
      return 0;
    }
  }

  public static class LsarDnsDomainInfo extends NdrObject
  {
    public rpc.unicode_string dns_domain;
    public rpc.unicode_string dns_forest;
    public rpc.uuid_t domain_guid;
    public rpc.unicode_string name;
    public rpc.sid_t sid;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.align(4);
      if (this.name == null)
        this.name = new rpc.unicode_string();
      this.name.length = ((short)paramNdrBuffer.dec_ndr_short());
      this.name.maximum_length = ((short)paramNdrBuffer.dec_ndr_short());
      int i = paramNdrBuffer.dec_ndr_long();
      paramNdrBuffer.align(4);
      if (this.dns_domain == null)
        this.dns_domain = new rpc.unicode_string();
      this.dns_domain.length = ((short)paramNdrBuffer.dec_ndr_short());
      this.dns_domain.maximum_length = ((short)paramNdrBuffer.dec_ndr_short());
      int j = paramNdrBuffer.dec_ndr_long();
      paramNdrBuffer.align(4);
      if (this.dns_forest == null)
        this.dns_forest = new rpc.unicode_string();
      this.dns_forest.length = ((short)paramNdrBuffer.dec_ndr_short());
      this.dns_forest.maximum_length = ((short)paramNdrBuffer.dec_ndr_short());
      int k = paramNdrBuffer.dec_ndr_long();
      paramNdrBuffer.align(4);
      if (this.domain_guid == null)
        this.domain_guid = new rpc.uuid_t();
      this.domain_guid.time_low = paramNdrBuffer.dec_ndr_long();
      this.domain_guid.time_mid = ((short)paramNdrBuffer.dec_ndr_short());
      this.domain_guid.time_hi_and_version = ((short)paramNdrBuffer.dec_ndr_short());
      this.domain_guid.clock_seq_hi_and_reserved = ((byte)paramNdrBuffer.dec_ndr_small());
      this.domain_guid.clock_seq_low = ((byte)paramNdrBuffer.dec_ndr_small());
      int m = paramNdrBuffer.index;
      paramNdrBuffer.advance(6);
      int n = paramNdrBuffer.dec_ndr_long();
      if (i != 0)
      {
        NdrBuffer localNdrBuffer5 = paramNdrBuffer.deferred;
        int i10 = localNdrBuffer5.dec_ndr_long();
        localNdrBuffer5.dec_ndr_long();
        int i11 = localNdrBuffer5.dec_ndr_long();
        int i12 = localNdrBuffer5.index;
        localNdrBuffer5.advance(i11 * 2);
        if (this.name.buffer == null)
        {
          if ((i10 < 0) || (i10 > 65535))
            throw new NdrException("invalid array conformance");
          this.name.buffer = new short[i10];
        }
        paramNdrBuffer = localNdrBuffer5.derive(i12);
        for (int i13 = 0; i13 < i11; i13++)
          this.name.buffer[i13] = ((short)paramNdrBuffer.dec_ndr_short());
      }
      if (j != 0)
      {
        NdrBuffer localNdrBuffer4 = paramNdrBuffer.deferred;
        int i6 = localNdrBuffer4.dec_ndr_long();
        localNdrBuffer4.dec_ndr_long();
        int i7 = localNdrBuffer4.dec_ndr_long();
        int i8 = localNdrBuffer4.index;
        localNdrBuffer4.advance(i7 * 2);
        if (this.dns_domain.buffer == null)
        {
          if ((i6 < 0) || (i6 > 65535))
            throw new NdrException("invalid array conformance");
          this.dns_domain.buffer = new short[i6];
        }
        paramNdrBuffer = localNdrBuffer4.derive(i8);
        for (int i9 = 0; i9 < i7; i9++)
          this.dns_domain.buffer[i9] = ((short)paramNdrBuffer.dec_ndr_short());
      }
      if (k != 0)
      {
        NdrBuffer localNdrBuffer3 = paramNdrBuffer.deferred;
        int i2 = localNdrBuffer3.dec_ndr_long();
        localNdrBuffer3.dec_ndr_long();
        int i3 = localNdrBuffer3.dec_ndr_long();
        int i4 = localNdrBuffer3.index;
        localNdrBuffer3.advance(i3 * 2);
        if (this.dns_forest.buffer == null)
        {
          if ((i2 < 0) || (i2 > 65535))
            throw new NdrException("invalid array conformance");
          this.dns_forest.buffer = new short[i2];
        }
        paramNdrBuffer = localNdrBuffer3.derive(i4);
        for (int i5 = 0; i5 < i3; i5++)
          this.dns_forest.buffer[i5] = ((short)paramNdrBuffer.dec_ndr_short());
      }
      if (this.domain_guid.node == null)
      {
        if ((6 < 0) || (6 > 65535))
          throw new NdrException("invalid array conformance");
        this.domain_guid.node = new byte[6];
      }
      NdrBuffer localNdrBuffer1 = paramNdrBuffer.derive(m);
      for (int i1 = 0; i1 < 6; i1++)
        this.domain_guid.node[i1] = ((byte)localNdrBuffer1.dec_ndr_small());
      if (n != 0)
      {
        if (this.sid == null)
          this.sid = new rpc.sid_t();
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.deferred;
        this.sid.decode(localNdrBuffer2);
      }
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_short(this.name.length);
      paramNdrBuffer.enc_ndr_short(this.name.maximum_length);
      paramNdrBuffer.enc_ndr_referent(this.name.buffer, 1);
      paramNdrBuffer.enc_ndr_short(this.dns_domain.length);
      paramNdrBuffer.enc_ndr_short(this.dns_domain.maximum_length);
      paramNdrBuffer.enc_ndr_referent(this.dns_domain.buffer, 1);
      paramNdrBuffer.enc_ndr_short(this.dns_forest.length);
      paramNdrBuffer.enc_ndr_short(this.dns_forest.maximum_length);
      paramNdrBuffer.enc_ndr_referent(this.dns_forest.buffer, 1);
      paramNdrBuffer.enc_ndr_long(this.domain_guid.time_low);
      paramNdrBuffer.enc_ndr_short(this.domain_guid.time_mid);
      paramNdrBuffer.enc_ndr_short(this.domain_guid.time_hi_and_version);
      paramNdrBuffer.enc_ndr_small(this.domain_guid.clock_seq_hi_and_reserved);
      paramNdrBuffer.enc_ndr_small(this.domain_guid.clock_seq_low);
      int i = paramNdrBuffer.index;
      paramNdrBuffer.advance(6);
      paramNdrBuffer.enc_ndr_referent(this.sid, 1);
      if (this.name.buffer != null)
      {
        NdrBuffer localNdrBuffer5 = paramNdrBuffer.deferred;
        int i4 = this.name.length / 2;
        localNdrBuffer5.enc_ndr_long(this.name.maximum_length / 2);
        localNdrBuffer5.enc_ndr_long(0);
        localNdrBuffer5.enc_ndr_long(i4);
        int i5 = localNdrBuffer5.index;
        localNdrBuffer5.advance(i4 * 2);
        paramNdrBuffer = localNdrBuffer5.derive(i5);
        for (int i6 = 0; i6 < i4; i6++)
          paramNdrBuffer.enc_ndr_short(this.name.buffer[i6]);
      }
      if (this.dns_domain.buffer != null)
      {
        NdrBuffer localNdrBuffer4 = paramNdrBuffer.deferred;
        int i1 = this.dns_domain.length / 2;
        localNdrBuffer4.enc_ndr_long(this.dns_domain.maximum_length / 2);
        localNdrBuffer4.enc_ndr_long(0);
        localNdrBuffer4.enc_ndr_long(i1);
        int i2 = localNdrBuffer4.index;
        localNdrBuffer4.advance(i1 * 2);
        paramNdrBuffer = localNdrBuffer4.derive(i2);
        for (int i3 = 0; i3 < i1; i3++)
          paramNdrBuffer.enc_ndr_short(this.dns_domain.buffer[i3]);
      }
      if (this.dns_forest.buffer != null)
      {
        NdrBuffer localNdrBuffer3 = paramNdrBuffer.deferred;
        int k = this.dns_forest.length / 2;
        localNdrBuffer3.enc_ndr_long(this.dns_forest.maximum_length / 2);
        localNdrBuffer3.enc_ndr_long(0);
        localNdrBuffer3.enc_ndr_long(k);
        int m = localNdrBuffer3.index;
        localNdrBuffer3.advance(k * 2);
        paramNdrBuffer = localNdrBuffer3.derive(m);
        for (int n = 0; n < k; n++)
          paramNdrBuffer.enc_ndr_short(this.dns_forest.buffer[n]);
      }
      NdrBuffer localNdrBuffer1 = paramNdrBuffer.derive(i);
      for (int j = 0; j < 6; j++)
        localNdrBuffer1.enc_ndr_small(this.domain_guid.node[j]);
      if (this.sid != null)
      {
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.deferred;
        this.sid.encode(localNdrBuffer2);
      }
    }
  }

  public static class LsarDomainInfo extends NdrObject
  {
    public rpc.unicode_string name;
    public rpc.sid_t sid;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.align(4);
      if (this.name == null)
        this.name = new rpc.unicode_string();
      this.name.length = ((short)paramNdrBuffer.dec_ndr_short());
      this.name.maximum_length = ((short)paramNdrBuffer.dec_ndr_short());
      int i = paramNdrBuffer.dec_ndr_long();
      int j = paramNdrBuffer.dec_ndr_long();
      if (i != 0)
      {
        NdrBuffer localNdrBuffer2 = paramNdrBuffer.deferred;
        int k = localNdrBuffer2.dec_ndr_long();
        localNdrBuffer2.dec_ndr_long();
        int m = localNdrBuffer2.dec_ndr_long();
        int n = localNdrBuffer2.index;
        localNdrBuffer2.advance(m * 2);
        if (this.name.buffer == null)
        {
          if ((k < 0) || (k > 65535))
            throw new NdrException("invalid array conformance");
          this.name.buffer = new short[k];
        }
        paramNdrBuffer = localNdrBuffer2.derive(n);
        for (int i1 = 0; i1 < m; i1++)
          this.name.buffer[i1] = ((short)paramNdrBuffer.dec_ndr_short());
      }
      if (j != 0)
      {
        if (this.sid == null)
          this.sid = new rpc.sid_t();
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        this.sid.decode(localNdrBuffer1);
      }
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_short(this.name.length);
      paramNdrBuffer.enc_ndr_short(this.name.maximum_length);
      paramNdrBuffer.enc_ndr_referent(this.name.buffer, 1);
      paramNdrBuffer.enc_ndr_referent(this.sid, 1);
      if (this.name.buffer != null)
      {
        NdrBuffer localNdrBuffer2 = paramNdrBuffer.deferred;
        int i = this.name.length / 2;
        localNdrBuffer2.enc_ndr_long(this.name.maximum_length / 2);
        localNdrBuffer2.enc_ndr_long(0);
        localNdrBuffer2.enc_ndr_long(i);
        int j = localNdrBuffer2.index;
        localNdrBuffer2.advance(i * 2);
        paramNdrBuffer = localNdrBuffer2.derive(j);
        for (int k = 0; k < i; k++)
          paramNdrBuffer.enc_ndr_short(this.name.buffer[k]);
      }
      if (this.sid != null)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        this.sid.encode(localNdrBuffer1);
      }
    }
  }

  public static class LsarLookupSids extends DcerpcMessage
  {
    public int count;
    public lsarpc.LsarRefDomainList domains;
    public rpc.policy_handle handle;
    public short level;
    public lsarpc.LsarTransNameArray names;
    public int retval;
    public lsarpc.LsarSidArray sids;

    public LsarLookupSids(rpc.policy_handle parampolicy_handle, lsarpc.LsarSidArray paramLsarSidArray, lsarpc.LsarRefDomainList paramLsarRefDomainList, lsarpc.LsarTransNameArray paramLsarTransNameArray, short paramShort, int paramInt)
    {
      this.handle = parampolicy_handle;
      this.sids = paramLsarSidArray;
      this.domains = paramLsarRefDomainList;
      this.names = paramLsarTransNameArray;
      this.level = paramShort;
      this.count = paramInt;
    }

    public void decode_out(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      if (paramNdrBuffer.dec_ndr_long() != 0)
      {
        if (this.domains == null)
          this.domains = new lsarpc.LsarRefDomainList();
        this.domains.decode(paramNdrBuffer);
      }
      this.names.decode(paramNdrBuffer);
      this.count = paramNdrBuffer.dec_ndr_long();
      this.retval = paramNdrBuffer.dec_ndr_long();
    }

    public void encode_in(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      this.handle.encode(paramNdrBuffer);
      this.sids.encode(paramNdrBuffer);
      this.names.encode(paramNdrBuffer);
      paramNdrBuffer.enc_ndr_short(this.level);
      paramNdrBuffer.enc_ndr_long(this.count);
    }

    public int getOpnum()
    {
      return 15;
    }
  }

  public static class LsarObjectAttributes extends NdrObject
  {
    public int attributes;
    public int length;
    public rpc.unicode_string object_name;
    public NdrSmall root_directory;
    public int security_descriptor;
    public lsarpc.LsarQosInfo security_quality_of_service;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      this.length = paramNdrBuffer.dec_ndr_long();
      int i = paramNdrBuffer.dec_ndr_long();
      int j = paramNdrBuffer.dec_ndr_long();
      this.attributes = paramNdrBuffer.dec_ndr_long();
      this.security_descriptor = paramNdrBuffer.dec_ndr_long();
      int k = paramNdrBuffer.dec_ndr_long();
      if (i != 0)
      {
        paramNdrBuffer = paramNdrBuffer.deferred;
        this.root_directory.decode(paramNdrBuffer);
      }
      if (j != 0)
      {
        if (this.object_name == null)
          this.object_name = new rpc.unicode_string();
        paramNdrBuffer = paramNdrBuffer.deferred;
        this.object_name.decode(paramNdrBuffer);
      }
      if (k != 0)
      {
        if (this.security_quality_of_service == null)
          this.security_quality_of_service = new lsarpc.LsarQosInfo();
        NdrBuffer localNdrBuffer = paramNdrBuffer.deferred;
        this.security_quality_of_service.decode(localNdrBuffer);
      }
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_long(this.length);
      paramNdrBuffer.enc_ndr_referent(this.root_directory, 1);
      paramNdrBuffer.enc_ndr_referent(this.object_name, 1);
      paramNdrBuffer.enc_ndr_long(this.attributes);
      paramNdrBuffer.enc_ndr_long(this.security_descriptor);
      paramNdrBuffer.enc_ndr_referent(this.security_quality_of_service, 1);
      if (this.root_directory != null)
      {
        paramNdrBuffer = paramNdrBuffer.deferred;
        this.root_directory.encode(paramNdrBuffer);
      }
      if (this.object_name != null)
      {
        paramNdrBuffer = paramNdrBuffer.deferred;
        this.object_name.encode(paramNdrBuffer);
      }
      if (this.security_quality_of_service != null)
      {
        NdrBuffer localNdrBuffer = paramNdrBuffer.deferred;
        this.security_quality_of_service.encode(localNdrBuffer);
      }
    }
  }

  public static class LsarOpenPolicy2 extends DcerpcMessage
  {
    public int desired_access;
    public lsarpc.LsarObjectAttributes object_attributes;
    public rpc.policy_handle policy_handle;
    public int retval;
    public String system_name;

    public LsarOpenPolicy2(String paramString, lsarpc.LsarObjectAttributes paramLsarObjectAttributes, int paramInt, rpc.policy_handle parampolicy_handle)
    {
      this.system_name = paramString;
      this.object_attributes = paramLsarObjectAttributes;
      this.desired_access = paramInt;
      this.policy_handle = parampolicy_handle;
    }

    public void decode_out(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      this.policy_handle.decode(paramNdrBuffer);
      this.retval = paramNdrBuffer.dec_ndr_long();
    }

    public void encode_in(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.enc_ndr_referent(this.system_name, 1);
      if (this.system_name != null)
        paramNdrBuffer.enc_ndr_string(this.system_name);
      this.object_attributes.encode(paramNdrBuffer);
      paramNdrBuffer.enc_ndr_long(this.desired_access);
    }

    public int getOpnum()
    {
      return 44;
    }
  }

  public static class LsarQosInfo extends NdrObject
  {
    public byte context_mode;
    public byte effective_only;
    public short impersonation_level;
    public int length;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      this.length = paramNdrBuffer.dec_ndr_long();
      this.impersonation_level = ((short)paramNdrBuffer.dec_ndr_short());
      this.context_mode = ((byte)paramNdrBuffer.dec_ndr_small());
      this.effective_only = ((byte)paramNdrBuffer.dec_ndr_small());
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_long(this.length);
      paramNdrBuffer.enc_ndr_short(this.impersonation_level);
      paramNdrBuffer.enc_ndr_small(this.context_mode);
      paramNdrBuffer.enc_ndr_small(this.effective_only);
    }
  }

  public static class LsarQueryInformationPolicy extends DcerpcMessage
  {
    public rpc.policy_handle handle;
    public NdrObject info;
    public short level;
    public int retval;

    public LsarQueryInformationPolicy(rpc.policy_handle parampolicy_handle, short paramShort, NdrObject paramNdrObject)
    {
      this.handle = parampolicy_handle;
      this.level = paramShort;
      this.info = paramNdrObject;
    }

    public void decode_out(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      if (paramNdrBuffer.dec_ndr_long() != 0)
      {
        paramNdrBuffer.dec_ndr_short();
        this.info.decode(paramNdrBuffer);
      }
      this.retval = paramNdrBuffer.dec_ndr_long();
    }

    public void encode_in(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      this.handle.encode(paramNdrBuffer);
      paramNdrBuffer.enc_ndr_short(this.level);
    }

    public int getOpnum()
    {
      return 7;
    }
  }

  public static class LsarQueryInformationPolicy2 extends DcerpcMessage
  {
    public rpc.policy_handle handle;
    public NdrObject info;
    public short level;
    public int retval;

    public LsarQueryInformationPolicy2(rpc.policy_handle parampolicy_handle, short paramShort, NdrObject paramNdrObject)
    {
      this.handle = parampolicy_handle;
      this.level = paramShort;
      this.info = paramNdrObject;
    }

    public void decode_out(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      if (paramNdrBuffer.dec_ndr_long() != 0)
      {
        paramNdrBuffer.dec_ndr_short();
        this.info.decode(paramNdrBuffer);
      }
      this.retval = paramNdrBuffer.dec_ndr_long();
    }

    public void encode_in(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      this.handle.encode(paramNdrBuffer);
      paramNdrBuffer.enc_ndr_short(this.level);
    }

    public int getOpnum()
    {
      return 46;
    }
  }

  public static class LsarRefDomainList extends NdrObject
  {
    public int count;
    public lsarpc.LsarTrustInformation[] domains;
    public int max_count;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      this.count = paramNdrBuffer.dec_ndr_long();
      int i = paramNdrBuffer.dec_ndr_long();
      this.max_count = paramNdrBuffer.dec_ndr_long();
      if (i != 0)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        int j = localNdrBuffer1.dec_ndr_long();
        int k = localNdrBuffer1.index;
        localNdrBuffer1.advance(j * 12);
        if (this.domains == null)
        {
          if ((j < 0) || (j > 65535))
            throw new NdrException("invalid array conformance");
          this.domains = new lsarpc.LsarTrustInformation[j];
        }
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(k);
        for (int m = 0; m < j; m++)
        {
          if (this.domains[m] == null)
            this.domains[m] = new lsarpc.LsarTrustInformation();
          this.domains[m].decode(localNdrBuffer2);
        }
      }
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_long(this.count);
      paramNdrBuffer.enc_ndr_referent(this.domains, 1);
      paramNdrBuffer.enc_ndr_long(this.max_count);
      if (this.domains != null)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        int i = this.count;
        localNdrBuffer1.enc_ndr_long(i);
        int j = localNdrBuffer1.index;
        localNdrBuffer1.advance(i * 12);
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
          this.domains[k].encode(localNdrBuffer2);
      }
    }
  }

  public static class LsarSidArray extends NdrObject
  {
    public int num_sids;
    public lsarpc.LsarSidPtr[] sids;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      this.num_sids = paramNdrBuffer.dec_ndr_long();
      if (paramNdrBuffer.dec_ndr_long() != 0)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        int i = localNdrBuffer1.dec_ndr_long();
        int j = localNdrBuffer1.index;
        localNdrBuffer1.advance(i * 4);
        if (this.sids == null)
        {
          if ((i < 0) || (i > 65535))
            throw new NdrException("invalid array conformance");
          this.sids = new lsarpc.LsarSidPtr[i];
        }
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
        {
          if (this.sids[k] == null)
            this.sids[k] = new lsarpc.LsarSidPtr();
          this.sids[k].decode(localNdrBuffer2);
        }
      }
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_long(this.num_sids);
      paramNdrBuffer.enc_ndr_referent(this.sids, 1);
      if (this.sids != null)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        int i = this.num_sids;
        localNdrBuffer1.enc_ndr_long(i);
        int j = localNdrBuffer1.index;
        localNdrBuffer1.advance(i * 4);
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
          this.sids[k].encode(localNdrBuffer2);
      }
    }
  }

  public static class LsarSidPtr extends NdrObject
  {
    public rpc.sid_t sid;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      if (paramNdrBuffer.dec_ndr_long() != 0)
      {
        if (this.sid == null)
          this.sid = new rpc.sid_t();
        NdrBuffer localNdrBuffer = paramNdrBuffer.deferred;
        this.sid.decode(localNdrBuffer);
      }
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_referent(this.sid, 1);
      if (this.sid != null)
      {
        NdrBuffer localNdrBuffer = paramNdrBuffer.deferred;
        this.sid.encode(localNdrBuffer);
      }
    }
  }

  public static class LsarTransNameArray extends NdrObject
  {
    public int count;
    public lsarpc.LsarTranslatedName[] names;

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
        localNdrBuffer1.advance(i * 16);
        if (this.names == null)
        {
          if ((i < 0) || (i > 65535))
            throw new NdrException("invalid array conformance");
          this.names = new lsarpc.LsarTranslatedName[i];
        }
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
        {
          if (this.names[k] == null)
            this.names[k] = new lsarpc.LsarTranslatedName();
          this.names[k].decode(localNdrBuffer2);
        }
      }
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_long(this.count);
      paramNdrBuffer.enc_ndr_referent(this.names, 1);
      if (this.names != null)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        int i = this.count;
        localNdrBuffer1.enc_ndr_long(i);
        int j = localNdrBuffer1.index;
        localNdrBuffer1.advance(i * 16);
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
          this.names[k].encode(localNdrBuffer2);
      }
    }
  }

  public static class LsarTransSidArray extends NdrObject
  {
    public int count;
    public lsarpc.LsarTranslatedSid[] sids;

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
        if (this.sids == null)
        {
          if ((i < 0) || (i > 65535))
            throw new NdrException("invalid array conformance");
          this.sids = new lsarpc.LsarTranslatedSid[i];
        }
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
        {
          if (this.sids[k] == null)
            this.sids[k] = new lsarpc.LsarTranslatedSid();
          this.sids[k].decode(localNdrBuffer2);
        }
      }
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_long(this.count);
      paramNdrBuffer.enc_ndr_referent(this.sids, 1);
      if (this.sids != null)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        int i = this.count;
        localNdrBuffer1.enc_ndr_long(i);
        int j = localNdrBuffer1.index;
        localNdrBuffer1.advance(i * 12);
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
          this.sids[k].encode(localNdrBuffer2);
      }
    }
  }

  public static class LsarTranslatedName extends NdrObject
  {
    public rpc.unicode_string name;
    public int sid_index;
    public short sid_type;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      this.sid_type = ((short)paramNdrBuffer.dec_ndr_short());
      paramNdrBuffer.align(4);
      if (this.name == null)
        this.name = new rpc.unicode_string();
      this.name.length = ((short)paramNdrBuffer.dec_ndr_short());
      this.name.maximum_length = ((short)paramNdrBuffer.dec_ndr_short());
      int i = paramNdrBuffer.dec_ndr_long();
      this.sid_index = paramNdrBuffer.dec_ndr_long();
      if (i != 0)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        int j = localNdrBuffer1.dec_ndr_long();
        localNdrBuffer1.dec_ndr_long();
        int k = localNdrBuffer1.dec_ndr_long();
        int m = localNdrBuffer1.index;
        localNdrBuffer1.advance(k * 2);
        if (this.name.buffer == null)
        {
          if ((j < 0) || (j > 65535))
            throw new NdrException("invalid array conformance");
          this.name.buffer = new short[j];
        }
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(m);
        for (int n = 0; n < k; n++)
          this.name.buffer[n] = ((short)localNdrBuffer2.dec_ndr_short());
      }
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_short(this.sid_type);
      paramNdrBuffer.enc_ndr_short(this.name.length);
      paramNdrBuffer.enc_ndr_short(this.name.maximum_length);
      paramNdrBuffer.enc_ndr_referent(this.name.buffer, 1);
      paramNdrBuffer.enc_ndr_long(this.sid_index);
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

  public static class LsarTranslatedSid extends NdrObject
  {
    public int rid;
    public int sid_index;
    public int sid_type;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      this.sid_type = paramNdrBuffer.dec_ndr_short();
      this.rid = paramNdrBuffer.dec_ndr_long();
      this.sid_index = paramNdrBuffer.dec_ndr_long();
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_short(this.sid_type);
      paramNdrBuffer.enc_ndr_long(this.rid);
      paramNdrBuffer.enc_ndr_long(this.sid_index);
    }
  }

  public static class LsarTrustInformation extends NdrObject
  {
    public rpc.unicode_string name;
    public rpc.sid_t sid;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.align(4);
      if (this.name == null)
        this.name = new rpc.unicode_string();
      this.name.length = ((short)paramNdrBuffer.dec_ndr_short());
      this.name.maximum_length = ((short)paramNdrBuffer.dec_ndr_short());
      int i = paramNdrBuffer.dec_ndr_long();
      int j = paramNdrBuffer.dec_ndr_long();
      if (i != 0)
      {
        NdrBuffer localNdrBuffer2 = paramNdrBuffer.deferred;
        int k = localNdrBuffer2.dec_ndr_long();
        localNdrBuffer2.dec_ndr_long();
        int m = localNdrBuffer2.dec_ndr_long();
        int n = localNdrBuffer2.index;
        localNdrBuffer2.advance(m * 2);
        if (this.name.buffer == null)
        {
          if ((k < 0) || (k > 65535))
            throw new NdrException("invalid array conformance");
          this.name.buffer = new short[k];
        }
        paramNdrBuffer = localNdrBuffer2.derive(n);
        for (int i1 = 0; i1 < m; i1++)
          this.name.buffer[i1] = ((short)paramNdrBuffer.dec_ndr_short());
      }
      if (j != 0)
      {
        if (this.sid == null)
          this.sid = new rpc.sid_t();
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        this.sid.decode(localNdrBuffer1);
      }
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_short(this.name.length);
      paramNdrBuffer.enc_ndr_short(this.name.maximum_length);
      paramNdrBuffer.enc_ndr_referent(this.name.buffer, 1);
      paramNdrBuffer.enc_ndr_referent(this.sid, 1);
      if (this.name.buffer != null)
      {
        NdrBuffer localNdrBuffer2 = paramNdrBuffer.deferred;
        int i = this.name.length / 2;
        localNdrBuffer2.enc_ndr_long(this.name.maximum_length / 2);
        localNdrBuffer2.enc_ndr_long(0);
        localNdrBuffer2.enc_ndr_long(i);
        int j = localNdrBuffer2.index;
        localNdrBuffer2.advance(i * 2);
        paramNdrBuffer = localNdrBuffer2.derive(j);
        for (int k = 0; k < i; k++)
          paramNdrBuffer.enc_ndr_short(this.name.buffer[k]);
      }
      if (this.sid != null)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        this.sid.encode(localNdrBuffer1);
      }
    }
  }
}