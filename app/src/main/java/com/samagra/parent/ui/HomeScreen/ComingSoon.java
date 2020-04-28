package com.samagra.parent.ui.HomeScreen;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.samagra.commons.CustomTabHelper;
import com.samagra.parent.R;
import com.samagra.parent.base.BaseActivity;

import static com.samagra.commons.CustomTabHelper.OPEN_URL;

public class ComingSoon extends BaseActivity {


    public TextView documentation_link;

    private CustomTabHelper websiteTabHelper;
    private Uri websiteUri;
    private static final String SAMAGRA_DOC_WEBSITE = "https://samagra-development.github.io/docs/docs/COMobileApplication";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coming_soon);
        setupToolbar();
        SpannableString content = new SpannableString(this.getResources().getString(R.string.check_out_the_documentation_here));
        documentation_link = findViewById(R.id.documentation_link);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        documentation_link.setText(content);
        websiteTabHelper = new CustomTabHelper();
        websiteUri = Uri.parse(SAMAGRA_DOC_WEBSITE);
        documentation_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!websiteTabHelper.openUri(v.getContext(), websiteUri)){
                    try {
                        //open in external browser
                        getActivityContext().startActivity(new Intent(Intent.ACTION_VIEW, websiteUri));
                    } catch (ActivityNotFoundException | SecurityException e) {
                        //open in webview
                        Intent intent = new Intent(getActivityContext(), WebViewActivity.class);
                        intent.putExtra(OPEN_URL, websiteUri.toString());
                        getActivityContext().startActivity(intent);
                    }
                }
            }
        });

    }

    @Override
    public void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Coming Soon");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(v -> finish());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(view -> finish());

    }


    @Override
    public void onStart() {
        super.onStart();
        websiteTabHelper.bindCustomTabsService(this, websiteUri); }

    @Override
    public void onDestroy() {
        unbindService(websiteTabHelper.getServiceConnection());
        super.onDestroy();
    }
}
