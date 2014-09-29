package net.cloudpath.xpressconnect.screens;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.io.PrintStream;
import java.util.WeakHashMap;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.screens.delegates.NfcWriterScreenDelegate;

public class NfcWriterScreen extends ScreenBase
  implements View.OnClickListener
{
  public static final int CANT_WRITE_NFC_TAGS = 90003;
  public static final int FAILED_TO_GATHER_TAG_DATA = 90000;
  public static final int READY_TO_WRITE_TAG = 90001;
  private NfcWriterScreenDelegate mDelegate = null;
  private Button mDoneButton = null;
  private RadioButton mFullConfig = null;
  private CheckBox mSetReadOnly = null;
  private RadioButton mWebOnly = null;
  private Button mWriteButton = null;
  public WeakHashMap<Integer, Dialog> managedDialogs = new WeakHashMap();

  @SuppressLint({"NewApi"})
  private void showMyDialog(int paramInt, String paramString)
  {
    NfcWriterScreenFragments.newInstance(paramInt).show(getFragmentManager(), paramString);
  }

  public NfcWriterScreenDelegate getDelegate()
  {
    return this.mDelegate;
  }

  public Dialog getDialogByName(String paramString)
  {
    return ((NfcWriterScreenFragments)getFragmentManager().findFragmentByTag(paramString)).getDialog();
  }

  public void onClick(View paramView)
  {
    if (paramView == this.mFullConfig)
      this.mDelegate.setDoingFullConfig(true);
    do
    {
      return;
      if (paramView == this.mWebOnly)
      {
        this.mDelegate.setDoingFullConfig(false);
        return;
      }
      if (paramView == this.mSetReadOnly)
      {
        this.mDelegate.setNfcReadOnly(this.mSetReadOnly.isChecked());
        return;
      }
      if (paramView == this.mDoneButton)
      {
        done(0);
        return;
      }
    }
    while (paramView != this.mWriteButton);
    if (!this.mDelegate.gatherTagData())
    {
      showMyDialog(90000, "dataGatherFailed");
      return;
    }
    this.mDelegate.enableWriteTag();
    showMyDialog(90001, "readyToWriteTag");
  }

  protected void onCreate(Bundle paramBundle)
  {
    preContentView();
    ParcelHelper localParcelHelper = new ParcelHelper("", this);
    setContentView(localParcelHelper.getLayoutId("xpc_write_nfc"));
    super.onCreate(paramBundle);
    this.mSetReadOnly = ((CheckBox)findViewById(localParcelHelper.getItemId("xpc_setReadOnly")));
    this.mSetReadOnly.setOnClickListener(this);
    this.mWriteButton = ((Button)findViewById(localParcelHelper.getItemId("xpc_WriteButton")));
    this.mWriteButton.setOnClickListener(this);
    this.mDoneButton = ((Button)findViewById(localParcelHelper.getItemId("xpc_DoneButton")));
    this.mDoneButton.setOnClickListener(this);
    this.mWebOnly = ((RadioButton)findViewById(localParcelHelper.getItemId("xpc_webOnlyButton")));
    this.mWebOnly.setOnClickListener(this);
    this.mFullConfig = ((RadioButton)findViewById(localParcelHelper.getItemId("xpc_fullConfigButton")));
    this.mFullConfig.setOnClickListener(this);
    this.mDelegate = new NfcWriterScreenDelegate(this);
    if (paramBundle == null)
    {
      this.mDelegate.setDoingFullConfig(true);
      this.mFullConfig.setChecked(true);
      this.mSetReadOnly.setChecked(false);
    }
    while (true)
    {
      if (Build.VERSION.SDK_INT < 16)
      {
        Util.log(this.mLogger, "Version is too old to support a full config tag.  Only allowing URL tags.");
        this.mFullConfig.setEnabled(false);
        this.mFullConfig.setChecked(false);
        this.mFullConfig.setVisibility(4);
        this.mWebOnly.setChecked(true);
      }
      System.out.println("+++++ NfcWriterScreen onCreate() done.");
      return;
      if (paramBundle.getBoolean("fullconfig"))
      {
        this.mDelegate.setDoingFullConfig(true);
        this.mFullConfig.setChecked(true);
      }
      while (true)
      {
        if (!paramBundle.getBoolean("readonly"))
          break label317;
        this.mSetReadOnly.setChecked(true);
        break;
        this.mDelegate.setDoingFullConfig(false);
        this.mWebOnly.setChecked(true);
      }
      label317: this.mSetReadOnly.setChecked(false);
    }
  }

  protected void onNewIntent(Intent paramIntent)
  {
    if ("android.nfc.action.TAG_DISCOVERED".equals(paramIntent.getAction()))
    {
      Tag localTag = (Tag)paramIntent.getParcelableExtra("android.nfc.extra.TAG");
      if (this.mDelegate.writeTag(this.mDelegate.getNdefRecord(), localTag))
        Toast.makeText(this, "Success : Wrote to nfc tag.", 1).show();
    }
    else
    {
      return;
    }
    Toast.makeText(this, "Write failed. (Tag may be read only)", 1).show();
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putBoolean("readonly", this.mSetReadOnly.isChecked());
    paramBundle.putBoolean("fullconfig", this.mFullConfig.isChecked());
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    super.onServiceBind(paramEncapsulationService);
    if (this.mParser == null)
      Util.log(this.mLogger, "Config was not properly bound!");
    this.mDelegate.setRequiredValues(this.mLogger, this.mParser);
    if (!this.mDelegate.canWriteNfcTags())
      showMyDialog(90003, "cantWriteNfcTags");
  }

  public void setDelegate(NfcWriterScreenDelegate paramNfcWriterScreenDelegate)
  {
    this.mDelegate = paramNfcWriterScreenDelegate;
  }

  public static class NfcWriterScreenFragments extends DialogFragment
  {
    public static NfcWriterScreenFragments newInstance(int paramInt)
    {
      NfcWriterScreenFragments localNfcWriterScreenFragments = new NfcWriterScreenFragments();
      Bundle localBundle = new Bundle();
      localBundle.putInt("id", paramInt);
      localNfcWriterScreenFragments.setArguments(localBundle);
      return localNfcWriterScreenFragments;
    }

    public Dialog onCreateDialog(Bundle paramBundle)
    {
      int i = getArguments().getInt("id");
      final NfcWriterScreen localNfcWriterScreen = (NfcWriterScreen)getActivity();
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
      if (i == 90000)
      {
        localBuilder.setNegativeButton(localNfcWriterScreen.mParcelHelper.getIdentifier("xpc_ok_string", "string"), new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
          {
            NfcWriterScreen.NfcWriterScreenFragments.this.getActivity().finish();
            NfcWriterScreen.NfcWriterScreenFragments.this.getActivity().overridePendingTransition(0, 0);
          }
        });
        localBuilder.setTitle(localNfcWriterScreen.mParcelHelper.getIdentifier("xpc_error_title", "string"));
        localBuilder.setMessage(localNfcWriterScreen.mParcelHelper.getIdentifier("xpc_gather_failed", "string"));
        localBuilder.setCancelable(false);
        return localBuilder.create();
      }
      if (i == 90001)
      {
        localBuilder.setPositiveButton(localNfcWriterScreen.mParcelHelper.getIdentifier("xpc_ok_string", "string"), new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
          {
            localNfcWriterScreen.mDelegate.disableWriteTag();
            Toast.makeText(localNfcWriterScreen, localNfcWriterScreen.mParcelHelper.getIdentifier("xpc_nfc_not_writing", "string"), 1).show();
          }
        });
        localBuilder.setTitle(localNfcWriterScreen.mParcelHelper.getIdentifier("xpc_nfc_writing", "string"));
        localBuilder.setMessage(localNfcWriterScreen.mParcelHelper.getIdentifier("xpc_nfc_write_text", "string"));
        return localBuilder.create();
      }
      if (i == 90003)
      {
        localBuilder.setNegativeButton(localNfcWriterScreen.mParcelHelper.getIdentifier("xpc_done_string", "string"), new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
          {
            localNfcWriterScreen.finish();
            localNfcWriterScreen.overridePendingTransition(0, 0);
          }
        });
        localBuilder.setTitle(localNfcWriterScreen.mParcelHelper.getIdentifier("xpc_error_title", "string"));
        localBuilder.setMessage(localNfcWriterScreen.mParcelHelper.getIdentifier("xpc_cant_write_tags", "string"));
        localBuilder.setCancelable(false);
        return localBuilder.create();
      }
      return null;
    }
  }
}