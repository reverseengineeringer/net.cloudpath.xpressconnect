package org.xbill.DNS;

public final class DClass
{
  public static final int ANY = 255;
  public static final int CH = 3;
  public static final int CHAOS = 3;
  public static final int HESIOD = 4;
  public static final int HS = 4;
  public static final int IN = 1;
  public static final int NONE = 254;
  private static Mnemonic classes = new DClassMnemonic();

  static
  {
    classes.add(1, "IN");
    classes.add(3, "CH");
    classes.addAlias(3, "CHAOS");
    classes.add(4, "HS");
    classes.addAlias(4, "HESIOD");
    classes.add(254, "NONE");
    classes.add(255, "ANY");
  }

  public static void check(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > 65535))
      throw new InvalidDClassException(paramInt);
  }

  public static String string(int paramInt)
  {
    return classes.getText(paramInt);
  }

  public static int value(String paramString)
  {
    return classes.getValue(paramString);
  }

  private static class DClassMnemonic extends Mnemonic
  {
    public DClassMnemonic()
    {
      super(2);
      setPrefix("CLASS");
    }

    public void check(int paramInt)
    {
      DClass.check(paramInt);
    }
  }
}