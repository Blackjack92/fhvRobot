package app.robo.fhv.roboapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import app.robo.fhv.roboapp.communication.NetworkClient;

public class MainActivity extends Activity {

    private NetworkClient client;

    private TextView textView;
    private EditText editText;
    private Button button;
    private SeekBar sbLeft;
    private SeekBar sbRight;
    private TextView tvLeft;
    private TextView tvRight;

    private int stepSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);

        textView.setText("text view");
        editText.setText("edit text");

        sbLeft = (SeekBar) findViewById(R.id.sbLeft);
        sbRight = (SeekBar) findViewById(R.id.sbRight);
        tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvRight = (TextView) findViewById(R.id.tvRight);

        sbLeft.setProgress(30);
        sbRight.setProgress(30);

        try {
            client = new NetworkClient(textView);
            new Thread(client).start();
        } catch (Exception e) {
            e.printStackTrace();

            textView.setText("Error");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.send(editText.getText().toString());
            }
        });


        sbLeft.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = (progress/stepSize)*stepSize;
                seekBar.setProgress(progress);
                tvLeft.setText(String.valueOf(progress));
                client.driveLeft(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbRight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = (progress/stepSize)*stepSize;
                seekBar.setProgress(progress);
                tvRight.setText(String.valueOf(progress));
                client.driveRight(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


}