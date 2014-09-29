package net.cloudpath.xpressconnect;

import android.content.Context;
import android.widget.SimpleAdapter;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.util.ArrayList;
import java.util.HashMap;

public class MultiLineList
{
  private ArrayList<HashMap<String, String>> list = new ArrayList();
  private Context parentContext;
  private SimpleAdapter taskAdapter;

  public MultiLineList(Context paramContext)
  {
    this.parentContext = paramContext;
    ParcelHelper localParcelHelper = new ParcelHelper("", this.parentContext);
    Context localContext = this.parentContext;
    ArrayList localArrayList = this.list;
    int i = localParcelHelper.getLayoutId("xpc_network_list_item_layout");
    String[] arrayOfString = { "largetext", "smalltext" };
    int[] arrayOfInt = new int[2];
    arrayOfInt[0] = localParcelHelper.getItemId("xpc_LargeText");
    arrayOfInt[1] = localParcelHelper.getItemId("xpc_SmallText");
    this.taskAdapter = new SimpleAdapter(localContext, localArrayList, i, arrayOfString, arrayOfInt);
  }

  public void addItem(String paramString1, String paramString2)
  {
    if ((paramString1 == null) || (paramString1.contentEquals("")))
      return;
    HashMap localHashMap = new HashMap();
    localHashMap.put("largetext", paramString1);
    localHashMap.put("smalltext", paramString2);
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