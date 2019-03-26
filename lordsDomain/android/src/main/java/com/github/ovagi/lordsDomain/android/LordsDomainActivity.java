package com.github.ovagi.lordsDomain.android;

import playn.android.GameActivity;

import com.github.ovagi.lordsDomain.core.LordsDomain;

public class LordsDomainActivity extends GameActivity {

  @Override public void main () {
    new LordsDomain(platform());
  }
}
