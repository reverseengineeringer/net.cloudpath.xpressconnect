package org.xbill.DNS;

import java.io.IOException;
import java.util.Arrays;

public abstract class EDNSOption
{
  private final int code;

  public EDNSOption(int paramInt)
  {
    this.code = Record.checkU16("code", paramInt);
  }

  static EDNSOption fromWire(DNSInput paramDNSInput)
    throws IOException
  {
    int i = paramDNSInput.readU16();
    int j = paramDNSInput.readU16();
    if (paramDNSInput.remaining() < j)
      throw new WireParseException("truncated option");
    int k = paramDNSInput.saveActive();
    paramDNSInput.setActive(j);
    Object localObject;
    switch (i)
    {
    default:
      localObject = new GenericEDNSOption(i);
    case 3:
    case 20730:
    }
    while (true)
    {
      ((EDNSOption)localObject).optionFromWire(paramDNSInput);
      paramDNSInput.restoreActive(k);
      return localObject;
      localObject = new NSIDOption();
      continue;
      localObject = new ClientSubnetOption();
    }
  }

  public static EDNSOption fromWire(byte[] paramArrayOfByte)
    throws IOException
  {
    return fromWire(new DNSInput(paramArrayOfByte));
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof EDNSOption)));
    EDNSOption localEDNSOption;
    do
    {
      return false;
      localEDNSOption = (EDNSOption)paramObject;
    }
    while (this.code != localEDNSOption.code);
    return Arrays.equals(getData(), localEDNSOption.getData());
  }

  public int getCode()
  {
    return this.code;
  }

  byte[] getData()
  {
    DNSOutput localDNSOutput = new DNSOutput();
    optionToWire(localDNSOutput);
    return localDNSOutput.toByteArray();
  }

  public int hashCode()
  {
    byte[] arrayOfByte = getData();
    int i = 0;
    for (int j = 0; j < arrayOfByte.length; j++)
      i += (i << 3) + (0xFF & arrayOfByte[j]);
    return i;
  }

  abstract void optionFromWire(DNSInput paramDNSInput)
    throws IOException;

  abstract String optionToString();

  abstract void optionToWire(DNSOutput paramDNSOutput);

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("{");
    localStringBuffer.append(Code.string(this.code));
    localStringBuffer.append(": ");
    localStringBuffer.append(optionToString());
    localStringBuffer.append("}");
    return localStringBuffer.toString();
  }

  void toWire(DNSOutput paramDNSOutput)
  {
    paramDNSOutput.writeU16(this.code);
    int i = paramDNSOutput.current();
    paramDNSOutput.writeU16(0);
    optionToWire(paramDNSOutput);
    paramDNSOutput.writeU16At(-2 + (paramDNSOutput.current() - i), i);
  }

  public byte[] toWire()
    throws IOException
  {
    DNSOutput localDNSOutput = new DNSOutput();
    toWire(localDNSOutput);
    return localDNSOutput.toByteArray();
  }

  public static class Code
  {
    public static final int CLIENT_SUBNET = 20730;
    public static final int NSID = 3;
    private static Mnemonic codes = new Mnemonic("EDNS Option Codes", 2);

    static
    {
      codes.setMaximum(65535);
      codes.setPrefix("CODE");
      codes.setNumericAllowed(true);
      codes.add(3, "NSID");
      codes.add(20730, "CLIENT_SUBNET");
    }

    public static String string(int paramInt)
    {
      return codes.getText(paramInt);
    }

    public static int value(String paramString)
    {
      return codes.getValue(paramString);
    }
  }
}