/**
 * @date 2014/9/2
 * @author ChenLong
 */
package chenlong.gmtesttool.function.client;

/**
 *
 * @author ChenLong
 */
public interface INotify {

    void connected();

    void disconnected();
    
    void received(String jsonStr);
}
