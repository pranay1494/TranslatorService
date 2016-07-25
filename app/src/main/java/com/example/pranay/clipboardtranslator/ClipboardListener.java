package com.example.pranay.clipboardtranslator;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

/**
 * Created by Pranay on 25-07-2016.
 */
public class ClipboardListener implements ClipboardManager.OnPrimaryClipChangedListener {

    Context context;
    ClipboardManager clipBoard;
    private String translation;
    private String text;
    private TextView tvOriginal;
    private TextView tvTranslated;
    private TextView tvHeader;

    public ClipboardListener(Clipboard clipboard, ClipboardManager clipBoard) {
        this.context = clipboard;
        this.clipBoard = clipBoard;
    }

    @Override
    public void onPrimaryClipChanged() {
        ClipData clipData = clipBoard.getPrimaryClip();
        ClipData.Item item = clipData.getItemAt(0);
        text = item.getText().toString();
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();
    }

    public class MyAsyncTask extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            Translate.setClientId("pranay_bansal");
            Translate.setClientSecret("XdjVDiC0XR7XXbnfwkt9u5ZYEcU5D7PFnRB3Qs9VJ/Q=");
            try {
                translation = Translate.execute(text, Language.AUTO_DETECT,Language.HINDI);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            Toast.makeText(context,translation,Toast.LENGTH_SHORT).show();
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog);
            dialog.setTitle("ClipBoardTranslator");
            tvOriginal = (TextView) dialog.findViewById(R.id.textView2);
            tvOriginal.setText(text);
            tvTranslated = (TextView) dialog.findViewById(R.id.textView3);
            tvTranslated.setText(translation);
            dialog.setCancelable(false);
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            tvHeader = (TextView) dialog.findViewById(R.id.textView);
            tvHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }
}
