package cn.little.prince.onlinejudgebackendjudgeservice.judge.codesandbox.impl;


import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.little.prince.onlinejudgebackendcommon.common.ErrorCode;
import cn.little.prince.onlinejudgebackendcommon.exception.BusinessException;
import cn.little.prince.onlinejudgebackendjudgeservice.judge.codesandbox.CodeSandbox;
import cn.little.prince.onlinejudgebackendmodel.codesandbox.ExecuteCodeRequest;
import cn.little.prince.onlinejudgebackendmodel.codesandbox.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 远程代码沙箱【实际调用接口的沙箱】
 *
 * @author 349807102
 */
@Slf4j
public class RemoteCodeSandbox implements CodeSandbox {
    /**
     * 定义鉴权请求头和密钥
     */
    private static final String AUTH_REQUEST_HEADER = "wuwangsheng";

    private static final String AUTH_REQUEST_SECRET = "wuwangsheng";

    /**
     * 沙箱服务地址
     */
    private static final String URL = "http://43.138.248.167/:8090/codesandbox/execute";

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        // 调用接口
        String json = JSONUtil.toJsonStr(executeCodeRequest);

        log.info("json: " + json);
        // 请求
        String responseStr = HttpUtil.createPost(URL).header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET).body(json).execute().body();
        log.info("responseStr: " + responseStr);
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "executeCode remoteSandbox error, message = " + responseStr);
        }
        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
    }
}
