package bluetoothandroidarduino.droidduinoconnection;

import android.app.Activity;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import android.os.Handler;


public class BluetoothActivity extends Activity{

    private static final String TAG = "BLUETOOTH";
    // MAC address of remote Bluetooth device
    // Replace this with the address of your own module
    private final String address = "98:D3:31:40:26:31";

    // The thread that does all the work
    BluetoothThread btt;

    // Handler for writing messages to the Bluetooth connection
    Handler writeHandler;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
    }



    /**
     * Launch the Bluetooth thread.
     */
    public void connectButtonPressed(View v) {
        Log.v(TAG, "Connect button pressed.");

        // Only one thread at a time
        if (btt != null) {
            Log.w(TAG, "Already connected!");
            return;
        }

        // Initialize the Bluetooth thread, passing in a MAC address
        // and a Handler that will receive incoming messages
        btt = new BluetoothThread(address, new Handler() {

            @Override
            public void handleMessage(Message message) {

                String s = (String) message.obj;

                // Do something with the message
                if (s.equals("CONNECTED")) {
                    TextView tv = (TextView) findViewById(R.id.statusText);
                    tv.setText("Connected.");
                    Button b = (Button) findViewById(R.id.writeButton);
                    b.setEnabled(true);

                    SeekBar sb = (SeekBar)findViewById(R.id.leftrightseekbar);
                    sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            Log.v(TAG, "LeftRight seekbar  pressed.");

                            int lr = (progress*255)/100; //  Regra de 3 pois a seek bar tem valores de 1 a 100, sendo que o motor tem valores de 0 a 255 de velocidade

                            String data = "lr = "+lr;

                            Message msg = Message.obtain();
                            msg.obj = data;
                            writeHandler.sendMessage(msg);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });

                    SeekBar ve = (SeekBar)findViewById(R.id.velocitySeekBar);
                    ve.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            Log.v(TAG, "Velocity seekbar  pressed.");

                             int velocity = (progress*255)/100; //  Regra de 3 pois a seek bar tem valores de 1 a 100, sendo que o motor tem valores de 0 a 255 de velocidade

                            String data = "ve = "+velocity;

                            Message msg = Message.obtain();
                            msg.obj = data;
                            writeHandler.sendMessage(msg);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });
                } else if (s.equals("DISCONNECTED")) {
                    TextView tv = (TextView) findViewById(R.id.statusText);
                    Button b = (Button) findViewById(R.id.writeButton);
                    b.setEnabled(false);
                    tv.setText("Disconnected.");
                } else if (s.equals("CONNECTION FAILED")) {
                    TextView tv = (TextView) findViewById(R.id.statusText);
                    tv.setText("Connection failed!");
                    btt = null;
                } else {
                    TextView tv = (TextView) findViewById(R.id.readField);
                    tv.setText(s);
                }
            }
        });

        // Get the handler that is used to send messages
        writeHandler = btt.getWriteHandler();

        // Run the thread
        btt.start();

        TextView tv = (TextView) findViewById(R.id.statusText);
        tv.setText("Connecting...");
    }

    /**
     * Kill the Bluetooth thread.
     */
    public void disconnectButtonPressed(View v) {
        Log.v(TAG, "Disconnect button pressed.");

        if(btt != null) {
            btt.interrupt();
            btt = null;
        }
    }

    /*
    *
    * Send the ForwardORBackward switch information, ON = Forward , OFF = Backward.
    * */

    public void forwardBackwardSwitchPressed(View v){
        Log.v(TAG, "ForwardORBackward switch pressed.");

        Switch sw = (Switch)findViewById(R.id.forwardbackwardswitch);

        int swValue = (sw.isChecked()) ? 1 : 0;
        int forwardBackwardValue = 1 + swValue;  //FORWARD = 1 , BACKWARD = 2 at adafruit documentation

        String data = "fb = "+forwardBackwardValue;

        Message msg = Message.obtain();
        msg.obj = data;
        writeHandler.sendMessage(msg);
    }


    /**
     * Send a message using the Bluetooth thread's write handler.
     */
    public void writeButtonPressed(View v) {
        Log.v(TAG, "Write button pressed.");

        TextView tv = (TextView)findViewById(R.id.writeField);
        String data = tv.getText().toString();

        Message msg = Message.obtain();
        msg.obj = data;
        writeHandler.sendMessage(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        Button b = (Button) findViewById(R.id.writeButton);
        b.setEnabled(false);
    }

    /**
     * Kill the thread when we leave the activity.
     */
    protected void onPause() {
        super.onPause();

        if(btt != null) {
            btt.interrupt();
            btt = null;
        }
    }


}
