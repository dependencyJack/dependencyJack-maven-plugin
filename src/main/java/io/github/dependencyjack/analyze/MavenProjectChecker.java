package io.github.dependencyjack.analyze;

import io.github.dependencyjack.analyze.utils.LauncherHelper;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarFile;

/**
 * Checks the dependencies of a given maven project using some DependencyAnalyzers
 */
public class MavenProjectChecker {
    private final MavenProject project;
    private final List<DependencyAnalyzer> analyzerList = new ArrayList<>();

    public MavenProjectChecker(MavenProject project) {
        this.project = project;
        analyzerList.add(new ImportAnalyzer());
        analyzerList.add(new InvocationArgumentAnalyzer());
    }

    /**
     * Check the project for artifacts which are unused
     *
     * @return Results containing the unused as well as used artifacts
     */
    public Results check() {
        var usedArtifacts = findUsedArtifacts();
        var unusedArtifacts = findUnusedArtifacts(usedArtifacts);

        return new Results(usedArtifacts, unusedArtifacts);
    }

    private Set<Artifact> findUsedArtifacts() {
        var model = LauncherHelper.getCtModel(project);
        var classDependencies = new HashSet<String>();

        analyzerList.forEach(analyzer -> {
            analyzer.setModel(model);
            classDependencies.addAll(analyzer.getDependencies());
        });

        var usedArtifacts = new HashSet<Artifact>();

        project.getArtifacts().forEach(artifact -> {
            if ("jar".equals(artifact.getType())) {
                try (var jar = new JarFile(artifact.getFile())) {
                    var entries = jar.entries();

                    while (entries.hasMoreElements()) {
                        var entry = entries.nextElement();
                        var fileName = entry.getName();

                        if (fileName.endsWith(".class")) {
                            var p = fileName
                                    .replace("/", ".")
                                    .replace(".class", "");

                            if (classDependencies.contains(p)) {
                                usedArtifacts.add(artifact);
                                break;
                            }
                        }
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return usedArtifacts;
    }

    private Set<Artifact> findUnusedArtifacts(Set<Artifact> usedArtifacts) {
        var unusedArtifacts = new HashSet<>(project.getArtifacts());
        unusedArtifacts.removeAll(usedArtifacts);
        return unusedArtifacts;
    }
}
