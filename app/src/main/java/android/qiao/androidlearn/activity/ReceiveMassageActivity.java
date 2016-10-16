package android.qiao.androidlearn.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.qiao.androidlearn.*;
import android.qiao.androidlearn.utils.*;

public class ReceiveMassageActivity extends AppCompatActivity {

    private EditText anotherKey;
    private EditText anotherIp;
    private Button anotherOk;
    private ListView msgList;
    private EditText myMsg;
    private Button mySend;

    private List<Msg> list = new ArrayList<>();
    private byte[] anotherKeyBytes;
    MsgAdapter adapter;
    private ServerSocket serverS;
    private int REC_PORT = 8091;

    Thread receiveT;
    Thread receiveUDP;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    Msg newms = (Msg) msg.obj;
                    list.add(newms);
                    adapter.notifyDataSetChanged();
                    myMsg.setText("");
                    msgList.setSelection(list.size());

                    myMsg.setText(newms.getMsg());
                    break;
                case -1:
                    String error = (String) msg.obj;
                    Toast.makeText(ReceiveMassageActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_massage);

        myMsg = (EditText) findViewById(R.id.edit_text_msg);

        list.add(new Msg("ex1", Msg.RECEIVE_SIGN));
        list.add(new Msg("ex2", Msg.SEND_SIGN));

        msgList = (ListView) findViewById(R.id.list_view_msg);
        adapter = new MsgAdapter(ReceiveMassageActivity.this, R.layout.msg_item, list);
        msgList.setAdapter(adapter);

        anotherIp = (EditText) findViewById(R.id.ip_aite);
        mySend = (Button)findViewById(R.id.button_send);
        mySend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        SocketCommu.SendMessageByUDP(anotherIp.getText().toString(), myMsg.getText().toString());
                    }
                }.start();

            }
        });

//        receiveT = new Thread(new ReceiveThread());
//        receiveT.start();
        receiveUDP = new Thread(new UDPReceiveThread());
        receiveUDP.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiveT != null) {
            receiveT.interrupt();
        }
        try {
            serverS.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    //receive mesage by tcp socket
    class ReceiveThread implements Runnable {           //监听8091端口

        @Override
        public void run() {
            Socket s = null;

            try {
                serverS = new ServerSocket(REC_PORT);
            } catch (Exception e) {
                e.printStackTrace();
            }

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Log.d("TAG", "run: start listening");
                    s = serverS.accept();
                    Log.d("TAG", "run: receive message");
                    new Thread(new chatOne(s)).start();
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }
    }


    class chatOne implements Runnable {   //受到请求 处理请求
        Socket so = null;

        public chatOne(Socket s) {
            so = s;
        }
        @Override
        public void run() {
            try {

                BufferedInputStream in = new BufferedInputStream(so.getInputStream());
                int size = in.read();
                byte[] content = new byte[size];
                if(in.read(content, 0, size) != -1) {
                    Log.d("TAG", "run: receive message " + new String(content, "utf-8"));
                    Msg newMsg = new Msg(new String(content, "utf-8"), Msg.RECEIVE_SIGN);
                    Message m = new Message();
                    m.obj = newMsg;
                    m.what = 1;
                    handler.sendMessage(m);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //Log.d("sockettest", e.getMessage());
            }
        }
    }

    //receive message by udp socket
    class UDPReceiveThread implements Runnable {           //监听8091端口

        @Override
        public void run() {
            DatagramSocket serverS = null;
            DatagramPacket message = null;

            try {
                message = new DatagramPacket(new byte[256], 256);
                serverS = new DatagramSocket(REC_PORT);
                Log.d("TAG", "run: start listening");
            } catch (Exception e) {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted()) {

                if(serverS == null) break;
                try {
                    serverS .receive(message);
                    Log.d("TAG", "run: receive message form " + message.getAddress().getHostName()
                            + " : " + message.getAddress().getHostAddress());
                    byte[] data = new byte[message.getData()[0]];
                    System.arraycopy(message.getData(), 1, data, 0, data.length);

                    Msg newMsg = new Msg(new String(data, "utf-8"), Msg.RECEIVE_SIGN);
                    Message m = new Message();
                    m.obj = newMsg;
                    m.what = 1;
                    handler.sendMessage(m);
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }
    }
}
