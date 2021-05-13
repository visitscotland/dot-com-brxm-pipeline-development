package com.visitscotland.brxm.config;

import org.hippoecm.hst.container.PingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

public class VsToolsFilter extends PingFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.getServletContext().getNamedDispatcher("VSIServlet").forward(request, response);
    }
}
