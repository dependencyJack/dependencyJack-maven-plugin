package io.github.dependencyjack.maven;

import io.github.dependencyjack.analyze.ImportAnalyzer;
import io.github.dependencyjack.analyze.utils.LauncherHelper;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtImport;
import spoon.reflect.declaration.CtType;
import spoon.reflect.visitor.ImportScanner;
import spoon.reflect.visitor.ImportScannerImpl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Mojo(name = "list-imports")
public class ListImportsMojo extends AbstractMojo {

    /**
     * The Maven project to analyze.
     */
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        var analyser = new ImportAnalyzer();
        analyser.setModel(LauncherHelper.getCtModel(project));
        var importedDependencies = analyser.getDependencies();

        for (var dep : importedDependencies) {
            this.getLog().info(dep);
        }
    }
}
