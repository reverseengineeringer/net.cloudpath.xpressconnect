package net.cloudpath.xpressconnect.screens.delegates;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Parcelable;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;

public class WebMainNfcDelegate
{
  private Intent mIntent = null;
  private Logger mLogger = null;
  private NdefMessage[] mMessages = null;

  public WebMainNfcDelegate(Intent paramIntent, Logger paramLogger)
  {
    this.mIntent = paramIntent;
    this.mLogger = paramLogger;
  }

  public boolean checkIntent()
  {
    if (this.mIntent == null)
      Util.log(this.mLogger, "Target intent was null.  Won't be able to detect NFC tag.");
    while (!"android.nfc.action.NDEF_DISCOVERED".equals(this.mIntent.getAction()))
      return false;
    this.mMessages = getNdefMessages(this.mIntent);
    return true;
  }

  public byte[] getData(int paramInt1, int paramInt2)
  {
    if ((this.mMessages == null) || (this.mMessages[0] == null) || (this.mMessages[0].getRecords() == null))
      return null;
    return this.mMessages[0].getRecords()[0].getPayload();
  }

  NdefMessage[] getNdefMessages(Intent paramIntent)
  {
    String str = paramIntent.getAction();
    if (("android.nfc.action.TAG_DISCOVERED".equals(str)) || ("android.nfc.action.NDEF_DISCOVERED".equals(str)))
    {
      Parcelable[] arrayOfParcelable = paramIntent.getParcelableArrayExtra("android.nfc.extra.NDEF_MESSAGES");
      if (arrayOfParcelable != null)
      {
        arrayOfNdefMessage = new NdefMessage[arrayOfParcelable.length];
        for (int i = 0; i < arrayOfParcelable.length; i++)
          arrayOfNdefMessage[i] = ((NdefMessage)arrayOfParcelable[i]);
      }
      byte[] arrayOfByte = new byte[0];
      NdefMessage[] arrayOfNdefMessage = { new NdefMessage(new NdefRecord[] { new NdefRecord(5, arrayOfByte, arrayOfByte, arrayOfByte) }) };
      return arrayOfNdefMessage;
    }
    return null;
  }

  public String getUrl()
  {
    byte[] arrayOfByte = getData(0, 0);
    if (arrayOfByte == null)
      return null;
    return new String(arrayOfByte);
  }
}