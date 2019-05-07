package com.github.ovagi.lordsDomain.html;

import com.google.gwt.core.client.EntryPoint;
import playn.html.HtmlPlatform;
import com.github.ovagi.lordsDomain.core.LordsDomain;

public class LordsDomainHtml implements EntryPoint {

  @Override public void onModuleLoad () {
    HtmlPlatform.Config config = new HtmlPlatform.Config();
    // use config to customize the HTML platform, if needed
    HtmlPlatform plat = new HtmlPlatform(config);
    plat.assets().setPathPrefix("lordsDomain/");
    new LordsDomain(plat);
    plat.start();
  }
}
