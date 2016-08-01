package com.example.pranay.clipboardtranslator;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
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
    private WindowManager windowManager;
    WindowManager.LayoutParams params;
    WindowManager.LayoutParams paramsForRView;
    private ImageView chatHead;
    private long deltaForlongClickUpTime;
    private long deltaForlongClickDownTime;
    private boolean isLongClicked = false;
    private ImageView removeView;

    public ClipboardListener(Clipboard clipboard, ClipboardManager clipBoard) {
        this.context = clipboard;
        this.clipBoard = clipBoard;
    }

    @Override
    public void onPrimaryClipChanged() {
        ClipData clipData = clipBoard.getPrimaryClip();
        text = clipData.getItemAt(0).coerceToText(context).toString();
      //  text = item.getText().toString();
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

            makeDialog();

/*
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
            dialog.show();*/
        }
    }

    private void makeDialog() {

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        makeRemoveIcon();
        params= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;
        chatHead = new ImageView(context);
        chatHead.setImageResource(R.drawable.chathead);
        chatHead.setLayoutParams(new android.view.ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        chatHead.setMaxHeight(130);
        chatHead.setMaxWidth(130);
        chatHead.setAdjustViewBounds(true);

        chatHead.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();

                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = motionEvent.getRawX();
                        initialTouchY = motionEvent.getRawY();

                       deltaForlongClickDownTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        deltaForlongClickUpTime = System.currentTimeMillis();
                        if(deltaForlongClickUpTime-deltaForlongClickDownTime>2000){
                            isLongClicked = true;
                        }
                        int rViewX = (int) (removeView.getX()+removeView.getWidth());
                        int cHeadX = (int) (chatHead.getX()+chatHead.getWidth());
                        int diffX = Math.abs(rViewX-cHeadX);

                        int rViewY = (int) (removeView.getY()+removeView.getHeight());
                        int cheadY = (int) (chatHead.getY()+chatHead.getHeight());
                        int diffY = Math.abs(rViewY-cheadY);
                        if(isLongClicked&&(diffX<300||diffY<300)){
                            chatHead.setVisibility(View.GONE);
                        }
                        else{
                            //todo open dialog
                        }
                        removeView.setVisibility(View.GONE);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        removeView.setVisibility(View.VISIBLE);
                        params.x = initialX
                                + (int) (motionEvent.getRawX() - initialTouchX);
                        params.y = initialY
                                + (int) (motionEvent.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(chatHead, params);
                        if(isLongClicked&&(removeView.getX()==params.x&&removeView.getY()==params.y)){
                            chatHead.setVisibility(View.GONE);
                            removeView.setVisibility(View.GONE);
                        }
                    default:
                }
                return false;
            }
        });
        windowManager.addView(chatHead, params);
    }

    private void makeRemoveIcon() {
        removeView = new ImageView(context);
        paramsForRView= new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        paramsForRView.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        removeView.setImageResource(R.drawable.close);
        removeView.setVisibility(View.GONE);
        removeView.setLayoutParams(new android.view.ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        removeView.setMaxHeight(130);
        removeView.setMaxWidth(130);
        removeView.setAdjustViewBounds(true);
        windowManager.addView(removeView, paramsForRView);
    }
}
