package com.lucky.roadpilot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;

public class DriverRegisterActivity extends AppCompatActivity {

    String feeds;
    private LinearLayout feed;
//    private ScrollView policy,cond;
    private EditText etSubject,etMessage;
    private TextView tvTo;
    private Button btSend;


    private PDFView
             policy_lay,terms_lay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        cond = findViewById(R.id.cond);
//        policy = findViewById(R.id.policy);
        feed = findViewById(R.id.feed);

        etMessage = findViewById(R.id.et_text);
        etSubject = findViewById(R.id.et_subject);
        tvTo = findViewById(R.id.et_to);
        btSend = findViewById(R.id.bt_send);


        policy_lay = findViewById(R.id.policy_lay);
        terms_lay = findViewById(R.id.terms_lay);


        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW
                        , Uri.parse("mailto: "+ tvTo.getText().toString()));
                intent.putExtra(Intent.EXTRA_SUBJECT, etSubject.getText().toString());
                intent.putExtra(Intent.EXTRA_TEXT, etMessage.getText().toString());
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        feeds = ""+intent.getStringExtra("key");

        if(feeds.equals("terms")){
            terms_lay.setVisibility(View.VISIBLE);
            policy_lay.setVisibility(View.INVISIBLE);
            feed.setVisibility(View.INVISIBLE);

            terms_lay.fromAsset("terms.pdf")
                    .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
//                    // allows to draw something on the current page, usually visible in the middle of the screen
//                    .onDraw(onDrawListener)
//                    // allows to draw something on all pages, separately for every page. Called only for visible pages
//                    .onDrawAll(onDrawListener)
//                    .onLoad(onLoadCompleteListener) // called after document is loaded and starts to be rendered
//                    .onPageChange(onPageChangeListener)
//                    .onPageScroll(onPageScrollListener)
//                    .onError(onErrorListener)
//                    .onPageError(onPageErrorListener)
//                    .onRender(onRenderListener) // called after document is rendered for the first time
//                    // called on single tap, return true if handled, false to toggle scroll handle visibility
//                    .onTap(onTapListener)
//                    .onLongPress(onLongPressListener)
                    .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                    .password(null)
                    .scrollHandle(null)
                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                    // spacing between pages in dp. To define spacing color, set view background
                    .spacing(0)
                    .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
//                    .linkHandler(DefaultLinkHandler)
//                    .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
                    .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
                    .pageSnap(false) // snap pages to screen boundaries
                    .pageFling(false) // make a fling change only a single page like ViewPager
                    .nightMode(false) // toggle night mode
                    .load();

//            terms_lay.loadUrl("https://drive.google.com/file/d/121F5Ug7G9wsyvtPQ5fplB600M-hwzJcC/view?usp=sharing");


        }else if(feeds.equals("privacy")){
            terms_lay.setVisibility(View.INVISIBLE);
            policy_lay.setVisibility(View.VISIBLE);
            feed.setVisibility(View.INVISIBLE);


            policy_lay.fromAsset("privacy_policy.pdf")
                    .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(false)
                    .enableDoubletap(true)
                    .defaultPage(0)
//                    // allows to draw something on the current page, usually visible in the middle of the screen
//                    .onDraw(onDrawListener)
//                    // allows to draw something on all pages, separately for every page. Called only for visible pages
//                    .onDrawAll(onDrawListener)
//                    .onLoad(onLoadCompleteListener) // called after document is loaded and starts to be rendered
//                    .onPageChange(onPageChangeListener)
//                    .onPageScroll(onPageScrollListener)
//                    .onError(onErrorListener)
//                    .onPageError(onPageErrorListener)
//                    .onRender(onRenderListener) // called after document is rendered for the first time
//                    // called on single tap, return true if handled, false to toggle scroll handle visibility
//                    .onTap(onTapListener)
//                    .onLongPress(onLongPressListener)
                    .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                    .password(null)
                    .scrollHandle(null)
                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                    // spacing between pages in dp. To define spacing color, set view background
                    .spacing(0)
                    .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
//                    .linkHandler(DefaultLinkHandler)
//                    .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
                    .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
                    .pageSnap(false) // snap pages to screen boundaries
                    .pageFling(false) // make a fling change only a single page like ViewPager
                    .nightMode(false) // toggle night mode
                    .load();



//            policy_lay.loadUrl("https://drive.google.com/file/d/1BGkpwAzi3qu3WsTe0i-fnS-6vwyOFbEJ/view?usp=sharing");

        }else if(feeds.equals("feedback")){
            terms_lay.setVisibility(View.INVISIBLE);
            policy_lay.setVisibility(View.INVISIBLE);
            feed.setVisibility(View.VISIBLE);




        }

    }
}