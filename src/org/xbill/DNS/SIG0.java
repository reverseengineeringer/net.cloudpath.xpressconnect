package org.xbill.DNS;

import java.security.PrivateKey;
import java.util.Date;

public class SIG0
{
  private static final short VALIDITY = 300;

  public static void signMessage(Message paramMessage, KEYRecord paramKEYRecord, PrivateKey paramPrivateKey, SIGRecord paramSIGRecord)
    throws DNSSEC.DNSSECException
  {
    int i = Options.intValue("sig0validity");
    if (i < 0)
      i = 300;
    long l = System.currentTimeMillis();
    paramMessage.addRecord(DNSSEC.signMessage(paramMessage, paramSIGRecord, paramKEYRecord, paramPrivateKey, new Date(l), new Date(l + i * 1000)), 3);
  }

  public static void verifyMessage(Message paramMessage, byte[] paramArrayOfByte, KEYRecord paramKEYRecord, SIGRecord paramSIGRecord)
    throws DNSSEC.DNSSECException
  {
    Record[] arrayOfRecord = paramMessage.getSectionArray(3);
    int i = 0;
    int j = arrayOfRecord.length;
    SIGRecord localSIGRecord = null;
    if (i < j)
    {
      if (arrayOfRecord[i].getType() != 24);
      while (((SIGRecord)arrayOfRecord[i]).getTypeCovered() != 0)
      {
        i++;
        break;
      }
      localSIGRecord = (SIGRecord)arrayOfRecord[i];
    }
    DNSSEC.verifyMessage(paramMessage, paramArrayOfByte, localSIGRecord, paramSIGRecord, paramKEYRecord);
  }
}