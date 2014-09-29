package org.xbill.DNS;

public final class Opcode
{
  public static final int IQUERY = 1;
  public static final int NOTIFY = 4;
  public static final int QUERY = 0;
  public static final int STATUS = 2;
  public static final int UPDATE = 5;
  private static Mnemonic opcodes = new Mnemonic("DNS Opcode", 2);

  static
  {
    opcodes.setMaximum(15);
    opcodes.setPrefix("RESERVED");
    opcodes.setNumericAllowed(true);
    opcodes.add(0, "QUERY");
    opcodes.add(1, "IQUERY");
    opcodes.add(2, "STATUS");
    opcodes.add(4, "NOTIFY");
    opcodes.add(5, "UPDATE");
  }

  public static String string(int paramInt)
  {
    return opcodes.getText(paramInt);
  }

  public static int value(String paramString)
  {
    return opcodes.getValue(paramString);
  }
}