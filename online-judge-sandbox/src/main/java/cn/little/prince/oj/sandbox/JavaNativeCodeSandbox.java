package cn.little.prince.oj.sandbox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.dfa.FoundWord;
import cn.hutool.dfa.WordTree;
import cn.little.prince.oj.sandbox.model.ExecuteCodeRequest;
import cn.little.prince.oj.sandbox.model.ExecuteCodeResponse;
import cn.little.prince.oj.sandbox.model.ExecuteMessage;
import cn.little.prince.oj.sandbox.model.JudgeInfo;
import cn.little.prince.oj.sandbox.security.MySecurityManager;
import cn.little.prince.oj.sandbox.utils.ProcessUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 原生 Java 代码沙箱 - 实现模板方法
 *
 * @author 349807102
 */
@Component
public class JavaNativeCodeSandbox implements CodeSandbox {

    /**
     * 全局代码目录名称
     */
    public static final String GLOBAL_CODE_DIR_NAME = "tmpCode";

    /**
     * 全局 JAVA 类名
     */
    public static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";


    /**
     * 超时时间
     */
    public static final long TIME_OUT = 5000L;

    /**
     * 代码黑名单
     */
    public static final List<String> BLACK_LIST = Arrays.asList("Files", "exec");

    /**
     * 字典树
     */
    public static final WordTree WORD_TREE = new WordTree();

    static {
        WORD_TREE.addWords(BLACK_LIST);
    }


    /**
     * 执行程序
     *
     * @param executeCodeRequest 执行代码请求
     * @return 执行代码响应
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        // 调用安全管理器
        System.setSecurityManager(new MySecurityManager());

        List<String> inputList = executeCodeRequest.getInputList();
        String language = executeCodeRequest.getLanguage();
        String code = executeCodeRequest.getCode();

        // 校验代码
        FoundWord foundWord = WORD_TREE.matchWord(code);
        if (foundWord != null) {
            System.out.println("包含敏感词[禁止词]: " + foundWord.getFoundWord());
            return null;
        }

        // 判断文件夹是否存在
        String userDir = System.getProperty("user.dir");

        // 代码目录
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;

        // 不存在代码目录 -> 创建
        if (FileUtil.exist(globalCodePathName)) {
            FileUtil.mkdir(globalCodePathName);
        }

        // 保存用户提交的代码 -> 隔离存放
        // 代码父级文件目录 -> 隔离
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();

        // 代码实际存放的目录
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_JAVA_CLASS_NAME;

        // 写入
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);

        // 编译代码，得到 .class 文件
        String compliedCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsoluteFile());

        try {

            // 调用 cmd 运行
            Process compileProcess = Runtime.getRuntime().exec(compliedCmd);

            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
            System.out.println(executeMessage);
        } catch (Exception e) {
            return getResponse(e);
        }

        // 3.执行代码
        // 获取输入用例
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs : inputList) {
            // -Dfile.encoding=utf-8 解决控制台中文乱码
            // -Xmx256m JVM 最大堆
            String runCmd = String.format("java -Xmx256m -Dfile.encoding=utf-8 -cp %s Main %s", userCodeParentPath, inputArgs);
            try {
                Process runProcess = Runtime.getRuntime().exec(runCmd);

                // 创建新的线程监控另一个线程[守护线程]
                new Thread(() -> {
                    try {
                        Thread.sleep(TIME_OUT);

                        System.out.println("超时了，中断");
                        // 监控进程睡眠最大时间 -> 然后杀死另一个线程
                        runProcess.destroy();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();

                ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "运行");
                System.out.println(executeMessage);
                executeMessageList.add(executeMessage);
            } catch (Exception e) {
                return getResponse(e);
            }
        }

        // 整理输出
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();

        // 正常输出信息
        List<String> outputList = new ArrayList<>();

        // 用时最大值 -> 判断是否超时
        long maxTime = 0;

        for (ExecuteMessage executeMessage : executeMessageList) {
            // 有错误信息,不添加到正常输出
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                executeCodeResponse.setMessage(errorMessage);

                // 用户代码执行中存在错误
                executeCodeResponse.setStatus(3);

                // 中断循环
                break;
            }
            // 添加正常信息
            outputList.add(executeMessage.getMessage());
            Long time = executeMessage.getTime();
            if (time != null) {

                // 获取最大运行时间
                maxTime = Math.max(maxTime, time);
            }
        }

        // 每一条信息都正常输出
        if (outputList.size() == executeMessageList.size()) {
            // 正常运行完成
            executeCodeResponse.setStatus(1);
        }

        // 设置输出列表
        executeCodeResponse.setOutputList(outputList);


        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);

        // 判题信息系
        executeCodeResponse.setJudgeInfo(judgeInfo);

        // 清理文件
        // 根目录不为空再删
        if (userCodeFile.getParent() != null) {
            boolean del = FileUtil.del(userCodeParentPath);
            System.out.println("删除文件" + (del ? "成功" : "失败"));
        }
        return executeCodeResponse;
    }

    /**
     * 获取错误处理响应
     *
     * @param e 异常
     * @return 执行返回信息
     */
    private ExecuteCodeResponse getResponse(Throwable e) {
        // 构建返回对象
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<String>());
        executeCodeResponse.setMessage(e.getMessage());

        // 代码沙箱错误
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }

    public static void main(String[] args) {
        JavaNativeCodeSandbox javaNativeCodeSandbox = new JavaNativeCodeSandbox();
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();

        // String code = ResourceUtil.readStr("testCode/simpleComputeArgs/Main.java", StandardCharsets.UTF_8);
        // String code = ResourceUtil.readStr("testCode/unsafeCode/SleepError.java", StandardCharsets.UTF_8);
        // String code = ResourceUtil.readStr("testCode/unsafeCode/MemoryError.java", StandardCharsets.UTF_8);
        String code = ResourceUtil.readStr("testCode/unsafeCode/ReadFileError.java", StandardCharsets.UTF_8);
        // String code = ResourceUtil.readStr("testCode/unsafeCode/WriteFileError.java", StandardCharsets.UTF_8);
        // String code = ResourceUtil.readStr("testCode/unsafeCode/RunFileError.java", StandardCharsets.UTF_8);
        executeCodeRequest.setInputList(Arrays.asList("1 2", "1 3"));
        executeCodeRequest.setCode(code);
        executeCodeRequest.setLanguage("java");

        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandbox.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }
}
