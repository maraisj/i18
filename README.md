# Swing Docking Plugins

A Maven multi-module demo of a Swing docking host that composes independently packaged plugins. Each plugin is a JAR with its own translation files and `ServiceLoader` provider declaration. Each app module selects its plugin set through ordinary Maven dependencies.

See [MAVEN-ARCHITECTURE.md](MAVEN-ARCHITECTURE.md) for the parent POM, aggregator, BOM, module ownership, and packaging explanation.

## Modules

| Module | Purpose |
| --- | --- |
| `plugin-api` | Shared provider contract |
| `docking-host` | JFrame, DockingFrames integration, locale selector, provider discovery |
| `plugin-greeting` | Welcome application and its English/French/German bundles |
| `plugin-counter` | Counter application and its own bundles |
| `plugin-notes` | Notes application and its own bundles |
| `app-a` | Host + Greeting |
| `app-ab` | Host + Greeting + Counter |
| `app-abd` | Host + Greeting + Counter + Notes |

## Build and run

Requirements: JDK 17+, Maven 3.8+.

From the project root, first build and install all modules into the local Maven repository:

```sh
mvn install
```

Then run the selected app module (substitute `app-a` or `app-ab` for another composition):

```sh
mvn -f app-abd/pom.xml exec:java
```

Do not run `mvn exec:java` from the root directory: the root POM only aggregates the modules and has no application entry point. Run the goal against one of the `app-*` POMs as shown above.

The app's dependencies form its plugin composition. The host finds the selected plugins through `ServiceLoader`; changing the dependencies in an app module changes the available dockable applications. The language selector broadcasts locale changes, and each plugin refreshes its UI from resources packaged in its own JAR.

## Parameterized translations

There is no single translation resource file for the whole application. The host owns its bundle, and each plugin owns a separate bundle inside its module. The host gives each plugin a **plugin-scoped `TranslationService`** during initialization. The plugin looks up short keys without knowing its bundle path:

```properties
greeting=Hello, {0}. How is {1} doing?
```

The localized bundles define the same key, with translated text and placeholders for the same values (the order may differ):

```properties
# Messages_fr.properties
greeting=Bonjour {0} ! Comment va {1} ?
```

In plugin code, request the message by key and pass placeholder values:

```java
String message = translations.text("greeting", userName, friendName);
```

The host binds that service to the plugin's bundle using the convention `com.example.dockingapp.plugin.<plugin-id>.Messages`, updates its locale when the user changes languages, and formats arguments with `MessageFormat` for that locale. `{0}` and `{1}` refer to the supplied values; localized bundles can reorder them. `MessageFormat` also supports formatted numbers, dates, and times. Plugins keep owning their resource files; the scoped service centralizes lookup and formatting without combining the files.

## Developing an independently owned plugin

Implement `com.example.dockingapp.plugin.Plugin`, provide a no-argument constructor, and register its class in `src/main/resources/META-INF/services/com.example.dockingapp.plugin.Plugin`. In `initialize`, store the provided `TranslationService`; use it for all plugin-owned text and refresh the UI in `localeChanged`. Keep its `Messages.properties` and localized variants under `src/main/resources/com/example/dockingapp/plugin/<plugin-id>/`. Add its artifact as a dependency of each app that should include it.
