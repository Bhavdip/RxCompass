package com.adb.example;

/**
 * Created by bhavdip on 2/5/18.
 */

import java.util.Comparator;

class Comparator2 implements Comparator {
  Comparator2() {
  }

  public int a(CompassLocation compassLocation, CompassLocation compassLocation2) {
    return compassLocation.f().compareToIgnoreCase(compassLocation2.f());
  }

  public int compare(Object obj, Object obj2) {
    return a((CompassLocation) obj, (CompassLocation) obj2);
  }
}