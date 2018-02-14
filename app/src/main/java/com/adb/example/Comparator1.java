package com.adb.example;

import java.util.Comparator;

/**
 * Created by bhavdip on 2/5/18.
 */

class Comparator1 implements Comparator {
  Comparator1() {
  }

  public int a(CompassLocation compassLocation, CompassLocation compassLocation2) {
    return compassLocation.c() < compassLocation2.c() ? -1
        : compassLocation.c() > compassLocation2.c() ? 1 : 0;
  }

  public int compare(Object obj, Object obj2) {
    return a((CompassLocation) obj, (CompassLocation) obj2);
  }
}