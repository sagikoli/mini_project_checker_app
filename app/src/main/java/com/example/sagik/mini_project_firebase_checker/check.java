package com.example.sagik.mini_project_firebase_checker;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class check extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        scan_but=(Button)findViewById(R.id.scan_but);
        final Activity act=this;
        scan_but.setOnClickListener(new View.OnClickListener()
                                    {
                                        public void onClick(View view)
                                        {
                                            IntentIntegrator integrator=new IntentIntegrator(act);
                                            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                                            integrator.setPrompt("scan");
                                            integrator.setCameraId(0);
                                            integrator.setOrientationLocked(false);
                                            integrator.setBeepEnabled(true);
                                            integrator.setBarcodeImageEnabled(false);
                                            integrator.initiateScan();
                                        }
                                    }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null)
        {
            if(result.getContents()==null)
            {
                Toast.makeText(this,"You cancelled Scanning",Toast.LENGTH_LONG).show();
            }
            else
            {
                Intent i=new Intent(this,info.class);
                i.putExtra("code",result.getContents());
                startActivity(i);
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private Button scan_but;

}