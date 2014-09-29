package jcifs.smb;

import java.io.IOException;

public class SecurityDescriptor
{
  public ACE[] aces;
  public int type;

  public SecurityDescriptor()
  {
  }

  public SecurityDescriptor(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    decode(paramArrayOfByte, paramInt1, paramInt2);
  }

  public int decode(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = 1 + (paramInt1 + 1);
    this.type = ServerMessageBlock.readInt2(paramArrayOfByte, i);
    int j = i + 2;
    ServerMessageBlock.readInt4(paramArrayOfByte, j);
    int k = j + 4;
    ServerMessageBlock.readInt4(paramArrayOfByte, k);
    int m = k + 4;
    ServerMessageBlock.readInt4(paramArrayOfByte, m);
    int n = ServerMessageBlock.readInt4(paramArrayOfByte, m + 4);
    int i1 = 1 + (1 + (paramInt1 + n));
    ServerMessageBlock.readInt2(paramArrayOfByte, i1);
    int i2 = i1 + 2;
    int i3 = ServerMessageBlock.readInt4(paramArrayOfByte, i2);
    int i4 = i2 + 4;
    if (i3 > 4096)
      throw new IOException("Invalid SecurityDescriptor");
    if (n != 0)
    {
      this.aces = new ACE[i3];
      for (int i5 = 0; i5 < i3; i5++)
      {
        this.aces[i5] = new ACE();
        i4 += this.aces[i5].decode(paramArrayOfByte, i4);
      }
    }
    this.aces = null;
    return i4 - paramInt1;
  }

  public String toString()
  {
    String str = "SecurityDescriptor:\n";
    if (this.aces != null)
      for (int i = 0; i < this.aces.length; i++)
        str = str + this.aces[i].toString() + "\n";
    str = str + "NULL";
    return str;
  }
}