package jcifs.netbios;

import java.io.IOException;

public class NbtException extends IOException
{
  public static final int ACT_ERR = 6;
  public static final int CALLED_NOT_PRESENT = 130;
  public static final int CFT_ERR = 7;
  public static final int CONNECTION_REFUSED = -1;
  public static final int ERR_NAM_SRVC = 1;
  public static final int ERR_SSN_SRVC = 2;
  public static final int FMT_ERR = 1;
  public static final int IMP_ERR = 4;
  public static final int NOT_LISTENING_CALLED = 128;
  public static final int NOT_LISTENING_CALLING = 129;
  public static final int NO_RESOURCES = 131;
  public static final int RFS_ERR = 5;
  public static final int SRV_ERR = 2;
  public static final int SUCCESS = 0;
  public static final int UNSPECIFIED = 143;
  public int errorClass;
  public int errorCode;

  public NbtException(int paramInt1, int paramInt2)
  {
    super(getErrorString(paramInt1, paramInt2));
    this.errorClass = paramInt1;
    this.errorCode = paramInt2;
  }

  public static String getErrorString(int paramInt1, int paramInt2)
  {
    switch (paramInt1)
    {
    default:
      return "" + "unknown error class: " + paramInt1;
    case 0:
      return "" + "SUCCESS";
    case 1:
      String str2 = "" + "ERR_NAM_SRVC/";
      switch (paramInt2)
      {
      default:
      case 1:
      }
      while (true)
      {
        return str2 + "Unknown error code: " + paramInt2;
        str2 = str2 + "FMT_ERR: Format Error";
      }
    case 2:
    }
    String str1 = "" + "ERR_SSN_SRVC/";
    switch (paramInt2)
    {
    default:
      return str1 + "Unknown error code: " + paramInt2;
    case -1:
      return str1 + "Connection refused";
    case 128:
      return str1 + "Not listening on called name";
    case 129:
      return str1 + "Not listening for calling name";
    case 130:
      return str1 + "Called name not present";
    case 131:
      return str1 + "Called name present, but insufficient resources";
    case 143:
    }
    return str1 + "Unspecified error";
  }

  public String toString()
  {
    return new String("errorClass=" + this.errorClass + ",errorCode=" + this.errorCode + ",errorString=" + getErrorString(this.errorClass, this.errorCode));
  }
}