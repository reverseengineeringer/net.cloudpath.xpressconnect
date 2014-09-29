package org.bouncycastle2.util;

import java.util.Collection;

public abstract interface StreamParser
{
  public abstract Object read()
    throws StreamParsingException;

  public abstract Collection readAll()
    throws StreamParsingException;
}