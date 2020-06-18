package cn.hellochaos.portscanner.controller.filter;

import cn.hellochaos.portscanner.entity.dto.ResultBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ServletComponentScan
@WebFilter(urlPatterns = "/*", filterName = "CrosFilter")
public class CrosFilter extends FormAuthenticationFilter {

  // 解决OPTIONS请求跨域问题
  @Override
  protected boolean isAccessAllowed(
      ServletRequest request, ServletResponse response, Object mappedValue) {
    if (request instanceof HttpServletRequest) {
      if (((HttpServletRequest) request).getMethod().toUpperCase().equals("OPTIONS")) {
        return true;
      }
    }
    return super.isAccessAllowed(request, response, mappedValue);
  }

  @Override
  protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
    HttpServletRequest httpRequest = WebUtils.toHttp(request);
    HttpServletResponse httpResponse = WebUtils.toHttp(response);
    if (httpRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
      httpResponse.setHeader("Access-control-Allow-Origin", httpRequest.getHeader("Origin"));
      httpResponse.setHeader("Access-Control-Allow-Methods", httpRequest.getMethod());
      httpResponse.setHeader(
          "Access-Control-Allow-Headers", httpRequest.getHeader("Access-Control-Request-Headers"));
      httpResponse.addHeader("Access-Control-Allow-Headers", "Content-Type,token,Authorization");
      httpResponse.addHeader("Access-Control-Expose-Headers", "Content-Type,token,Authorization");
      httpResponse.setStatus(HttpStatus.OK.value());
      return false;
    }
    return super.preHandle(request, response);
  }

  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
      throws IOException {
    HttpServletResponse httpServletResponse = (HttpServletResponse) response;
    if (isAjax(request)) {
      setResponse(httpServletResponse);
    } else {
      setResponse(httpServletResponse);
    }
    return false;
  }

  private boolean isAjax(ServletRequest request) {
    String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
    if ("XMLHttpRequest".equalsIgnoreCase(header)) {
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
  }

  public static void setResponse(HttpServletResponse response) {

    try {
      ObjectMapper objectMapper = new ObjectMapper();
      response.setCharacterEncoding("UTF-8");
      response.setContentType("application/json");
      ResultBean resultBean = ResultBean.builder().code("3001").message("请登录后操作!").build();
      response.getWriter().write(objectMapper.writeValueAsString(resultBean));
      response.getWriter().flush();
      response.getWriter().close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
