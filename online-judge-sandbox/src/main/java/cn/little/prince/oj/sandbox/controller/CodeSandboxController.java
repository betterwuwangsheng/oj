package cn.little.prince.oj.sandbox.controller;

import cn.little.prince.oj.sandbox.template.CodeSandboxFactory;
import cn.little.prince.oj.sandbox.template.CodeSandboxTemplate;
import cn.little.prince.oj.sandbox.model.ExecuteCodeRequest;
import cn.little.prince.oj.sandbox.model.ExecuteCodeResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static cn.little.prince.oj.sandbox.constants.AuthRequest.AUTH_REQUEST_HEADER;
import static cn.little.prince.oj.sandbox.constants.AuthRequest.AUTH_REQUEST_SECRET;

/**
 * 执行代码
 *
 * @author 349807102
 */
@RestController
@RequestMapping("/codesandbox")
public class CodeSandboxController {

    /**
     * 执行代码
     *
     * @param executeCodeRequest 执行代码请求
     * @param request            请求体
     * @param response           响应体
     * @return 执行代码响应
     */
    @PostMapping("/execute")
    public ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest, HttpServletRequest request, HttpServletResponse response) {
        if (executeCodeRequest == null) {
            throw new RuntimeException("请求参数为空");
        }

        // 基本的认证
        String authHeader = request.getHeader(AUTH_REQUEST_HEADER);
        if (!AUTH_REQUEST_SECRET.equals(authHeader)) {
            response.setStatus(403);
            return null;
        }

        // 根据语言选择沙箱
        CodeSandboxTemplate sandboxTemplate = CodeSandboxFactory.getInstance(executeCodeRequest.getLanguage());
        return sandboxTemplate.executeCode(executeCodeRequest);
    }
}
