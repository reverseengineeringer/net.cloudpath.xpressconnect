package jcifs.netbios;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import jcifs.Config;
import jcifs.util.Hexdump;

public final class NbtAddress
{
  private static final HashMap ADDRESS_CACHE;
  static final String ANY_HOSTS_NAME = "";
  public static final int B_NODE = 0;
  private static final int CACHE_POLICY = 0;
  private static final NameServiceClient CLIENT;
  private static final int DEFAULT_CACHE_POLICY = 30;
  private static final int FOREVER = -1;
  public static final int H_NODE = 3;
  private static final HashMap LOOKUP_TABLE;
  public static final String MASTER_BROWSER_NAME = "\001\002__MSBROWSE__\002";
  public static final int M_NODE = 2;
  static final InetAddress[] NBNS = Config.getInetAddressArray("jcifs.netbios.wins", ",", new InetAddress[0]);
  public static final int P_NODE = 1;
  public static final String SMBSERVER_NAME = "*SMBSERVER     ";
  static final NbtAddress UNKNOWN_ADDRESS;
  static final byte[] UNKNOWN_MAC_ADDRESS;
  static final Name UNKNOWN_NAME;
  static NbtAddress localhost;
  private static int nbnsIndex;
  int address;
  String calledName;
  boolean groupName;
  Name hostName;
  boolean isActive;
  boolean isBeingDeleted;
  boolean isDataFromNodeStatus;
  boolean isInConflict;
  boolean isPermanent;
  byte[] macAddress;
  int nodeType;

  static
  {
    CLIENT = new NameServiceClient();
    CACHE_POLICY = Config.getInt("jcifs.netbios.cachePolicy", 30);
    nbnsIndex = 0;
    ADDRESS_CACHE = new HashMap();
    LOOKUP_TABLE = new HashMap();
    UNKNOWN_NAME = new Name("0.0.0.0", 0, null);
    UNKNOWN_ADDRESS = new NbtAddress(UNKNOWN_NAME, 0, false, 0);
    UNKNOWN_MAC_ADDRESS = new byte[] { 0, 0, 0, 0, 0, 0 };
    ADDRESS_CACHE.put(UNKNOWN_NAME, new CacheEntry(UNKNOWN_NAME, UNKNOWN_ADDRESS, -1L));
    Object localObject = CLIENT.laddr;
    if (localObject == null);
    try
    {
      InetAddress localInetAddress2 = InetAddress.getLocalHost();
      localObject = localInetAddress2;
      String str = Config.getProperty("jcifs.netbios.hostname", null);
      if ((str == null) || (str.length() == 0))
      {
        byte[] arrayOfByte = ((InetAddress)localObject).getAddress();
        str = "JCIFS" + (0xFF & arrayOfByte[2]) + "_" + (0xFF & arrayOfByte[3]) + "_" + Hexdump.toHexString((int)(255.0D * Math.random()), 2);
      }
      Name localName = new Name(str, 0, Config.getProperty("jcifs.netbios.scope", null));
      localhost = new NbtAddress(localName, ((InetAddress)localObject).hashCode(), false, 0, false, false, true, false, UNKNOWN_MAC_ADDRESS);
      cacheAddress(localName, localhost, -1L);
      return;
    }
    catch (UnknownHostException localUnknownHostException1)
    {
      while (true)
        try
        {
          InetAddress localInetAddress1 = InetAddress.getByName("127.0.0.1");
          localObject = localInetAddress1;
        }
        catch (UnknownHostException localUnknownHostException2)
        {
        }
    }
  }

  NbtAddress(Name paramName, int paramInt1, boolean paramBoolean, int paramInt2)
  {
    this.hostName = paramName;
    this.address = paramInt1;
    this.groupName = paramBoolean;
    this.nodeType = paramInt2;
  }

  NbtAddress(Name paramName, int paramInt1, boolean paramBoolean1, int paramInt2, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, byte[] paramArrayOfByte)
  {
    this.hostName = paramName;
    this.address = paramInt1;
    this.groupName = paramBoolean1;
    this.nodeType = paramInt2;
    this.isBeingDeleted = paramBoolean2;
    this.isInConflict = paramBoolean3;
    this.isActive = paramBoolean4;
    this.isPermanent = paramBoolean5;
    this.macAddress = paramArrayOfByte;
    this.isDataFromNodeStatus = true;
  }

  static void cacheAddress(Name paramName, NbtAddress paramNbtAddress)
  {
    if (CACHE_POLICY == 0)
      return;
    long l = -1L;
    if (CACHE_POLICY != -1)
      l = System.currentTimeMillis() + 1000 * CACHE_POLICY;
    cacheAddress(paramName, paramNbtAddress, l);
  }

  static void cacheAddress(Name paramName, NbtAddress paramNbtAddress, long paramLong)
  {
    if (CACHE_POLICY == 0)
      return;
    while (true)
    {
      CacheEntry localCacheEntry1;
      synchronized (ADDRESS_CACHE)
      {
        localCacheEntry1 = (CacheEntry)ADDRESS_CACHE.get(paramName);
        if (localCacheEntry1 == null)
        {
          CacheEntry localCacheEntry2 = new CacheEntry(paramName, paramNbtAddress, paramLong);
          ADDRESS_CACHE.put(paramName, localCacheEntry2);
          return;
        }
      }
      localCacheEntry1.address = paramNbtAddress;
      localCacheEntry1.expiration = paramLong;
    }
  }

  static void cacheAddressArray(NbtAddress[] paramArrayOfNbtAddress)
  {
    if (CACHE_POLICY == 0)
      return;
    long l = -1L;
    if (CACHE_POLICY != -1)
      l = System.currentTimeMillis() + 1000 * CACHE_POLICY;
    HashMap localHashMap = ADDRESS_CACHE;
    for (int i = 0; ; i++)
    {
      try
      {
        if (i < paramArrayOfNbtAddress.length)
        {
          CacheEntry localCacheEntry1 = (CacheEntry)ADDRESS_CACHE.get(paramArrayOfNbtAddress[i].hostName);
          if (localCacheEntry1 == null)
          {
            CacheEntry localCacheEntry2 = new CacheEntry(paramArrayOfNbtAddress[i].hostName, paramArrayOfNbtAddress[i], l);
            ADDRESS_CACHE.put(paramArrayOfNbtAddress[i].hostName, localCacheEntry2);
          }
          else
          {
            localCacheEntry1.address = paramArrayOfNbtAddress[i];
            localCacheEntry1.expiration = l;
          }
        }
      }
      finally
      {
      }
      return;
    }
  }

  private static Object checkLookupTable(Name paramName)
  {
    NbtAddress localNbtAddress;
    synchronized (LOOKUP_TABLE)
    {
      if (!LOOKUP_TABLE.containsKey(paramName))
      {
        LOOKUP_TABLE.put(paramName, paramName);
        return null;
      }
      while (true)
      {
        boolean bool = LOOKUP_TABLE.containsKey(paramName);
        if (!bool)
          break;
        try
        {
          LOOKUP_TABLE.wait();
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
      localNbtAddress = getCachedAddress(paramName);
      if (localNbtAddress == null)
        synchronized (LOOKUP_TABLE)
        {
          LOOKUP_TABLE.put(paramName, paramName);
          return localNbtAddress;
        }
    }
    return localNbtAddress;
  }

  static NbtAddress doNameQuery(Name paramName, InetAddress paramInetAddress)
    throws UnknownHostException
  {
    if ((paramName.hexCode == 29) && (paramInetAddress == null))
      paramInetAddress = CLIENT.baddr;
    int i;
    if (paramInetAddress != null)
      i = paramInetAddress.hashCode();
    Object localObject1;
    while (true)
    {
      paramName.srcHashCode = i;
      localObject1 = getCachedAddress(paramName);
      if (localObject1 == null)
      {
        localObject1 = (NbtAddress)checkLookupTable(paramName);
        if (localObject1 != null);
      }
      try
      {
        NbtAddress localNbtAddress = CLIENT.getByName(paramName, paramInetAddress);
        localObject1 = localNbtAddress;
        cacheAddress(paramName, (NbtAddress)localObject1);
        updateLookupTable(paramName);
        if (localObject1 == UNKNOWN_ADDRESS)
        {
          throw new UnknownHostException(paramName.toString());
          i = 0;
        }
      }
      catch (UnknownHostException localUnknownHostException)
      {
        while (true)
        {
          localObject1 = UNKNOWN_ADDRESS;
          cacheAddress(paramName, (NbtAddress)localObject1);
          updateLookupTable(paramName);
        }
      }
      finally
      {
        cacheAddress(paramName, (NbtAddress)localObject1);
        updateLookupTable(paramName);
      }
    }
    return localObject1;
  }

  public static NbtAddress[] getAllByAddress(String paramString)
    throws UnknownHostException
  {
    return getAllByAddress(getByName(paramString, 0, null));
  }

  public static NbtAddress[] getAllByAddress(String paramString1, int paramInt, String paramString2)
    throws UnknownHostException
  {
    return getAllByAddress(getByName(paramString1, paramInt, paramString2));
  }

  public static NbtAddress[] getAllByAddress(NbtAddress paramNbtAddress)
    throws UnknownHostException
  {
    StringBuffer localStringBuffer;
    try
    {
      NbtAddress[] arrayOfNbtAddress = CLIENT.getNodeStatus(paramNbtAddress);
      cacheAddressArray(arrayOfNbtAddress);
      return arrayOfNbtAddress;
    }
    catch (UnknownHostException localUnknownHostException)
    {
      localStringBuffer = new StringBuffer().append("no name with type 0x").append(Hexdump.toHexString(paramNbtAddress.hostName.hexCode, 2));
      if (paramNbtAddress.hostName.scope == null)
        break label69;
    }
    if (paramNbtAddress.hostName.scope.length() == 0);
    label69: for (String str = " with no scope"; ; str = " with scope " + paramNbtAddress.hostName.scope)
      throw new UnknownHostException(str + " for host " + paramNbtAddress.getHostAddress());
  }

  public static NbtAddress[] getAllByName(String paramString1, int paramInt, String paramString2, InetAddress paramInetAddress)
    throws UnknownHostException
  {
    return CLIENT.getAllByName(new Name(paramString1, paramInt, paramString2), paramInetAddress);
  }

  public static NbtAddress getByName(String paramString)
    throws UnknownHostException
  {
    return getByName(paramString, 0, null);
  }

  public static NbtAddress getByName(String paramString1, int paramInt, String paramString2)
    throws UnknownHostException
  {
    return getByName(paramString1, paramInt, paramString2, null);
  }

  public static NbtAddress getByName(String paramString1, int paramInt, String paramString2, InetAddress paramInetAddress)
    throws UnknownHostException
  {
    if ((paramString1 == null) || (paramString1.length() == 0))
      return getLocalHost();
    if (!Character.isDigit(paramString1.charAt(0)))
      return doNameQuery(new Name(paramString1, paramInt, paramString2), paramInetAddress);
    int i = 0;
    int j = 0;
    char[] arrayOfChar = paramString1.toCharArray();
    for (int k = 0; k < arrayOfChar.length; k++)
    {
      int m = arrayOfChar[k];
      if ((m < 48) || (m > 57))
        return doNameQuery(new Name(paramString1, paramInt, paramString2), paramInetAddress);
      int n = 0;
      while (true)
      {
        if (m != 46)
        {
          if ((m < 48) || (m > 57))
            return doNameQuery(new Name(paramString1, paramInt, paramString2), paramInetAddress);
          n = -48 + (m + n * 10);
          k++;
          if (k < arrayOfChar.length);
        }
        else
        {
          if (n <= 255)
            break;
          return doNameQuery(new Name(paramString1, paramInt, paramString2), paramInetAddress);
        }
        m = arrayOfChar[k];
      }
      i = n + (i << 8);
      j++;
    }
    if ((j != 4) || (paramString1.endsWith(".")))
      return doNameQuery(new Name(paramString1, paramInt, paramString2), paramInetAddress);
    return new NbtAddress(UNKNOWN_NAME, i, false, 0);
  }

  static NbtAddress getCachedAddress(Name paramName)
  {
    if (CACHE_POLICY == 0)
      return null;
    synchronized (ADDRESS_CACHE)
    {
      CacheEntry localCacheEntry = (CacheEntry)ADDRESS_CACHE.get(paramName);
      if ((localCacheEntry != null) && (localCacheEntry.expiration < System.currentTimeMillis()) && (localCacheEntry.expiration >= 0L))
        localCacheEntry = null;
      NbtAddress localNbtAddress = null;
      if (localCacheEntry != null)
        localNbtAddress = localCacheEntry.address;
      return localNbtAddress;
    }
  }

  public static NbtAddress getLocalHost()
    throws UnknownHostException
  {
    return localhost;
  }

  public static Name getLocalName()
  {
    return localhost.hostName;
  }

  public static InetAddress getWINSAddress()
  {
    if (NBNS.length == 0)
      return null;
    return NBNS[nbnsIndex];
  }

  public static boolean isWINS(InetAddress paramInetAddress)
  {
    for (int i = 0; (paramInetAddress != null) && (i < NBNS.length); i++)
      if (paramInetAddress.hashCode() == NBNS[i].hashCode())
        return true;
    return false;
  }

  static InetAddress switchWINS()
  {
    if (1 + nbnsIndex < NBNS.length);
    for (int i = 1 + nbnsIndex; ; i = 0)
    {
      nbnsIndex = i;
      if (NBNS.length != 0)
        break;
      return null;
    }
    return NBNS[nbnsIndex];
  }

  private static void updateLookupTable(Name paramName)
  {
    synchronized (LOOKUP_TABLE)
    {
      LOOKUP_TABLE.remove(paramName);
      LOOKUP_TABLE.notifyAll();
      return;
    }
  }

  void checkData()
    throws UnknownHostException
  {
    if (this.hostName == UNKNOWN_NAME)
      getAllByAddress(this);
  }

  void checkNodeStatusData()
    throws UnknownHostException
  {
    if (!this.isDataFromNodeStatus)
      getAllByAddress(this);
  }

  public boolean equals(Object paramObject)
  {
    return (paramObject != null) && ((paramObject instanceof NbtAddress)) && (((NbtAddress)paramObject).address == this.address);
  }

  public String firstCalledName()
  {
    this.calledName = this.hostName.name;
    int i;
    int j;
    char[] arrayOfChar;
    int k;
    label46: int m;
    if (Character.isDigit(this.calledName.charAt(0)))
    {
      i = 0;
      j = this.calledName.length();
      arrayOfChar = this.calledName.toCharArray();
      k = 0;
      if (k >= j)
        break label171;
      m = k + 1;
      if (Character.isDigit(arrayOfChar[k]))
      {
        if ((m != j) || (i != 3))
          break label90;
        this.calledName = "*SMBSERVER     ";
      }
    }
    label171: 
    while (true)
    {
      return this.calledName;
      label90: if ((m < j) && (arrayOfChar[m] == '.'))
      {
        i++;
        k = m + 1;
        break;
      }
      switch (this.hostName.hexCode)
      {
      default:
        break;
      case 27:
      case 28:
      case 29:
        this.calledName = "*SMBSERVER     ";
        continue;
        k = m;
        break label46;
      }
    }
  }

  public byte[] getAddress()
  {
    byte[] arrayOfByte = new byte[4];
    arrayOfByte[0] = ((byte)(0xFF & this.address >>> 24));
    arrayOfByte[1] = ((byte)(0xFF & this.address >>> 16));
    arrayOfByte[2] = ((byte)(0xFF & this.address >>> 8));
    arrayOfByte[3] = ((byte)(0xFF & this.address));
    return arrayOfByte;
  }

  public String getHostAddress()
  {
    return (0xFF & this.address >>> 24) + "." + (0xFF & this.address >>> 16) + "." + (0xFF & this.address >>> 8) + "." + (0xFF & this.address >>> 0);
  }

  public String getHostName()
  {
    try
    {
      checkData();
      return this.hostName.name;
    }
    catch (UnknownHostException localUnknownHostException)
    {
    }
    return getHostAddress();
  }

  public InetAddress getInetAddress()
    throws UnknownHostException
  {
    return InetAddress.getByName(getHostAddress());
  }

  public byte[] getMacAddress()
    throws UnknownHostException
  {
    checkNodeStatusData();
    return this.macAddress;
  }

  public int getNameType()
  {
    return this.hostName.hexCode;
  }

  public int getNodeType()
    throws UnknownHostException
  {
    checkData();
    return this.nodeType;
  }

  public int hashCode()
  {
    return this.address;
  }

  public boolean isActive()
    throws UnknownHostException
  {
    checkNodeStatusData();
    return this.isActive;
  }

  public boolean isBeingDeleted()
    throws UnknownHostException
  {
    checkNodeStatusData();
    return this.isBeingDeleted;
  }

  public boolean isGroupAddress()
    throws UnknownHostException
  {
    checkData();
    return this.groupName;
  }

  public boolean isInConflict()
    throws UnknownHostException
  {
    checkNodeStatusData();
    return this.isInConflict;
  }

  public boolean isPermanent()
    throws UnknownHostException
  {
    checkNodeStatusData();
    return this.isPermanent;
  }

  public String nextCalledName()
  {
    String str1;
    if (this.calledName == this.hostName.name)
    {
      this.calledName = "*SMBSERVER     ";
      str1 = this.calledName;
      label25: return str1;
    }
    if (this.calledName == "*SMBSERVER     ");
    while (true)
    {
      int i;
      try
      {
        NbtAddress[] arrayOfNbtAddress = CLIENT.getNodeStatus(this);
        if (this.hostName.hexCode == 29)
        {
          i = 0;
          int j = arrayOfNbtAddress.length;
          str1 = null;
          if (i >= j)
            break label25;
          if (arrayOfNbtAddress[i].hostName.hexCode != 32)
            break label139;
          return arrayOfNbtAddress[i].hostName.name;
        }
        if (!this.isDataFromNodeStatus)
          break;
        this.calledName = null;
        String str2 = this.hostName.name;
        return str2;
      }
      catch (UnknownHostException localUnknownHostException)
      {
        this.calledName = null;
      }
      break;
      this.calledName = null;
      break;
      label139: i++;
    }
  }

  public String toString()
  {
    return this.hostName.toString() + "/" + getHostAddress();
  }

  static final class CacheEntry
  {
    NbtAddress address;
    long expiration;
    Name hostName;

    CacheEntry(Name paramName, NbtAddress paramNbtAddress, long paramLong)
    {
      this.hostName = paramName;
      this.address = paramNbtAddress;
      this.expiration = paramLong;
    }
  }
}