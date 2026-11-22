# MenuDownloader — Fabric 1.21.11

Client-side Fabric mod targeting Minecraft 1.21.11 and Java 21.

## Build
1. Install JDK 21.
2. Run `gradle build` (or use the included Gradle wrapper if you add one).
3. Copy `build/libs/MenuDownloader-1.0.0.jar` into `.minecraft/mods`.

## Commands
- `.panel` — open the purple control panel.
- `.menu download` — export contents of an open vanilla generic container to `mods/MenuDownloader/*.yml`.
- `.tab download` — export the player list and display names visible to the client to `mods/TAB/client-visible.yml`.
- `.holo download` — export nearby text-display and named armor-stand text to `mods/Holograms/holo_1.yml`.

## Scope and limitations
This mod exports only data already sent to and visible on the client. It does not retrieve server-side plugin configuration, permissions, hidden player metadata, server files, or unloaded world chunks. The TAB export is not the original TAB plugin configuration. The menu export is a neutral YAML snapshot, not a guaranteed DeluxeMenus/BMenu configuration. The hologram export is a text snapshot and cannot recover original plugin metadata or exact formatting components.

Server resource-pack download is intentionally not included in this starter implementation: Fabric's accepted-pack storage and server pack lifecycle require a dedicated implementation and must respect server/resource-pack access rules.

The command names are registered as client-side Brigadier commands. Exact command parsing behavior can vary with other client mods that also claim dot-prefixed commands.
