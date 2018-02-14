package com.adb.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.adb.rxsensor.CompassHelper;
import com.adb.rxsensor.SensorUpdateCallback;
import java.util.ArrayList;

public class SensorActivity extends AppCompatActivity implements SensorUpdateCallback {
  public CompassView mCompassView;
  private ArrayList<CompassLocation> compassLocationList = new ArrayList<>();
  private CompassHelper mCompassHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sensor);
    mCompassView = findViewById(R.id.compass_fragment_compassview);
    mCompassView.addCompassLocationList(this.compassLocationList);
    mCompassHelper = new CompassHelper(this, getWindowManager().getDefaultDisplay());
  }

  @Override
  protected void onStart() {
    super.onStart();
    mCompassHelper.registerAndStartListen(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
    mCompassHelper.stopListen();
  }

  public void setBearing(float f) {
    int round = Math.round(((float) this.mCompassView.getCorrectiveAngle()) + f);
    while (round < 0) {
      round += 360;
    }
    int i = round;
    while (i >= 360) {
      i -= 360;
    }
    Log.d("tag", new StringBuilder(String.valueOf(i)).append("°").toString());
    ((TextView) (findViewById(R.id.textViewBearing))).setText(
        new StringBuilder(String.valueOf(i)).append("°").toString());
  }

  @Override
  public void onBearingValue(float f) {
    mCompassView.setBearing(f);
    setBearing(f);
  }
}
