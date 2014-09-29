package jcifs.netbios;

import java.net.InetAddress;
import jcifs.util.Hexdump;

abstract class NameServicePacket
{
  static final int A = 1;
  static final int ACT_ERR = 6;
  static final int ADDITIONAL_OFFSET = 10;
  static final int ANSWER_OFFSET = 6;
  static final int AUTHORITY_OFFSET = 8;
  static final int CFT_ERR = 7;
  static final int FMT_ERR = 1;
  static final int HEADER_LENGTH = 12;
  static final int IMP_ERR = 4;
  static final int IN = 1;
  static final int NB = 32;
  static final int NBSTAT = 33;
  static final int NBSTAT_IN = 2162689;
  static final int NB_IN = 2097153;
  static final int NS = 2;
  static final int NULL = 10;
  static final int OPCODE_OFFSET = 2;
  static final int QUERY = 0;
  static final int QUESTION_OFFSET = 4;
  static final int RFS_ERR = 5;
  static final int SRV_ERR = 2;
  static final int WACK = 7;
  int additionalCount;
  InetAddress addr;
  NbtAddress[] addrEntry;
  int addrIndex;
  int answerCount;
  int authorityCount;
  boolean isAuthAnswer;
  boolean isBroadcast = true;
  boolean isRecurAvailable;
  boolean isRecurDesired = true;
  boolean isResponse;
  boolean isTruncated;
  int nameTrnId;
  int opCode;
  int questionClass = 1;
  int questionCount = 1;
  Name questionName;
  int questionType;
  int rDataLength;
  boolean received;
  int recordClass;
  Name recordName;
  int recordType;
  int resultCode;
  int ttl;

  static int readInt2(byte[] paramArrayOfByte, int paramInt)
  {
    return ((0xFF & paramArrayOfByte[paramInt]) << 8) + (0xFF & paramArrayOfByte[(paramInt + 1)]);
  }

  static int readInt4(byte[] paramArrayOfByte, int paramInt)
  {
    return ((0xFF & paramArrayOfByte[paramInt]) << 24) + ((0xFF & paramArrayOfByte[(paramInt + 1)]) << 16) + ((0xFF & paramArrayOfByte[(paramInt + 2)]) << 8) + (0xFF & paramArrayOfByte[(paramInt + 3)]);
  }

  static int readNameTrnId(byte[] paramArrayOfByte, int paramInt)
  {
    return readInt2(paramArrayOfByte, paramInt);
  }

  static void writeInt2(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    int i = paramInt2 + 1;
    paramArrayOfByte[paramInt2] = ((byte)(0xFF & paramInt1 >> 8));
    paramArrayOfByte[i] = ((byte)(paramInt1 & 0xFF));
  }

  static void writeInt4(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    int i = paramInt2 + 1;
    paramArrayOfByte[paramInt2] = ((byte)(0xFF & paramInt1 >> 24));
    int j = i + 1;
    paramArrayOfByte[i] = ((byte)(0xFF & paramInt1 >> 16));
    int k = j + 1;
    paramArrayOfByte[j] = ((byte)(0xFF & paramInt1 >> 8));
    paramArrayOfByte[k] = ((byte)(paramInt1 & 0xFF));
  }

  abstract int readBodyWireFormat(byte[] paramArrayOfByte, int paramInt);

  int readHeaderWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    this.nameTrnId = readInt2(paramArrayOfByte, paramInt);
    boolean bool1;
    boolean bool2;
    label55: boolean bool3;
    label74: boolean bool4;
    label93: boolean bool5;
    label116: boolean bool6;
    if ((0x80 & paramArrayOfByte[(paramInt + 2)]) == 0)
    {
      bool1 = false;
      this.isResponse = bool1;
      this.opCode = ((0x78 & paramArrayOfByte[(paramInt + 2)]) >> 3);
      if ((0x4 & paramArrayOfByte[(paramInt + 2)]) != 0)
        break label217;
      bool2 = false;
      this.isAuthAnswer = bool2;
      if ((0x2 & paramArrayOfByte[(paramInt + 2)]) != 0)
        break label223;
      bool3 = false;
      this.isTruncated = bool3;
      if ((0x1 & paramArrayOfByte[(paramInt + 2)]) != 0)
        break label229;
      bool4 = false;
      this.isRecurDesired = bool4;
      if ((0x80 & paramArrayOfByte[(1 + (paramInt + 2))]) != 0)
        break label235;
      bool5 = false;
      this.isRecurAvailable = bool5;
      int i = 0x10 & paramArrayOfByte[(1 + (paramInt + 2))];
      bool6 = false;
      if (i != 0)
        break label241;
    }
    while (true)
    {
      this.isBroadcast = bool6;
      this.resultCode = (0xF & paramArrayOfByte[(1 + (paramInt + 2))]);
      this.questionCount = readInt2(paramArrayOfByte, paramInt + 4);
      this.answerCount = readInt2(paramArrayOfByte, paramInt + 6);
      this.authorityCount = readInt2(paramArrayOfByte, paramInt + 8);
      this.additionalCount = readInt2(paramArrayOfByte, paramInt + 10);
      return 12;
      bool1 = true;
      break;
      label217: bool2 = true;
      break label55;
      label223: bool3 = true;
      break label74;
      label229: bool4 = true;
      break label93;
      label235: bool5 = true;
      break label116;
      label241: bool6 = true;
    }
  }

  int readQuestionSectionWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + this.questionName.readWireFormat(paramArrayOfByte, paramInt);
    this.questionType = readInt2(paramArrayOfByte, i);
    int j = i + 2;
    this.questionClass = readInt2(paramArrayOfByte, j);
    return j + 2 - paramInt;
  }

  abstract int readRDataWireFormat(byte[] paramArrayOfByte, int paramInt);

  int readResourceRecordWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    if ((0xC0 & paramArrayOfByte[paramInt]) == 192)
      this.recordName = this.questionName;
    int n;
    for (int i = paramInt + 2; ; i = paramInt + this.recordName.readWireFormat(paramArrayOfByte, paramInt))
    {
      this.recordType = readInt2(paramArrayOfByte, i);
      int j = i + 2;
      this.recordClass = readInt2(paramArrayOfByte, j);
      int k = j + 2;
      this.ttl = readInt4(paramArrayOfByte, k);
      int m = k + 4;
      this.rDataLength = readInt2(paramArrayOfByte, m);
      n = m + 2;
      this.addrEntry = new NbtAddress[this.rDataLength / 6];
      int i1 = n + this.rDataLength;
      for (this.addrIndex = 0; n < i1; this.addrIndex = (1 + this.addrIndex))
        n += readRDataWireFormat(paramArrayOfByte, n);
    }
    return n - paramInt;
  }

  int readWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + readHeaderWireFormat(paramArrayOfByte, paramInt);
    return i + readBodyWireFormat(paramArrayOfByte, i) - paramInt;
  }

  public String toString()
  {
    String str1;
    label115: String str2;
    label167: String str3;
    label247: String str4;
    label447: StringBuffer localStringBuffer2;
    switch (this.opCode)
    {
    default:
      str1 = Integer.toString(this.opCode);
      switch (this.resultCode)
      {
      case 3:
      default:
        new StringBuffer().append("0x").append(Hexdump.toHexString(this.resultCode, 1)).toString();
        switch (this.questionType)
        {
        default:
          str2 = "0x" + Hexdump.toHexString(this.questionType, 4);
          switch (this.recordType)
          {
          default:
            str3 = "0x" + Hexdump.toHexString(this.recordType, 4);
            StringBuffer localStringBuffer1 = new StringBuffer().append("nameTrnId=").append(this.nameTrnId).append(",isResponse=").append(this.isResponse).append(",opCode=").append(str1).append(",isAuthAnswer=").append(this.isAuthAnswer).append(",isTruncated=").append(this.isTruncated).append(",isRecurAvailable=").append(this.isRecurAvailable).append(",isRecurDesired=").append(this.isRecurDesired).append(",isBroadcast=").append(this.isBroadcast).append(",resultCode=").append(this.resultCode).append(",questionCount=").append(this.questionCount).append(",answerCount=").append(this.answerCount).append(",authorityCount=").append(this.authorityCount).append(",additionalCount=").append(this.additionalCount).append(",questionName=").append(this.questionName).append(",questionType=").append(str2).append(",questionClass=");
            if (this.questionClass == 1)
            {
              str4 = "IN";
              localStringBuffer2 = localStringBuffer1.append(str4).append(",recordName=").append(this.recordName).append(",recordType=").append(str3).append(",recordClass=");
              if (this.recordClass != 1)
                break label639;
            }
            break;
          case 1:
          case 2:
          case 10:
          case 32:
          case 33:
          }
          break;
        case 32:
        case 33:
        }
        break;
      case 1:
      case 2:
      case 4:
      case 5:
      case 6:
      case 7:
      }
      break;
    case 0:
    case 7:
    }
    label639: for (String str5 = "IN"; ; str5 = "0x" + Hexdump.toHexString(this.recordClass, 4))
    {
      return new String(str5 + ",ttl=" + this.ttl + ",rDataLength=" + this.rDataLength);
      str1 = "QUERY";
      break;
      str1 = "WACK";
      break;
      break label115;
      break label115;
      break label115;
      break label115;
      break label115;
      break label115;
      str2 = "NB";
      break label167;
      str2 = "NBSTAT";
      break label167;
      str3 = "A";
      break label247;
      str3 = "NS";
      break label247;
      str3 = "NULL";
      break label247;
      str3 = "NB";
      break label247;
      str3 = "NBSTAT";
      break label247;
      str4 = "0x" + Hexdump.toHexString(this.questionClass, 4);
      break label447;
    }
  }

  abstract int writeBodyWireFormat(byte[] paramArrayOfByte, int paramInt);

  int writeHeaderWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = 128;
    writeInt2(this.nameTrnId, paramArrayOfByte, paramInt);
    int j = paramInt + 2;
    int k;
    int n;
    label52: int i2;
    label69: int i4;
    label86: int i5;
    if (this.isResponse)
    {
      k = i;
      int m = k + (0x78 & this.opCode << 3);
      if (!this.isAuthAnswer)
        break label201;
      n = 4;
      int i1 = m + n;
      if (!this.isTruncated)
        break label207;
      i2 = 2;
      int i3 = i1 + i2;
      if (!this.isRecurDesired)
        break label213;
      i4 = 1;
      paramArrayOfByte[j] = ((byte)(i4 + i3));
      i5 = 1 + (paramInt + 2);
      if (!this.isRecurAvailable)
        break label219;
    }
    while (true)
    {
      boolean bool = this.isBroadcast;
      int i6 = 0;
      if (bool)
        i6 = 16;
      paramArrayOfByte[i5] = ((byte)(i + i6 + (0xF & this.resultCode)));
      writeInt2(this.questionCount, paramArrayOfByte, paramInt + 4);
      writeInt2(this.answerCount, paramArrayOfByte, paramInt + 6);
      writeInt2(this.authorityCount, paramArrayOfByte, paramInt + 8);
      writeInt2(this.additionalCount, paramArrayOfByte, paramInt + 10);
      return 12;
      k = 0;
      break;
      label201: n = 0;
      break label52;
      label207: i2 = 0;
      break label69;
      label213: i4 = 0;
      break label86;
      label219: i = 0;
    }
  }

  int writeQuestionSectionWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + this.questionName.writeWireFormat(paramArrayOfByte, paramInt);
    writeInt2(this.questionType, paramArrayOfByte, i);
    int j = i + 2;
    writeInt2(this.questionClass, paramArrayOfByte, j);
    return j + 2 - paramInt;
  }

  abstract int writeRDataWireFormat(byte[] paramArrayOfByte, int paramInt);

  int writeResourceRecordWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i;
    if (this.recordName == this.questionName)
    {
      int n = paramInt + 1;
      paramArrayOfByte[paramInt] = -64;
      i = n + 1;
      paramArrayOfByte[n] = 12;
    }
    while (true)
    {
      writeInt2(this.recordType, paramArrayOfByte, i);
      int j = i + 2;
      writeInt2(this.recordClass, paramArrayOfByte, j);
      int k = j + 2;
      writeInt4(this.ttl, paramArrayOfByte, k);
      int m = k + 4;
      this.rDataLength = writeRDataWireFormat(paramArrayOfByte, m + 2);
      writeInt2(this.rDataLength, paramArrayOfByte, m);
      return m + (2 + this.rDataLength) - paramInt;
      i = paramInt + this.recordName.writeWireFormat(paramArrayOfByte, paramInt);
    }
  }

  int writeWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + writeHeaderWireFormat(paramArrayOfByte, paramInt);
    return i + writeBodyWireFormat(paramArrayOfByte, i) - paramInt;
  }
}