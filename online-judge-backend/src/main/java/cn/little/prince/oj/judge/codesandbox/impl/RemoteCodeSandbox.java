package cn.little.prince.oj.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.little.prince.oj.common.ErrorCode;
import cn.little.prince.oj.exception.BusinessException;
import cn.little.prince.oj.judge.codesandbox.CodeSandbox;
import cn.little.prince.oj.judge.codesandbox.model.ExecuteCodeRequest;
import cn.little.prince.oj.judge.codesandbox.model.ExecuteCodeResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * 远程代码沙箱【实际调用接口的沙箱】
 *
 * @author 349807102
 */
public class RemoteCodeSandbox implements CodeSandbox {
    /**
     * 定义鉴权请求头和密钥
     */
    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "secretKey";

    /**
     * 沙箱服务地址
     */
    private static final String URL = "http://localhost:8090/codesandbox/execute";

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        // 调用接口
        String json = JSONUtil.toJsonStr(executeCodeRequest);

        // 请求
        String responseStr = HttpUtil.createPost(URL).header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET).body(json).execute().body();
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "executeCode remoteSandbox error, message = " + responseStr);
        }
        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
    }
}
