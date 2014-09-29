package org.xbill.DNS;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

public class DNSSEC
{
  private static final int ASN1_INT = 2;
  private static final int ASN1_SEQ = 48;
  private static final int DSA_LEN = 20;

  private static int BigIntegerLength(BigInteger paramBigInteger)
  {
    return (7 + paramBigInteger.bitLength()) / 8;
  }

  private static byte[] DSASignaturefromDNS(byte[] paramArrayOfByte)
    throws DNSSEC.DNSSECException, IOException
  {
    if (paramArrayOfByte.length != 41)
      throw new SignatureVerificationException();
    DNSInput localDNSInput = new DNSInput(paramArrayOfByte);
    DNSOutput localDNSOutput = new DNSOutput();
    localDNSInput.readU8();
    byte[] arrayOfByte1 = localDNSInput.readByteArray(20);
    int i = 20;
    if (arrayOfByte1[0] < 0)
      i++;
    byte[] arrayOfByte2 = localDNSInput.readByteArray(20);
    int j = 20;
    if (arrayOfByte2[0] < 0)
      j++;
    localDNSOutput.writeU8(48);
    localDNSOutput.writeU8(4 + (i + j));
    localDNSOutput.writeU8(2);
    localDNSOutput.writeU8(i);
    if (i > 20)
      localDNSOutput.writeU8(0);
    localDNSOutput.writeByteArray(arrayOfByte1);
    localDNSOutput.writeU8(2);
    localDNSOutput.writeU8(j);
    if (j > 20)
      localDNSOutput.writeU8(0);
    localDNSOutput.writeByteArray(arrayOfByte2);
    return localDNSOutput.toByteArray();
  }

  private static byte[] DSASignaturetoDNS(byte[] paramArrayOfByte, int paramInt)
    throws IOException
  {
    DNSInput localDNSInput = new DNSInput(paramArrayOfByte);
    DNSOutput localDNSOutput = new DNSOutput();
    localDNSOutput.writeU8(paramInt);
    if (localDNSInput.readU8() != 48)
      throw new IOException();
    localDNSInput.readU8();
    if (localDNSInput.readU8() != 2)
      throw new IOException();
    int i = localDNSInput.readU8();
    if (i == 21)
    {
      if (localDNSInput.readU8() != 0)
        throw new IOException();
    }
    else if (i != 20)
      throw new IOException();
    localDNSOutput.writeByteArray(localDNSInput.readByteArray(20));
    if (localDNSInput.readU8() != 2)
      throw new IOException();
    int j = localDNSInput.readU8();
    if (j == 21)
    {
      if (localDNSInput.readU8() != 0)
        throw new IOException();
    }
    else if (j != 20)
      throw new IOException();
    localDNSOutput.writeByteArray(localDNSInput.readByteArray(20));
    return localDNSOutput.toByteArray();
  }

  public static String algString(int paramInt)
    throws DNSSEC.UnsupportedAlgorithmException
  {
    switch (paramInt)
    {
    case 2:
    case 4:
    case 9:
    default:
      throw new UnsupportedAlgorithmException(paramInt);
    case 1:
      return "MD5withRSA";
    case 3:
    case 6:
      return "SHA1withDSA";
    case 5:
    case 7:
      return "SHA1withRSA";
    case 8:
      return "SHA256withRSA";
    case 10:
    }
    return "SHA512withRSA";
  }

  static void checkAlgorithm(PrivateKey paramPrivateKey, int paramInt)
    throws DNSSEC.UnsupportedAlgorithmException
  {
    switch (paramInt)
    {
    case 2:
    case 4:
    case 9:
    default:
      throw new UnsupportedAlgorithmException(paramInt);
    case 1:
    case 5:
    case 7:
    case 8:
    case 10:
      if (!(paramPrivateKey instanceof RSAPrivateKey))
        throw new IncompatibleKeyException();
      break;
    case 3:
    case 6:
      if (!(paramPrivateKey instanceof DSAPrivateKey))
        throw new IncompatibleKeyException();
      break;
    }
  }

  public static byte[] digestMessage(SIGRecord paramSIGRecord, Message paramMessage, byte[] paramArrayOfByte)
  {
    DNSOutput localDNSOutput = new DNSOutput();
    digestSIG(localDNSOutput, paramSIGRecord);
    if (paramArrayOfByte != null)
      localDNSOutput.writeByteArray(paramArrayOfByte);
    paramMessage.toWire(localDNSOutput);
    return localDNSOutput.toByteArray();
  }

  public static byte[] digestRRset(RRSIGRecord paramRRSIGRecord, RRset paramRRset)
  {
    DNSOutput localDNSOutput1 = new DNSOutput();
    digestSIG(localDNSOutput1, paramRRSIGRecord);
    int i = paramRRset.size();
    Record[] arrayOfRecord = new Record[i];
    Iterator localIterator = paramRRset.rrs();
    Name localName1 = paramRRset.getName();
    int j = 1 + paramRRSIGRecord.getLabels();
    int k = localName1.labels();
    Name localName2 = null;
    if (k > j)
      localName2 = localName1.wild(localName1.labels() - j);
    while (localIterator.hasNext())
    {
      i--;
      arrayOfRecord[i] = ((Record)localIterator.next());
    }
    Arrays.sort(arrayOfRecord);
    DNSOutput localDNSOutput2 = new DNSOutput();
    if (localName2 != null)
      localName2.toWireCanonical(localDNSOutput2);
    while (true)
    {
      localDNSOutput2.writeU16(paramRRset.getType());
      localDNSOutput2.writeU16(paramRRset.getDClass());
      localDNSOutput2.writeU32(paramRRSIGRecord.getOrigTTL());
      for (int m = 0; m < arrayOfRecord.length; m++)
      {
        localDNSOutput1.writeByteArray(localDNSOutput2.toByteArray());
        int n = localDNSOutput1.current();
        localDNSOutput1.writeU16(0);
        localDNSOutput1.writeByteArray(arrayOfRecord[m].rdataToWireCanonical());
        int i1 = -2 + (localDNSOutput1.current() - n);
        localDNSOutput1.save();
        localDNSOutput1.jump(n);
        localDNSOutput1.writeU16(i1);
        localDNSOutput1.restore();
      }
      localName1.toWireCanonical(localDNSOutput2);
    }
    return localDNSOutput1.toByteArray();
  }

  private static void digestSIG(DNSOutput paramDNSOutput, SIGBase paramSIGBase)
  {
    paramDNSOutput.writeU16(paramSIGBase.getTypeCovered());
    paramDNSOutput.writeU8(paramSIGBase.getAlgorithm());
    paramDNSOutput.writeU8(paramSIGBase.getLabels());
    paramDNSOutput.writeU32(paramSIGBase.getOrigTTL());
    paramDNSOutput.writeU32(paramSIGBase.getExpire().getTime() / 1000L);
    paramDNSOutput.writeU32(paramSIGBase.getTimeSigned().getTime() / 1000L);
    paramDNSOutput.writeU16(paramSIGBase.getFootprint());
    paramSIGBase.getSigner().toWireCanonical(paramDNSOutput);
  }

  private static byte[] fromDSAPublicKey(DSAPublicKey paramDSAPublicKey)
  {
    DNSOutput localDNSOutput = new DNSOutput();
    BigInteger localBigInteger1 = paramDSAPublicKey.getParams().getQ();
    BigInteger localBigInteger2 = paramDSAPublicKey.getParams().getP();
    BigInteger localBigInteger3 = paramDSAPublicKey.getParams().getG();
    BigInteger localBigInteger4 = paramDSAPublicKey.getY();
    localDNSOutput.writeU8((-64 + localBigInteger2.toByteArray().length) / 8);
    writeBigInteger(localDNSOutput, localBigInteger1);
    writeBigInteger(localDNSOutput, localBigInteger2);
    writeBigInteger(localDNSOutput, localBigInteger3);
    writeBigInteger(localDNSOutput, localBigInteger4);
    return localDNSOutput.toByteArray();
  }

  static byte[] fromPublicKey(PublicKey paramPublicKey, int paramInt)
    throws DNSSEC.DNSSECException
  {
    switch (paramInt)
    {
    case 2:
    case 4:
    case 9:
    default:
      throw new UnsupportedAlgorithmException(paramInt);
    case 1:
    case 5:
    case 7:
    case 8:
    case 10:
      if (!(paramPublicKey instanceof RSAPublicKey))
        throw new IncompatibleKeyException();
      return fromRSAPublicKey((RSAPublicKey)paramPublicKey);
    case 3:
    case 6:
    }
    if (!(paramPublicKey instanceof DSAPublicKey))
      throw new IncompatibleKeyException();
    return fromDSAPublicKey((DSAPublicKey)paramPublicKey);
  }

  private static byte[] fromRSAPublicKey(RSAPublicKey paramRSAPublicKey)
  {
    DNSOutput localDNSOutput = new DNSOutput();
    BigInteger localBigInteger1 = paramRSAPublicKey.getPublicExponent();
    BigInteger localBigInteger2 = paramRSAPublicKey.getModulus();
    int i = BigIntegerLength(localBigInteger1);
    if (i < 256)
      localDNSOutput.writeU8(i);
    while (true)
    {
      writeBigInteger(localDNSOutput, localBigInteger1);
      writeBigInteger(localDNSOutput, localBigInteger2);
      return localDNSOutput.toByteArray();
      localDNSOutput.writeU8(0);
      localDNSOutput.writeU16(i);
    }
  }

  static byte[] generateDS(DNSKEYRecord paramDNSKEYRecord, int paramInt)
  {
    DNSOutput localDNSOutput = new DNSOutput();
    localDNSOutput.writeU16(paramDNSKEYRecord.getFootprint());
    localDNSOutput.writeU8(paramDNSKEYRecord.getAlgorithm());
    localDNSOutput.writeU8(paramInt);
    MessageDigest localMessageDigest2;
    switch (paramInt)
    {
    default:
      try
      {
        throw new IllegalArgumentException("unknown DS digest type " + paramInt);
      }
      catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
      {
        throw new IllegalStateException("no message digest support");
      }
    case 1:
      localMessageDigest2 = MessageDigest.getInstance("sha-1");
    case 2:
    }
    MessageDigest localMessageDigest1;
    for (Object localObject = localMessageDigest2; ; localObject = localMessageDigest1)
    {
      ((MessageDigest)localObject).update(paramDNSKEYRecord.getName().toWire());
      ((MessageDigest)localObject).update(paramDNSKEYRecord.rdataToWireCanonical());
      localDNSOutput.writeByteArray(((MessageDigest)localObject).digest());
      return localDNSOutput.toByteArray();
      localMessageDigest1 = MessageDigest.getInstance("sha-256");
    }
  }

  private static boolean matches(SIGBase paramSIGBase, KEYBase paramKEYBase)
  {
    return (paramKEYBase.getAlgorithm() == paramSIGBase.getAlgorithm()) && (paramKEYBase.getFootprint() == paramSIGBase.getFootprint()) && (paramKEYBase.getName().equals(paramSIGBase.getSigner()));
  }

  private static BigInteger readBigInteger(DNSInput paramDNSInput)
  {
    return new BigInteger(1, paramDNSInput.readByteArray());
  }

  private static BigInteger readBigInteger(DNSInput paramDNSInput, int paramInt)
    throws IOException
  {
    return new BigInteger(1, paramDNSInput.readByteArray(paramInt));
  }

  public static RRSIGRecord sign(RRset paramRRset, DNSKEYRecord paramDNSKEYRecord, PrivateKey paramPrivateKey, Date paramDate1, Date paramDate2)
    throws DNSSEC.DNSSECException
  {
    return sign(paramRRset, paramDNSKEYRecord, paramPrivateKey, paramDate1, paramDate2, null);
  }

  public static RRSIGRecord sign(RRset paramRRset, DNSKEYRecord paramDNSKEYRecord, PrivateKey paramPrivateKey, Date paramDate1, Date paramDate2, String paramString)
    throws DNSSEC.DNSSECException
  {
    int i = paramDNSKEYRecord.getAlgorithm();
    checkAlgorithm(paramPrivateKey, i);
    RRSIGRecord localRRSIGRecord = new RRSIGRecord(paramRRset.getName(), paramRRset.getDClass(), paramRRset.getTTL(), paramRRset.getType(), i, paramRRset.getTTL(), paramDate2, paramDate1, paramDNSKEYRecord.getFootprint(), paramDNSKEYRecord.getName(), null);
    localRRSIGRecord.setSignature(sign(paramPrivateKey, paramDNSKEYRecord.getPublicKey(), i, digestRRset(localRRSIGRecord, paramRRset), paramString));
    return localRRSIGRecord;
  }

  // ERROR //
  private static byte[] sign(PrivateKey paramPrivateKey, PublicKey paramPublicKey, int paramInt, byte[] paramArrayOfByte, String paramString)
    throws DNSSEC.DNSSECException
  {
    // Byte code:
    //   0: aload 4
    //   2: ifnull +81 -> 83
    //   5: iload_2
    //   6: invokestatic 358	org/xbill/DNS/DNSSEC:algString	(I)Ljava/lang/String;
    //   9: aload 4
    //   11: invokestatic 363	java/security/Signature:getInstance	(Ljava/lang/String;Ljava/lang/String;)Ljava/security/Signature;
    //   14: astore 7
    //   16: aload 7
    //   18: aload_0
    //   19: invokevirtual 367	java/security/Signature:initSign	(Ljava/security/PrivateKey;)V
    //   22: aload 7
    //   24: aload_3
    //   25: invokevirtual 368	java/security/Signature:update	([B)V
    //   28: aload 7
    //   30: invokevirtual 370	java/security/Signature:sign	()[B
    //   33: astore 8
    //   35: aload 8
    //   37: astore 9
    //   39: aload_1
    //   40: instanceof 214
    //   43: ifeq +37 -> 80
    //   46: aload 9
    //   48: bipush 192
    //   50: aload_1
    //   51: checkcast 214	java/security/interfaces/DSAPublicKey
    //   54: invokeinterface 218 1 0
    //   59: invokeinterface 227 1 0
    //   64: invokestatic 256	org/xbill/DNS/DNSSEC:BigIntegerLength	(Ljava/math/BigInteger;)I
    //   67: iadd
    //   68: bipush 8
    //   70: idiv
    //   71: invokestatic 372	org/xbill/DNS/DNSSEC:DSASignaturetoDNS	([BI)[B
    //   74: astore 11
    //   76: aload 11
    //   78: astore 9
    //   80: aload 9
    //   82: areturn
    //   83: iload_2
    //   84: invokestatic 358	org/xbill/DNS/DNSSEC:algString	(I)Ljava/lang/String;
    //   87: invokestatic 375	java/security/Signature:getInstance	(Ljava/lang/String;)Ljava/security/Signature;
    //   90: astore 6
    //   92: aload 6
    //   94: astore 7
    //   96: goto -80 -> 16
    //   99: astore 5
    //   101: new 27	org/xbill/DNS/DNSSEC$DNSSECException
    //   104: dup
    //   105: aload 5
    //   107: invokevirtual 376	java/security/GeneralSecurityException:toString	()Ljava/lang/String;
    //   110: invokespecial 377	org/xbill/DNS/DNSSEC$DNSSECException:<init>	(Ljava/lang/String;)V
    //   113: athrow
    //   114: astore 10
    //   116: new 287	java/lang/IllegalStateException
    //   119: dup
    //   120: invokespecial 378	java/lang/IllegalStateException:<init>	()V
    //   123: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   5	16	99	java/security/GeneralSecurityException
    //   16	35	99	java/security/GeneralSecurityException
    //   83	92	99	java/security/GeneralSecurityException
    //   46	76	114	java/io/IOException
  }

  static SIGRecord signMessage(Message paramMessage, SIGRecord paramSIGRecord, KEYRecord paramKEYRecord, PrivateKey paramPrivateKey, Date paramDate1, Date paramDate2)
    throws DNSSEC.DNSSECException
  {
    int i = paramKEYRecord.getAlgorithm();
    checkAlgorithm(paramPrivateKey, i);
    SIGRecord localSIGRecord = new SIGRecord(Name.root, 255, 0L, 0, i, 0L, paramDate2, paramDate1, paramKEYRecord.getFootprint(), paramKEYRecord.getName(), null);
    DNSOutput localDNSOutput = new DNSOutput();
    digestSIG(localDNSOutput, localSIGRecord);
    if (paramSIGRecord != null)
      localDNSOutput.writeByteArray(paramSIGRecord.getSignature());
    paramMessage.toWire(localDNSOutput);
    localSIGRecord.setSignature(sign(paramPrivateKey, paramKEYRecord.getPublicKey(), i, localDNSOutput.toByteArray(), null));
    return localSIGRecord;
  }

  private static PublicKey toDSAPublicKey(KEYBase paramKEYBase)
    throws IOException, GeneralSecurityException, DNSSEC.MalformedKeyException
  {
    DNSInput localDNSInput = new DNSInput(paramKEYBase.getKey());
    int i = localDNSInput.readU8();
    if (i > 8)
      throw new MalformedKeyException(paramKEYBase);
    BigInteger localBigInteger1 = readBigInteger(localDNSInput, 20);
    BigInteger localBigInteger2 = readBigInteger(localDNSInput, 64 + i * 8);
    BigInteger localBigInteger3 = readBigInteger(localDNSInput, 64 + i * 8);
    BigInteger localBigInteger4 = readBigInteger(localDNSInput, 64 + i * 8);
    return KeyFactory.getInstance("DSA").generatePublic(new DSAPublicKeySpec(localBigInteger4, localBigInteger2, localBigInteger1, localBigInteger3));
  }

  static PublicKey toPublicKey(KEYBase paramKEYBase)
    throws DNSSEC.DNSSECException
  {
    int i = paramKEYBase.getAlgorithm();
    switch (i)
    {
    case 2:
    case 4:
    case 9:
    default:
    case 1:
    case 5:
    case 7:
    case 8:
    case 10:
    case 3:
    case 6:
    }
    try
    {
      throw new UnsupportedAlgorithmException(i);
    }
    catch (IOException localIOException)
    {
      throw new MalformedKeyException(paramKEYBase);
      return toRSAPublicKey(paramKEYBase);
      PublicKey localPublicKey = toDSAPublicKey(paramKEYBase);
      return localPublicKey;
    }
    catch (GeneralSecurityException localGeneralSecurityException)
    {
      throw new DNSSECException(localGeneralSecurityException.toString());
    }
  }

  private static PublicKey toRSAPublicKey(KEYBase paramKEYBase)
    throws IOException, GeneralSecurityException
  {
    DNSInput localDNSInput = new DNSInput(paramKEYBase.getKey());
    int i = localDNSInput.readU8();
    if (i == 0)
      i = localDNSInput.readU16();
    BigInteger localBigInteger1 = readBigInteger(localDNSInput, i);
    BigInteger localBigInteger2 = readBigInteger(localDNSInput);
    return KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(localBigInteger2, localBigInteger1));
  }

  private static void verify(PublicKey paramPublicKey, int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws DNSSEC.DNSSECException
  {
    if ((paramPublicKey instanceof DSAPublicKey));
    try
    {
      byte[] arrayOfByte = DSASignaturefromDNS(paramArrayOfByte2);
      paramArrayOfByte2 = arrayOfByte;
      try
      {
        Signature localSignature = Signature.getInstance(algString(paramInt));
        localSignature.initVerify(paramPublicKey);
        localSignature.update(paramArrayOfByte1);
        if (!localSignature.verify(paramArrayOfByte2))
          throw new SignatureVerificationException();
      }
      catch (GeneralSecurityException localGeneralSecurityException)
      {
        throw new DNSSECException(localGeneralSecurityException.toString());
      }
    }
    catch (IOException localIOException)
    {
      throw new IllegalStateException();
    }
  }

  public static void verify(RRset paramRRset, RRSIGRecord paramRRSIGRecord, DNSKEYRecord paramDNSKEYRecord)
    throws DNSSEC.DNSSECException
  {
    if (!matches(paramRRSIGRecord, paramDNSKEYRecord))
      throw new KeyMismatchException(paramDNSKEYRecord, paramRRSIGRecord);
    Date localDate = new Date();
    if (localDate.compareTo(paramRRSIGRecord.getExpire()) > 0)
      throw new SignatureExpiredException(paramRRSIGRecord.getExpire(), localDate);
    if (localDate.compareTo(paramRRSIGRecord.getTimeSigned()) < 0)
      throw new SignatureNotYetValidException(paramRRSIGRecord.getTimeSigned(), localDate);
    verify(paramDNSKEYRecord.getPublicKey(), paramRRSIGRecord.getAlgorithm(), digestRRset(paramRRSIGRecord, paramRRset), paramRRSIGRecord.getSignature());
  }

  static void verifyMessage(Message paramMessage, byte[] paramArrayOfByte, SIGRecord paramSIGRecord1, SIGRecord paramSIGRecord2, KEYRecord paramKEYRecord)
    throws DNSSEC.DNSSECException
  {
    if (!matches(paramSIGRecord1, paramKEYRecord))
      throw new KeyMismatchException(paramKEYRecord, paramSIGRecord1);
    Date localDate = new Date();
    if (localDate.compareTo(paramSIGRecord1.getExpire()) > 0)
      throw new SignatureExpiredException(paramSIGRecord1.getExpire(), localDate);
    if (localDate.compareTo(paramSIGRecord1.getTimeSigned()) < 0)
      throw new SignatureNotYetValidException(paramSIGRecord1.getTimeSigned(), localDate);
    DNSOutput localDNSOutput = new DNSOutput();
    digestSIG(localDNSOutput, paramSIGRecord1);
    if (paramSIGRecord2 != null)
      localDNSOutput.writeByteArray(paramSIGRecord2.getSignature());
    Header localHeader = (Header)paramMessage.getHeader().clone();
    localHeader.decCount(3);
    localDNSOutput.writeByteArray(localHeader.toWire());
    localDNSOutput.writeByteArray(paramArrayOfByte, 12, -12 + paramMessage.sig0start);
    verify(paramKEYRecord.getPublicKey(), paramSIGRecord1.getAlgorithm(), localDNSOutput.toByteArray(), paramSIGRecord1.getSignature());
  }

  private static void writeBigInteger(DNSOutput paramDNSOutput, BigInteger paramBigInteger)
  {
    byte[] arrayOfByte = paramBigInteger.toByteArray();
    if (arrayOfByte[0] == 0)
    {
      paramDNSOutput.writeByteArray(arrayOfByte, 1, -1 + arrayOfByte.length);
      return;
    }
    paramDNSOutput.writeByteArray(arrayOfByte);
  }

  public static class Algorithm
  {
    public static final int DH = 2;
    public static final int DSA = 3;
    public static final int DSA_NSEC3_SHA1 = 6;
    public static final int ECC = 4;
    public static final int INDIRECT = 252;
    public static final int PRIVATEDNS = 253;
    public static final int PRIVATEOID = 254;
    public static final int RSAMD5 = 1;
    public static final int RSASHA1 = 5;
    public static final int RSASHA256 = 8;
    public static final int RSASHA512 = 10;
    public static final int RSA_NSEC3_SHA1 = 7;
    private static Mnemonic algs = new Mnemonic("DNSSEC algorithm", 2);

    static
    {
      algs.setMaximum(255);
      algs.setNumericAllowed(true);
      algs.add(1, "RSAMD5");
      algs.add(2, "DH");
      algs.add(3, "DSA");
      algs.add(4, "ECC");
      algs.add(5, "RSASHA1");
      algs.add(6, "DSA-NSEC3-SHA1");
      algs.add(7, "RSA-NSEC3-SHA1");
      algs.add(8, "RSASHA256");
      algs.add(10, "RSASHA512");
      algs.add(252, "INDIRECT");
      algs.add(253, "PRIVATEDNS");
      algs.add(254, "PRIVATEOID");
    }

    public static String string(int paramInt)
    {
      return algs.getText(paramInt);
    }

    public static int value(String paramString)
    {
      return algs.getValue(paramString);
    }
  }

  public static class DNSSECException extends Exception
  {
    DNSSECException(String paramString)
    {
      super();
    }
  }

  public static class IncompatibleKeyException extends IllegalArgumentException
  {
    IncompatibleKeyException()
    {
      super();
    }
  }

  public static class KeyMismatchException extends DNSSEC.DNSSECException
  {
    private KEYBase key;
    private SIGBase sig;

    KeyMismatchException(KEYBase paramKEYBase, SIGBase paramSIGBase)
    {
      super();
    }
  }

  public static class MalformedKeyException extends DNSSEC.DNSSECException
  {
    MalformedKeyException(KEYBase paramKEYBase)
    {
      super();
    }
  }

  public static class SignatureExpiredException extends DNSSEC.DNSSECException
  {
    private Date now;
    private Date when;

    SignatureExpiredException(Date paramDate1, Date paramDate2)
    {
      super();
      this.when = paramDate1;
      this.now = paramDate2;
    }

    public Date getExpiration()
    {
      return this.when;
    }

    public Date getVerifyTime()
    {
      return this.now;
    }
  }

  public static class SignatureNotYetValidException extends DNSSEC.DNSSECException
  {
    private Date now;
    private Date when;

    SignatureNotYetValidException(Date paramDate1, Date paramDate2)
    {
      super();
      this.when = paramDate1;
      this.now = paramDate2;
    }

    public Date getExpiration()
    {
      return this.when;
    }

    public Date getVerifyTime()
    {
      return this.now;
    }
  }

  public static class SignatureVerificationException extends DNSSEC.DNSSECException
  {
    SignatureVerificationException()
    {
      super();
    }
  }

  public static class UnsupportedAlgorithmException extends DNSSEC.DNSSECException
  {
    UnsupportedAlgorithmException(int paramInt)
    {
      super();
    }
  }
}