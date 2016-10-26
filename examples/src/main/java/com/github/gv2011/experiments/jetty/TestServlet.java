package com.github.gv2011.experiments.jetty;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;

import javax.mail.internet.ContentType;
import javax.mail.internet.ParameterList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet{

  private static final ContentType TXT = utf8Txt();

  @Override
  protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
    throws ServletException, IOException
  {
    resp.setContentType(TXT.toString());
    resp.getOutputStream().write("¡Hola Mundo!".getBytes(UTF_8));
  }

  private static ContentType utf8Txt() {
    final ParameterList cs = new ParameterList();
    cs.set("charset", UTF_8.name());
    return new ContentType("text", "plain", cs);
  }

}
