package com.github.gv2011.experiments.webdav;

import io.milton.http.HttpManager;
import io.milton.http.Request;
import io.milton.http.Response;
import io.milton.servlet.ServletConfigWrapper;
import io.milton.servlet.ServletRequest;
import io.milton.servlet.ServletRequestAccess;
import io.milton.servlet.ServletResponse;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MServlet implements Servlet {

    private final Logger log = LoggerFactory.getLogger(MServlet.class);
    private static final ThreadLocal<HttpServletRequest> originalRequest = new ThreadLocal<HttpServletRequest>();
    private static final ThreadLocal<HttpServletResponse> originalResponse = new ThreadLocal<HttpServletResponse>();
    private static final ThreadLocal<ServletConfig> tlServletConfig = new ThreadLocal<ServletConfig>();

    public static HttpServletRequest request() {
        return originalRequest.get();
    }

    public static HttpServletResponse response() {
        return originalResponse.get();
    }

    /**
     * Make the servlet config available to any code on this thread.
     *
     * @return
     */
    public static ServletConfig servletConfig() {
        return tlServletConfig.get();
    }

    public static void forward(final String url) {
        try {
            request().getRequestDispatcher(url).forward(originalRequest.get(), originalResponse.get());
        } catch (final IOException ex) {
            throw new RuntimeException(ex);
        } catch (final ServletException ex) {
            throw new RuntimeException(ex);
        }
    }
    private ServletConfigWrapper config;
    private ServletContext servletContext;
    protected HttpManager httpManager;

    private final Configurator configurator;

    public MServlet(final Configurator configurator) {
      this.configurator = configurator;
    }

    @Override
    public void init(final ServletConfig config) throws ServletException {
        try {
            this.config = new ServletConfigWrapper(config);
            servletContext = config.getServletContext();

            httpManager = configurator.configure(this.config);

        } catch (final ServletException ex) {
            log.error("Exception starting milton servlet", ex);
            throw ex;
        } catch (final Throwable ex) {
            log.error("Exception starting milton servlet", ex);
            throw new RuntimeException(ex);
        }
    }


    @Override
    public void destroy() {
        log.debug("destroy");
        if (configurator == null) {
            return;
        }
        configurator.shutdown();
    }

    @Override
    public void service(final javax.servlet.ServletRequest servletRequest, final javax.servlet.ServletResponse servletResponse) throws ServletException, IOException {
        final HttpServletRequest req = (HttpServletRequest) servletRequest;
        final HttpServletResponse resp = (HttpServletResponse) servletResponse;
        try {
            setThreadlocals(req, resp);
            tlServletConfig.set(config.getServletConfig());
            final Request request = new ServletRequest(req, servletContext);
            final Response response = new ServletResponse(resp);
            httpManager.process(request, response);
        } finally {
            clearThreadlocals();
            tlServletConfig.remove();
            ServletRequestAccess.servletRequestClearThreadLocals();
            servletResponse.getOutputStream().flush();
            servletResponse.flushBuffer();
        }
    }

    public static void clearThreadlocals() {
        originalRequest.remove();
        originalResponse.remove();
    }

    public static void setThreadlocals(final HttpServletRequest req, final HttpServletResponse resp) {
        originalRequest.set(req);
        originalResponse.set(resp);
    }

    @Override
    public String getServletInfo() {
        return "MiltonServlet";
    }

    @Override
    public ServletConfig getServletConfig() {
        return config.getServletConfig();
    }
}
