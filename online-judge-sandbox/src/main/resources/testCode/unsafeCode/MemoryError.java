import java.util.ArrayList;
import java.util.List;

/**
 * 恶意代码 -> 无限占用空间（浪费系统内存）
 *
 * @author 349807102
 */
public class Main {
    public static void main(String[] args) {
        List<byte[]> bytes = new ArrayList<>();
        while (true) {
            // 新建空间
            bytes.add(new byte[10000]);
        }
    }
}
