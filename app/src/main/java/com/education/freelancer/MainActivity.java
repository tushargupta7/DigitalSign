package com.education.freelancer;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private WebView licenseWebView;
    private int currentPage = 1;
    private TextView box1;
    private TextView box2;
    private TextView box3;
    private TextView box4;
    private TextView box5;
    private TextView box6;
    private TextView previousBtn;
    private TextView nextBtn;
    private ScrollView page1;
    private ScrollView page2;
    private ScrollView page3;
    private ScrollView page4;
    private ScrollView page5;
    private ScrollView page6;
    private ImageButton page3_1,page3_5,page6_3;
    Button btn_get_sign, mClear, mGetSign, mCancel;
    EditText page2_1,page3_2,page3_3,page3_4,page3_6,page6_1,page6_2,page6_4;
    File file;

    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/DigitSign/";
    // String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    //String pic_name="hello";
   // String StoredPath = DIRECTORY + pic_name + ".png";
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        box1 = (TextView)findViewById(R.id.checked1);
        box2 = (TextView)findViewById(R.id.checked2);
        box3 = (TextView)findViewById(R.id.checked3);
        box4 = (TextView)findViewById(R.id.checked4);
        box5 = (TextView)findViewById(R.id.checked5);
        box6 = (TextView)findViewById(R.id.checked6);
        previousBtn = (TextView)findViewById(R.id.prev_button);
        previousBtn.setClickable(false);
        nextBtn = (TextView)findViewById(R.id.next_button);
        previousBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        page1 = (ScrollView)findViewById(R.id.page_1);
        page2 = (ScrollView)findViewById(R.id.page_2);
        page3 = (ScrollView)findViewById(R.id.page_3);
        page4 = (ScrollView)findViewById(R.id.page_4);
        page5 = (ScrollView)findViewById(R.id.page_5);
        page6 = (ScrollView)findViewById(R.id.page_6);
        page3_1 =(ImageButton)findViewById(R.id.page3_1);
        page3_5 =(ImageButton)findViewById(R.id.page3_5);
        page6_3 =(ImageButton)findViewById(R.id.page6_3);
        page2_1=(EditText)findViewById(R.id.page2_1);
        page3_2=(EditText)findViewById(R.id.page3_2);
        page3_3=(EditText)findViewById(R.id.page3_3);
        page3_4=(EditText)findViewById(R.id.page3_4);
        page3_6=(EditText)findViewById(R.id.page3_6);
        page6_1=(EditText)findViewById(R.id.page6_1);
        page6_2=(EditText)findViewById(R.id.page6_2);
        page6_4=(EditText)findViewById(R.id.page6_4);
        page3_1.setOnClickListener(this);
        page3_5.setOnClickListener(this);
        page6_3.setOnClickListener(this);
        dialog = new Dialog(this);
        // Removing the features of Normal Dialogs
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_signature);
        dialog.setCancelable(true);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id== R.id.prev_button){
            currentPage--;
            if(currentPage==1)
                previousBtn.setClickable(false);
            setGreenColor(currentPage);
            setPage(currentPage);
        }else if(id==R.id.next_button){
            currentPage++;
            previousBtn.setClickable(true);
            if(currentPage==6)
                nextBtn.setText("Save");
            if(currentPage>6){
                doSaveToPdf();
                Toast.makeText(this,"pdf saved",Toast.LENGTH_SHORT).show();
            }
            setRedColor(currentPage);
            setPage(currentPage);
        }else if(id==R.id.page3_1){
           dialog_action(0);
        }else if(id==R.id.page3_5){
          dialog_action(1);
        }else if(id==R.id.page6_3){
            dialog_action(2);
        }
    }

    private void doSaveToPdf() {
        PdfReader reader = null;
        SharedPreferences sharedpreferences = getSharedPreferences("my_shared", Context.MODE_PRIVATE);
      //  SharedPreferences prefs = this.getSharedPreferences("general_settings", Context.MODE_PRIVATE);
        String count = sharedpreferences.getString("count", "1");
        String pdfPath1 = Environment.getExternalStorageDirectory().getPath() + "/DigitSign/abc"+count+".pdf";
        try {
            reader=new PdfReader(getAssets().open("abc.pdf"));
           // reader = new PdfReader(getResources().openRawResource(R.raw.abc));
            //  reader = new PdfReader( pdfPath );
        } catch (IOException e) {
            e.printStackTrace();
        }
        AcroFields fields = reader.getAcroFields();

        try {
            OutputStream output = null;
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(pdfPath1));
            AcroFields acroFields = stamper.getAcroFields();
            acroFields.setField("page2_1", getUserdetails(0));
            acroFields.setField("page3_2", getUserdetails(1));
            acroFields.setField("page3_3", getUserdetails(2));
            acroFields.setField("page3_4", getUserdetails(3));
            acroFields.setField("page3_6", getUserdetails(4));
            acroFields.setField("page6_1", getUserdetails(5));
            acroFields.setField("page6_2", getUserdetails(6));
            acroFields.setField("page6_4", getUserdetails(7));
            setSignatureStamp(stamper,acroFields,"page3_1");
            setSignatureStamp(stamper,acroFields,"page3_5");
            setSignatureStamp(stamper,acroFields,"page6_3");
            stamper.setFormFlattening(true);
            stamper.close();
            int cou=Integer.parseInt(count);

            String newCount=Integer.toString(++cou);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("count", newCount);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    private void setSignatureStamp(PdfStamper stamper,AcroFields acroFields,String signId) {
        String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/DigitSign/";
        String StoredPath = DIRECTORY + signId + ".png";
        AcroFields.FieldPosition f = acroFields.getFieldPositions(signId).get(0);
        int page = f.page;
        Rectangle rect = f.position;
        File imgFile = new  File(StoredPath);
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        if(myBitmap==null){
            return;
        }
       // myBitmap=getResizedBitmap(myBitmap,160);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image img = null;
        try {
            img = Image.getInstance(stream.toByteArray());
            img.scaleToFit(rect.getWidth(), rect.getHeight());
            img.setAbsolutePosition(rect.getLeft(), rect.getBottom());
            stamper.getOverContent(page).addImage(img);
            imgFile.delete();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getUserdetails(int textViewid) {
        switch (textViewid){
            case 0:{
                 return page2_1.getText().toString();
            }
            case 1:{
                return page3_2.getText().toString();
            }
            case 2:{
                return page3_3.getText().toString();
            }
            case 3:{
                return page3_4.getText().toString();
            }
            case 4:{
                return page3_6.getText().toString();
            }
            case 5:{
                return page6_1.getText().toString();
            }
            case 6:{
                return page6_2.getText().toString();
            }
            case 7:{
                return page6_4.getText().toString();
            }
        }
        return null;
    }

    public void setPage(int page){
        for(int i=1; i<=6; i++){
            if(page == 1){
                setVisibilityGone();
                page1.setVisibility(View.VISIBLE);
            }else if(page == 2){
                setVisibilityGone();
                page2.setVisibility(View.VISIBLE);
            }else if(page == 3){
                setVisibilityGone();
                page3.setVisibility(View.VISIBLE);
            }else if(page == 4){
                setVisibilityGone();
                page4.setVisibility(View.VISIBLE);
            }else if(page == 5){
                setVisibilityGone();
                page5.setVisibility(View.VISIBLE);
            }else if(page == 6){
                setVisibilityGone();
                page6.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setVisibilityGone(){
        page1.setVisibility(View.GONE);
        page2.setVisibility(View.GONE);
        page3.setVisibility(View.GONE);
        page4.setVisibility(View.GONE);
        page5.setVisibility(View.GONE);
        page6.setVisibility(View.GONE);
    }

    public void setGreenColor(int page){
        int i = 6;
        if(i>page){
            box6.setBackgroundColor(Color.RED);
            i--;
            if(i>page){
                box5.setBackgroundColor(Color.RED);
                i--;
                if(i>page){
                    box4.setBackgroundColor(Color.RED);
                    i--;
                    if(i>page){
                        box3.setBackgroundColor(Color.RED);
                        i--;
                        if(i>page){
                            box2.setBackgroundColor(Color.RED);
                            i--;
                        }
                    }
                }
            }
        }
    }

    public void setRedColor(int page) {

        int i = 1;
        if (i <= page) {
            box1.setBackgroundColor(Color.GREEN);
            i++;
            if (i <= page) {
                box2.setBackgroundColor(Color.GREEN);
                i++;
                if (i <= page) {
                    box3.setBackgroundColor(Color.GREEN);
                    i++;
                    if (i <= page) {
                        box4.setBackgroundColor(Color.GREEN);
                        i++;
                        if (i <= page) {
                            box5.setBackgroundColor(Color.GREEN);
                            i++;
                        }
                    }
                }
            }
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    private void dialog_action(final int id) {
        final LinearLayout mContent;
        final View view;
        final signature mSignature;
        mContent = (LinearLayout) dialog.findViewById(R.id.linearLayout);
        mContent.removeAllViews();
        mSignature = new signature(this, null);
       // mSignature.clear();
        mSignature.setBackgroundColor(Color.WHITE);
        // Dynamically generating Layout through java code
       // mContent.removeAllViews();
        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mClear = (Button) dialog.findViewById(R.id.clear);
        mGetSign = (Button) dialog.findViewById(R.id.getsign);
        mGetSign.setEnabled(false);
        mCancel = (Button) dialog.findViewById(R.id.cancel);
        view = mContent;
       // mSignature.clear();
        String pic_name=null;
        switch (id){
            case 0: {
                pic_name="page3_1";
                break;}
            case 1: {
                pic_name="page3_5";
                break;
            }
            case 2: {
                pic_name="page6_3";
            }
        }
        final String StoredPath = DIRECTORY + pic_name + ".png";
        mClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Cleared");
                mSignature.clear();
                mGetSign.setEnabled(false);
            }
        });

        mGetSign.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Log.v("log_tag", "Panel Saved");
               // view.setDrawingCacheEnabled(true);
                mSignature.save(view, StoredPath,id,mContent);
                dialog.dismiss();
                Toast.makeText(getBaseContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();
                // Calling the same class

            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Canceled");
                dialog.dismiss();
                // Calling the same class
                //recreate();
            }
        });
        dialog.show();
    }

    public class signature extends View {

        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public void save(View v, String StoredPath ,int id,LinearLayout mContent) {
            Log.v("log_tag", "Width: " + v.getWidth());
            Log.v("log_tag", "Height: " + v.getHeight());
            Bitmap bitmap;
            //if (bitmap == null) {
                bitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            //}
            Canvas canvas = new Canvas(bitmap);
            try {
                // Output the file
                File wallpaperDirectory = new File(DIRECTORY);
                wallpaperDirectory.mkdir();
                FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
                v.draw(canvas);
                bitmap=getResizedBitmap(bitmap,160);
                if(id==0){
                    page3_1.setImageBitmap(bitmap);
                }
                else if(id==1){
                    page3_5.setImageBitmap(bitmap);
                }
                else {
                    page6_3.setImageBitmap(bitmap);
                }
                //parentSign.setImageBitmap(bitmap);
                // Convert the output file to Image such as .png
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                mFileOutStream.flush();
                mFileOutStream.close();

            } catch (Exception e) {
                Log.v("log_tag", e.toString());
            }

        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            mGetSign.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {

            Log.v("log_tag", string);

        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }
    // TODO: Rename method, update argument and hook method into UI event



    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}

