/**
 * @date 2014/10/11
 * @author ChenLong
 */
package game.server.http;

import java.util.HashMap;
import java.util.Map;
import org.apache.mina.http.api.HttpResponse;
import org.apache.mina.http.api.HttpStatus;
import org.apache.mina.http.api.HttpVersion;

/**
 * mina-http提供的DefaultHttpResponse神一般的设计只有构造初始化, 没有提供任何set方法, 无法改变其中的值, 不得已而提供自己的实现, Fuck ...
 *
 * @author ChenLong
 */
public class GameHttpResponse implements HttpResponse
{
    private HttpVersion version = HttpVersion.HTTP_1_1;
    private HttpStatus status = HttpStatus.SUCCESS_OK;
    private Map<String, String> headers = new HashMap<>();

    public GameHttpResponse()
    {
        defaultHeaders();
    }

    public GameHttpResponse(HttpStatus status, Map<String, String> headers)
    {
        this.status = status;
        this.headers = headers;
        defaultHeaders();
    }

    public GameHttpResponse(HttpVersion version, HttpStatus status, Map<String, String> headers)
    {
        this.version = version;
        this.status = status;
        this.headers = headers;
        defaultHeaders();
    }

    public void setContentLength(int length)
    {
        headers.put("Content-Length", Integer.toString(length));
    }
    
    public void setHttpStatus(HttpStatus status)
    {
        this.status = status;
    }

    private void defaultHeaders()
    {
        headers.put("Content-Type", "text/html;charset=UTF-8");
        
        // no cache response page
        headers.put("Pragma", "no-cache");
        headers.put("Cache-Control", "no-cache");
        headers.put("Expires", "0");
    }

    ////////////////////////////////////////////// 以下为DefaultHttpResponse复制过来的
    @Override
    public HttpVersion getProtocolVersion()
    {
        return version;
    }

    @Override
    public String getContentType()
    {
        return headers.get("content-type");
    }

    @Override
    public boolean isKeepAlive()
    {
        // TODO check header and version for keep alive
        return false;
    }

    @Override
    public String getHeader(String name)
    {
        return headers.get(name);
    }

    @Override
    public boolean containsHeader(String name)
    {
        return headers.containsKey(name);
    }

    @Override
    public Map<String, String> getHeaders()
    {
        return headers;
    }

    @Override
    public HttpStatus getStatus()
    {
        return status;
    }

    @Override
    public String toString()
    {
        String result = "HTTP RESPONSE STATUS: " + status + "\n";
        result += "VERSION: " + version + "\n";

        result += "--- HEADER --- \n";
        for (String key : headers.keySet())
        {
            String value = headers.get(key);
            result += key + ":" + value + "\n";
        }

        return result;
    }
}
