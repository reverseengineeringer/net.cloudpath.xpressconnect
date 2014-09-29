package org.bouncycastle2.util;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests
{
  public static void main(String[] paramArrayOfString)
  {
  }

  public static Test suite()
  {
    TestSuite localTestSuite = new TestSuite("util tests");
    localTestSuite.addTestSuite(IPTest.class);
    return localTestSuite;
  }
}