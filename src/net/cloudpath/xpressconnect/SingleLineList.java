package net.cloudpath.xpressconnect;

import android.content.Context;
import android.widget.SimpleAdapter;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

public class SingleLineList
{
  private ArrayList<HashMap<String, String>> list = new ArrayList();
  private Context parentContext;
  private SimpleAdapter taskAdapter;

  public SingleLineList(Context paramContext)
  {
    this.parentContext = paramContext;
    ParcelHelper localParcelHelper = new ParcelHelper("", this.parentContext);
    Context localContext = this.parentContext;
    ArrayList localArrayList = this.list;
    int i = localParcelHelper.getLayoutId("xpc_itemselectlayout");
    String[] arrayOfString = { "text1" };
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = localParcelHelper.getItemId("xpc_text1");
    this.taskAdapter = new SimpleAdapter(localContext, localArrayList, i, arrayOfString, arrayOfInt);
  }

  public void addItem(String paramString)
  {
    if ((paramString == null) || (paramString.contentEquals("")))
    {
      System.out.println("Text item is empty or null in addItem()!");
      return;
    }
    HashMap localHashMap = new HashMap();
    localHashMap.put("text1", paramString);
    this.list.add(localHashMap);
  }

  public void clear()
  {
    this.list.clear();
  }

  public SimpleAdapter getData()
  {
    return this.taskAdapter;
  }
}