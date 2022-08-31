package io.dourl.lib_plugin_jg.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;

/**
 * File description.
 *
 * @author dourl
 * @date 2022/8/30
 */
public class UploadTask extends DefaultTask {

    public Project targetProject;

    public void init(Project project){
        this.targetProject = project;
    }
    @TaskAction
    public void uploadApk(){

    }
}
