package cn.little.prince.oj.sandbox.unsafe;

/**
 * 恶意代码 -> 无限睡眠
 * 执行阻塞，占用资源不释放
 *
 * @author 349807102
 */
public class SleepError {
    private static final long ONE_HOUR = 60 * 60 * 1000L;

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(ONE_HOUR);
        System.out.println("sleep over");
    }
}
