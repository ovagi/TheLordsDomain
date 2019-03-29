package com.github.ovagi.lordsDomain.java;

import playn.java.LWJGLPlatform;

import com.github.ovagi.lordsDomain.core.LordsDomain;

public class LordsDomainJava {

  public static void main (String[] args) {
    LWJGLPlatform.Config config = new LWJGLPlatform.Config();
    // use config to customize the Java platform, if needed
    config.height = 800;
    config.width = 1280;
    LWJGLPlatform plat = new LWJGLPlatform(config);
    new LordsDomain(plat);
    plat.start();
  }
}
