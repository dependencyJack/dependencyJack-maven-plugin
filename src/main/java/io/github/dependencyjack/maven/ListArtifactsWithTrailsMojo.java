package io.github.dependencyjack.maven;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.util.Set;

@Mojo(name = "list-artifacts-with-trails", defaultPhase = LifecyclePhase.COMPILE, threadSafe = true, requiresDependencyResolution = ResolutionScope.COMPILE)
public class ListArtifactsWithTrailsMojo extends AbstractMojo {

    /**
     * The Maven project to analyze.
     */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        //jar lookup
        Set<Artifact> artifacts = project.getArtifacts();
        artifacts.forEach(artifact -> {
            this.getLog().info("artifact");
            this.getLog().info(artifact.getId());
            this.getLog().info("dependencyTrail");
            artifact.getDependencyTrail().forEach(
                    string -> this.getLog().info(string));
        });
    }
}
