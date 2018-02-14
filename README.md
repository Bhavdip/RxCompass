# RxCompass
This a sample project to learn and build a compass application on Android platform.
 
### Usages
private CompassHelper mCompassHelper;

@Override
 protected void onCreate(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);
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

@Override
public void onBearingValue(float f) {
 // Listen bearing value
}