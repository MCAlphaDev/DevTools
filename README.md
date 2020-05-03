# Minecraft Alpha DevTools
Tools for developing mods for `Minecraft Alpha 1.1.2_01`.
The project is a __work-in-progress__

You need python installed to run the setup scripts!

Run `setup.bat` followed by `eclipse.bat` to set up a workspace.

### Features:
 - Setup script and Eclipse workspace script
 - Game Launcher
 - Mod Loader
 - Mixin
 - Event system
 - API
 - Example Mod
 - Per-World Id Remapping (Modded Items and Tiles).
   - This means that changes to what modded content is in the game won't affect the instances of content in saves, as long as the modded content is registered to the remapper with `Tiles.register` or `Items.register`, depending on the content type.
   - However, new content that were not present in a save may fill the id gap of removed modded content, since said content no longer exists in the remapper registry.
