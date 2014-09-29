package org.xbill.DNS;

import java.io.IOException;
import org.xbill.DNS.utils.base64;

public class CERTRecord extends Record
{
  public static final int OID = 254;
  public static final int PGP = 3;
  public static final int PKIX = 1;
  public static final int SPKI = 2;
  public static final int URI = 253;
  private static final long serialVersionUID = 4763014646517016835L;
  private int alg;
  private byte[] cert;
  private int certType;
  private int keyTag;

  CERTRecord()
  {
  }

  public CERTRecord(Name paramName, int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte)
  {
    super(paramName, 37, paramInt1, paramLong);
    this.certType = checkU16("certType", paramInt2);
    this.keyTag = checkU16("keyTag", paramInt3);
    this.alg = checkU8("alg", paramInt4);
    this.cert = paramArrayOfByte;
  }

  public int getAlgorithm()
  {
    return this.alg;
  }

  public byte[] getCert()
  {
    return this.cert;
  }

  public int getCertType()
  {
    return this.certType;
  }

  public int getKeyTag()
  {
    return this.keyTag;
  }

  Record getObject()
  {
    return new CERTRecord();
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    String str1 = paramTokenizer.getString();
    this.certType = CertificateType.value(str1);
    if (this.certType < 0)
      throw paramTokenizer.exception("Invalid certificate type: " + str1);
    this.keyTag = paramTokenizer.getUInt16();
    String str2 = paramTokenizer.getString();
    this.alg = DNSSEC.Algorithm.value(str2);
    if (this.alg < 0)
      throw paramTokenizer.exception("Invalid algorithm: " + str2);
    this.cert = paramTokenizer.getBase64();
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.certType = paramDNSInput.readU16();
    this.keyTag = paramDNSInput.readU16();
    this.alg = paramDNSInput.readU8();
    this.cert = paramDNSInput.readByteArray();
  }

  String rrToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(this.certType);
    localStringBuffer.append(" ");
    localStringBuffer.append(this.keyTag);
    localStringBuffer.append(" ");
    localStringBuffer.append(this.alg);
    if (this.cert != null)
    {
      if (!Options.check("multiline"))
        break label93;
      localStringBuffer.append(" (\n");
      localStringBuffer.append(base64.formatString(this.cert, 64, "\t", true));
    }
    while (true)
    {
      return localStringBuffer.toString();
      label93: localStringBuffer.append(" ");
      localStringBuffer.append(base64.toString(this.cert));
    }
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    paramDNSOutput.writeU16(this.certType);
    paramDNSOutput.writeU16(this.keyTag);
    paramDNSOutput.writeU8(this.alg);
    paramDNSOutput.writeByteArray(this.cert);
  }

  public static class CertificateType
  {
    public static final int ACPKIX = 7;
    public static final int IACPKIX = 8;
    public static final int IPGP = 6;
    public static final int IPKIX = 4;
    public static final int ISPKI = 5;
    public static final int OID = 254;
    public static final int PGP = 3;
    public static final int PKIX = 1;
    public static final int SPKI = 2;
    public static final int URI = 253;
    private static Mnemonic types = new Mnemonic("Certificate type", 2);

    static
    {
      types.setMaximum(65535);
      types.setNumericAllowed(true);
      types.add(1, "PKIX");
      types.add(2, "SPKI");
      types.add(3, "PGP");
      types.add(1, "IPKIX");
      types.add(2, "ISPKI");
      types.add(3, "IPGP");
      types.add(3, "ACPKIX");
      types.add(3, "IACPKIX");
      types.add(253, "URI");
      types.add(254, "OID");
    }

    public static String string(int paramInt)
    {
      return types.getText(paramInt);
    }

    public static int value(String paramString)
    {
      return types.getValue(paramString);
    }
  }
}