package cn.little.prince.oj.judge.codesandbox;

import cn.little.prince.oj.judge.codesandbox.impl.ExampleCodeSandbox;
import cn.little.prince.oj.judge.codesandbox.impl.RemoteCodeSandbox;
import cn.little.prince.oj.judge.codesandbox.impl.ThirdPartyCodeSandbox;
import org.springframework.beans.factory.annotation.Value;

/**
 * 代码沙箱工厂（根据字符串参数创建指定的代码沙箱实例），
 *
 * @author 349807102
 */
public class CodeSandboxFactory {
    @Value("${codesandbox.type:example}")
    private String type;

    /**
     * 创建代码沙箱实例
     * <p>
     * 使用工厂模式，根据用户传入的字符串参数（沙箱类别），来生成对应的代码沙箱实现类
     * 此处使用静态工厂模式
     *
     * @param type 沙箱类型
     * @return 根据字符串参数创建指定的代码沙箱实例
     */
    public static CodeSandbox newInstance(String type) {
        switch (type) {
            case "remote":
                return new RemoteCodeSandbox();
            case "thirdParty":
                return new ThirdPartyCodeSandbox();
            case "example":
            default:
                return new ExampleCodeSandbox();
        }
    }
}
