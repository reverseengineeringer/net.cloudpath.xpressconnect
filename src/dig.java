import java.io.IOException;
import java.io.PrintStream;
import org.xbill.DNS.Header;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

public class dig
{
  static int dclass = 1;
  static Name name = null;
  static int type = 1;

  static void doAXFR(Message paramMessage)
    throws IOException
  {
    System.out.println("; java dig 0.0 <> " + name + " axfr");
    if (paramMessage.isSigned())
    {
      System.out.print(";; TSIG ");
      if (!paramMessage.isVerified())
        break label77;
      System.out.println("ok");
    }
    while (paramMessage.getRcode() != 0)
    {
      System.out.println(paramMessage);
      return;
      label77: System.out.println("failed");
    }
    Record[] arrayOfRecord = paramMessage.getSectionArray(1);
    for (int i = 0; i < arrayOfRecord.length; i++)
      System.out.println(arrayOfRecord[i]);
    System.out.print(";; done (");
    System.out.print(paramMessage.getHeader().getCount(1));
    System.out.print(" records, ");
    System.out.print(paramMessage.getHeader().getCount(3));
    System.out.println(" additional)");
  }

  static void doQuery(Message paramMessage, long paramLong)
    throws IOException
  {
    System.out.println("; java dig 0.0");
    System.out.println(paramMessage);
    System.out.println(";; Query time: " + paramLong + " ms");
  }

  // ERROR //
  public static void main(java.lang.String[] paramArrayOfString)
    throws IOException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: iconst_0
    //   3: istore_2
    //   4: aload_0
    //   5: arraylength
    //   6: iconst_1
    //   7: if_icmpge +6 -> 13
    //   10: invokestatic 122	dig:usage	()V
    //   13: aload_0
    //   14: iconst_0
    //   15: aaload
    //   16: ldc 124
    //   18: invokevirtual 130	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   21: istore 10
    //   23: iload 10
    //   25: ifeq +732 -> 757
    //   28: iconst_0
    //   29: iconst_1
    //   30: iadd
    //   31: istore 11
    //   33: aload_0
    //   34: iconst_0
    //   35: aaload
    //   36: iconst_1
    //   37: invokevirtual 134	java/lang/String:substring	(I)Ljava/lang/String;
    //   40: astore 12
    //   42: aconst_null
    //   43: astore_1
    //   44: aload 12
    //   46: ifnull +218 -> 264
    //   49: new 136	org/xbill/DNS/SimpleResolver
    //   52: dup
    //   53: aload 12
    //   55: invokespecial 138	org/xbill/DNS/SimpleResolver:<init>	(Ljava/lang/String;)V
    //   58: astore 13
    //   60: aload 13
    //   62: astore_1
    //   63: iload 11
    //   65: iconst_1
    //   66: iadd
    //   67: istore 14
    //   69: aload_0
    //   70: iload 11
    //   72: aaload
    //   73: astore 15
    //   75: aload 15
    //   77: ldc 140
    //   79: invokevirtual 144	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   82: istore 16
    //   84: iload 16
    //   86: ifeq +193 -> 279
    //   89: iload 14
    //   91: iconst_1
    //   92: iadd
    //   93: istore 11
    //   95: aload_0
    //   96: iload 14
    //   98: aaload
    //   99: invokestatic 150	org/xbill/DNS/ReverseMap:fromAddress	(Ljava/lang/String;)Lorg/xbill/DNS/Name;
    //   102: putstatic 13	dig:name	Lorg/xbill/DNS/Name;
    //   105: bipush 12
    //   107: putstatic 15	dig:type	I
    //   110: iconst_1
    //   111: putstatic 17	dig:dclass	I
    //   114: iload 11
    //   116: istore 14
    //   118: aload_0
    //   119: iload 14
    //   121: aaload
    //   122: ldc 152
    //   124: invokevirtual 130	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   127: ifeq +218 -> 345
    //   130: aload_0
    //   131: iload 14
    //   133: aaload
    //   134: invokevirtual 155	java/lang/String:length	()I
    //   137: iconst_1
    //   138: if_icmple +207 -> 345
    //   141: aload_0
    //   142: iload 14
    //   144: aaload
    //   145: iconst_1
    //   146: invokevirtual 159	java/lang/String:charAt	(I)C
    //   149: tableswitch	default:+91 -> 240, 98:+354->503, 99:+91->240, 100:+570->719, 101:+483->632, 102:+91->240, 103:+91->240, 104:+91->240, 105:+475->624, 106:+91->240, 107:+418->567, 108:+91->240, 109:+91->240, 110:+91->240, 111:+91->240, 112:+284->433, 113:+582->731, 114:+91->240, 115:+91->240, 116:+467->616
    //   241: nop
    //   242: lload_0
    //   243: ldc 161
    //   245: invokevirtual 65	java/io/PrintStream:print	(Ljava/lang/String;)V
    //   248: getstatic 30	java/lang/System:out	Ljava/io/PrintStream;
    //   251: aload_0
    //   252: iload 14
    //   254: aaload
    //   255: invokevirtual 54	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   258: iinc 14 1
    //   261: goto -143 -> 118
    //   264: new 136	org/xbill/DNS/SimpleResolver
    //   267: dup
    //   268: invokespecial 162	org/xbill/DNS/SimpleResolver:<init>	()V
    //   271: astore 29
    //   273: aload 29
    //   275: astore_1
    //   276: goto -213 -> 63
    //   279: aload 15
    //   281: getstatic 167	org/xbill/DNS/Name:root	Lorg/xbill/DNS/Name;
    //   284: invokestatic 171	org/xbill/DNS/Name:fromString	(Ljava/lang/String;Lorg/xbill/DNS/Name;)Lorg/xbill/DNS/Name;
    //   287: putstatic 13	dig:name	Lorg/xbill/DNS/Name;
    //   290: aload_0
    //   291: iload 14
    //   293: aaload
    //   294: invokestatic 177	org/xbill/DNS/Type:value	(Ljava/lang/String;)I
    //   297: putstatic 15	dig:type	I
    //   300: getstatic 15	dig:type	I
    //   303: ifge +116 -> 419
    //   306: iconst_1
    //   307: putstatic 15	dig:type	I
    //   310: aload_0
    //   311: iload 14
    //   313: aaload
    //   314: invokestatic 180	org/xbill/DNS/DClass:value	(Ljava/lang/String;)I
    //   317: putstatic 17	dig:dclass	I
    //   320: getstatic 17	dig:dclass	I
    //   323: ifge +102 -> 425
    //   326: iconst_1
    //   327: putstatic 17	dig:dclass	I
    //   330: iconst_0
    //   331: istore_2
    //   332: goto -214 -> 118
    //   335: astore_3
    //   336: getstatic 13	dig:name	Lorg/xbill/DNS/Name;
    //   339: ifnonnull +6 -> 345
    //   342: invokestatic 122	dig:usage	()V
    //   345: aload_1
    //   346: ifnonnull +11 -> 357
    //   349: new 136	org/xbill/DNS/SimpleResolver
    //   352: dup
    //   353: invokespecial 162	org/xbill/DNS/SimpleResolver:<init>	()V
    //   356: astore_1
    //   357: getstatic 13	dig:name	Lorg/xbill/DNS/Name;
    //   360: getstatic 15	dig:type	I
    //   363: getstatic 17	dig:dclass	I
    //   366: invokestatic 186	org/xbill/DNS/Record:newRecord	(Lorg/xbill/DNS/Name;II)Lorg/xbill/DNS/Record;
    //   369: invokestatic 190	org/xbill/DNS/Message:newQuery	(Lorg/xbill/DNS/Record;)Lorg/xbill/DNS/Message;
    //   372: astore 4
    //   374: iload_2
    //   375: ifeq +11 -> 386
    //   378: getstatic 30	java/lang/System:out	Ljava/io/PrintStream;
    //   381: aload 4
    //   383: invokevirtual 77	java/io/PrintStream:println	(Ljava/lang/Object;)V
    //   386: invokestatic 194	java/lang/System:currentTimeMillis	()J
    //   389: lstore 5
    //   391: aload_1
    //   392: aload 4
    //   394: invokevirtual 198	org/xbill/DNS/SimpleResolver:send	(Lorg/xbill/DNS/Message;)Lorg/xbill/DNS/Message;
    //   397: astore 7
    //   399: invokestatic 194	java/lang/System:currentTimeMillis	()J
    //   402: lstore 8
    //   404: getstatic 15	dig:type	I
    //   407: sipush 252
    //   410: if_icmpne +326 -> 736
    //   413: aload 7
    //   415: invokestatic 200	dig:doAXFR	(Lorg/xbill/DNS/Message;)V
    //   418: return
    //   419: iinc 14 1
    //   422: goto -112 -> 310
    //   425: iinc 14 1
    //   428: iconst_0
    //   429: istore_2
    //   430: goto -312 -> 118
    //   433: aload_0
    //   434: iload 14
    //   436: aaload
    //   437: invokevirtual 155	java/lang/String:length	()I
    //   440: iconst_2
    //   441: if_icmple +41 -> 482
    //   444: aload_0
    //   445: iload 14
    //   447: aaload
    //   448: iconst_2
    //   449: invokevirtual 134	java/lang/String:substring	(I)Ljava/lang/String;
    //   452: astore 25
    //   454: aload 25
    //   456: invokestatic 205	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   459: istore 26
    //   461: iload 26
    //   463: iflt +10 -> 473
    //   466: iload 26
    //   468: ldc 206
    //   470: if_icmple +24 -> 494
    //   473: getstatic 30	java/lang/System:out	Ljava/io/PrintStream;
    //   476: ldc 208
    //   478: invokevirtual 54	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   481: return
    //   482: iinc 14 1
    //   485: aload_0
    //   486: iload 14
    //   488: aaload
    //   489: astore 25
    //   491: goto -37 -> 454
    //   494: aload_1
    //   495: iload 26
    //   497: invokevirtual 211	org/xbill/DNS/SimpleResolver:setPort	(I)V
    //   500: goto -242 -> 258
    //   503: aload_0
    //   504: iload 14
    //   506: aaload
    //   507: invokevirtual 155	java/lang/String:length	()I
    //   510: iconst_2
    //   511: if_icmple +33 -> 544
    //   514: aload_0
    //   515: iload 14
    //   517: aaload
    //   518: iconst_2
    //   519: invokevirtual 134	java/lang/String:substring	(I)Ljava/lang/String;
    //   522: astore 24
    //   524: aload 24
    //   526: astore 21
    //   528: aload 21
    //   530: invokestatic 217	java/net/InetAddress:getByName	(Ljava/lang/String;)Ljava/net/InetAddress;
    //   533: astore 23
    //   535: aload_1
    //   536: aload 23
    //   538: invokevirtual 221	org/xbill/DNS/SimpleResolver:setLocalAddress	(Ljava/net/InetAddress;)V
    //   541: goto -283 -> 258
    //   544: iinc 14 1
    //   547: aload_0
    //   548: iload 14
    //   550: aaload
    //   551: astore 21
    //   553: goto -25 -> 528
    //   556: astore 22
    //   558: getstatic 30	java/lang/System:out	Ljava/io/PrintStream;
    //   561: ldc 223
    //   563: invokevirtual 54	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   566: return
    //   567: aload_0
    //   568: iload 14
    //   570: aaload
    //   571: invokevirtual 155	java/lang/String:length	()I
    //   574: iconst_2
    //   575: if_icmple +29 -> 604
    //   578: aload_0
    //   579: iload 14
    //   581: aaload
    //   582: iconst_2
    //   583: invokevirtual 134	java/lang/String:substring	(I)Ljava/lang/String;
    //   586: astore 19
    //   588: aload 19
    //   590: invokestatic 228	org/xbill/DNS/TSIG:fromString	(Ljava/lang/String;)Lorg/xbill/DNS/TSIG;
    //   593: astore 20
    //   595: aload_1
    //   596: aload 20
    //   598: invokevirtual 232	org/xbill/DNS/SimpleResolver:setTSIGKey	(Lorg/xbill/DNS/TSIG;)V
    //   601: goto -343 -> 258
    //   604: iinc 14 1
    //   607: aload_0
    //   608: iload 14
    //   610: aaload
    //   611: astore 19
    //   613: goto -25 -> 588
    //   616: aload_1
    //   617: iconst_1
    //   618: invokevirtual 236	org/xbill/DNS/SimpleResolver:setTCP	(Z)V
    //   621: goto -363 -> 258
    //   624: aload_1
    //   625: iconst_1
    //   626: invokevirtual 239	org/xbill/DNS/SimpleResolver:setIgnoreTruncation	(Z)V
    //   629: goto -371 -> 258
    //   632: aload_0
    //   633: iload 14
    //   635: aaload
    //   636: invokevirtual 155	java/lang/String:length	()I
    //   639: iconst_2
    //   640: if_icmple +58 -> 698
    //   643: aload_0
    //   644: iload 14
    //   646: aaload
    //   647: iconst_2
    //   648: invokevirtual 134	java/lang/String:substring	(I)Ljava/lang/String;
    //   651: astore 17
    //   653: aload 17
    //   655: invokestatic 205	java/lang/Integer:parseInt	(Ljava/lang/String;)I
    //   658: istore 18
    //   660: iload 18
    //   662: iflt +9 -> 671
    //   665: iload 18
    //   667: iconst_1
    //   668: if_icmple +42 -> 710
    //   671: getstatic 30	java/lang/System:out	Ljava/io/PrintStream;
    //   674: new 32	java/lang/StringBuffer
    //   677: dup
    //   678: invokespecial 33	java/lang/StringBuffer:<init>	()V
    //   681: ldc 241
    //   683: invokevirtual 39	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   686: iload 18
    //   688: invokevirtual 244	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
    //   691: invokevirtual 48	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   694: invokevirtual 54	java/io/PrintStream:println	(Ljava/lang/String;)V
    //   697: return
    //   698: iinc 14 1
    //   701: aload_0
    //   702: iload 14
    //   704: aaload
    //   705: astore 17
    //   707: goto -54 -> 653
    //   710: aload_1
    //   711: iload 18
    //   713: invokevirtual 247	org/xbill/DNS/SimpleResolver:setEDNS	(I)V
    //   716: goto -458 -> 258
    //   719: aload_1
    //   720: iconst_0
    //   721: iconst_0
    //   722: ldc 248
    //   724: aconst_null
    //   725: invokevirtual 251	org/xbill/DNS/SimpleResolver:setEDNS	(IIILjava/util/List;)V
    //   728: goto -470 -> 258
    //   731: iconst_1
    //   732: istore_2
    //   733: goto -475 -> 258
    //   736: aload 7
    //   738: lload 8
    //   740: lload 5
    //   742: lsub
    //   743: invokestatic 253	dig:doQuery	(Lorg/xbill/DNS/Message;J)V
    //   746: return
    //   747: astore 27
    //   749: iload 11
    //   751: pop
    //   752: iconst_0
    //   753: istore_2
    //   754: goto -418 -> 336
    //   757: iconst_0
    //   758: istore 11
    //   760: aconst_null
    //   761: astore 12
    //   763: goto -721 -> 42
    //
    // Exception table:
    //   from	to	target	type
    //   13	23	335	java/lang/ArrayIndexOutOfBoundsException
    //   69	84	335	java/lang/ArrayIndexOutOfBoundsException
    //   118	240	335	java/lang/ArrayIndexOutOfBoundsException
    //   240	258	335	java/lang/ArrayIndexOutOfBoundsException
    //   279	310	335	java/lang/ArrayIndexOutOfBoundsException
    //   310	330	335	java/lang/ArrayIndexOutOfBoundsException
    //   433	454	335	java/lang/ArrayIndexOutOfBoundsException
    //   454	461	335	java/lang/ArrayIndexOutOfBoundsException
    //   473	481	335	java/lang/ArrayIndexOutOfBoundsException
    //   485	491	335	java/lang/ArrayIndexOutOfBoundsException
    //   494	500	335	java/lang/ArrayIndexOutOfBoundsException
    //   503	524	335	java/lang/ArrayIndexOutOfBoundsException
    //   528	535	335	java/lang/ArrayIndexOutOfBoundsException
    //   535	541	335	java/lang/ArrayIndexOutOfBoundsException
    //   547	553	335	java/lang/ArrayIndexOutOfBoundsException
    //   558	566	335	java/lang/ArrayIndexOutOfBoundsException
    //   567	588	335	java/lang/ArrayIndexOutOfBoundsException
    //   588	601	335	java/lang/ArrayIndexOutOfBoundsException
    //   607	613	335	java/lang/ArrayIndexOutOfBoundsException
    //   616	621	335	java/lang/ArrayIndexOutOfBoundsException
    //   624	629	335	java/lang/ArrayIndexOutOfBoundsException
    //   632	653	335	java/lang/ArrayIndexOutOfBoundsException
    //   653	660	335	java/lang/ArrayIndexOutOfBoundsException
    //   671	697	335	java/lang/ArrayIndexOutOfBoundsException
    //   701	707	335	java/lang/ArrayIndexOutOfBoundsException
    //   710	716	335	java/lang/ArrayIndexOutOfBoundsException
    //   719	728	335	java/lang/ArrayIndexOutOfBoundsException
    //   528	535	556	java/lang/Exception
    //   33	42	747	java/lang/ArrayIndexOutOfBoundsException
    //   49	60	747	java/lang/ArrayIndexOutOfBoundsException
    //   95	114	747	java/lang/ArrayIndexOutOfBoundsException
    //   264	273	747	java/lang/ArrayIndexOutOfBoundsException
  }

  static void usage()
  {
    System.out.println("Usage: dig [@server] name [<type>] [<class>] [options]");
    System.exit(0);
  }
}