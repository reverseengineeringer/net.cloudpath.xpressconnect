package org.xbill.DNS;

import java.io.PrintStream;
import java.util.Date;
import org.xbill.DNS.utils.HMAC;
import org.xbill.DNS.utils.base64;

public class TSIG
{
  public static final short FUDGE = 300;
  public static final Name HMAC = HMAC_MD5;
  public static final Name HMAC_MD5 = Name.fromConstantString("HMAC-MD5.SIG-ALG.REG.INT.");
  private static final String HMAC_MD5_STR = "HMAC-MD5.SIG-ALG.REG.INT.";
  public static final Name HMAC_SHA1 = Name.fromConstantString("hmac-sha1.");
  private static final String HMAC_SHA1_STR = "hmac-sha1.";
  public static final Name HMAC_SHA224 = Name.fromConstantString("hmac-sha224.");
  private static final String HMAC_SHA224_STR = "hmac-sha224.";
  public static final Name HMAC_SHA256 = Name.fromConstantString("hmac-sha256.");
  private static final String HMAC_SHA256_STR = "hmac-sha256.";
  public static final Name HMAC_SHA384 = Name.fromConstantString("hmac-sha384.");
  private static final String HMAC_SHA384_STR = "hmac-sha384.";
  public static final Name HMAC_SHA512 = Name.fromConstantString("hmac-sha512.");
  private static final String HMAC_SHA512_STR = "hmac-sha512.";
  private Name alg;
  private String digest;
  private int digestBlockLength;
  private byte[] key;
  private Name name;

  public TSIG(String paramString1, String paramString2)
  {
    this(HMAC_MD5, paramString1, paramString2);
  }

  public TSIG(String paramString1, String paramString2, String paramString3)
  {
    this(HMAC_MD5, paramString2, paramString3);
    if (paramString1.equalsIgnoreCase("hmac-md5"))
      this.alg = HMAC_MD5;
    while (true)
    {
      getDigest();
      return;
      if (paramString1.equalsIgnoreCase("hmac-sha1"))
      {
        this.alg = HMAC_SHA1;
      }
      else if (paramString1.equalsIgnoreCase("hmac-sha224"))
      {
        this.alg = HMAC_SHA224;
      }
      else if (paramString1.equalsIgnoreCase("hmac-sha256"))
      {
        this.alg = HMAC_SHA256;
      }
      else if (paramString1.equalsIgnoreCase("hmac-sha384"))
      {
        this.alg = HMAC_SHA384;
      }
      else
      {
        if (!paramString1.equalsIgnoreCase("hmac-sha512"))
          break;
        this.alg = HMAC_SHA512;
      }
    }
    throw new IllegalArgumentException("Invalid TSIG algorithm");
  }

  public TSIG(Name paramName, String paramString1, String paramString2)
  {
    this.key = base64.fromString(paramString2);
    if (this.key == null)
      throw new IllegalArgumentException("Invalid TSIG key string");
    try
    {
      this.name = Name.fromString(paramString1, Name.root);
      this.alg = paramName;
      getDigest();
      return;
    }
    catch (TextParseException localTextParseException)
    {
    }
    throw new IllegalArgumentException("Invalid TSIG key name");
  }

  public TSIG(Name paramName1, Name paramName2, byte[] paramArrayOfByte)
  {
    this.name = paramName2;
    this.alg = paramName1;
    this.key = paramArrayOfByte;
    getDigest();
  }

  public TSIG(Name paramName, byte[] paramArrayOfByte)
  {
    this(HMAC_MD5, paramName, paramArrayOfByte);
  }

  public static TSIG fromString(String paramString)
  {
    String[] arrayOfString = paramString.split("[:/]", 3);
    if (arrayOfString.length < 2)
      throw new IllegalArgumentException("Invalid TSIG key specification");
    if (arrayOfString.length == 3)
      try
      {
        TSIG localTSIG = new TSIG(arrayOfString[0], arrayOfString[1], arrayOfString[2]);
        return localTSIG;
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        arrayOfString = paramString.split("[:/]", 2);
      }
    return new TSIG(HMAC_MD5, arrayOfString[0], arrayOfString[1]);
  }

  private void getDigest()
  {
    if (this.alg.equals(HMAC_MD5))
    {
      this.digest = "md5";
      this.digestBlockLength = 64;
      return;
    }
    if (this.alg.equals(HMAC_SHA1))
    {
      this.digest = "sha-1";
      this.digestBlockLength = 64;
      return;
    }
    if (this.alg.equals(HMAC_SHA224))
    {
      this.digest = "sha-224";
      this.digestBlockLength = 64;
      return;
    }
    if (this.alg.equals(HMAC_SHA256))
    {
      this.digest = "sha-256";
      this.digestBlockLength = 64;
      return;
    }
    if (this.alg.equals(HMAC_SHA512))
    {
      this.digest = "sha-512";
      this.digestBlockLength = 128;
      return;
    }
    if (this.alg.equals(HMAC_SHA384))
    {
      this.digest = "sha-384";
      this.digestBlockLength = 128;
      return;
    }
    throw new IllegalArgumentException("Invalid algorithm");
  }

  public void apply(Message paramMessage, int paramInt, TSIGRecord paramTSIGRecord)
  {
    paramMessage.addRecord(generate(paramMessage, paramMessage.toWire(), paramInt, paramTSIGRecord), 3);
    paramMessage.tsigState = 3;
  }

  public void apply(Message paramMessage, TSIGRecord paramTSIGRecord)
  {
    apply(paramMessage, 0, paramTSIGRecord);
  }

  public void applyStream(Message paramMessage, TSIGRecord paramTSIGRecord, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      apply(paramMessage, paramTSIGRecord);
      return;
    }
    Date localDate = new Date();
    HMAC localHMAC = new HMAC(this.digest, this.digestBlockLength, this.key);
    int i = Options.intValue("tsigfudge");
    if ((i < 0) || (i > 32767))
      i = 300;
    DNSOutput localDNSOutput1 = new DNSOutput();
    localDNSOutput1.writeU16(paramTSIGRecord.getSignature().length);
    localHMAC.update(localDNSOutput1.toByteArray());
    localHMAC.update(paramTSIGRecord.getSignature());
    localHMAC.update(paramMessage.toWire());
    DNSOutput localDNSOutput2 = new DNSOutput();
    long l1 = localDate.getTime() / 1000L;
    int j = (int)(l1 >> 32);
    long l2 = l1 & 0xFFFFFFFF;
    localDNSOutput2.writeU16(j);
    localDNSOutput2.writeU32(l2);
    localDNSOutput2.writeU16(i);
    localHMAC.update(localDNSOutput2.toByteArray());
    byte[] arrayOfByte = localHMAC.sign();
    paramMessage.addRecord(new TSIGRecord(this.name, 255, 0L, this.alg, localDate, i, arrayOfByte, paramMessage.getHeader().getID(), 0, null), 3);
    paramMessage.tsigState = 3;
  }

  public TSIGRecord generate(Message paramMessage, byte[] paramArrayOfByte, int paramInt, TSIGRecord paramTSIGRecord)
  {
    Date localDate;
    HMAC localHMAC;
    int i;
    if (paramInt != 18)
    {
      localDate = new Date();
      if (paramInt != 0)
      {
        localHMAC = null;
        if (paramInt != 18);
      }
      else
      {
        localHMAC = new HMAC(this.digest, this.digestBlockLength, this.key);
      }
      i = Options.intValue("tsigfudge");
      if ((i < 0) || (i > 32767))
        i = 300;
      if (paramTSIGRecord != null)
      {
        DNSOutput localDNSOutput1 = new DNSOutput();
        localDNSOutput1.writeU16(paramTSIGRecord.getSignature().length);
        if (localHMAC != null)
        {
          localHMAC.update(localDNSOutput1.toByteArray());
          localHMAC.update(paramTSIGRecord.getSignature());
        }
      }
      if (localHMAC != null)
        localHMAC.update(paramArrayOfByte);
      DNSOutput localDNSOutput2 = new DNSOutput();
      this.name.toWireCanonical(localDNSOutput2);
      localDNSOutput2.writeU16(255);
      localDNSOutput2.writeU32(0L);
      this.alg.toWireCanonical(localDNSOutput2);
      long l1 = localDate.getTime() / 1000L;
      int j = (int)(l1 >> 32);
      long l2 = l1 & 0xFFFFFFFF;
      localDNSOutput2.writeU16(j);
      localDNSOutput2.writeU32(l2);
      localDNSOutput2.writeU16(i);
      localDNSOutput2.writeU16(paramInt);
      localDNSOutput2.writeU16(0);
      if (localHMAC != null)
        localHMAC.update(localDNSOutput2.toByteArray());
      if (localHMAC == null)
        break label380;
    }
    label380: for (byte[] arrayOfByte1 = localHMAC.sign(); ; arrayOfByte1 = new byte[0])
    {
      byte[] arrayOfByte2 = null;
      if (paramInt == 18)
      {
        DNSOutput localDNSOutput3 = new DNSOutput();
        long l3 = new Date().getTime() / 1000L;
        int k = (int)(l3 >> 32);
        long l4 = l3 & 0xFFFFFFFF;
        localDNSOutput3.writeU16(k);
        localDNSOutput3.writeU32(l4);
        arrayOfByte2 = localDNSOutput3.toByteArray();
      }
      return new TSIGRecord(this.name, 255, 0L, this.alg, localDate, i, arrayOfByte1, paramMessage.getHeader().getID(), paramInt, arrayOfByte2);
      localDate = paramTSIGRecord.getTimeSigned();
      break;
    }
  }

  public int recordLength()
  {
    return 8 + (4 + (18 + (8 + (10 + this.name.length() + this.alg.length()))));
  }

  public byte verify(Message paramMessage, byte[] paramArrayOfByte, int paramInt, TSIGRecord paramTSIGRecord)
  {
    paramMessage.tsigState = 4;
    TSIGRecord localTSIGRecord = paramMessage.getTSIG();
    HMAC localHMAC = new HMAC(this.digest, this.digestBlockLength, this.key);
    if (localTSIGRecord == null)
      return 1;
    if ((!localTSIGRecord.getName().equals(this.name)) || (!localTSIGRecord.getAlgorithm().equals(this.alg)))
    {
      if (Options.check("verbose"))
        System.err.println("BADKEY failure");
      return 17;
    }
    long l1 = System.currentTimeMillis();
    long l2 = localTSIGRecord.getTimeSigned().getTime();
    long l3 = localTSIGRecord.getFudge();
    if (Math.abs(l1 - l2) > 1000L * l3)
    {
      if (Options.check("verbose"))
        System.err.println("BADTIME failure");
      return 18;
    }
    if ((paramTSIGRecord != null) && (localTSIGRecord.getError() != 17) && (localTSIGRecord.getError() != 16))
    {
      DNSOutput localDNSOutput2 = new DNSOutput();
      localDNSOutput2.writeU16(paramTSIGRecord.getSignature().length);
      localHMAC.update(localDNSOutput2.toByteArray());
      localHMAC.update(paramTSIGRecord.getSignature());
    }
    paramMessage.getHeader().decCount(3);
    byte[] arrayOfByte1 = paramMessage.getHeader().toWire();
    paramMessage.getHeader().incCount(3);
    localHMAC.update(arrayOfByte1);
    int i = paramMessage.tsigstart - arrayOfByte1.length;
    localHMAC.update(paramArrayOfByte, arrayOfByte1.length, i);
    DNSOutput localDNSOutput1 = new DNSOutput();
    localTSIGRecord.getName().toWireCanonical(localDNSOutput1);
    localDNSOutput1.writeU16(localTSIGRecord.dclass);
    localDNSOutput1.writeU32(localTSIGRecord.ttl);
    localTSIGRecord.getAlgorithm().toWireCanonical(localDNSOutput1);
    long l4 = localTSIGRecord.getTimeSigned().getTime() / 1000L;
    int j = (int)(l4 >> 32);
    long l5 = l4 & 0xFFFFFFFF;
    localDNSOutput1.writeU16(j);
    localDNSOutput1.writeU32(l5);
    localDNSOutput1.writeU16(localTSIGRecord.getFudge());
    localDNSOutput1.writeU16(localTSIGRecord.getError());
    byte[] arrayOfByte2;
    int k;
    if (localTSIGRecord.getOther() != null)
    {
      localDNSOutput1.writeU16(localTSIGRecord.getOther().length);
      localDNSOutput1.writeByteArray(localTSIGRecord.getOther());
      localHMAC.update(localDNSOutput1.toByteArray());
      arrayOfByte2 = localTSIGRecord.getSignature();
      k = localHMAC.digestLength();
      if (!this.digest.equals("md5"))
        break label490;
    }
    label490: for (int m = 10; ; m = k / 2)
    {
      if (arrayOfByte2.length <= k)
        break label499;
      if (Options.check("verbose"))
        System.err.println("BADSIG: signature too long");
      return 16;
      localDNSOutput1.writeU16(0);
      break;
    }
    label499: if (arrayOfByte2.length < m)
    {
      if (Options.check("verbose"))
        System.err.println("BADSIG: signature too short");
      return 16;
    }
    if (!localHMAC.verify(arrayOfByte2, true))
    {
      if (Options.check("verbose"))
        System.err.println("BADSIG: signature verification");
      return 16;
    }
    paramMessage.tsigState = 1;
    return 0;
  }

  public int verify(Message paramMessage, byte[] paramArrayOfByte, TSIGRecord paramTSIGRecord)
  {
    return verify(paramMessage, paramArrayOfByte, paramArrayOfByte.length, paramTSIGRecord);
  }

  public static class StreamVerifier
  {
    private TSIG key;
    private TSIGRecord lastTSIG;
    private int lastsigned;
    private int nresponses;
    private HMAC verifier;

    public StreamVerifier(TSIG paramTSIG, TSIGRecord paramTSIGRecord)
    {
      this.key = paramTSIG;
      this.verifier = new HMAC(this.key.digest, this.key.digestBlockLength, this.key.key);
      this.nresponses = 0;
      this.lastTSIG = paramTSIGRecord;
    }

    public int verify(Message paramMessage, byte[] paramArrayOfByte)
    {
      TSIGRecord localTSIGRecord = paramMessage.getTSIG();
      this.nresponses = (1 + this.nresponses);
      if (this.nresponses == 1)
      {
        int m = this.key.verify(paramMessage, paramArrayOfByte, this.lastTSIG);
        if (m == 0)
        {
          byte[] arrayOfByte2 = localTSIGRecord.getSignature();
          DNSOutput localDNSOutput3 = new DNSOutput();
          localDNSOutput3.writeU16(arrayOfByte2.length);
          this.verifier.update(localDNSOutput3.toByteArray());
          this.verifier.update(arrayOfByte2);
        }
        this.lastTSIG = localTSIGRecord;
        return m;
      }
      if (localTSIGRecord != null)
        paramMessage.getHeader().decCount(3);
      byte[] arrayOfByte1 = paramMessage.getHeader().toWire();
      if (localTSIGRecord != null)
        paramMessage.getHeader().incCount(3);
      this.verifier.update(arrayOfByte1);
      if (localTSIGRecord == null);
      for (int i = paramArrayOfByte.length - arrayOfByte1.length; ; i = paramMessage.tsigstart - arrayOfByte1.length)
      {
        this.verifier.update(paramArrayOfByte, arrayOfByte1.length, i);
        if (localTSIGRecord == null)
          break;
        this.lastsigned = this.nresponses;
        this.lastTSIG = localTSIGRecord;
        if ((localTSIGRecord.getName().equals(this.key.name)) && (localTSIGRecord.getAlgorithm().equals(this.key.alg)))
          break label292;
        if (Options.check("verbose"))
          System.err.println("BADKEY failure");
        paramMessage.tsigState = 4;
        return 17;
      }
      if (this.nresponses - this.lastsigned >= 100);
      for (int j = 1; j != 0; j = 0)
      {
        paramMessage.tsigState = 4;
        return 1;
      }
      paramMessage.tsigState = 2;
      return 0;
      label292: DNSOutput localDNSOutput1 = new DNSOutput();
      long l1 = localTSIGRecord.getTimeSigned().getTime() / 1000L;
      int k = (int)(l1 >> 32);
      long l2 = l1 & 0xFFFFFFFF;
      localDNSOutput1.writeU16(k);
      localDNSOutput1.writeU32(l2);
      localDNSOutput1.writeU16(localTSIGRecord.getFudge());
      this.verifier.update(localDNSOutput1.toByteArray());
      if (!this.verifier.verify(localTSIGRecord.getSignature()))
      {
        if (Options.check("verbose"))
          System.err.println("BADSIG failure");
        paramMessage.tsigState = 4;
        return 16;
      }
      this.verifier.clear();
      DNSOutput localDNSOutput2 = new DNSOutput();
      localDNSOutput2.writeU16(localTSIGRecord.getSignature().length);
      this.verifier.update(localDNSOutput2.toByteArray());
      this.verifier.update(localTSIGRecord.getSignature());
      paramMessage.tsigState = 1;
      return 0;
    }
  }
}