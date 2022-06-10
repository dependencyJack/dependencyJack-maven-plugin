# dependencyJack Maven Plugin
Prototype Maven plugin for optimizing dependencies

## Installation

- Clone the repository
- Run ```mvn install ```
- Add plugin to the desired Maven projects pom file in the plugin section:
```xml
<plugin>
  <groupId>io.github.dependencyjack</groupId>
  <artifactId>dependencyJack-maven-plugin</artifactId>
  <version>1.0-SNAPSHOT</version>
</plugin> 
```

## Usage

Execute the analyze goal of the plugin ``` dependencyJack:analyze``` to get an analysis of your project.

## Advanced Usage

To get a list of all imports use the goal ```list-imports```.

To get a list of all invocations use the goal ```list-invocations```.

To get a list of all dependencies with trails ```list-artifacts-with-trails```.







