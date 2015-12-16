package uk.gov.phe.erdst.sc.awag.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/*")
// Use the same URL pattern as <security-constraint>
public class NoCacheFilter implements Filter
{
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException
    {
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setHeader("Pragma", "no-cache");
        httpResponse.setDateHeader("Expires", 0);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy()
    {
        // NO-OP
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException
    {
        // NO-OP
    }

}
