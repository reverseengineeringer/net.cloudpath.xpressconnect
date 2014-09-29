package net.cloudpath.xpressconnect.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.TextView;
import java.io.PrintStream;

public class TabTextView extends TextView
{
  private Bitmap mBitmapDrawable = null;
  private int mLastHeight = 0;
  private int mLastWidth = 0;
  private Paint mPainter = null;
  private GradientDrawable mTabDrawable = null;

  public TabTextView(Context paramContext)
  {
    super(paramContext);
    setupDrawable();
  }

  public TabTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setupDrawable();
  }

  private void compositeTab(int paramInt1, int paramInt2)
  {
    if ((paramInt1 <= 0) || (paramInt2 <= 0))
      System.out.println("Invalid width or height!  Not drawing tab!");
    while ((paramInt1 == this.mLastWidth) && (paramInt2 == this.mLastHeight))
      return;
    this.mLastWidth = paramInt1;
    this.mLastHeight = paramInt2;
    int i = View.MeasureSpec.getSize(paramInt2);
    this.mBitmapDrawable = Bitmap.createBitmap(paramInt1 + 100, paramInt2, Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(this.mBitmapDrawable);
    this.mTabDrawable.setBounds(new Rect(0, 0, paramInt1 + 100, i));
    this.mTabDrawable.draw(localCanvas);
  }

  private void setupDrawable()
  {
    this.mPainter = new Paint();
    setupTabDrawable();
  }

  private void setupTabDrawable()
  {
    this.mTabDrawable = ((GradientDrawable)getResources().getDrawable(2130837510));
    this.mTabDrawable.setColor(-65536);
    this.mTabDrawable.setStroke(2, -16711936);
  }

  protected void onDraw(Canvas paramCanvas)
  {
    if ((this.mBitmapDrawable != null) && (this.mPainter != null))
      paramCanvas.drawBitmap(this.mBitmapDrawable, 0.0F, 0.0F, this.mPainter);
    super.onDraw(paramCanvas);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    compositeTab(getMeasuredWidth(), getMeasuredHeight());
  }

  public void setBackgroundColor(int paramInt)
  {
    this.mTabDrawable.setColor(paramInt);
  }

  public void setBorderColor(int paramInt)
  {
    this.mTabDrawable.setStroke(2, paramInt);
  }
}