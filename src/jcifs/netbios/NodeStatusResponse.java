package jcifs.netbios;

import java.io.UnsupportedEncodingException;

class NodeStatusResponse extends NameServicePacket
{
  NbtAddress[] addressArray;
  private byte[] macAddress;
  private int numberOfNames;
  private NbtAddress queryAddress;
  private byte[] stats;

  NodeStatusResponse(NbtAddress paramNbtAddress)
  {
    this.queryAddress = paramNbtAddress;
    this.recordName = new Name();
    this.macAddress = new byte[6];
  }

  private int readNodeNameArray(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt;
    this.addressArray = new NbtAddress[this.numberOfNames];
    String str1 = this.queryAddress.hostName.scope;
    int j = 0;
    int k = 0;
    while (true)
    {
      try
      {
        if (k < this.numberOfNames)
        {
          int m = paramInt + 14;
          if (paramArrayOfByte[m] == 32)
          {
            m--;
            continue;
          }
          int n = 1 + (m - paramInt);
          String str2 = Name.OEM_ENCODING;
          String str3 = new String(paramArrayOfByte, paramInt, n, str2);
          int i1 = 0xFF & paramArrayOfByte[(paramInt + 15)];
          if ((0x80 & paramArrayOfByte[(paramInt + 16)]) != 128)
            break label439;
          bool1 = true;
          int i2 = (0x60 & paramArrayOfByte[(paramInt + 16)]) >> 5;
          if ((0x10 & paramArrayOfByte[(paramInt + 16)]) != 16)
            break label445;
          bool2 = true;
          if ((0x8 & paramArrayOfByte[(paramInt + 16)]) != 8)
            break label451;
          bool3 = true;
          if ((0x4 & paramArrayOfByte[(paramInt + 16)]) != 4)
            break label457;
          bool4 = true;
          if ((0x2 & paramArrayOfByte[(paramInt + 16)]) != 2)
            break label463;
          bool5 = true;
          if ((j == 0) && (this.queryAddress.hostName.hexCode == i1) && ((this.queryAddress.hostName == NbtAddress.UNKNOWN_NAME) || (this.queryAddress.hostName.name.equals(str3))))
          {
            if (this.queryAddress.hostName == NbtAddress.UNKNOWN_NAME)
              this.queryAddress.hostName = new Name(str3, i1, str1);
            this.queryAddress.groupName = bool1;
            this.queryAddress.nodeType = i2;
            this.queryAddress.isBeingDeleted = bool2;
            this.queryAddress.isInConflict = bool3;
            this.queryAddress.isActive = bool4;
            this.queryAddress.isPermanent = bool5;
            this.queryAddress.macAddress = this.macAddress;
            this.queryAddress.isDataFromNodeStatus = true;
            j = 1;
            this.addressArray[k] = this.queryAddress;
          }
          else
          {
            this.addressArray[k] = new NbtAddress(new Name(str3, i1, str1), this.queryAddress.address, bool1, i2, bool2, bool3, bool4, bool5, this.macAddress);
          }
        }
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
      }
      return paramInt - i;
      paramInt += 18;
      k++;
      continue;
      label439: boolean bool1 = false;
      continue;
      label445: boolean bool2 = false;
      continue;
      label451: boolean bool3 = false;
      continue;
      label457: boolean bool4 = false;
      continue;
      label463: boolean bool5 = false;
    }
  }

  int readBodyWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return readResourceRecordWireFormat(paramArrayOfByte, paramInt);
  }

  int readRDataWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    this.numberOfNames = (0xFF & paramArrayOfByte[paramInt]);
    int i = 18 * this.numberOfNames;
    int j = -1 + (this.rDataLength - i);
    int k = paramInt + 1;
    this.numberOfNames = (0xFF & paramArrayOfByte[paramInt]);
    System.arraycopy(paramArrayOfByte, k + i, this.macAddress, 0, 6);
    int m = k + readNodeNameArray(paramArrayOfByte, k);
    this.stats = new byte[j];
    System.arraycopy(paramArrayOfByte, m, this.stats, 0, j);
    return m + j - paramInt;
  }

  public String toString()
  {
    return new String("NodeStatusResponse[" + super.toString() + "]");
  }

  int writeBodyWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int writeRDataWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }
}