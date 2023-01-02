package com.example.a07_thread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ThreadHandler thread_handler;
    private Button startButton;
    private Button stopButton;
    private TextView time_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = (Button) findViewById(R.id.start_button);
        stopButton = (Button) findViewById(R.id.stop_button);
        time_textView = (TextView) findViewById(R.id.time_textView);

        thread_handler = new ThreadHandler();

        ButtonHandler handler = new ButtonHandler();
        startButton.setOnClickListener(handler);
        stopButton.setOnClickListener(handler);
    }//onCreate()

    public class ThreadHandler extends Handler {
        public void handleMessage(Message msg){
            Bundle bundle = msg.getData();
            String timeStr = bundle.getString("time");
            time_textView.setText(timeStr);
        }//End of handleMessage
    }//End of ThreadHandler class


    public class TimerThread implements Runnable {
        boolean is_stop = false ;
        public void run() {
            SimpleDateFormat date = new SimpleDateFormat("HH:mm:ss MM/dd/yyyy", Locale. TAIWAN);
            is_stop = false;
            while(! is_stop ){
                String timeStr = date.format(new Date());
                Bundle bundle = new Bundle();
                bundle. putString("time", timeStr);
                Message message = thread_handler.obtainMessage();
                message.setData(bundle);
                thread_handler.sendMessage(message);
                long startTime = System.currentTimeMillis();
                while(System.currentTimeMillis() - startTime <= 1000);
            }// while()
        }//run()
        public void stop(){
            is_stop = true;
        }//stop()
    }//Timer Thread class


    public class ButtonHandler implements View.OnClickListener {
        TimerThread timer = new TimerThread();

        public void onClick(View v) {
            if(v == startButton){
                Thread thread = new Thread(timer);
                thread.start();
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
            }//End of if-condition
            if(v == stopButton){
                timer.stop();
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
            }
        }//onClick
    }//Button Handler class

}//MainActivity class
