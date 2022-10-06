package io.dourl.lib_plugin_jg;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

import io.dourl.lib_plugin_jg.task.UploadTask;

public class JiaPlugin implements Plugin<Project> {
    public static final String UPLOAD_EXTENSION_NAME = "upload"; //插件的名字


    @Override
    public void apply(Project project) {
        System.out.println("Hello OSS upload");
       // applyExtension(project);
    }
    private void applyExtension(Project project) {
        // 创建扩展，并添加到 ExtensionContainer
        project.getExtensions().create(UPLOAD_EXTENSION_NAME,Extension.class,project);
        // 配置完后执行
        project.afterEvaluate(new Action<Project>() {
            @Override
            public void execute(Project project) {
                  //获取apk

                UploadTask uploadTask = project.getTasks().create("文件明", UploadTask.class);
                uploadTask.init(project);

                // 建立依赖关系
            }
        });
    }
}