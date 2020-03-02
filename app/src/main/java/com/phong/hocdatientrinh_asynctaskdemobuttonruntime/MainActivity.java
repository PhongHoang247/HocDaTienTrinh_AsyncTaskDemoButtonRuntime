package com.phong.hocdatientrinh_asynctaskdemobuttonruntime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    EditText edtButton;
    Button btnVe;
    TextView txtPercent;
    LinearLayout llButton;
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT );
    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = Integer.parseInt(edtButton.getText().toString());
                //Tạo một đối tượng có khả năng chạy đa tiến trình:
                ButtonTask task = new ButtonTask();
                task.execute(n);//muốn ra lệnh 1 tiến trình kích hoạt gọi execute, có đối số thì truyền vào
            }
        });
    }

    private void addControls() {
        edtButton = findViewById(R.id.edtButton);
        btnVe = findViewById(R.id.btnVe);
        txtPercent = findViewById(R.id.txtPercent);
        llButton = findViewById(R.id.llButton);
    }

    class ButtonTask extends AsyncTask<Integer,Integer,Void>{
        //Đối số trong AsyncTask ko chấp nhận primitive data(int,long,double,float,...) phải dùng Wrapper Class

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txtPercent.setText("0%");
            llButton.removeAllViews();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            txtPercent.setText("100%");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //Lấy đúng thứ tự publishProgress:
            int value = values[0];
            int percent = values[1];
            //Hiển thị lên giao diện:
            txtPercent.setText(percent + "%");
            Button btn = new Button(MainActivity.this);
            btn.setText(value + "");
            btn.setLayoutParams(layoutParams);
            llButton.addView(btn);
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            int n = integers[0];
            for (int i = 0; i < n; i++){
                int percent = i * 100/n;
                int value = random.nextInt(100);
                //Gửi thông tin trên cho onProgressUpdate: update giao diện
                //Truyền bao nhiêu đối số cũng đc nhưng lấy đúng thứ tự
                publishProgress(value,percent);
                SystemClock.sleep(100);
            }
            return null;
        }
    }
}
