package cn.little.prince.oj.sandbox.language;

import cn.little.prince.oj.sandbox.template.CodeSandboxTemplate;
import cn.little.prince.oj.sandbox.model.CodeSandboxCmd;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @author 349807102
 */
@Slf4j
public class CppNativeCodeSandbox extends CodeSandboxTemplate {
    private static final String PREFIX = File.separator + "cpp";

    private static final String GLOBAL_CODE_DIR_PATH = File.separator + "tempCode";

    private static final String GLOBAL_CPP_NAME = File.separator + "main.cpp";

    public CppNativeCodeSandbox() {
        super.prefix = PREFIX;
        super.globalCodeDirPath = GLOBAL_CODE_DIR_PATH;
        super.globalCodeFileName = GLOBAL_CPP_NAME;
    }

    @Override
    public CodeSandboxCmd getCmd(String userCodeParentPath, String userCodePath) {
        return CodeSandboxCmd
                .builder()
                .compileCmd(String.format("g++ -finput-charset=UTF-8 -fexec-charset=UTF-8 %s -o %s", userCodePath, userCodePath.substring(0, userCodePath.length() - 4)))
                .runCmd(userCodeParentPath + File.separator + "main")
                .build();
    }
}
