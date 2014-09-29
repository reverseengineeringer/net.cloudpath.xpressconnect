package android.support.v13.app;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public abstract class FragmentPagerAdapter extends PagerAdapter
{
  private static final boolean DEBUG = false;
  private static final String TAG = "FragmentPagerAdapter";
  private FragmentTransaction mCurTransaction = null;
  private Fragment mCurrentPrimaryItem = null;
  private final FragmentManager mFragmentManager;

  public FragmentPagerAdapter(FragmentManager paramFragmentManager)
  {
    this.mFragmentManager = paramFragmentManager;
  }

  private static String makeFragmentName(int paramInt, long paramLong)
  {
    return "android:switcher:" + paramInt + ":" + paramLong;
  }

  public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
  {
    if (this.mCurTransaction == null)
      this.mCurTransaction = this.mFragmentManager.beginTransaction();
    this.mCurTransaction.detach((Fragment)paramObject);
  }

  public void finishUpdate(ViewGroup paramViewGroup)
  {
    if (this.mCurTransaction != null)
    {
      this.mCurTransaction.commitAllowingStateLoss();
      this.mCurTransaction = null;
      this.mFragmentManager.executePendingTransactions();
    }
  }

  public abstract Fragment getItem(int paramInt);

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
  {
    if (this.mCurTransaction == null)
      this.mCurTransaction = this.mFragmentManager.beginTransaction();
    long l = getItemId(paramInt);
    String str = makeFragmentName(paramViewGroup.getId(), l);
    Fragment localFragment = this.mFragmentManager.findFragmentByTag(str);
    if (localFragment != null)
      this.mCurTransaction.attach(localFragment);
    while (true)
    {
      if (localFragment != this.mCurrentPrimaryItem)
      {
        FragmentCompat.setMenuVisibility(localFragment, false);
        FragmentCompat.setUserVisibleHint(localFragment, false);
      }
      return localFragment;
      localFragment = getItem(paramInt);
      this.mCurTransaction.add(paramViewGroup.getId(), localFragment, makeFragmentName(paramViewGroup.getId(), l));
    }
  }

  public boolean isViewFromObject(View paramView, Object paramObject)
  {
    return ((Fragment)paramObject).getView() == paramView;
  }

  public void restoreState(Parcelable paramParcelable, ClassLoader paramClassLoader)
  {
  }

  public Parcelable saveState()
  {
    return null;
  }

  public void setPrimaryItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
  {
    Fragment localFragment = (Fragment)paramObject;
    if (localFragment != this.mCurrentPrimaryItem)
    {
      if (this.mCurrentPrimaryItem != null)
      {
        FragmentCompat.setMenuVisibility(this.mCurrentPrimaryItem, false);
        FragmentCompat.setUserVisibleHint(this.mCurrentPrimaryItem, false);
      }
      if (localFragment != null)
      {
        FragmentCompat.setMenuVisibility(localFragment, true);
        FragmentCompat.setUserVisibleHint(localFragment, true);
      }
      this.mCurrentPrimaryItem = localFragment;
    }
  }

  public void startUpdate(ViewGroup paramViewGroup)
  {
  }
}