package com.github.gv2011.experiments.webdav;

import static org.slf4j.LoggerFactory.getLogger;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean2;
import org.slf4j.Logger;

import io.milton.config.HttpManagerBuilder;
import io.milton.http.AuthenticationHandler;
import io.milton.http.Filter;
import io.milton.http.HttpManager;
import io.milton.http.ResourceFactory;
import io.milton.http.webdav.WebDavResponseHandler;
import io.milton.servlet.Config;
import io.milton.servlet.DefaultMiltonConfigurator;
import io.milton.servlet.Initable;
import io.milton.servlet.MiltonConfigurator;

public class Configurator implements MiltonConfigurator {

  private static final Logger log = getLogger(Configurator.class);
  private final HttpManagerBuilder builder = new HttpManagerBuilder();
  protected List<Initable> initables;
  protected HttpManager httpManager;
  private final ResourceFactory resourceFactory;



  public Configurator(final ResourceFactory resourceFactory) {
    this.resourceFactory = resourceFactory;
  }

  @Override
  public HttpManager configure(final Config config) throws ServletException {

    log.info("Listing all config parameters:");
    final Map<String, String> props = new HashMap<String, String>();
    for (final String s : config.getInitParameterNames()) {
      final String val = config.getInitParameter(s);
      log.info(" " + s + " = " + val);
      if (!s.contains(".") && !s.contains("_")) {
        props.put(s, val);
      }
    }


    final String authHandlers = config.getInitParameter("authenticationHandlers");
    if (authHandlers != null) {
      props.remove("authenticationHandlers"); // so the bub doesnt try to set it
      initAuthHandlers(authHandlers);
    }

    final String extraAuthHandlers = config.getInitParameter("extraAuthenticationHandlers");
    if (extraAuthHandlers != null) {
      props.remove("extraAuthenticationHandlers"); // so the bub doesnt try to set it
      initExtraAuthHandlers(extraAuthHandlers);
    }


//    final String resourceFactoryClassName = config.getInitParameter("resource.factory.class");
//    if (resourceFactoryClassName != null) {
//      final ResourceFactory rf = DefaultMiltonConfigurator.instantiate(resourceFactoryClassName);
//      builder.setMainResourceFactory(rf);
//    } else {
//      log.warn("No custom ResourceFactory class name provided in resource.factory.class");
//    }
    builder.setMainResourceFactory(resourceFactory);

    final String responseHandlerClassName = config.getInitParameter("response.handler.class");
    if (responseHandlerClassName != null) {
      final WebDavResponseHandler davResponseHandler =
        DefaultMiltonConfigurator.instantiate(responseHandlerClassName)
      ;
      builder.setWebdavResponseHandler(davResponseHandler);
    }
    List<Filter> filters = null;
    final List<String> params = config.getInitParameterNames();
    for (final String paramName : params) {
      if (paramName.startsWith("filter_")) {
        final String filterClass = config.getInitParameter(paramName);
        final Filter f = DefaultMiltonConfigurator.instantiate(filterClass);
        if (filters == null) {
          filters = new ArrayList<Filter>();
        }
        filters.add(f);
      }
    }
    if (filters != null) {
      builder.setFilters(filters);
    }

    try {
      final ConvertUtilsBean2 convertUtilsBean = new ConvertUtilsBean2();
      final BeanUtilsBean bub = new BeanUtilsBean(convertUtilsBean);
      bub.populate(builder, props);
    } catch (final IllegalAccessException e) {
      throw new ServletException(e);
    } catch (final InvocationTargetException e) {
      throw new ServletException(e);
    }

//    final ResourceFactory rf = builder.getMainResourceFactory();
//    if (rf instanceof AnnotationResourceFactory) {
//      AnnotationResourceFactory arf = (AnnotationResourceFactory) rf;
//      arf.setContextPath("/");
//      if (arf.getViewResolver() == null) {
//        ViewResolver viewResolver = new JspViewResolver(config.getServletContext());
//        arf.setViewResolver(viewResolver);
//      }
//    }


    build();
    initables = new ArrayList<Initable>();

    checkAddInitable(initables, builder.getAuthenticationHandlers());
    checkAddInitable(initables, builder.getMainResourceFactory());
    checkAddInitable(initables, builder.getWebdavResponseHandler());
    checkAddInitable(initables, builder.getFilters());

    for (final Initable i : initables) {
      i.init(config, httpManager);
    }
    return httpManager;
  }

  /**
   * Actually builds the httpManager. Can be overridden by subclasses
   */
  protected void build() {
    httpManager = builder.buildHttpManager();
  }

  @Override
  public void shutdown() {
    if (httpManager != null) {
      httpManager.shutdown();
    }
    if (initables != null) {
      for (final Initable i : initables) {
        i.destroy(httpManager);
      }
    }
  }

  private void initAuthHandlers(final String classNames) throws ServletException {
    final List<String> authHandlers = loadAuthHandlersIfAny(classNames);
    if (authHandlers == null) {
      return;
    }
    final List<AuthenticationHandler> list = new ArrayList<AuthenticationHandler>();
    for (final String authHandlerClassName : authHandlers) {
      final Object o = DefaultMiltonConfigurator.instantiate(authHandlerClassName);
      if (o instanceof AuthenticationHandler) {
        final AuthenticationHandler auth = (AuthenticationHandler) o;
        list.add(auth);
      } else {
        throw new ServletException("Class: " + authHandlerClassName + " is not a: " + AuthenticationHandler.class.getCanonicalName());
      }
    }
    builder.setAuthenticationHandlers(list);
  }

  private void initExtraAuthHandlers(final String classNames) throws ServletException {
    final List<String> authHandlers = loadAuthHandlersIfAny(classNames);
    if (authHandlers == null) {
      return;
    }
    final List<AuthenticationHandler> list = new ArrayList<AuthenticationHandler>();
    for (final String authHandlerClassName : authHandlers) {
      final Object o = DefaultMiltonConfigurator.instantiate(authHandlerClassName);
      if (o instanceof AuthenticationHandler) {
        final AuthenticationHandler auth = (AuthenticationHandler) o;
        list.add(auth);
      } else {
        throw new ServletException("Class: " + authHandlerClassName + " is not a: " + AuthenticationHandler.class.getCanonicalName());
      }
    }
    builder.setExtraAuthenticationHandlers(list);
  }

  private List<String> loadAuthHandlersIfAny(final String initParameter) {
    if (initParameter == null) {
      return null;
    }
    final String[] arr = initParameter.split(",");
    final List<String> list = new ArrayList<String>();
    for (String s : arr) {
      s = s.trim();
      if (s.length() > 0) {
        list.add(s);
      }
    }
    return list;
  }

  private void checkAddInitable(final List<Initable> initables, final Object o) {
    if (o instanceof Initable) {
      initables.add((Initable) o);
    } else if (o instanceof List) {
      for (final Object o2 : (List<?>) o) {
        checkAddInitable(initables, o2);
      }
    }
  }
}
