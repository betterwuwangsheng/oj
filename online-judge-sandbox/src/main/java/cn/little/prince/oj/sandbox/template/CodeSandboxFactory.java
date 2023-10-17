package cn.little.prince.oj.sandbox.template;

import cn.little.prince.oj.sandbox.enums.QuestionSubmitLanguageEnum;
import cn.little.prince.oj.sandbox.language.CppNativeCodeSandbox;
import cn.little.prince.oj.sandbox.language.JavaNativeCodeSandbox;

/**
 * 代码沙箱工厂
 *
 * @author 349807102
 */
public class CodeSandboxFactory {
    public static CodeSandboxTemplate getInstance(QuestionSubmitLanguageEnum language) {
        switch (language) {
            case JAVA:
                return new JavaNativeCodeSandbox();
            case CPP:
                return new CppNativeCodeSandbox();
            default:
                throw new RuntimeException("暂不支持");
        }
    }
}
