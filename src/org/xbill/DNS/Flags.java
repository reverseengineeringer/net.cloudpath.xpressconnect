package org.xbill.DNS;

public final class Flags
{
  public static final byte AA = 5;
  public static final byte AD = 10;
  public static final byte CD = 11;
  public static final int DO = 32768;
  public static final byte QR = 0;
  public static final byte RA = 8;
  public static final byte RD = 7;
  public static final byte TC = 6;
  private static Mnemonic flags = new Mnemonic("DNS Header Flag", 3);

  static
  {
    flags.setMaximum(15);
    flags.setPrefix("FLAG");
    flags.setNumericAllowed(true);
    flags.add(0, "qr");
    flags.add(5, "aa");
    flags.add(6, "tc");
    flags.add(7, "rd");
    flags.add(8, "ra");
    flags.add(10, "ad");
    flags.add(11, "cd");
  }

  public static boolean isFlag(int paramInt)
  {
    int i = 1;
    flags.check(paramInt);
    if (((paramInt >= i) && (paramInt <= 4)) || (paramInt >= 12))
      i = 0;
    return i;
  }

  public static String string(int paramInt)
  {
    return flags.getText(paramInt);
  }

  public static int value(String paramString)
  {
    return flags.getValue(paramString);
  }
}