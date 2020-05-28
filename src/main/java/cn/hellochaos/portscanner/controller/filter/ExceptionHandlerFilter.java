package cn.hellochaos.portscanner.controller.filter;

import cn.hellochaos.portscanner.entity.dto.ResultBean;
import cn.hellochaos.portscanner.exception.bizException.BizException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 过滤器异常处理器
 * @date 2020-05-22 22:09
 */
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Throwable e) {
            // custom error response class used across my project
            responseException(e, response, new ResultBean<>(new BizException(e.getMessage())));
        }
    }

    private void responseException(Throwable e, HttpServletResponse response, ResultBean<?> resultBean) {
        e.printStackTrace();
        ObjectMapper mapper = new ObjectMapper();
        try {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json;charset=utf-8");
            mapper.writeValue(response.getOutputStream(), resultBean);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}
