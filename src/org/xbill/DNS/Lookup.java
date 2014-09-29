package org.xbill.DNS;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.PrintStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class Lookup
{
  public static final int HOST_NOT_FOUND = 3;
  public static final int SUCCESSFUL = 0;
  public static final int TRY_AGAIN = 2;
  public static final int TYPE_NOT_FOUND = 4;
  public static final int UNRECOVERABLE = 1;
  static Class class$org$xbill$DNS$Lookup;
  private static Map defaultCaches;
  private static int defaultNdots;
  private static Resolver defaultResolver;
  private static Name[] defaultSearchPath;
  private static final Name[] noAliases = new Name[0];
  private List aliases;
  private Record[] answers;
  private boolean badresponse;
  private String badresponse_error;
  private Cache cache;
  private int credibility;
  private int dclass;
  private boolean done;
  private boolean doneCurrent;
  private String error;
  private boolean foundAlias;
  private int iterations;
  private Name name;
  private boolean nametoolong;
  private boolean networkerror;
  private boolean nxdomain;
  private boolean referral;
  private Resolver resolver;
  private int result;
  private Name[] searchPath;
  private boolean temporary_cache;
  private boolean timedout;
  private int type;
  private boolean verbose;

  static
  {
    refreshDefault();
  }

  public Lookup(String paramString)
    throws TextParseException
  {
    this(Name.fromString(paramString), 1, 1);
  }

  public Lookup(String paramString, int paramInt)
    throws TextParseException
  {
    this(Name.fromString(paramString), paramInt, 1);
  }

  public Lookup(String paramString, int paramInt1, int paramInt2)
    throws TextParseException
  {
    this(Name.fromString(paramString), paramInt1, paramInt2);
  }

  public Lookup(Name paramName)
  {
    this(paramName, 1, 1);
  }

  public Lookup(Name paramName, int paramInt)
  {
    this(paramName, paramInt, 1);
  }

  public Lookup(Name paramName, int paramInt1, int paramInt2)
  {
    Type.check(paramInt1);
    DClass.check(paramInt2);
    if ((!Type.isRR(paramInt1)) && (paramInt1 != 255))
      throw new IllegalArgumentException("Cannot query for meta-types other than ANY");
    this.name = paramName;
    this.type = paramInt1;
    this.dclass = paramInt2;
    Class localClass;
    if (class$org$xbill$DNS$Lookup == null)
    {
      localClass = class$("org.xbill.DNS.Lookup");
      class$org$xbill$DNS$Lookup = localClass;
    }
    while (true)
      try
      {
        this.resolver = getDefaultResolver();
        this.searchPath = getDefaultSearchPath();
        this.cache = getDefaultCache(paramInt2);
        this.credibility = 3;
        this.verbose = Options.check("verbose");
        this.result = -1;
        return;
        localClass = class$org$xbill$DNS$Lookup;
      }
      finally
      {
      }
  }

  private void checkDone()
  {
    if ((this.done) && (this.result != -1))
      return;
    StringBuffer localStringBuffer = new StringBuffer("Lookup of " + this.name + " ");
    if (this.dclass != 1)
      localStringBuffer.append(DClass.string(this.dclass) + " ");
    localStringBuffer.append(Type.string(this.type) + " isn't done");
    throw new IllegalStateException(localStringBuffer.toString());
  }

  static Class class$(String paramString)
  {
    try
    {
      Class localClass = Class.forName(paramString);
      return localClass;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      throw new NoClassDefFoundError().initCause(localClassNotFoundException);
    }
  }

  private void follow(Name paramName1, Name paramName2)
  {
    this.foundAlias = true;
    this.badresponse = false;
    this.networkerror = false;
    this.timedout = false;
    this.nxdomain = false;
    this.referral = false;
    this.iterations = (1 + this.iterations);
    if ((this.iterations >= 6) || (paramName1.equals(paramName2)))
    {
      this.result = 1;
      this.error = "CNAME loop";
      this.done = true;
      return;
    }
    if (this.aliases == null)
      this.aliases = new ArrayList();
    this.aliases.add(paramName2);
    lookup(paramName1);
  }

  public static Cache getDefaultCache(int paramInt)
  {
    try
    {
      DClass.check(paramInt);
      Cache localCache = (Cache)defaultCaches.get(Mnemonic.toInteger(paramInt));
      if (localCache == null)
      {
        localCache = new Cache(paramInt);
        defaultCaches.put(Mnemonic.toInteger(paramInt), localCache);
      }
      return localCache;
    }
    finally
    {
    }
  }

  public static Resolver getDefaultResolver()
  {
    try
    {
      Resolver localResolver = defaultResolver;
      return localResolver;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public static Name[] getDefaultSearchPath()
  {
    try
    {
      Name[] arrayOfName = defaultSearchPath;
      return arrayOfName;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  private void lookup(Name paramName)
  {
    SetResponse localSetResponse1 = this.cache.lookupRecords(paramName, this.type, this.credibility);
    if (this.verbose)
    {
      System.err.println("lookup " + paramName + " " + Type.string(this.type));
      System.err.println(localSetResponse1);
    }
    processResponse(paramName, localSetResponse1);
    if ((this.done) || (this.doneCurrent))
      return;
    Message localMessage1 = Message.newQuery(Record.newRecord(paramName, this.type, this.dclass));
    Message localMessage2;
    try
    {
      localMessage2 = this.resolver.send(localMessage1);
      int i = localMessage2.getHeader().getRcode();
      if ((i != 0) && (i != 3))
      {
        this.badresponse = true;
        this.badresponse_error = Rcode.string(i);
        return;
      }
    }
    catch (IOException localIOException)
    {
      if ((localIOException instanceof InterruptedIOException))
      {
        this.timedout = true;
        return;
      }
      this.networkerror = true;
      return;
    }
    if (!localMessage1.getQuestion().equals(localMessage2.getQuestion()))
    {
      this.badresponse = true;
      this.badresponse_error = "response does not match query";
      return;
    }
    SetResponse localSetResponse2 = this.cache.addMessage(localMessage2);
    if (localSetResponse2 == null)
      localSetResponse2 = this.cache.lookupRecords(paramName, this.type, this.credibility);
    if (this.verbose)
    {
      System.err.println("queried " + paramName + " " + Type.string(this.type));
      System.err.println(localSetResponse2);
    }
    processResponse(paramName, localSetResponse2);
  }

  private void processResponse(Name paramName, SetResponse paramSetResponse)
  {
    if (paramSetResponse.isSuccessful())
    {
      RRset[] arrayOfRRset = paramSetResponse.answers();
      ArrayList localArrayList = new ArrayList();
      for (int i = 0; i < arrayOfRRset.length; i++)
      {
        Iterator localIterator = arrayOfRRset[i].rrs();
        while (localIterator.hasNext())
          localArrayList.add(localIterator.next());
      }
      this.result = 0;
      this.answers = ((Record[])localArrayList.toArray(new Record[localArrayList.size()]));
      this.done = true;
    }
    do
    {
      do
      {
        return;
        if (!paramSetResponse.isNXDOMAIN())
          break;
        this.nxdomain = true;
        this.doneCurrent = true;
      }
      while (this.iterations <= 0);
      this.result = 3;
      this.done = true;
      return;
      if (paramSetResponse.isNXRRSET())
      {
        this.result = 4;
        this.answers = null;
        this.done = true;
        return;
      }
      if (paramSetResponse.isCNAME())
      {
        follow(paramSetResponse.getCNAME().getTarget(), paramName);
        return;
      }
      if (paramSetResponse.isDNAME())
      {
        DNAMERecord localDNAMERecord = paramSetResponse.getDNAME();
        try
        {
          follow(paramName.fromDNAME(localDNAMERecord), paramName);
          return;
        }
        catch (NameTooLongException localNameTooLongException)
        {
          this.result = 1;
          this.error = "Invalid DNAME target";
          this.done = true;
          return;
        }
      }
    }
    while (!paramSetResponse.isDelegation());
    this.referral = true;
  }

  public static void refreshDefault()
  {
    try
    {
      defaultResolver = new ExtendedResolver();
      defaultSearchPath = ResolverConfig.getCurrentConfig().searchPath();
      defaultCaches = new HashMap();
      defaultNdots = ResolverConfig.getCurrentConfig().ndots();
      return;
    }
    catch (UnknownHostException localUnknownHostException)
    {
      throw new RuntimeException("Failed to initialize resolver");
    }
    finally
    {
    }
  }

  private final void reset()
  {
    this.iterations = 0;
    this.foundAlias = false;
    this.done = false;
    this.doneCurrent = false;
    this.aliases = null;
    this.answers = null;
    this.result = -1;
    this.error = null;
    this.nxdomain = false;
    this.badresponse = false;
    this.badresponse_error = null;
    this.networkerror = false;
    this.timedout = false;
    this.nametoolong = false;
    this.referral = false;
    if (this.temporary_cache)
      this.cache.clearCache();
  }

  private void resolve(Name paramName1, Name paramName2)
  {
    this.doneCurrent = false;
    Object localObject;
    if (paramName2 == null)
      localObject = paramName1;
    while (true)
    {
      lookup((Name)localObject);
      return;
      try
      {
        Name localName = Name.concatenate(paramName1, paramName2);
        localObject = localName;
      }
      catch (NameTooLongException localNameTooLongException)
      {
        this.nametoolong = true;
      }
    }
  }

  public static void setDefaultCache(Cache paramCache, int paramInt)
  {
    try
    {
      DClass.check(paramInt);
      defaultCaches.put(Mnemonic.toInteger(paramInt), paramCache);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public static void setDefaultResolver(Resolver paramResolver)
  {
    try
    {
      defaultResolver = paramResolver;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public static void setDefaultSearchPath(String[] paramArrayOfString)
    throws TextParseException
  {
    if (paramArrayOfString == null);
    try
    {
      Name[] arrayOfName;
      for (defaultSearchPath = null; ; defaultSearchPath = arrayOfName)
      {
        return;
        arrayOfName = new Name[paramArrayOfString.length];
        for (int i = 0; i < paramArrayOfString.length; i++)
          arrayOfName[i] = Name.fromString(paramArrayOfString[i], Name.root);
      }
    }
    finally
    {
    }
  }

  public static void setDefaultSearchPath(Name[] paramArrayOfName)
  {
    try
    {
      defaultSearchPath = paramArrayOfName;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public Name[] getAliases()
  {
    checkDone();
    if (this.aliases == null)
      return noAliases;
    return (Name[])this.aliases.toArray(new Name[this.aliases.size()]);
  }

  public Record[] getAnswers()
  {
    checkDone();
    return this.answers;
  }

  public String getErrorString()
  {
    checkDone();
    if (this.error != null)
      return this.error;
    switch (this.result)
    {
    default:
      throw new IllegalStateException("unknown result");
    case 0:
      return "successful";
    case 1:
      return "unrecoverable error";
    case 2:
      return "try again";
    case 3:
      return "host not found";
    case 4:
    }
    return "type not found";
  }

  public int getResult()
  {
    checkDone();
    return this.result;
  }

  public Record[] run()
  {
    if (this.done)
      reset();
    if (this.name.isAbsolute())
    {
      resolve(this.name, null);
      if (!this.done)
      {
        if (!this.badresponse)
          break label174;
        this.result = 2;
        this.error = this.badresponse_error;
        this.done = true;
      }
    }
    while (true)
    {
      return this.answers;
      if (this.searchPath == null)
      {
        resolve(this.name, Name.root);
        break;
      }
      if (this.name.labels() > defaultNdots)
        resolve(this.name, Name.root);
      if (this.done)
        return this.answers;
      for (int i = 0; ; i++)
      {
        if (i >= this.searchPath.length)
          break label172;
        resolve(this.name, this.searchPath[i]);
        if (this.done)
          return this.answers;
        if (this.foundAlias)
          break;
      }
      label172: break;
      label174: if (this.timedout)
      {
        this.result = 2;
        this.error = "timed out";
        this.done = true;
      }
      else if (this.networkerror)
      {
        this.result = 2;
        this.error = "network error";
        this.done = true;
      }
      else if (this.nxdomain)
      {
        this.result = 3;
        this.done = true;
      }
      else if (this.referral)
      {
        this.result = 1;
        this.error = "referral";
        this.done = true;
      }
      else if (this.nametoolong)
      {
        this.result = 1;
        this.error = "name too long";
        this.done = true;
      }
    }
  }

  public void setCache(Cache paramCache)
  {
    if (paramCache == null)
    {
      this.cache = new Cache(this.dclass);
      this.temporary_cache = true;
      return;
    }
    this.cache = paramCache;
    this.temporary_cache = false;
  }

  public void setCredibility(int paramInt)
  {
    this.credibility = paramInt;
  }

  public void setNdots(int paramInt)
  {
    if (paramInt < 0)
      throw new IllegalArgumentException("Illegal ndots value: " + paramInt);
    defaultNdots = paramInt;
  }

  public void setResolver(Resolver paramResolver)
  {
    this.resolver = paramResolver;
  }

  public void setSearchPath(String[] paramArrayOfString)
    throws TextParseException
  {
    if (paramArrayOfString == null)
    {
      this.searchPath = null;
      return;
    }
    Name[] arrayOfName = new Name[paramArrayOfString.length];
    for (int i = 0; i < paramArrayOfString.length; i++)
      arrayOfName[i] = Name.fromString(paramArrayOfString[i], Name.root);
    this.searchPath = arrayOfName;
  }

  public void setSearchPath(Name[] paramArrayOfName)
  {
    this.searchPath = paramArrayOfName;
  }
}