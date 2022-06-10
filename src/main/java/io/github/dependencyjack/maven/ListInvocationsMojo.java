package io.github.dependencyjack.maven;

import io.github.dependencyjack.analyze.InvocationArgumentAnalyzer;
import io.github.dependencyjack.analyze.utils.LauncherHelper;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "list-invocations")
public class ListInvocationsMojo extends AbstractMojo {

    /**
     * The Maven project to analyze.
     */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        var analyser = new InvocationArgumentAnalyzer();
        analyser.setModel(LauncherHelper.getCtModel(project));
        var invocationArgumentDependencies = analyser.getDependencies();

        for (var dep : invocationArgumentDependencies) {
            this.getLog().info(dep);
        }
    }
}
