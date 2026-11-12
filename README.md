# Swing Docking Plugins

A small Maven desktop application demonstrating a Swing host with dockable plugin applications. The host owns the main `JFrame`, docking area, and language selector. Each plugin contributes a Swing application window and owns its own translation bundle.

## Requirements

- JDK 17 or newer
- Maven 3.8 or newer

## Run

```sh
mvn compile exec:java
```

The demo opens three dockable applications: Welcome, Counter, and Notes. Drag their tabs to rearrange or detach them. Use the language selector in the host toolbar to switch between English, French, and German; the host and every plugin update immediately.

## Adding a plugin

Implement `Plugin` (usually by extending `LocalizedPlugin`), create a `DefaultSingleCDockable` containing your Swing UI, and refresh all visible strings in `updateTexts()`. Add a plugin-owned `Messages.properties` bundle and localized variants next to the other plugin resources. Register the plugin in `DemoApplication`.

Plugins receive the shared `LocaleManager`, but own their translation keys and resources. The locale manager sends updates on Swing's event dispatch thread.
