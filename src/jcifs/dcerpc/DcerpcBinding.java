package jcifs.dcerpc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import jcifs.dcerpc.msrpc.lsarpc;
import jcifs.dcerpc.msrpc.netdfs;
import jcifs.dcerpc.msrpc.samr;
import jcifs.dcerpc.msrpc.srvsvc;

class DcerpcBinding
{
  private static HashMap INTERFACES = new HashMap();
  String endpoint = null;
  int major;
  int minor;
  HashMap options = null;
  String proto;
  String server;
  UUID uuid = null;

  static
  {
    INTERFACES.put("srvsvc", srvsvc.getSyntax());
    INTERFACES.put("lsarpc", lsarpc.getSyntax());
    INTERFACES.put("samr", samr.getSyntax());
    INTERFACES.put("netdfs", netdfs.getSyntax());
  }

  DcerpcBinding(String paramString1, String paramString2)
  {
    this.proto = paramString1;
    this.server = paramString2;
  }

  Object getOption(String paramString)
  {
    if (paramString.equals("endpoint"))
      return this.endpoint;
    return this.options.get(paramString);
  }

  void setOption(String paramString, Object paramObject)
    throws DcerpcException
  {
    if (paramString.equals("endpoint"))
    {
      this.endpoint = paramObject.toString().toLowerCase();
      if (this.endpoint.startsWith("\\pipe\\"))
      {
        String str = (String)INTERFACES.get(this.endpoint.substring(6));
        if (str != null)
        {
          int i = str.indexOf(':');
          int j = str.indexOf('.', i + 1);
          this.uuid = new UUID(str.substring(0, i));
          this.major = Integer.parseInt(str.substring(i + 1, j));
          this.minor = Integer.parseInt(str.substring(j + 1));
          return;
        }
      }
      throw new DcerpcException("Bad endpoint: " + this.endpoint);
    }
    if (this.options == null)
      this.options = new HashMap();
    this.options.put(paramString, paramObject);
  }

  public String toString()
  {
    String str = this.proto + ":" + this.server + "[" + this.endpoint;
    if (this.options != null)
    {
      Iterator localIterator = this.options.keySet().iterator();
      while (localIterator.hasNext())
      {
        Object localObject1 = localIterator.next();
        Object localObject2 = this.options.get(localObject1);
        str = str + "," + localObject1 + "=" + localObject2;
      }
    }
    return str + "]";
  }
}