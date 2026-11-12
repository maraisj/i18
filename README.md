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

Build and install all modules into the local Maven repository:

```sh
mvn install
```

Then run one composition (substitute `app-a` or `app-ab`):

```sh
mvn -f app-abd/pom.xml exec:java
```

The app's dependencies form its plugin composition. The host finds the selected plugins through `ServiceLoader`; changing the dependencies in an app module changes the available dockable applications. The language selector broadcasts locale changes, and each plugin refreshes its UI from resources packaged in its own JAR.

## Parameterized translations

There is no single translation resource for the whole application. The host owns its bundle, and each plugin owns a separate bundle inside its module. A plugin can use `MessageFormat` placeholders for dynamic values. For example, in that plugin's `Messages.properties`:

```properties
greeting=Hello, {0}. How is {1} doing?
```

The localized bundles define the same key, with translated text and placeholders for the same values (the order may differ):

```properties
# Messages_fr.properties
greeting=Bonjour {0} ! Comment va {1} ?
```

Format the message with the active locale:

```java
String pattern = ResourceBundle
        .getBundle("com.example.dockingapp.plugin.greeting.Messages", locale)
        .getString("greeting");
String message = new MessageFormat(pattern, locale)
        .format(new Object[] { userName, friendName });
```

`{0}` and `{1}` refer to arguments in the supplied array. `MessageFormat` also supports formatted numbers, dates, and times. Use `new MessageFormat(pattern, locale)` so formatting follows the selected application locale.

## Developing an independently owned plugin

Implement `com.example.dockingapp.plugin.Plugin`, provide a no-argument constructor, and register its class in `src/main/resources/META-INF/services/com.example.dockingapp.plugin.Plugin`. Keep its `Messages.properties` and localized variants under that plugin module's `src/main/resources`. Add its artifact as a dependency of each app that should include it.
