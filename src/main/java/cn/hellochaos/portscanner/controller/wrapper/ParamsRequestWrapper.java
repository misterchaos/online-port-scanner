package cn.hellochaos.portscanner.controller.wrapper;

import cn.hellochaos.portscanner.exception.bizException.BizException;
import lombok.extern.slf4j.Slf4j;
import org.andy.sensitivewdfilter.WordFilter;
import org.apache.http.entity.ContentType;
import org.apache.poi.util.IOUtils;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
 * @description 过滤请求参数
 * @date 2020-05-22 20:20
 */

@Slf4j
public class ParamsRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 保存处理后的参数
     */
    private final Map<String, String[]> params = new HashMap<String, String[]>();
    /**
     * 保存内容类型为文本类型
     * 比如Content-Type:text/plain,application/json,text/html等
     */
    private byte[] content;

    public ParamsRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.params.putAll(request.getParameterMap());
        //自定义方法，用于参数去重
        this.modifyParameterValues();
        if (ContentType.APPLICATION_JSON.getMimeType().equals(request.getContentType())) {
            //获取文本数据;
            this.content = IOUtils.toByteArray(request.getInputStream());
            //过滤请求体
            this.modifyInputStream();
        }
    }


    /**
     * 处理请求参数
     *
     * @notice none
     * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
     * @date 2020-05-22
     */
    private void modifyParameterValues() {
        Set<Map.Entry<String, String[]>> entrys = params.entrySet();
        for (Map.Entry<String, String[]> entry : entrys) {
            String[] values = entry.getValue();
            for (int i = 0; i < values.length; i++) {
                //过滤xss攻击
                values[i] = HtmlUtils.htmlEscape(values[i]);
            }
            this.params.put(entry.getKey(), values);
        }
    }


    /**
     * 处理请求体文本信息
     *
     * @notice none
     * @author <a href="mailto:kobe524348@gmail.com">黄钰朝</a>
     * @date 2020-05-22
     */
    private void modifyInputStream() {
        //对json字符串进行处理
        //过滤敏感词
        String strContent = new String(this.content);
        if (WordFilter.isContains(strContent)) {
            log.warn("内容包含敏感词:" + strContent);
            throw new BizException("您输入的内容包含敏感词，请重新输入");
        }
    }

    @Override
    public String[] getParameterValues(String name) {
        Object v = super.getParameterMap().get(name);
        if (v == null) {
            return null;
        } else {
            return (String[]) v;
        }
    }

    @Override
    public String getParameter(String name) {
        Object v = super.getParameterMap().get(name);
        if (v == null) {
            return null;
        } else {
            String[] strArr = (String[]) v;
            if (strArr.length > 0) {
                return strArr[0];
            } else {
                return null;
            }
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        //这种获取的参数的方式针对于内容类型为文本类型，比如Content-Type:text/plain,application/json,text/html等
        //在springmvc中可以使用@RequestBody 来获取 json数据类型
        //其他文本类型不做处理，重点处理json数据格式
        if (!"application/json".equalsIgnoreCase(super.getHeader("Content-Type"))) {
            return super.getInputStream();
        } else {
            //根据自己的需要重新指定方法
            ByteArrayInputStream in = new ByteArrayInputStream(this.content);
            return new ServletInputStream() {
                @Override
                public int read() throws IOException {
                    return in.read();
                }

                @Override
                public int read(byte[] b, int off, int len) throws IOException {
                    return in.read(b, off, len);
                }

                @Override
                public int read(byte[] b) throws IOException {
                    return in.read(b);
                }

                @Override
                public void setReadListener(ReadListener listener) {
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public long skip(long n) throws IOException {
                    return in.skip(n);
                }

                @Override
                public void close() throws IOException {
                    in.close();
                }

                @Override
                public synchronized void mark(int readlimit) {
                    in.mark(readlimit);
                }

                @Override
                public synchronized void reset() throws IOException {
                    in.reset();
                }
            };
        }
    }
}
