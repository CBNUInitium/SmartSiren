package com.kms.smartsiren.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.kms.smartsiren.R;

import org.w3c.dom.Text;

public class DetailInfo extends Activity {
    TextView detailContents;
    TextView sortOfCase;
    TextView reliabilityLvl;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.show_detail_info);


        sortOfCase = (TextView)findViewById(R.id.event_code);
        reliabilityLvl = (TextView)findViewById(R.id.reliability_lvl);
        detailContents = (TextView)findViewById(R.id.detail);

        Intent intent = getIntent();
        String eventCode = intent.getStringExtra("eventCode");
        String reliability = intent.getStringExtra("reliabilityLvl");
        String detail = intent.getStringExtra("detail");

        sortOfCase.setText(eventCode);
        reliabilityLvl.setText(reliability);
        detailContents.setText(detail);
    }

    public void closeDetailInfo(View v){
        finish();
    }
}
