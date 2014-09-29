package jcifs.smb;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import jcifs.Config;
import jcifs.UniAddress;
import jcifs.util.LogStream;

public class Dfs
{
  static final boolean DISABLED = Config.getBoolean("jcifs.smb.client.dfs.disabled", false);
  protected static CacheEntry FALSE_ENTRY = new CacheEntry(0L);
  static final long TTL;
  static LogStream log = LogStream.getInstance();
  static final boolean strictView = Config.getBoolean("jcifs.smb.client.dfs.strictView", false);
  protected CacheEntry _domains = null;
  protected CacheEntry referrals = null;

  static
  {
    TTL = Config.getLong("jcifs.smb.client.dfs.ttl", 300L);
  }

  public SmbTransport getDc(String paramString, NtlmPasswordAuthentication paramNtlmPasswordAuthentication)
    throws SmbAuthException
  {
    if (DISABLED);
    do
      while (true)
      {
        return null;
        try
        {
          DfsReferral[] arrayOfDfsReferral = SmbTransport.getSmbTransport(UniAddress.getByName(paramString, true), 0).getDfsReferrals(paramNtlmPasswordAuthentication, "\\" + paramString, 1);
          if (arrayOfDfsReferral.length == 1)
          {
            SmbTransport localSmbTransport = SmbTransport.getSmbTransport(UniAddress.getByName(arrayOfDfsReferral[0].server), 0);
            return localSmbTransport;
          }
        }
        catch (IOException localIOException)
        {
          if (LogStream.level >= 3)
            localIOException.printStackTrace(log);
        }
      }
    while ((!strictView) || (!(localIOException instanceof SmbAuthException)));
    throw ((SmbAuthException)localIOException);
  }

  public DfsReferral getReferral(SmbTransport paramSmbTransport, String paramString1, String paramString2, String paramString3, NtlmPasswordAuthentication paramNtlmPasswordAuthentication)
    throws SmbAuthException
  {
    if (DISABLED);
    do
      while (true)
      {
        return null;
        try
        {
          String str = "\\" + paramString1 + "\\" + paramString2;
          if (paramString3 != null)
            str = str + paramString3;
          DfsReferral[] arrayOfDfsReferral = paramSmbTransport.getDfsReferrals(paramNtlmPasswordAuthentication, str, 1);
          if (arrayOfDfsReferral.length == 1)
          {
            DfsReferral localDfsReferral = arrayOfDfsReferral[0];
            return localDfsReferral;
          }
        }
        catch (IOException localIOException)
        {
          if (LogStream.level >= 3)
            localIOException.printStackTrace(log);
        }
      }
    while ((!strictView) || (!(localIOException instanceof SmbAuthException)));
    throw ((SmbAuthException)localIOException);
  }

  public HashMap getTrustedDomains(NtlmPasswordAuthentication paramNtlmPasswordAuthentication)
    throws SmbAuthException
  {
    if (DISABLED);
    do
    {
      return null;
      if ((this._domains != null) && (System.currentTimeMillis() > this._domains.expiration))
        this._domains = null;
      if (this._domains != null)
        return this._domains.map;
      try
      {
        DfsReferral[] arrayOfDfsReferral = SmbTransport.getSmbTransport(UniAddress.getByName(paramNtlmPasswordAuthentication.domain, true), 0).getDfsReferrals(paramNtlmPasswordAuthentication, "", 0);
        CacheEntry localCacheEntry = new CacheEntry(10L * TTL);
        for (int i = 0; i < arrayOfDfsReferral.length; i++)
        {
          String str = arrayOfDfsReferral[i].server;
          localCacheEntry.map.put(str, new HashMap());
        }
        this._domains = localCacheEntry;
        HashMap localHashMap = this._domains.map;
        return localHashMap;
      }
      catch (IOException localIOException)
      {
        if (LogStream.level >= 3)
          localIOException.printStackTrace(log);
      }
    }
    while ((!strictView) || (!(localIOException instanceof SmbAuthException)));
    throw ((SmbAuthException)localIOException);
  }

  void insert(String paramString, DfsReferral paramDfsReferral)
  {
    try
    {
      boolean bool = DISABLED;
      if (bool);
      while (true)
      {
        return;
        int i = paramString.indexOf('\\', 1);
        int j = paramString.indexOf('\\', i + 1);
        String str1 = paramString.substring(1, i);
        String str2 = paramString.substring(i + 1, j);
        String str3 = paramString.substring(0, paramDfsReferral.pathConsumed).toLowerCase();
        paramDfsReferral.pathConsumed -= 1 + (1 + str1.length()) + str2.length();
        if ((this.referrals != null) && (10000L + System.currentTimeMillis() > this.referrals.expiration))
          this.referrals = null;
        if (this.referrals == null)
          this.referrals = new CacheEntry(0L);
        this.referrals.map.put(str3, paramDfsReferral);
      }
    }
    finally
    {
    }
  }

  public boolean isTrustedDomain(String paramString, NtlmPasswordAuthentication paramNtlmPasswordAuthentication)
    throws SmbAuthException
  {
    HashMap localHashMap = getTrustedDomains(paramNtlmPasswordAuthentication);
    if (localHashMap == null);
    while (localHashMap.get(paramString.toLowerCase()) == null)
      return false;
    return true;
  }

  public DfsReferral resolve(String paramString1, String paramString2, String paramString3, NtlmPasswordAuthentication paramNtlmPasswordAuthentication)
    throws SmbAuthException
  {
    while (true)
    {
      long l;
      Object localObject2;
      CacheEntry localCacheEntry1;
      String str4;
      try
      {
        l = System.currentTimeMillis();
        if (!DISABLED)
        {
          boolean bool1 = paramString2.equals("IPC$");
          if (!bool1);
        }
        else
        {
          localObject2 = null;
          return localObject2;
        }
        HashMap localHashMap1 = getTrustedDomains(paramNtlmPasswordAuthentication);
        localDfsReferral = null;
        if (localHashMap1 == null)
          break label464;
        paramString1 = paramString1.toLowerCase();
        HashMap localHashMap2 = (HashMap)localHashMap1.get(paramString1);
        localDfsReferral = null;
        if (localHashMap2 == null)
          break label464;
        paramString2 = paramString2.toLowerCase();
        localCacheEntry1 = (CacheEntry)localHashMap2.get(paramString2);
        if ((localCacheEntry1 != null) && (l > localCacheEntry1.expiration))
        {
          localHashMap2.remove(paramString2);
          localCacheEntry1 = null;
        }
        if (localCacheEntry1 == null)
        {
          localSmbTransport = getDc(paramString1, paramNtlmPasswordAuthentication);
          if (localSmbTransport == null)
          {
            localObject2 = null;
            continue;
          }
          localDfsReferral = getReferral(localSmbTransport, paramString1, paramString2, paramString3, paramNtlmPasswordAuthentication);
          if (localDfsReferral != null)
          {
            localDfsReferral.pathConsumed -= 1 + (1 + paramString1.length()) + paramString2.length();
            localCacheEntry1 = new CacheEntry(0L);
            if (paramString3 == null)
              localCacheEntry1.map.put("\\", localDfsReferral);
            localHashMap2.put(paramString2, localCacheEntry1);
            if (localCacheEntry1 == null)
              break label464;
            str4 = "\\";
            if (paramString3 != null)
            {
              int m = paramString3.indexOf("\\", 1);
              if (m <= 0)
                break label392;
              str4 = paramString3.substring(1, m);
            }
            localDfsReferral = (DfsReferral)localCacheEntry1.map.get(str4);
            if ((localDfsReferral != null) && (l > localDfsReferral.expiration))
            {
              localDfsReferral = null;
              localCacheEntry1.map.remove(str4);
            }
            if (localDfsReferral != null)
              break label464;
            if (localSmbTransport != null)
              break label402;
            localSmbTransport = getDc(paramString1, paramNtlmPasswordAuthentication);
            if (localSmbTransport != null)
              break label402;
            localObject2 = null;
            continue;
          }
          if (paramString3 != null)
            continue;
          CacheEntry localCacheEntry2 = FALSE_ENTRY;
          localHashMap2.put(paramString2, localCacheEntry2);
          continue;
        }
      }
      finally
      {
      }
      CacheEntry localCacheEntry3 = FALSE_ENTRY;
      CacheEntry localCacheEntry4 = localCacheEntry1;
      SmbTransport localSmbTransport = null;
      DfsReferral localDfsReferral = null;
      if (localCacheEntry4 == localCacheEntry3)
      {
        localSmbTransport = null;
        localDfsReferral = null;
        localCacheEntry1 = null;
        continue;
        label392: str4 = paramString3.substring(1);
        continue;
        label402: localDfsReferral = getReferral(localSmbTransport, paramString1, paramString2, paramString3, paramNtlmPasswordAuthentication);
        if (localDfsReferral != null)
        {
          localDfsReferral.pathConsumed -= 1 + (1 + paramString1.length()) + paramString2.length();
          localDfsReferral.link = str4;
          localCacheEntry1.map.put(str4, localDfsReferral);
        }
        label464: if ((localDfsReferral == null) && (paramString3 != null))
        {
          if ((this.referrals != null) && (l > this.referrals.expiration))
            this.referrals = null;
          if (this.referrals == null)
            this.referrals = new CacheEntry(0L);
          String str1 = "\\" + paramString1 + "\\" + paramString2;
          if (!paramString3.equals("\\"))
            str1 = str1 + paramString3;
          String str2 = str1.toLowerCase();
          Iterator localIterator = this.referrals.map.keySet().iterator();
          if (localIterator.hasNext())
          {
            String str3 = (String)localIterator.next();
            int i = str3.length();
            if (i == str2.length())
              bool2 = str3.equals(str2);
            while (true)
              if (bool2)
              {
                localDfsReferral = (DfsReferral)this.referrals.map.get(str3);
                break;
                int j = str2.length();
                bool2 = false;
                if (i < j)
                {
                  if (!str3.regionMatches(0, str2, 0, i))
                    break label727;
                  int k = str2.charAt(i);
                  if (k != 92)
                    break label727;
                }
              }
            label727: for (boolean bool2 = true; ; bool2 = false)
            {
              break label649;
              break;
            }
          }
        }
        label649: localObject2 = localDfsReferral;
      }
    }
  }

  static class CacheEntry
  {
    long expiration;
    HashMap map;

    CacheEntry(long paramLong)
    {
      if (paramLong == 0L)
        paramLong = Dfs.TTL;
      this.expiration = (System.currentTimeMillis() + 1000L * paramLong);
      this.map = new HashMap();
    }
  }
}