package io.github.dependencyjack.analyze.utils;

import org.apache.maven.project.MavenProject;
import spoon.MavenLauncher;
import spoon.reflect.CtModel;

public final class LauncherHelper {

    private LauncherHelper() {
        throw new AssertionError("Instantiating utility class.");
    }

    public static CtModel getCtModel(MavenProject project) {
        MavenLauncher launcher = new MavenLauncher(project.getBasedir().getAbsolutePath(), MavenLauncher.SOURCE_TYPE.APP_SOURCE);
        launcher.buildModel();
        return launcher.getModel();
    }
}
