package org.bouncycastle2.i18n;

import java.util.Locale;

public class LocalizedException extends Exception
{
  private Throwable cause;
  protected ErrorBundle message;

  public LocalizedException(ErrorBundle paramErrorBundle)
  {
    super(paramErrorBundle.getText(Locale.getDefault()));
    this.message = paramErrorBundle;
  }

  public LocalizedException(ErrorBundle paramErrorBundle, Throwable paramThrowable)
  {
    super(paramErrorBundle.getText(Locale.getDefault()));
    this.message = paramErrorBundle;
    this.cause = paramThrowable;
  }

  public Throwable getCause()
  {
    return this.cause;
  }

  public ErrorBundle getErrorMessage()
  {
    return this.message;
  }
}