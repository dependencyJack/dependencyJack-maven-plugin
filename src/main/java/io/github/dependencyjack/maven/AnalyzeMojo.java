package io.github.dependencyjack.maven;

import io.github.dependencyjack.analyze.MavenProjectChecker;
import io.github.dependencyjack.analyze.Results;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.nio.file.Files;

@Mojo(name = "analyze", defaultPhase = LifecyclePhase.COMPILE, threadSafe = true, requiresDependencyResolution = ResolutionScope.COMPILE)
public class AnalyzeMojo extends AbstractMojo {

    /**
     * The Maven project to analyze.
     */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        var projectChecker = new MavenProjectChecker(project);
        var results = projectChecker.check();
        printResults(results);
    }

    /**
     * Output the results into the console using the logger
     */
    private void printResults(Results res) {
        var unusedArtifactsBytesUsed = res.unusedArtifacts().stream().map(a -> {
            try {
                return Files.size(a.getFile().toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).reduce(0L, Long::sum);

        log("\n\n");
        log("The following dependencies were found in the project but are unused:");
        log("");
        res.unusedArtifacts().forEach(artifact -> log(artifact.getId()));
        log("\n");
        log("==========================");
        log("Project Summary");
        log("Total artifacts:    " + project.getArtifacts().size());
        log("Artifacts used:     " + res.usedArtifacts().size());
        log("Artifacts not used: " + res.unusedArtifacts().size());
        log("");
        log("Up to " + unusedArtifactsBytesUsed / 1000 + " kB can potentially be saved by removing unused artifacts.");
        log("==========================");
    }

    private void log(String str) {
        this.getLog().info(str);
    }
}
