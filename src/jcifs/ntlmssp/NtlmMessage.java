package jcifs.ntlmssp;

import jcifs.Config;

public abstract class NtlmMessage
  implements NtlmFlags
{
  protected static final byte[] NTLMSSP_SIGNATURE = { 78, 84, 76, 77, 83, 83, 80, 0 };
  private static final String OEM_ENCODING = Config.getProperty("jcifs.encoding", "Cp850");
  private int flags;

  static String getOEMEncoding()
  {
    return OEM_ENCODING;
  }

  static byte[] readSecurityBuffer(byte[] paramArrayOfByte, int paramInt)
  {
    int i = readUShort(paramArrayOfByte, paramInt);
    int j = readULong(paramArrayOfByte, paramInt + 4);
    byte[] arrayOfByte = new byte[i];
    System.arraycopy(paramArrayOfByte, j, arrayOfByte, 0, i);
    return arrayOfByte;
  }

  static int readULong(byte[] paramArrayOfByte, int paramInt)
  {
    return 0xFF & paramArrayOfByte[paramInt] | (0xFF & paramArrayOfByte[(paramInt + 1)]) << 8 | (0xFF & paramArrayOfByte[(paramInt + 2)]) << 16 | (0xFF & paramArrayOfByte[(paramInt + 3)]) << 24;
  }

  static int readUShort(byte[] paramArrayOfByte, int paramInt)
  {
    return 0xFF & paramArrayOfByte[paramInt] | (0xFF & paramArrayOfByte[(paramInt + 1)]) << 8;
  }

  static void writeSecurityBuffer(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2)
  {
    if (paramArrayOfByte2 != null);
    for (int i = paramArrayOfByte2.length; i == 0; i = 0)
      return;
    writeUShort(paramArrayOfByte1, paramInt1, i);
    writeUShort(paramArrayOfByte1, paramInt1 + 2, i);
    writeULong(paramArrayOfByte1, paramInt1 + 4, paramInt2);
    System.arraycopy(paramArrayOfByte2, 0, paramArrayOfByte1, paramInt2, i);
  }

  static void writeULong(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    paramArrayOfByte[paramInt1] = ((byte)(paramInt2 & 0xFF));
    paramArrayOfByte[(paramInt1 + 1)] = ((byte)(0xFF & paramInt2 >> 8));
    paramArrayOfByte[(paramInt1 + 2)] = ((byte)(0xFF & paramInt2 >> 16));
    paramArrayOfByte[(paramInt1 + 3)] = ((byte)(0xFF & paramInt2 >> 24));
  }

  static void writeUShort(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    paramArrayOfByte[paramInt1] = ((byte)(paramInt2 & 0xFF));
    paramArrayOfByte[(paramInt1 + 1)] = ((byte)(0xFF & paramInt2 >> 8));
  }

  public boolean getFlag(int paramInt)
  {
    return (paramInt & getFlags()) != 0;
  }

  public int getFlags()
  {
    return this.flags;
  }

  public void setFlag(int paramInt, boolean paramBoolean)
  {
    if (paramBoolean);
    for (int i = paramInt | getFlags(); ; i = getFlags() & (paramInt ^ 0xFFFFFFFF))
    {
      setFlags(i);
      return;
    }
  }

  public void setFlags(int paramInt)
  {
    this.flags = paramInt;
  }

  public abstract byte[] toByteArray();
}