package jcifs.dcerpc;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import jcifs.smb.WinError;
import jcifs.util.Hexdump;

public class DcerpcException extends IOException
  implements DcerpcError, WinError
{
  private int error;
  private Throwable rootCause;

  DcerpcException(int paramInt)
  {
    super(getMessageByDcerpcError(paramInt));
    this.error = paramInt;
  }

  DcerpcException(String paramString)
  {
    super(paramString);
  }

  DcerpcException(String paramString, Throwable paramThrowable)
  {
    super(paramString);
    this.rootCause = paramThrowable;
  }

  static String getMessageByDcerpcError(int paramInt)
  {
    int i = 0;
    int j = WinError.DCERPC_FAULT_CODES.length;
    while (j >= i)
    {
      int k = (i + j) / 2;
      if (paramInt > WinError.DCERPC_FAULT_CODES[k])
        i = k + 1;
      else if (paramInt < WinError.DCERPC_FAULT_CODES[k])
        j = k - 1;
      else
        return WinError.DCERPC_FAULT_MESSAGES[k];
    }
    return "0x" + Hexdump.toHexString(paramInt, 8);
  }

  public int getErrorCode()
  {
    return this.error;
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