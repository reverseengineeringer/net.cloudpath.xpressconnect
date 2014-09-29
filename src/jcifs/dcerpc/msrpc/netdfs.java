package jcifs.dcerpc.msrpc;

import jcifs.dcerpc.DcerpcMessage;
import jcifs.dcerpc.ndr.NdrBuffer;
import jcifs.dcerpc.ndr.NdrException;
import jcifs.dcerpc.ndr.NdrLong;
import jcifs.dcerpc.ndr.NdrObject;

public class netdfs
{
  public static final int DFS_STORAGE_STATE_ACTIVE = 4;
  public static final int DFS_STORAGE_STATE_OFFLINE = 1;
  public static final int DFS_STORAGE_STATE_ONLINE = 2;
  public static final int DFS_VOLUME_FLAVOR_AD_BLOB = 512;
  public static final int DFS_VOLUME_FLAVOR_STANDALONE = 256;

  public static String getSyntax()
  {
    return "4fc742e0-4a10-11cf-8273-00aa004ae673:3.0";
  }

  public static class DfsEnumArray1 extends NdrObject
  {
    public int count;
    public netdfs.DfsInfo1[] s;

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
        if (this.s == null)
        {
          if ((i < 0) || (i > 65535))
            throw new NdrException("invalid array conformance");
          this.s = new netdfs.DfsInfo1[i];
        }
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
        {
          if (this.s[k] == null)
            this.s[k] = new netdfs.DfsInfo1();
          this.s[k].decode(localNdrBuffer2);
        }
      }
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_long(this.count);
      paramNdrBuffer.enc_ndr_referent(this.s, 1);
      if (this.s != null)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        int i = this.count;
        localNdrBuffer1.enc_ndr_long(i);
        int j = localNdrBuffer1.index;
        localNdrBuffer1.advance(i * 4);
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
          this.s[k].encode(localNdrBuffer2);
      }
    }
  }

  public static class DfsEnumArray200 extends NdrObject
  {
    public int count;
    public netdfs.DfsInfo200[] s;

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
        if (this.s == null)
        {
          if ((i < 0) || (i > 65535))
            throw new NdrException("invalid array conformance");
          this.s = new netdfs.DfsInfo200[i];
        }
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
        {
          if (this.s[k] == null)
            this.s[k] = new netdfs.DfsInfo200();
          this.s[k].decode(localNdrBuffer2);
        }
      }
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_long(this.count);
      paramNdrBuffer.enc_ndr_referent(this.s, 1);
      if (this.s != null)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        int i = this.count;
        localNdrBuffer1.enc_ndr_long(i);
        int j = localNdrBuffer1.index;
        localNdrBuffer1.advance(i * 4);
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
          this.s[k].encode(localNdrBuffer2);
      }
    }
  }

  public static class DfsEnumArray3 extends NdrObject
  {
    public int count;
    public netdfs.DfsInfo3[] s;

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
        localNdrBuffer1.advance(i * 20);
        if (this.s == null)
        {
          if ((i < 0) || (i > 65535))
            throw new NdrException("invalid array conformance");
          this.s = new netdfs.DfsInfo3[i];
        }
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
        {
          if (this.s[k] == null)
            this.s[k] = new netdfs.DfsInfo3();
          this.s[k].decode(localNdrBuffer2);
        }
      }
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_long(this.count);
      paramNdrBuffer.enc_ndr_referent(this.s, 1);
      if (this.s != null)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        int i = this.count;
        localNdrBuffer1.enc_ndr_long(i);
        int j = localNdrBuffer1.index;
        localNdrBuffer1.advance(i * 20);
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
          this.s[k].encode(localNdrBuffer2);
      }
    }
  }

  public static class DfsEnumArray300 extends NdrObject
  {
    public int count;
    public netdfs.DfsInfo300[] s;

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
        localNdrBuffer1.advance(i * 8);
        if (this.s == null)
        {
          if ((i < 0) || (i > 65535))
            throw new NdrException("invalid array conformance");
          this.s = new netdfs.DfsInfo300[i];
        }
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
        {
          if (this.s[k] == null)
            this.s[k] = new netdfs.DfsInfo300();
          this.s[k].decode(localNdrBuffer2);
        }
      }
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_long(this.count);
      paramNdrBuffer.enc_ndr_referent(this.s, 1);
      if (this.s != null)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        int i = this.count;
        localNdrBuffer1.enc_ndr_long(i);
        int j = localNdrBuffer1.index;
        localNdrBuffer1.advance(i * 8);
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
          this.s[k].encode(localNdrBuffer2);
      }
    }
  }

  public static class DfsEnumStruct extends NdrObject
  {
    public NdrObject e;
    public int level;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      this.level = paramNdrBuffer.dec_ndr_long();
      paramNdrBuffer.dec_ndr_long();
      if (paramNdrBuffer.dec_ndr_long() != 0)
      {
        if (this.e == null)
          this.e = new netdfs.DfsEnumArray1();
        NdrBuffer localNdrBuffer = paramNdrBuffer.deferred;
        this.e.decode(localNdrBuffer);
      }
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_long(this.level);
      paramNdrBuffer.enc_ndr_long(this.level);
      paramNdrBuffer.enc_ndr_referent(this.e, 1);
      if (this.e != null)
      {
        NdrBuffer localNdrBuffer = paramNdrBuffer.deferred;
        this.e.encode(localNdrBuffer);
      }
    }
  }

  public static class DfsInfo1 extends NdrObject
  {
    public String entry_path;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      if (paramNdrBuffer.dec_ndr_long() != 0)
        this.entry_path = paramNdrBuffer.deferred.dec_ndr_string();
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_referent(this.entry_path, 1);
      if (this.entry_path != null)
        paramNdrBuffer.deferred.enc_ndr_string(this.entry_path);
    }
  }

  public static class DfsInfo200 extends NdrObject
  {
    public String dfs_name;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      if (paramNdrBuffer.dec_ndr_long() != 0)
        this.dfs_name = paramNdrBuffer.deferred.dec_ndr_string();
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_referent(this.dfs_name, 1);
      if (this.dfs_name != null)
        paramNdrBuffer.deferred.enc_ndr_string(this.dfs_name);
    }
  }

  public static class DfsInfo3 extends NdrObject
  {
    public String comment;
    public int num_stores;
    public String path;
    public int state;
    public netdfs.DfsStorageInfo[] stores;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      int i = paramNdrBuffer.dec_ndr_long();
      int j = paramNdrBuffer.dec_ndr_long();
      this.state = paramNdrBuffer.dec_ndr_long();
      this.num_stores = paramNdrBuffer.dec_ndr_long();
      int k = paramNdrBuffer.dec_ndr_long();
      if (i != 0)
      {
        paramNdrBuffer = paramNdrBuffer.deferred;
        this.path = paramNdrBuffer.dec_ndr_string();
      }
      if (j != 0)
      {
        paramNdrBuffer = paramNdrBuffer.deferred;
        this.comment = paramNdrBuffer.dec_ndr_string();
      }
      if (k != 0)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        int m = localNdrBuffer1.dec_ndr_long();
        int n = localNdrBuffer1.index;
        localNdrBuffer1.advance(m * 12);
        if (this.stores == null)
        {
          if ((m < 0) || (m > 65535))
            throw new NdrException("invalid array conformance");
          this.stores = new netdfs.DfsStorageInfo[m];
        }
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(n);
        for (int i1 = 0; i1 < m; i1++)
        {
          if (this.stores[i1] == null)
            this.stores[i1] = new netdfs.DfsStorageInfo();
          this.stores[i1].decode(localNdrBuffer2);
        }
      }
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_referent(this.path, 1);
      paramNdrBuffer.enc_ndr_referent(this.comment, 1);
      paramNdrBuffer.enc_ndr_long(this.state);
      paramNdrBuffer.enc_ndr_long(this.num_stores);
      paramNdrBuffer.enc_ndr_referent(this.stores, 1);
      if (this.path != null)
      {
        paramNdrBuffer = paramNdrBuffer.deferred;
        paramNdrBuffer.enc_ndr_string(this.path);
      }
      if (this.comment != null)
      {
        paramNdrBuffer = paramNdrBuffer.deferred;
        paramNdrBuffer.enc_ndr_string(this.comment);
      }
      if (this.stores != null)
      {
        NdrBuffer localNdrBuffer1 = paramNdrBuffer.deferred;
        int i = this.num_stores;
        localNdrBuffer1.enc_ndr_long(i);
        int j = localNdrBuffer1.index;
        localNdrBuffer1.advance(i * 12);
        NdrBuffer localNdrBuffer2 = localNdrBuffer1.derive(j);
        for (int k = 0; k < i; k++)
          this.stores[k].encode(localNdrBuffer2);
      }
    }
  }

  public static class DfsInfo300 extends NdrObject
  {
    public String dfs_name;
    public int flags;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      this.flags = paramNdrBuffer.dec_ndr_long();
      if (paramNdrBuffer.dec_ndr_long() != 0)
        this.dfs_name = paramNdrBuffer.deferred.dec_ndr_string();
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_long(this.flags);
      paramNdrBuffer.enc_ndr_referent(this.dfs_name, 1);
      if (this.dfs_name != null)
        paramNdrBuffer.deferred.enc_ndr_string(this.dfs_name);
    }
  }

  public static class DfsStorageInfo extends NdrObject
  {
    public String server_name;
    public String share_name;
    public int state;

    public void decode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      this.state = paramNdrBuffer.dec_ndr_long();
      int i = paramNdrBuffer.dec_ndr_long();
      int j = paramNdrBuffer.dec_ndr_long();
      if (i != 0)
      {
        paramNdrBuffer = paramNdrBuffer.deferred;
        this.server_name = paramNdrBuffer.dec_ndr_string();
      }
      if (j != 0)
        this.share_name = paramNdrBuffer.deferred.dec_ndr_string();
    }

    public void encode(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.align(4);
      paramNdrBuffer.enc_ndr_long(this.state);
      paramNdrBuffer.enc_ndr_referent(this.server_name, 1);
      paramNdrBuffer.enc_ndr_referent(this.share_name, 1);
      if (this.server_name != null)
      {
        paramNdrBuffer = paramNdrBuffer.deferred;
        paramNdrBuffer.enc_ndr_string(this.server_name);
      }
      if (this.share_name != null)
        paramNdrBuffer.deferred.enc_ndr_string(this.share_name);
    }
  }

  public static class NetrDfsEnumEx extends DcerpcMessage
  {
    public String dfs_name;
    public netdfs.DfsEnumStruct info;
    public int level;
    public int prefmaxlen;
    public int retval;
    public NdrLong totalentries;

    public NetrDfsEnumEx(String paramString, int paramInt1, int paramInt2, netdfs.DfsEnumStruct paramDfsEnumStruct, NdrLong paramNdrLong)
    {
      this.dfs_name = paramString;
      this.level = paramInt1;
      this.prefmaxlen = paramInt2;
      this.info = paramDfsEnumStruct;
      this.totalentries = paramNdrLong;
    }

    public void decode_out(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      if (paramNdrBuffer.dec_ndr_long() != 0)
      {
        if (this.info == null)
          this.info = new netdfs.DfsEnumStruct();
        this.info.decode(paramNdrBuffer);
      }
      if (paramNdrBuffer.dec_ndr_long() != 0)
        this.totalentries.decode(paramNdrBuffer);
      this.retval = paramNdrBuffer.dec_ndr_long();
    }

    public void encode_in(NdrBuffer paramNdrBuffer)
      throws NdrException
    {
      paramNdrBuffer.enc_ndr_string(this.dfs_name);
      paramNdrBuffer.enc_ndr_long(this.level);
      paramNdrBuffer.enc_ndr_long(this.prefmaxlen);
      paramNdrBuffer.enc_ndr_referent(this.info, 1);
      if (this.info != null)
        this.info.encode(paramNdrBuffer);
      paramNdrBuffer.enc_ndr_referent(this.totalentries, 1);
      if (this.totalentries != null)
        this.totalentries.encode(paramNdrBuffer);
    }

    public int getOpnum()
    {
      return 21;
    }
  }
}