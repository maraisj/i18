# Maven architecture for the plugin demo

This guide describes how to organize the project when plugins are developed independently and different applications assemble different plugin combinations.

## The project shape

Each plugin is its own Maven artifact (normally a JAR). It contains the plugin implementation and its own translation resources. Each application is another Maven artifact that depends on the docking host and exactly the plugins it wants to include.

```text
desktop-project/
├── pom.xml                  # root aggregator and shared parent POM
├── plugin-api/              # shared plugin contract and locale API
├── docking-host/            # JFrame, docking framework, locale selector
├── plugin-greeting/         # plugin code and its own translations
├── plugin-counter/
├── plugin-notes/
├── app-a/                   # docking-host + greeting
├── app-ab/                  # docking-host + greeting + counter
└── app-abd/                 # docking-host + greeting + counter + notes
```

The build should make the distinction between compile-time composition and runtime discovery clear:

- Each app declares its chosen plugin artifacts as dependencies.
- The host discovers and starts the plugins on the runtime classpath, for example through Java's `ServiceLoader` API.
- The shared `plugin-api` defines the plugin lifecycle and locale-change contract.
- Each plugin updates its own UI using translation files packaged in that plugin's JAR.

Separate Maven modules make independent development and packaging possible. They do not, by themselves, make a plugin dynamically installable. Loading a plugin JAR after the app has already been built and deployed would additionally need a runtime plugin directory/class-loader strategy and lifecycle/error handling. For this simulation, separate artifacts selected by each app module are enough.

## Root POM, aggregator, and parent POM

These terms describe different Maven roles, but one file can have several roles:

- **Root POM** is the informal name for the top-level `pom.xml` in the source tree.
- **Aggregator POM** has `<packaging>pom</packaging>` and a `<modules>` list. It tells Maven which projects to include in the reactor build.
- **Parent POM** is a POM that child projects reference in a `<parent>` section. Its configuration is inherited by those children.

A common arrangement uses the root POM as both aggregator and parent. Maven treats aggregation and inheritance as separate mechanisms; they are simply combined in this layout. The Maven reactor collects the modules and sorts them so dependencies are built before the projects that need them.

Example root `pom.xml`:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>desktop-project</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging>

    <modules>
        <module>plugin-api</module>
        <module>docking-host</module>
        <module>plugin-a</module>
        <module>plugin-b</module>
        <module>plugin-d</module>
        <module>app-a</module>
        <module>app-ab</module>
        <module>app-abd</module>
    </modules>

    <properties>
        <maven.compiler.release>17</maven.compiler.release>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.example</groupId>
                <artifactId>plugin-api</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>com.example</groupId>
                <artifactId>docking-host</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>com.example</groupId>
                <artifactId>plugin-a</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>com.example</groupId>
                <artifactId>plugin-b</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>com.example</groupId>
                <artifactId>plugin-d</artifactId>
                <version>${project.version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```

The `dependencyManagement` section centralizes versions; it does **not** add those dependencies to every child. An app adds only the artifacts it wants in its own `<dependencies>` section.

An app module inherits the common build settings and selects its composition:

```xml
<parent>
    <groupId>com.example</groupId>
    <artifactId>desktop-project</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <relativePath>../pom.xml</relativePath>
</parent>

<artifactId>app-ab</artifactId>

<dependencies>
    <dependency>
        <groupId>com.example</groupId>
        <artifactId>docking-host</artifactId>
    </dependency>
    <dependency>
        <groupId>com.example</groupId>
        <artifactId>plugin-a</artifactId>
    </dependency>
    <dependency>
        <groupId>com.example</groupId>
        <artifactId>plugin-b</artifactId>
    </dependency>
</dependencies>
```

This app includes plugin A and B. `app-a` would depend on only A; `app-abd` would depend on A, B, and D. The dependency declarations both select the composition and give the reactor actual project relationships to build.

## What a BOM does

A **Bill of Materials (BOM)** is a POM used as a catalog of dependency versions. Projects import it into their own `<dependencyManagement>` section. A BOM does not make a project inherit general build configuration, does not aggregate modules, and does not add dependencies to the consuming project.

An app can import a BOM like this:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>desktop-bom</artifactId>
            <version>1.0.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

It can then declare the chosen dependencies without versions; the imported BOM supplies the managed versions. The app still explicitly chooses which plugins to include.

## Parent versus BOM

| Capability | Parent POM | BOM |
| --- | --- | --- |
| Shares properties and build configuration by inheritance | Yes | No |
| Can list modules for a reactor build | Yes, if also an aggregator | No |
| Manages dependency versions | Yes, with `dependencyManagement` | Yes |
| Adds dependencies to a project automatically | No | No |
| Typical use | Projects sharing build setup | Consumers aligning dependency versions |

Maven permits a POM to be both a parent and an aggregator. A BOM can also be a separate artifact. It is possible to use both, but they solve separate needs.

## What to use here

For this working project, one root POM is both parent and aggregator. Common Java/compiler/plugin configuration lives there, and `dependencyManagement` centralizes versions shared by modules. Each plugin has its own POM and artifact; each app composition has its own POM with explicit plugin dependencies.

A separate BOM is optional. Add one if app projects in other repositories or teams need a published catalog of compatible host/plugin versions. It is especially useful when app compositions are built independently from the plugin source repository. It is unnecessary just to select different plugins among app modules in one reactor.

If a plugin is built in a separate repository, it may inherit from the shared parent only if that parent POM has been made available (for example, installed or published). Otherwise, it can use a suitable local parent and still publish its plugin JAR. The consuming app can use a BOM to align versions, or declare the selected plugin versions directly.

## Translation ownership

Keep each plugin's resource bundles in that plugin module, for example:

```text
plugin-a/
└── src/main/resources/com/example/plugins/a/Messages.properties
                            Messages_fr.properties
                            Messages_de.properties
```

Maven packages `src/main/resources` into the plugin JAR. The plugin should use a unique bundle base name and own the keys for its UI. The host shares the selected `Locale` through the plugin API and notifies loaded plugins when it changes; each plugin then refreshes its own labels, buttons, dialogs, and dock title.

## Independent development and releases

There are two useful development setups:

1. **One repository, one reactor:** all modules are checked out together and built with `mvn package` at the root. Developers can work on one plugin while still building the apps that use it.
2. **Separate repositories:** the plugin API and parent need a stable published version. Each plugin publishes its JAR independently. App projects then select plugin versions through direct dependencies or an imported BOM.

The first setup is a good simulation of team ownership without needing to establish artifact publishing immediately. The second is closer to independently released components. In both setups, the app's dependency list is the composition definition.

## References

- [Maven: Guide to Working with Multiple Modules](https://maven.apache.org/guides/mini/guide-multiple-modules.html)
- [Maven: Introduction to the POM](https://maven.apache.org/guides/introduction/introduction-to-the-pom.html)
- [Maven: POM Reference](https://maven.apache.org/pom.html)
- [Maven: Introduction to the Dependency Mechanism (BOMs)](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism)
