package org.xbill.DNS;

public final class Section
{
  public static final int ADDITIONAL = 3;
  public static final int ANSWER = 1;
  public static final int AUTHORITY = 2;
  public static final int PREREQ = 1;
  public static final int QUESTION = 0;
  public static final int UPDATE = 2;
  public static final int ZONE;
  private static String[] longSections;
  private static Mnemonic sections = new Mnemonic("Message Section", 3);
  private static String[] updateSections;

  static
  {
    longSections = new String[4];
    updateSections = new String[4];
    sections.setMaximum(3);
    sections.setNumericAllowed(true);
    sections.add(0, "qd");
    sections.add(1, "an");
    sections.add(2, "au");
    sections.add(3, "ad");
    longSections[0] = "QUESTIONS";
    longSections[1] = "ANSWERS";
    longSections[2] = "AUTHORITY RECORDS";
    longSections[3] = "ADDITIONAL RECORDS";
    updateSections[0] = "ZONE";
    updateSections[1] = "PREREQUISITES";
    updateSections[2] = "UPDATE RECORDS";
    updateSections[3] = "ADDITIONAL RECORDS";
  }

  public static String longString(int paramInt)
  {
    sections.check(paramInt);
    return longSections[paramInt];
  }

  public static String string(int paramInt)
  {
    return sections.getText(paramInt);
  }

  public static String updString(int paramInt)
  {
    sections.check(paramInt);
    return updateSections[paramInt];
  }

  public static int value(String paramString)
  {
    return sections.getValue(paramString);
  }
}