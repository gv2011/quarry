package com.github.gv2011.quarry.mvn;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

import org.slf4j.Logger;

public class ClWrapper extends ClassLoader{

  private static final Logger LOG = getLogger(ClWrapper.class);

  private final ClassLoader delegate;

  @Override
  public Class<?> loadClass(final String name) throws ClassNotFoundException {
    return delegate.loadClass(name);
  }

  @Override
  public URL getResource(final String name) {
    final URL url = delegate.getResource(name);
    if(url!=null)LOG.debug("Resource {} at {}.", name, url);
    else LOG.debug("No resource {}.", name, url);
    return url;
  }

  @Override
  public Enumeration<URL> getResources(final String name) throws IOException {
    return delegate.getResources(name);
  }

  @Override
  public InputStream getResourceAsStream(final String name) {
    return delegate.getResourceAsStream(name);
  }

  @Override
  public void setDefaultAssertionStatus(final boolean enabled) {
    delegate.setDefaultAssertionStatus(enabled);
  }

  @Override
  public void setPackageAssertionStatus(final String packageName, final boolean enabled) {
    delegate.setPackageAssertionStatus(packageName, enabled);
  }

  @Override
  public void setClassAssertionStatus(final String className, final boolean enabled) {
    delegate.setClassAssertionStatus(className, enabled);
  }

  @Override
  public void clearAssertionStatus() {
    delegate.clearAssertionStatus();
  }

  ClWrapper(final ClassLoader delegate) {
    this.delegate = delegate;
  }



}
