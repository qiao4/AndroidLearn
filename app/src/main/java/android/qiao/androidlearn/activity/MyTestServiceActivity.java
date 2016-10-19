package android.qiao.androidlearn.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.qiao.androidlearn.R;
import android.qiao.androidlearn.service.MyTestService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MyTestServiceActivity extends AppCompatActivity implements View.OnClickListener {

    Button button_start_service;
    Button button_stop_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_test_service);

        button_start_service = (Button) findViewById(R.id.button_start_service);
        button_stop_service = (Button) findViewById(R.id.button_stop_service);
        button_start_service.setOnClickListener(this);
        button_stop_service.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_start_service :
                Intent my_test_start = new Intent(this, MyTestService.class);
                startService(my_test_start);
                break;
            case R.id.button_stop_service :
                Intent my_test_stop = new Intent(this, MyTestService.class);
                stopService(my_test_stop);
                break;
            default:
                break;
        }
    }
}
