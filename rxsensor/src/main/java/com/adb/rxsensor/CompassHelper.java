package com.adb.rxsensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import java.util.ArrayList;
import java.util.Iterator;

import static android.view.Surface.ROTATION_0;
import static android.view.Surface.ROTATION_180;
import static android.view.Surface.ROTATION_270;
import static android.view.Surface.ROTATION_90;

/**
 * Created by bhavdip on 2/6/18.
 */

public class CompassHelper {
  boolean threeSensorPresent = false;
  private ArrayList<SensorUpdateCallback> callbackList = new ArrayList<>();
  private boolean isAllSensorReg = false;
  private boolean isStarted = false;
  public float e = 0.0f;
  private long f = Long.MAX_VALUE;
  private Context mContext;
  private Display mDisplay;
  private SensorEventListener magnaticAccelListener = new MagneticsAcceptListener(this);
  private SensorEventListener rotationVectorListener = new RotationVectorListener(this);
  private int displayRotation = 0;

  public CompassHelper(Context context, Display display) {
    this.mContext = context;
    this.mDisplay = display;
  }

  public int getRotation() {
    switch (mDisplay.getRotation()) {
      case ROTATION_90:
        displayRotation = 90;
        break;
      case ROTATION_180:
        displayRotation = 180;
        break;
      case ROTATION_270:
        this.displayRotation = 270;
        break;
      default:
        this.displayRotation = ROTATION_0;
        break;
    }
    return this.displayRotation;
  }

  public void notifyValue(float f) {
    Iterator it = this.callbackList.iterator();
    while (it.hasNext()) {
      ((SensorUpdateCallback) it.next()).onBearingValue(f);
    }
  }

  public void b(float f) {
    float abs = Math.abs(f - this.e);
    while (abs > 180.0f) {
      abs = Math.abs(abs - 360.0f);
    }
    if (abs < 145.0f) {
      this.f = System.currentTimeMillis();
    } else if (System.currentTimeMillis() - this.f > 800) {
      unregisterSensorListener();
      registerSensorListener();
    }
  }

  private void registerSensorListener() {
    SensorManager sensorManager =
        (SensorManager) this.mContext.getSystemService(Context.SENSOR_SERVICE);
    if (sensorManager != null) {
      Sensor sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
      Sensor sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
      Sensor sensorRotationVector = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
      this.threeSensorPresent = false;
      if (sensorRotationVector != null) {
        this.threeSensorPresent = true;
      } else if (sensorAccelerometer == null) {
        return;
      } else {
        if (sensorMagneticField == null) {
          return;
        }
      }

      if (this.threeSensorPresent) {
        sensorManager.registerListener(this.rotationVectorListener, sensorRotationVector, 33000);
        sensorManager.registerListener(this.magnaticAccelListener, sensorMagneticField,
            SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this.magnaticAccelListener, sensorAccelerometer,
            SensorManager.SENSOR_DELAY_NORMAL);
      } else {
        sensorManager.registerListener(this.magnaticAccelListener, sensorMagneticField,
            SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this.magnaticAccelListener, sensorAccelerometer,
            SensorManager.SENSOR_DELAY_UI);
      }
      this.isAllSensorReg = true;
    }
  }

  private void unregisterSensorListener() {
    if (this.isAllSensorReg) {
      SensorManager sensorManager =
          (SensorManager) this.mContext.getSystemService(Context.SENSOR_SERVICE);
      sensorManager.unregisterListener(this.magnaticAccelListener,
          sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
      sensorManager.unregisterListener(this.magnaticAccelListener,
          sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
      sensorManager.unregisterListener(this.rotationVectorListener,
          sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR));
      this.f = Long.MAX_VALUE;
      this.isAllSensorReg = false;
    }
  }

  public void registerAndStartListen(SensorUpdateCallback sensorUpdateCallbackVar) {
    this.callbackList.add(sensorUpdateCallbackVar);
    if (!this.isStarted && !this.isAllSensorReg) {
      registerSensorListener();
      this.isStarted = true;
    }
  }

  public void stopListen() {
    if (this.isAllSensorReg) {
      unregisterSensorListener();
      if (callbackList.size() > 0) {
        callbackList.clear();
      }
      this.isStarted = false;
    }
  }
}