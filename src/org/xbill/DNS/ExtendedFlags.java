package org.xbill.DNS;

public final class ExtendedFlags
{
  public static final int DO = 32768;
  private static Mnemonic extflags = new Mnemonic("EDNS Flag", 3);

  static
  {
    extflags.setMaximum(65535);
    extflags.setPrefix("FLAG");
    extflags.setNumericAllowed(true);
    extflags.add(32768, "do");
  }

  public static String string(int paramInt)
  {
    return extflags.getText(paramInt);
  }

  public static int value(String paramString)
  {
    return extflags.getValue(paramString);
  }
}