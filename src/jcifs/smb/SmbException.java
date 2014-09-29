package jcifs.smb;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import jcifs.util.Hexdump;

public class SmbException extends IOException
  implements NtStatus, DosError, WinError
{
  private Throwable rootCause;
  private int status;

  SmbException()
  {
  }

  SmbException(int paramInt, Throwable paramThrowable)
  {
    super(getMessageByCode(paramInt));
    this.status = getStatusByCode(paramInt);
    this.rootCause = paramThrowable;
  }

  public SmbException(int paramInt, boolean paramBoolean)
  {
  }

  SmbException(String paramString)
  {
    super(paramString);
    this.status = -1073741823;
  }

  SmbException(String paramString, Throwable paramThrowable)
  {
    super(paramString);
    this.rootCause = paramThrowable;
    this.status = -1073741823;
  }

  static String getMessageByCode(int paramInt)
  {
    if (paramInt == 0)
      return "NT_STATUS_SUCCESS";
    if ((paramInt & 0xC0000000) == -1073741824)
    {
      int m = 1;
      int n = -1 + WinError.NT_STATUS_CODES.length;
      int i1;
      while (true)
      {
        if (n < m)
          break label150;
        i1 = (m + n) / 2;
        if (paramInt > WinError.NT_STATUS_CODES[i1])
        {
          m = i1 + 1;
        }
        else
        {
          if (paramInt >= WinError.NT_STATUS_CODES[i1])
            break;
          n = i1 - 1;
        }
      }
      return WinError.NT_STATUS_MESSAGES[i1];
    }
    int i = 0;
    int j = -1 + DosError.DOS_ERROR_CODES.length;
    while (j >= i)
    {
      int k = (i + j) / 2;
      if (paramInt > DosError.DOS_ERROR_CODES[k][0])
        i = k + 1;
      else if (paramInt < DosError.DOS_ERROR_CODES[k][0])
        j = k - 1;
      else
        return DosError.DOS_ERROR_MESSAGES[k];
    }
    label150: return "0x" + Hexdump.toHexString(paramInt, 8);
  }

  static String getMessageByWinerrCode(int paramInt)
  {
    int i = 0;
    int j = -1 + WinError.WINERR_CODES.length;
    while (j >= i)
    {
      int k = (i + j) / 2;
      if (paramInt > WinError.WINERR_CODES[k])
        i = k + 1;
      else if (paramInt < WinError.WINERR_CODES[k])
        j = k - 1;
      else
        return WinError.WINERR_MESSAGES[k];
    }
    return paramInt + "";
  }

  static int getStatusByCode(int paramInt)
  {
    if ((0xC0000000 & paramInt) != 0)
      return paramInt;
    int i = 0;
    int j = -1 + DosError.DOS_ERROR_CODES.length;
    while (j >= i)
    {
      int k = (i + j) / 2;
      if (paramInt > DosError.DOS_ERROR_CODES[k][0])
        i = k + 1;
      else if (paramInt < DosError.DOS_ERROR_CODES[k][0])
        j = k - 1;
      else
        return DosError.DOS_ERROR_CODES[k][1];
    }
    return -1073741823;
  }

  public int getNtStatus()
  {
    return this.status;
  }

  public Throwable getRootCause()
  {
    return this.rootCause;
  }

  public String toString()
  {
    if (this.rootCause != null)
    {
      StringWriter localStringWriter = new StringWriter();
      PrintWriter localPrintWriter = new PrintWriter(localStringWriter);
      this.rootCause.printStackTrace(localPrintWriter);
      return super.toString() + "\n" + localStringWriter;
    }
    return super.toString();
  }
}