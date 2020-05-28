package cn.hellochaos.portscanner.controller.filter;

import cn.hellochaos.portscanner.controller.wrapper.ParamsRequestWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 过滤请求参数
 * @date 2020-05-22 20:18
 */
public class ParamsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        ParamsRequestWrapper requestWrapper = new ParamsRequestWrapper(httpRequest);
        filterChain.doFilter(requestWrapper, servletResponse);

    }

    @Override
    public void destroy() {

    }

}