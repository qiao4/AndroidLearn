package android.qiao.androidlearn.activity;

import android.content.Intent;
import android.qiao.androidlearn.R;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button button_receive_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_receive_message = (Button) findViewById(R.id.button_receive_message);
        button_receive_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), ReceiveMassageActivity.class);
                startActivity(intent);
            }
        });
    }
}
