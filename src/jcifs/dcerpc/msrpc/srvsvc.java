package jcifs.dcerpc.msrpc;

import jcifs.dcerpc.DcerpcMessage;
import jcifs.dcerpc.ndr.NdrBuffer;
import jcifs.dcerpc.ndr.NdrException;
import jcifs.dcerpc.ndr.NdrObject;

public class srvsvc
{
  public static String getSyntax()
  {
    return "4b324fc8-1670-01d3-1278-5a47bf6ee188:3.0";
  }

  public static class RemoteTOD extends DcerpcMessage
  {
    public srvsvc.TimeOfDayInfo info;
    public int retval;
    public String servername;

    public RemoteTOD(String paramString, srvsvc.TimeOfDayInfo paramTimeOfDayInfo)
    {
      this.servername = paramString;
      this.info = paramTimeOfDayInfo;
    }

    public void decode_out(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      if (paramNdrBuffer.dec_ndr_long() != 0)
      {
        if (this.info == null)
          this.info = new srvsvc.TimeOfDayInfo();
        this.info.decode(paramNdrBuffer);
      }
      this.retval = paramNdrBuffer.dec_ndr_long();
    }

    public void encode_in(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.enc_ndr_referent(this.servername, 1);
      if (this.servername != null)
        paramNdrBuffer.enc_ndr_string(this.servername);
    }

    public int getOpnum()
    {
      return 28;
    }
  }

  public static class ServerGetInfo extends DcerpcMessage
  {
    public NdrObject info;
    public int level;
    public int retval;
    public String servername;

    public ServerGetInfo(String paramString, int paramInt, NdrObject paramNdrObject)
    {
      this.servername = paramString;
      this.level = paramInt;
      this.info = paramNdrObject;
    }

    public void decode_out(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.dec_ndr_long();
      if (paramNdrBuffer.dec_ndr_long() != 0)
      {
        if (this.info == null)
          this.info = new srvsvc.ServerInfo100();
        paramNdrBuffer = paramNdrBuffer.deferred;
        this.info.decode(paramNdrBuffer);
      }
      this.retval = paramNdrBuffer.dec_ndr_long();
    }

    public void encode_in(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.enc_ndr_referent(this.servername, 1);
      if (this.servername != null)
        paramNdrBuffer.enc_ndr_string(this.servername);
      paramNdrBuffer.enc_ndr_long(this.level);
    }

    public int getOpnum()
    {
      return 21;
    }
  }

  public static class ServerInfo100 extends NdrObject
  {
    public String name;
    public int platform_id;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      this.platform_id = paramNdrBuffer.dec_ndr_long();
      if (paramNdrBuffer.dec_ndr_long() != 0)
        this.name = paramNdrBuffer.deferred.dec_ndr_string();
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_long(this.platform_id);
      paramNdrBuffer.enc_ndr_referent(this.name, 1);
      if (this.name != null)
        paramNdrBuffer.deferred.enc_ndr_string(this.name);
    }
  }

  public static class ShareEnumAll extends DcerpcMessage
  {
    public NdrObject info;
    public int level;
    public int prefmaxlen;
    public int resume_handle;
    public int retval;
    public String servername;
    public int totalentries;

    public ShareEnumAll(String paramString, int paramInt1, NdrObject paramNdrObject, int paramInt2, int paramInt3, int paramInt4)
    {
      this.servername = paramString;
      this.level = paramInt1;
      this.info = paramNdrObject;
      this.prefmaxlen = paramInt2;
      this.totalentries = paramInt3;
      this.resume_handle = paramInt4;
    }

    public void decode_out(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      this.level = paramNdrBuffer.dec_ndr_long();
      paramNdrBuffer.dec_ndr_long();
      if (paramNdrBuffer.dec_ndr_long() != 0)
      {
        if (this.info == null)
          this.info = new srvsvc.ShareInfoCtr0();
        paramNdrBuffer = paramNdrBuffer.deferred;
        this.info.decode(paramNdrBuffer);
      }
      this.totalentries = paramNdrBuffer.dec_ndr_long();
      this.resume_handle = paramNdrBuffer.dec_ndr_long();
      this.retval = paramNdrBuffer.dec_ndr_long();
    }

    public void encode_in(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.enc_ndr_referent(this.servername, 1);
      if (this.servername != null)
        paramNdrBuffer.enc_ndr_string(this.servername);
      paramNdrBuffer.enc_ndr_long(this.level);
      paramNdrBuffer.enc_ndr_long(this.level);
      paramNdrBuffer.enc_ndr_referent(this.info, 1);
      if (this.info != null)
      {
        paramNdrBuffer = paramNdrBuffer.deferred;
        this.info.encode(paramNdrBuffer);
      }
      paramNdrBuffer.enc_ndr_long(this.prefmaxlen);
      paramNdrBuffer.enc_ndr_long(this.resume_handle);
    }

    public int getOpnum()
    {
      return 15;
    }
  }

  public static class ShareGetInfo extends DcerpcMessage
  {
    public NdrObject info;
    public int level;
    public int retval;
    public String servername;
    public String sharename;

    public ShareGetInfo(String paramString1, String paramString2, int paramInt, NdrObject paramNdrObject)
    {
      this.servername = paramString1;
      this.sharename = paramString2;
      this.level = paramInt;
      this.info = paramNdrObject;
    }

    public void decode_out(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.dec_ndr_long();
      if (paramNdrBuffer.dec_ndr_long() != 0)
      {
        if (this.info == null)
          this.info = new srvsvc.ShareInfo0();
        paramNdrBuffer = paramNdrBuffer.deferred;
        this.info.decode(paramNdrBuffer);
      }
      this.retval = paramNdrBuffer.dec_ndr_long();
    }

    public void encode_in(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.enc_ndr_referent(this.servername, 1);
      if (this.servername != null)
        paramNdrBuffer.enc_ndr_string(this.servername);
      paramNdrBuffer.enc_ndr_string(this.sharename);
      paramNdrBuffer.enc_ndr_long(this.level);
    }

    public int getOpnum()
    {
      return 16;
    }
  }

  public static class ShareInfo0 extends NdrObject
  {
    public String netname;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      if (paramNdrBuffer.dec_ndr_long() != 0)
        this.netname = paramNdrBuffer.deferred.dec_ndr_string();
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_referent(this.netname, 1);
      if (this.netname != null)
        paramNdrBuffer.deferred.enc_ndr_string(this.netname);
    }
  }

  public static class ShareInfo1 extends NdrObject
  {
    public String netname;
    public String remark;
    public int type;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      int i = paramNdrBuffer.dec_ndr_long();
      this.type = paramNdrBuffer.dec_ndr_long();
      int j = paramNdrBuffer.dec_ndr_long();
      if (i != 0)
      {
        paramNdrBuffer = paramNdrBuffer.deferred;
        this.netname = paramNdrBuffer.dec_ndr_string();
      }
      if (j != 0)
        this.remark = paramNdrBuffer.deferred.dec_ndr_string();
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_referent(this.netname, 1);
      paramNdrBuffer.enc_ndr_long(this.type);
      paramNdrBuffer.enc_ndr_referent(this.remark, 1);
      if (this.netname != null)
      {
        paramNdrBuffer = paramNdrBuffer.deferred;
        paramNdrBuffer.enc_ndr_string(this.netname);
      }
      if (this.remark != null)
        paramNdrBuffer.deferred.enc_ndr_string(this.remark);
    }
  }

  public static class ShareInfo502 extends NdrObject
  {
    public int current_uses;
    public int max_uses;
    public String netname;
    public String password;
    public String path;
    public int permissions;
    public String remark;
    public int sd_size;
    public byte[] security_descriptor;
    public int type;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      int i = paramNdrBuffer.dec_ndr_long();
      this.type = paramNdrBuffer.dec_ndr_long();
      int j = paramNdrBuffer.dec_ndr_long();
      this.permissions = paramNdrBuffer.dec_ndr_long();
      this.max_uses = paramNdrBuffer.dec_ndr_long();
      this.current_uses = paramNdrBuffer.dec_ndr_long();
      int k = paramNdrBuffer.dec_ndr_long();
      int m = paramNdrBuffer.dec_ndr_long();
      this.sd_size = paramNdrBuffer.dec_ndr_long();
      int n = paramNdrBuffer.dec_ndr_long();
      if (i != 0)
      {
        paramNdrBuffer = paramNdrBuffer.deferred;
        this.netname = paramNdrBuffer.dec_ndr_string();
      }
      if (j != 0)
      {
        paramNdrBuffer = paramNdrBuffer.deferred;
        this.remark = paramNdrBuffer.dec_ndr_string();
      }
      if (k != 0)
      {
        paramNdrBuffer = paramNdrBuffer.deferred;
        this.path = paramNdrBuffer.dec_ndr_string();
      }
      if (m != 0)
      {
        paramNdrBuffer = paramNdrBuffer.deferred;
        this.password = paramNdrBuffer.dec_ndr_string();
      }
      if (n != 0)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        int i1 = localNdrBuffer1.dec_ndr_long();
        int i2 = localNdrBuffer1.index;
        localNdrBuffer1.advance(i1 * 1);
        if (this.security_descriptor == null)
        {
          if ((i1 < 0) || (i1 > 65535))
            throw new NdrException("invalid array conformance");
          this.security_descriptor = new byte[i1];
        }
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(i2);
        for (int i3 = 0; i3 < i1; i3++)
          this.security_descriptor[i3] = ((byte)localNdrBuffer2.dec_ndr_small());
      }
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_referent(this.netname, 1);
      paramNdrBuffer.enc_ndr_long(this.type);
      paramNdrBuffer.enc_ndr_referent(this.remark, 1);
      paramNdrBuffer.enc_ndr_long(this.permissions);
      paramNdrBuffer.enc_ndr_long(this.max_uses);
      paramNdrBuffer.enc_ndr_long(this.current_uses);
      paramNdrBuffer.enc_ndr_referent(this.path, 1);
      paramNdrBuffer.enc_ndr_referent(this.password, 1);
      paramNdrBuffer.enc_ndr_long(this.sd_size);
      paramNdrBuffer.enc_ndr_referent(this.security_descriptor, 1);
      if (this.netname != null)
      {
        paramNdrBuffer = paramNdrBuffer.deferred;
        paramNdrBuffer.enc_ndr_string(this.netname);
      }
      if (this.remark != null)
      {
        paramNdrBuffer = paramNdrBuffer.deferred;
        paramNdrBuffer.enc_ndr_string(this.remark);
      }
      if (this.path != null)
      {
        paramNdrBuffer = paramNdrBuffer.deferred;
        paramNdrBuffer.enc_ndr_string(this.path);
      }
      if (this.password != null)
      {
        paramNdrBuffer = paramNdrBuffer.deferred;
        paramNdrBuffer.enc_ndr_string(this.password);
      }
      if (this.security_descriptor != null)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        int i = this.sd_size;
        localNdrBuffer1.enc_ndr_long(i);
        int j = localNdrBuffer1.index;
        localNdrBuffer1.advance(i * 1);
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
          localNdrBuffer2.enc_ndr_small(this.security_descriptor[k]);
      }
    }
  }

  public static class ShareInfoCtr0 extends NdrObject
  {
    public srvsvc.ShareInfo0[] array;
    public int count;

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
        localNdrBuffer1.advance(i * 4);
        if (this.array == null)
        {
          if ((i < 0) || (i > 65535))
            throw new NdrException("invalid array conformance");
          this.array = new srvsvc.ShareInfo0[i];
        }
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
        {
          if (this.array[k] == null)
            this.array[k] = new srvsvc.ShareInfo0();
          this.array[k].decode(localNdrBuffer2);
        }
      }
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_long(this.count);
      paramNdrBuffer.enc_ndr_referent(this.array, 1);
      if (this.array != null)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        int i = this.count;
        localNdrBuffer1.enc_ndr_long(i);
        int j = localNdrBuffer1.index;
        localNdrBuffer1.advance(i * 4);
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
          this.array[k].encode(localNdrBuffer2);
      }
    }
  }

  public static class ShareInfoCtr1 extends NdrObject
  {
    public srvsvc.ShareInfo1[] array;
    public int count;

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
        if (this.array == null)
        {
          if ((i < 0) || (i > 65535))
            throw new NdrException("invalid array conformance");
          this.array = new srvsvc.ShareInfo1[i];
        }
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
        {
          if (this.array[k] == null)
            this.array[k] = new srvsvc.ShareInfo1();
          this.array[k].decode(localNdrBuffer2);
        }
      }
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_long(this.count);
      paramNdrBuffer.enc_ndr_referent(this.array, 1);
      if (this.array != null)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        int i = this.count;
        localNdrBuffer1.enc_ndr_long(i);
        int j = localNdrBuffer1.index;
        localNdrBuffer1.advance(i * 12);
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
          this.array[k].encode(localNdrBuffer2);
      }
    }
  }

  public static class ShareInfoCtr502 extends NdrObject
  {
    public srvsvc.ShareInfo502[] array;
    public int count;

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
        localNdrBuffer1.advance(i * 40);
        if (this.array == null)
        {
          if ((i < 0) || (i > 65535))
            throw new NdrException("invalid array conformance");
          this.array = new srvsvc.ShareInfo502[i];
        }
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
        {
          if (this.array[k] == null)
            this.array[k] = new srvsvc.ShareInfo502();
          this.array[k].decode(localNdrBuffer2);
        }
      }
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_long(this.count);
      paramNdrBuffer.enc_ndr_referent(this.array, 1);
      if (this.array != null)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        int i = this.count;
        localNdrBuffer1.enc_ndr_long(i);
        int j = localNdrBuffer1.index;
        localNdrBuffer1.advance(i * 40);
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
          this.array[k].encode(localNdrBuffer2);
      }
    }
  }

  public static class TimeOfDayInfo extends NdrObject
  {
    public int day;
    public int elapsedt;
    public int hours;
    public int hunds;
    public int mins;
    public int month;
    public int msecs;
    public int secs;
    public int timezone;
    public int tinterval;
    public int weekday;
    public int year;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      this.elapsedt = paramNdrBuffer.dec_ndr_long();
      this.msecs = paramNdrBuffer.dec_ndr_long();
      this.hours = paramNdrBuffer.dec_ndr_long();
      this.mins = paramNdrBuffer.dec_ndr_long();
      this.secs = paramNdrBuffer.dec_ndr_long();
      this.hunds = paramNdrBuffer.dec_ndr_long();
      this.timezone = paramNdrBuffer.dec_ndr_long();
      this.tinterval = paramNdrBuffer.dec_ndr_long();
      this.day = paramNdrBuffer.dec_ndr_long();
      this.month = paramNdrBuffer.dec_ndr_long();
      this.year = paramNdrBuffer.dec_ndr_long();
      this.weekday = paramNdrBuffer.dec_ndr_long();
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_long(this.elapsedt);
      paramNdrBuffer.enc_ndr_long(this.msecs);
      paramNdrBuffer.enc_ndr_long(this.hours);
      paramNdrBuffer.enc_ndr_long(this.mins);
      paramNdrBuffer.enc_ndr_long(this.secs);
      paramNdrBuffer.enc_ndr_long(this.hunds);
      paramNdrBuffer.enc_ndr_long(this.timezone);
      paramNdrBuffer.enc_ndr_long(this.tinterval);
      paramNdrBuffer.enc_ndr_long(this.day);
      paramNdrBuffer.enc_ndr_long(this.month);
      paramNdrBuffer.enc_ndr_long(this.year);
      paramNdrBuffer.enc_ndr_long(this.weekday);
    }
  }
}