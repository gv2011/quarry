package io.milton.servlet;

public class ServletRequestAccess {


  public static final void servletRequestClearThreadLocals(){
    ServletRequest.clearThreadLocals();
  }
}
