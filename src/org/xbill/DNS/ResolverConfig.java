package org.xbill.DNS;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class ResolverConfig
{
  static Class class$org$xbill$DNS$ResolverConfig;
  private static ResolverConfig currentConfig;
  private int ndots = -1;
  private Name[] searchlist = null;
  private String[] servers = null;

  static
  {
    refresh();
  }

  public ResolverConfig()
  {
    if (findProperty());
    while ((findSunJVM()) || ((this.servers != null) && (this.searchlist != null)))
      return;
    String str1 = System.getProperty("os.name");
    String str2 = System.getProperty("java.vendor");
    if (str1.indexOf("Windows") != -1)
    {
      if ((str1.indexOf("95") != -1) || (str1.indexOf("98") != -1) || (str1.indexOf("ME") != -1))
      {
        find95();
        return;
      }
      findNT();
      return;
    }
    if (str1.indexOf("NetWare") != -1)
    {
      findNetware();
      return;
    }
    if (str2.indexOf("Android") != -1)
    {
      findAndroid();
      return;
    }
    findUnix();
  }

  private void addSearch(String paramString, List paramList)
  {
    if (Options.check("verbose"))
      System.out.println("adding search " + paramString);
    Name localName;
    try
    {
      localName = Name.fromString(paramString, Name.root);
      if (paramList.contains(localName))
        return;
    }
    catch (TextParseException localTextParseException)
    {
      return;
    }
    paramList.add(localName);
  }

  private void addServer(String paramString, List paramList)
  {
    if (paramList.contains(paramString))
      return;
    if (Options.check("verbose"))
      System.out.println("adding server " + paramString);
    paramList.add(paramString);
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

  private void configureFromLists(List paramList1, List paramList2)
  {
    if ((this.servers == null) && (paramList1.size() > 0))
      this.servers = ((String[])paramList1.toArray(new String[0]));
    if ((this.searchlist == null) && (paramList2.size() > 0))
      this.searchlist = ((Name[])paramList2.toArray(new Name[0]));
  }

  private void configureNdots(int paramInt)
  {
    if ((this.ndots < 0) && (paramInt > 0))
      this.ndots = paramInt;
  }

  private void find95()
  {
    try
    {
      Runtime.getRuntime().exec("winipcfg /all /batch " + "winipcfg.out").waitFor();
      findWin(new FileInputStream(new File("winipcfg.out")));
      new File("winipcfg.out").delete();
      return;
    }
    catch (Exception localException)
    {
    }
  }

  private void findAndroid()
  {
    try
    {
      ArrayList localArrayList1 = new ArrayList();
      ArrayList localArrayList2 = new ArrayList();
      BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("getprop").getInputStream()));
      while (true)
      {
        String str1 = localBufferedReader.readLine();
        if (str1 == null)
          break;
        StringTokenizer localStringTokenizer = new StringTokenizer(str1, ":");
        if (localStringTokenizer.nextToken().indexOf("net.dns") > -1)
        {
          String str2 = localStringTokenizer.nextToken().replaceAll("[ \\[\\]]", "");
          if (((str2.matches("^\\d+(\\.\\d+){3}$")) || (str2.matches("^[0-9a-f]+(:[0-9a-f]*)+:[0-9a-f]+$"))) && (!localArrayList1.contains(str2)))
            localArrayList1.add(str2);
        }
      }
      configureFromLists(localArrayList1, localArrayList2);
      return;
    }
    catch (Exception localException)
    {
    }
  }

  private void findNT()
  {
    try
    {
      Process localProcess = Runtime.getRuntime().exec("ipconfig /all");
      findWin(localProcess.getInputStream());
      localProcess.destroy();
      return;
    }
    catch (Exception localException)
    {
    }
  }

  private void findNetware()
  {
    findResolvConf("sys:/etc/resolv.cfg");
  }

  private boolean findProperty()
  {
    ArrayList localArrayList1 = new ArrayList(0);
    ArrayList localArrayList2 = new ArrayList(0);
    String str1 = System.getProperty("dns.server");
    if (str1 != null)
    {
      StringTokenizer localStringTokenizer1 = new StringTokenizer(str1, ",");
      while (localStringTokenizer1.hasMoreTokens())
        addServer(localStringTokenizer1.nextToken(), localArrayList1);
    }
    String str2 = System.getProperty("dns.search");
    if (str2 != null)
    {
      StringTokenizer localStringTokenizer2 = new StringTokenizer(str2, ",");
      while (localStringTokenizer2.hasMoreTokens())
        addSearch(localStringTokenizer2.nextToken(), localArrayList2);
    }
    configureFromLists(localArrayList1, localArrayList2);
    String[] arrayOfString = this.servers;
    boolean bool = false;
    if (arrayOfString != null)
    {
      Name[] arrayOfName = this.searchlist;
      bool = false;
      if (arrayOfName != null)
        bool = true;
    }
    return bool;
  }

  private void findResolvConf(String paramString)
  {
    label313: 
    while (true)
    {
      BufferedReader localBufferedReader;
      ArrayList localArrayList2;
      int i;
      String str1;
      try
      {
        FileInputStream localFileInputStream = new FileInputStream(paramString);
        localBufferedReader = new BufferedReader(new InputStreamReader(localFileInputStream));
        ArrayList localArrayList1 = new ArrayList(0);
        localArrayList2 = new ArrayList(0);
        i = -1;
        try
        {
          str1 = localBufferedReader.readLine();
          if (str1 == null)
            break label313;
          if (!str1.startsWith("nameserver"))
            break label121;
          StringTokenizer localStringTokenizer1 = new StringTokenizer(str1);
          localStringTokenizer1.nextToken();
          addServer(localStringTokenizer1.nextToken(), localArrayList1);
          continue;
        }
        catch (IOException localIOException)
        {
        }
        configureFromLists(localArrayList1, localArrayList2);
        configureNdots(i);
        return;
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        return;
      }
      label121: if (str1.startsWith("domain"))
      {
        StringTokenizer localStringTokenizer2 = new StringTokenizer(str1);
        localStringTokenizer2.nextToken();
        if ((localStringTokenizer2.hasMoreTokens()) && (localArrayList2.isEmpty()))
          addSearch(localStringTokenizer2.nextToken(), localArrayList2);
      }
      else
      {
        if (str1.startsWith("search"))
        {
          if (!localArrayList2.isEmpty())
            localArrayList2.clear();
          StringTokenizer localStringTokenizer4 = new StringTokenizer(str1);
          localStringTokenizer4.nextToken();
          while (localStringTokenizer4.hasMoreTokens())
            addSearch(localStringTokenizer4.nextToken(), localArrayList2);
        }
        if (str1.startsWith("options"))
        {
          StringTokenizer localStringTokenizer3 = new StringTokenizer(str1);
          localStringTokenizer3.nextToken();
          while (localStringTokenizer3.hasMoreTokens())
          {
            String str2 = localStringTokenizer3.nextToken();
            if (str2.startsWith("ndots:"))
              i = parseNdots(str2);
          }
          localBufferedReader.close();
        }
      }
    }
  }

  private boolean findSunJVM()
  {
    ArrayList localArrayList1 = new ArrayList(0);
    ArrayList localArrayList2 = new ArrayList(0);
    List localList1;
    List localList2;
    try
    {
      Class[] arrayOfClass = new Class[0];
      Object[] arrayOfObject = new Object[0];
      Class localClass = Class.forName("sun.net.dns.ResolverConfiguration");
      Object localObject = localClass.getDeclaredMethod("open", arrayOfClass).invoke(null, arrayOfObject);
      localList1 = (List)localClass.getMethod("nameservers", arrayOfClass).invoke(localObject, arrayOfObject);
      localList2 = (List)localClass.getMethod("searchlist", arrayOfClass).invoke(localObject, arrayOfObject);
      if (localList1.size() == 0)
        return false;
    }
    catch (Exception localException)
    {
      return false;
    }
    if (localList1.size() > 0)
    {
      Iterator localIterator2 = localList1.iterator();
      while (localIterator2.hasNext())
        addServer((String)localIterator2.next(), localArrayList1);
    }
    if (localList2.size() > 0)
    {
      Iterator localIterator1 = localList2.iterator();
      while (localIterator1.hasNext())
        addSearch((String)localIterator1.next(), localArrayList2);
    }
    configureFromLists(localArrayList1, localArrayList2);
    return true;
  }

  private void findUnix()
  {
    findResolvConf("/etc/resolv.conf");
  }

  private void findWin(InputStream paramInputStream)
  {
    int i = Integer.getInteger("org.xbill.DNS.windows.parse.buffer", 8192).intValue();
    BufferedInputStream localBufferedInputStream = new BufferedInputStream(paramInputStream, i);
    localBufferedInputStream.mark(i);
    findWin(localBufferedInputStream, null);
    if (this.servers == null);
    try
    {
      localBufferedInputStream.reset();
      findWin(localBufferedInputStream, new Locale("", ""));
      return;
    }
    catch (IOException localIOException)
    {
    }
  }

  // ERROR //
  private void findWin(InputStream paramInputStream, Locale paramLocale)
  {
    // Byte code:
    //   0: getstatic 377	org/xbill/DNS/ResolverConfig:class$org$xbill$DNS$ResolverConfig	Ljava/lang/Class;
    //   3: ifnonnull +187 -> 190
    //   6: ldc_w 379
    //   9: invokestatic 381	org/xbill/DNS/ResolverConfig:class$	(Ljava/lang/String;)Ljava/lang/Class;
    //   12: astore_3
    //   13: aload_3
    //   14: putstatic 377	org/xbill/DNS/ResolverConfig:class$org$xbill$DNS$ResolverConfig	Ljava/lang/Class;
    //   17: aload_3
    //   18: invokevirtual 385	java/lang/Class:getPackage	()Ljava/lang/Package;
    //   21: invokevirtual 390	java/lang/Package:getName	()Ljava/lang/String;
    //   24: astore 4
    //   26: new 96	java/lang/StringBuffer
    //   29: dup
    //   30: invokespecial 97	java/lang/StringBuffer:<init>	()V
    //   33: aload 4
    //   35: invokevirtual 103	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   38: ldc_w 392
    //   41: invokevirtual 103	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   44: invokevirtual 107	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   47: astore 5
    //   49: aload_2
    //   50: ifnull +147 -> 197
    //   53: aload 5
    //   55: aload_2
    //   56: invokestatic 398	java/util/ResourceBundle:getBundle	(Ljava/lang/String;Ljava/util/Locale;)Ljava/util/ResourceBundle;
    //   59: astore 6
    //   61: aload 6
    //   63: ldc_w 400
    //   66: invokevirtual 403	java/util/ResourceBundle:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   69: astore 7
    //   71: aload 6
    //   73: ldc_w 405
    //   76: invokevirtual 403	java/util/ResourceBundle:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   79: astore 8
    //   81: aload 6
    //   83: ldc_w 407
    //   86: invokevirtual 403	java/util/ResourceBundle:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   89: astore 9
    //   91: aload 6
    //   93: ldc_w 409
    //   96: invokevirtual 403	java/util/ResourceBundle:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   99: astore 10
    //   101: new 209	java/io/InputStreamReader
    //   104: dup
    //   105: aload_1
    //   106: invokespecial 217	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
    //   109: astore 11
    //   111: new 207	java/io/BufferedReader
    //   114: dup
    //   115: aload 11
    //   117: invokespecial 220	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   120: astore 12
    //   122: new 204	java/util/ArrayList
    //   125: dup
    //   126: invokespecial 205	java/util/ArrayList:<init>	()V
    //   129: astore 13
    //   131: new 204	java/util/ArrayList
    //   134: dup
    //   135: invokespecial 205	java/util/ArrayList:<init>	()V
    //   138: astore 14
    //   140: iconst_0
    //   141: istore 15
    //   143: iconst_0
    //   144: istore 16
    //   146: aload 12
    //   148: invokevirtual 223	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   151: astore 18
    //   153: aload 18
    //   155: ifnull +306 -> 461
    //   158: new 225	java/util/StringTokenizer
    //   161: dup
    //   162: aload 18
    //   164: invokespecial 290	java/util/StringTokenizer:<init>	(Ljava/lang/String;)V
    //   167: astore 19
    //   169: aload 19
    //   171: invokevirtual 273	java/util/StringTokenizer:hasMoreTokens	()Z
    //   174: istore 20
    //   176: iload 20
    //   178: ifne +29 -> 207
    //   181: iconst_0
    //   182: istore 16
    //   184: iconst_0
    //   185: istore 15
    //   187: goto -41 -> 146
    //   190: getstatic 377	org/xbill/DNS/ResolverConfig:class$org$xbill$DNS$ResolverConfig	Ljava/lang/Class;
    //   193: astore_3
    //   194: goto -177 -> 17
    //   197: aload 5
    //   199: invokestatic 412	java/util/ResourceBundle:getBundle	(Ljava/lang/String;)Ljava/util/ResourceBundle;
    //   202: astore 6
    //   204: goto -143 -> 61
    //   207: aload 19
    //   209: invokevirtual 233	java/util/StringTokenizer:nextToken	()Ljava/lang/String;
    //   212: astore 21
    //   214: aload 18
    //   216: ldc 227
    //   218: invokevirtual 53	java/lang/String:indexOf	(Ljava/lang/String;)I
    //   221: iconst_m1
    //   222: if_icmpeq +9 -> 231
    //   225: iconst_0
    //   226: istore 15
    //   228: iconst_0
    //   229: istore 16
    //   231: aload 18
    //   233: aload 7
    //   235: invokevirtual 53	java/lang/String:indexOf	(Ljava/lang/String;)I
    //   238: iconst_m1
    //   239: if_icmpeq +53 -> 292
    //   242: aload 19
    //   244: invokevirtual 273	java/util/StringTokenizer:hasMoreTokens	()Z
    //   247: ifeq +17 -> 264
    //   250: aload 19
    //   252: invokevirtual 233	java/util/StringTokenizer:nextToken	()Ljava/lang/String;
    //   255: astore 24
    //   257: aload 24
    //   259: astore 21
    //   261: goto -19 -> 242
    //   264: aload 21
    //   266: aconst_null
    //   267: invokestatic 123	org/xbill/DNS/Name:fromString	(Ljava/lang/String;Lorg/xbill/DNS/Name;)Lorg/xbill/DNS/Name;
    //   270: astore 23
    //   272: aload 23
    //   274: invokevirtual 415	org/xbill/DNS/Name:labels	()I
    //   277: iconst_1
    //   278: if_icmpeq -132 -> 146
    //   281: aload_0
    //   282: aload 21
    //   284: aload 14
    //   286: invokespecial 279	org/xbill/DNS/ResolverConfig:addSearch	(Ljava/lang/String;Ljava/util/List;)V
    //   289: goto -143 -> 146
    //   292: aload 18
    //   294: aload 8
    //   296: invokevirtual 53	java/lang/String:indexOf	(Ljava/lang/String;)I
    //   299: iconst_m1
    //   300: if_icmpeq +45 -> 345
    //   303: aload 19
    //   305: invokevirtual 273	java/util/StringTokenizer:hasMoreTokens	()Z
    //   308: ifeq +13 -> 321
    //   311: aload 19
    //   313: invokevirtual 233	java/util/StringTokenizer:nextToken	()Ljava/lang/String;
    //   316: astore 21
    //   318: goto -15 -> 303
    //   321: aload 21
    //   323: ldc 227
    //   325: invokevirtual 418	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   328: ifne -182 -> 146
    //   331: aload_0
    //   332: aload 21
    //   334: aload 14
    //   336: invokespecial 279	org/xbill/DNS/ResolverConfig:addSearch	(Ljava/lang/String;Ljava/util/List;)V
    //   339: iconst_1
    //   340: istore 16
    //   342: goto -196 -> 146
    //   345: iload 16
    //   347: ifne +14 -> 361
    //   350: aload 18
    //   352: aload 9
    //   354: invokevirtual 53	java/lang/String:indexOf	(Ljava/lang/String;)I
    //   357: iconst_m1
    //   358: if_icmpeq +45 -> 403
    //   361: aload 19
    //   363: invokevirtual 273	java/util/StringTokenizer:hasMoreTokens	()Z
    //   366: ifeq +13 -> 379
    //   369: aload 19
    //   371: invokevirtual 233	java/util/StringTokenizer:nextToken	()Ljava/lang/String;
    //   374: astore 21
    //   376: goto -15 -> 361
    //   379: aload 21
    //   381: ldc 227
    //   383: invokevirtual 418	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   386: ifne -240 -> 146
    //   389: aload_0
    //   390: aload 21
    //   392: aload 14
    //   394: invokespecial 279	org/xbill/DNS/ResolverConfig:addSearch	(Ljava/lang/String;Ljava/util/List;)V
    //   397: iconst_1
    //   398: istore 16
    //   400: goto -254 -> 146
    //   403: iload 15
    //   405: ifne +14 -> 419
    //   408: aload 18
    //   410: aload 10
    //   412: invokevirtual 53	java/lang/String:indexOf	(Ljava/lang/String;)I
    //   415: iconst_m1
    //   416: if_icmpeq -270 -> 146
    //   419: aload 19
    //   421: invokevirtual 273	java/util/StringTokenizer:hasMoreTokens	()Z
    //   424: ifeq +13 -> 437
    //   427: aload 19
    //   429: invokevirtual 233	java/util/StringTokenizer:nextToken	()Ljava/lang/String;
    //   432: astore 21
    //   434: goto -15 -> 419
    //   437: aload 21
    //   439: ldc 227
    //   441: invokevirtual 418	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   444: ifne -298 -> 146
    //   447: aload_0
    //   448: aload 21
    //   450: aload 13
    //   452: invokespecial 275	org/xbill/DNS/ResolverConfig:addServer	(Ljava/lang/String;Ljava/util/List;)V
    //   455: iconst_1
    //   456: istore 15
    //   458: goto -312 -> 146
    //   461: aload_0
    //   462: aload 13
    //   464: aload 14
    //   466: invokespecial 254	org/xbill/DNS/ResolverConfig:configureFromLists	(Ljava/util/List;Ljava/util/List;)V
    //   469: return
    //   470: astore 17
    //   472: return
    //   473: astore 22
    //   475: goto -329 -> 146
    //
    // Exception table:
    //   from	to	target	type
    //   122	140	470	java/io/IOException
    //   146	153	470	java/io/IOException
    //   158	176	470	java/io/IOException
    //   207	225	470	java/io/IOException
    //   231	242	470	java/io/IOException
    //   242	257	470	java/io/IOException
    //   264	272	470	java/io/IOException
    //   272	289	470	java/io/IOException
    //   292	303	470	java/io/IOException
    //   303	318	470	java/io/IOException
    //   321	339	470	java/io/IOException
    //   350	361	470	java/io/IOException
    //   361	376	470	java/io/IOException
    //   379	397	470	java/io/IOException
    //   408	419	470	java/io/IOException
    //   419	434	470	java/io/IOException
    //   437	455	470	java/io/IOException
    //   461	469	470	java/io/IOException
    //   264	272	473	org/xbill/DNS/TextParseException
  }

  public static ResolverConfig getCurrentConfig()
  {
    try
    {
      ResolverConfig localResolverConfig = currentConfig;
      return localResolverConfig;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  private int parseNdots(String paramString)
  {
    String str = paramString.substring(6);
    try
    {
      int i = Integer.parseInt(str);
      if (i >= 0)
      {
        if (Options.check("verbose"))
          System.out.println("setting ndots " + str);
        return i;
      }
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    return -1;
  }

  public static void refresh()
  {
    ResolverConfig localResolverConfig = new ResolverConfig();
    Class localClass;
    if (class$org$xbill$DNS$ResolverConfig == null)
    {
      localClass = class$("org.xbill.DNS.ResolverConfig");
      class$org$xbill$DNS$ResolverConfig = localClass;
    }
    while (true)
      try
      {
        currentConfig = localResolverConfig;
        return;
        localClass = class$org$xbill$DNS$ResolverConfig;
      }
      finally
      {
      }
  }

  public int ndots()
  {
    if (this.ndots < 0)
      return 1;
    return this.ndots;
  }

  public Name[] searchPath()
  {
    return this.searchlist;
  }

  public String server()
  {
    if (this.servers == null)
      return null;
    return this.servers[0];
  }

  public String[] servers()
  {
    return this.servers;
  }
}