package jcifs.netbios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Hashtable;
import jcifs.Config;
import jcifs.smb.SmbFileInputStream;
import jcifs.util.LogStream;

public class Lmhosts
{
  private static final String FILENAME = Config.getProperty("jcifs.netbios.lmhosts");
  private static final Hashtable TAB = new Hashtable();
  private static int alt;
  private static long lastModified = 1L;
  private static LogStream log = LogStream.getInstance();

  public static NbtAddress getByName(String paramString)
  {
    try
    {
      NbtAddress localNbtAddress = getByName(new Name(paramString, 32, null));
      return localNbtAddress;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  // ERROR //
  static NbtAddress getByName(Name paramName)
  {
    // Byte code:
    //   0: ldc 2
    //   2: monitorenter
    //   3: getstatic 26	jcifs/netbios/Lmhosts:FILENAME	Ljava/lang/String;
    //   6: astore 9
    //   8: aconst_null
    //   9: astore 4
    //   11: aload 9
    //   13: ifnull +70 -> 83
    //   16: new 60	java/io/File
    //   19: dup
    //   20: getstatic 26	jcifs/netbios/Lmhosts:FILENAME	Ljava/lang/String;
    //   23: invokespecial 63	java/io/File:<init>	(Ljava/lang/String;)V
    //   26: astore 10
    //   28: aload 10
    //   30: invokevirtual 66	java/io/File:lastModified	()J
    //   33: lstore 11
    //   35: lload 11
    //   37: getstatic 35	jcifs/netbios/Lmhosts:lastModified	J
    //   40: lcmp
    //   41: ifle +30 -> 71
    //   44: lload 11
    //   46: putstatic 35	jcifs/netbios/Lmhosts:lastModified	J
    //   49: getstatic 33	jcifs/netbios/Lmhosts:TAB	Ljava/util/Hashtable;
    //   52: invokevirtual 69	java/util/Hashtable:clear	()V
    //   55: iconst_0
    //   56: putstatic 71	jcifs/netbios/Lmhosts:alt	I
    //   59: new 73	java/io/FileReader
    //   62: dup
    //   63: aload 10
    //   65: invokespecial 76	java/io/FileReader:<init>	(Ljava/io/File;)V
    //   68: invokestatic 80	jcifs/netbios/Lmhosts:populate	(Ljava/io/Reader;)V
    //   71: getstatic 33	jcifs/netbios/Lmhosts:TAB	Ljava/util/Hashtable;
    //   74: aload_0
    //   75: invokevirtual 84	java/util/Hashtable:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   78: checkcast 86	jcifs/netbios/NbtAddress
    //   81: astore 4
    //   83: ldc 2
    //   85: monitorexit
    //   86: aload 4
    //   88: areturn
    //   89: astore 6
    //   91: getstatic 43	jcifs/netbios/Lmhosts:log	Ljcifs/util/LogStream;
    //   94: pop
    //   95: getstatic 89	jcifs/util/LogStream:level	I
    //   98: istore 8
    //   100: aconst_null
    //   101: astore 4
    //   103: iload 8
    //   105: iconst_1
    //   106: if_icmple -23 -> 83
    //   109: getstatic 43	jcifs/netbios/Lmhosts:log	Ljcifs/util/LogStream;
    //   112: new 91	java/lang/StringBuffer
    //   115: dup
    //   116: invokespecial 92	java/lang/StringBuffer:<init>	()V
    //   119: ldc 94
    //   121: invokevirtual 98	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   124: getstatic 26	jcifs/netbios/Lmhosts:FILENAME	Ljava/lang/String;
    //   127: invokevirtual 98	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   130: invokevirtual 102	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   133: invokevirtual 105	jcifs/util/LogStream:println	(Ljava/lang/String;)V
    //   136: aload 6
    //   138: getstatic 43	jcifs/netbios/Lmhosts:log	Ljcifs/util/LogStream;
    //   141: invokevirtual 109	java/io/FileNotFoundException:printStackTrace	(Ljava/io/PrintStream;)V
    //   144: aconst_null
    //   145: astore 4
    //   147: goto -64 -> 83
    //   150: astore 5
    //   152: ldc 2
    //   154: monitorexit
    //   155: aload 5
    //   157: athrow
    //   158: astore_1
    //   159: getstatic 43	jcifs/netbios/Lmhosts:log	Ljcifs/util/LogStream;
    //   162: pop
    //   163: getstatic 89	jcifs/util/LogStream:level	I
    //   166: istore_3
    //   167: aconst_null
    //   168: astore 4
    //   170: iload_3
    //   171: ifle -88 -> 83
    //   174: aload_1
    //   175: getstatic 43	jcifs/netbios/Lmhosts:log	Ljcifs/util/LogStream;
    //   178: invokevirtual 110	java/io/IOException:printStackTrace	(Ljava/io/PrintStream;)V
    //   181: aconst_null
    //   182: astore 4
    //   184: goto -101 -> 83
    //
    // Exception table:
    //   from	to	target	type
    //   3	8	89	java/io/FileNotFoundException
    //   16	71	89	java/io/FileNotFoundException
    //   71	83	89	java/io/FileNotFoundException
    //   3	8	150	finally
    //   16	71	150	finally
    //   71	83	150	finally
    //   91	100	150	finally
    //   109	144	150	finally
    //   159	167	150	finally
    //   174	181	150	finally
    //   3	8	158	java/io/IOException
    //   16	71	158	java/io/IOException
    //   71	83	158	java/io/IOException
  }

  static void populate(Reader paramReader)
    throws IOException
  {
    BufferedReader localBufferedReader = new BufferedReader(paramReader);
    while (true)
    {
      String str1 = localBufferedReader.readLine();
      if (str1 == null)
        break;
      String str2 = str1.toUpperCase().trim();
      if (str2.length() != 0)
        if (str2.charAt(0) == '#')
        {
          if (str2.startsWith("#INCLUDE "))
          {
            String str3 = str2.substring(str2.indexOf('\\'));
            String str4 = "smb:" + str3.replace('\\', '/');
            if (alt > 0)
              try
              {
                populate(new InputStreamReader(new SmbFileInputStream(str4)));
                alt = -1 + alt;
                String str5;
                do
                {
                  str5 = localBufferedReader.readLine();
                  if (str5 == null)
                    break;
                }
                while (!str5.toUpperCase().trim().startsWith("#END_ALTERNATE"));
              }
              catch (IOException localIOException)
              {
                log.println("lmhosts URL: " + str4);
                localIOException.printStackTrace(log);
              }
            else
              populate(new InputStreamReader(new SmbFileInputStream(str4)));
          }
          else if (str2.startsWith("#BEGIN_ALTERNATE"))
          {
            alt = 1 + alt;
          }
          else if ((str2.startsWith("#END_ALTERNATE")) && (alt > 0))
          {
            alt = -1 + alt;
            throw new IOException("no lmhosts alternate includes loaded");
          }
        }
        else if (Character.isDigit(str2.charAt(0)))
        {
          char[] arrayOfChar = str2.toCharArray();
          int i = 46;
          int j = 0;
          int k = 0;
          while (true)
          {
            int m = arrayOfChar.length;
            if ((j >= m) || (i != 46))
              break;
            int i3 = 0;
            while (true)
            {
              int i4 = arrayOfChar.length;
              if (j >= i4)
                break;
              i = arrayOfChar[j];
              if ((i < 48) || (i > 57))
                break;
              i3 = -48 + (i + i3 * 10);
              j++;
            }
            k = i3 + (k << 8);
            j++;
          }
          while (true)
          {
            int n = arrayOfChar.length;
            if ((j >= n) || (!Character.isWhitespace(arrayOfChar[j])))
              break;
            j++;
          }
          for (int i1 = j; ; i1++)
          {
            int i2 = arrayOfChar.length;
            if ((i1 >= i2) || (Character.isWhitespace(arrayOfChar[i1])))
              break;
          }
          Name localName = new Name(str2.substring(j, i1), 32, null);
          NbtAddress localNbtAddress = new NbtAddress(localName, k, false, 0, false, false, true, true, NbtAddress.UNKNOWN_MAC_ADDRESS);
          TAB.put(localName, localNbtAddress);
        }
    }
  }
}