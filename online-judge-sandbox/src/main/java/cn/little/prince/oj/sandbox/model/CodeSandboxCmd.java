package cn.little.prince.oj.sandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 代码沙箱 cmd
 *
 * @author 349807102
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeSandboxCmd {
    private String compileCmd;
    private String runCmd;
}
