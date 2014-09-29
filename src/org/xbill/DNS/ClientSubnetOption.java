package org.xbill.DNS;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ClientSubnetOption extends EDNSOption
{
  private static final long serialVersionUID = -3868158449890266347L;
  private InetAddress address;
  private int family;
  private int scopeNetmask;
  private int sourceNetmask;

  ClientSubnetOption()
  {
    super(20730);
  }

  public ClientSubnetOption(int paramInt1, int paramInt2, InetAddress paramInetAddress)
  {
    super(20730);
    this.family = Address.familyOf(paramInetAddress);
    this.sourceNetmask = checkMaskLength("source netmask", this.family, paramInt1);
    this.scopeNetmask = checkMaskLength("scope netmask", this.family, paramInt2);
    this.address = Address.truncate(paramInetAddress, paramInt1);
    if (!paramInetAddress.equals(this.address))
      throw new IllegalArgumentException("source netmask is not valid for address");
  }

  public ClientSubnetOption(int paramInt, InetAddress paramInetAddress)
  {
    this(paramInt, 0, paramInetAddress);
  }

  private static int checkMaskLength(String paramString, int paramInt1, int paramInt2)
  {
    int i = 8 * Address.addressLength(paramInt1);
    if ((paramInt2 < 0) || (paramInt2 > i))
      throw new IllegalArgumentException("\"" + paramString + "\" " + paramInt2 + " must be in the range " + "[0.." + i + "]");
    return paramInt2;
  }

  public InetAddress getAddress()
  {
    return this.address;
  }

  public int getFamily()
  {
    return this.family;
  }

  public int getScopeNetmask()
  {
    return this.scopeNetmask;
  }

  public int getSourceNetmask()
  {
    return this.sourceNetmask;
  }

  void optionFromWire(DNSInput paramDNSInput)
    throws WireParseException
  {
    this.family = paramDNSInput.readU16();
    if ((this.family != 1) && (this.family != 2))
      throw new WireParseException("unknown address family");
    this.sourceNetmask = paramDNSInput.readU8();
    if (this.sourceNetmask > 8 * Address.addressLength(this.family))
      throw new WireParseException("invalid source netmask");
    this.scopeNetmask = paramDNSInput.readU8();
    if (this.scopeNetmask > 8 * Address.addressLength(this.family))
      throw new WireParseException("invalid scope netmask");
    byte[] arrayOfByte1 = paramDNSInput.readByteArray();
    if (arrayOfByte1.length != (7 + this.sourceNetmask) / 8)
      throw new WireParseException("invalid address");
    byte[] arrayOfByte2 = new byte[Address.addressLength(this.family)];
    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte1.length);
    try
    {
      this.address = InetAddress.getByAddress(arrayOfByte2);
      if (!Address.truncate(this.address, this.sourceNetmask).equals(this.address))
        throw new WireParseException("invalid padding");
    }
    catch (UnknownHostException localUnknownHostException)
    {
      throw new WireParseException("invalid address", localUnknownHostException);
    }
  }

  String optionToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(this.address.getHostAddress());
    localStringBuffer.append("/");
    localStringBuffer.append(this.sourceNetmask);
    localStringBuffer.append(", scope netmask ");
    localStringBuffer.append(this.scopeNetmask);
    return localStringBuffer.toString();
  }

  void optionToWire(DNSOutput paramDNSOutput)
  {
    paramDNSOutput.writeU16(this.family);
    paramDNSOutput.writeU8(this.sourceNetmask);
    paramDNSOutput.writeU8(this.scopeNetmask);
    paramDNSOutput.writeByteArray(this.address.getAddress(), 0, (7 + this.sourceNetmask) / 8);
  }
}