# Minecraft Alpha DevTools
Tools for developing mods for `Minecraft Alpha 1.1.2_01`.
The project is a __work-in-progress__

You need python installed to run the setup scripts!

Run `setup.bat` followed by `eclipse.bat` to set up a workspace.

### Features:
 - Setup script and Eclipse workspace script
 - Game Launcher
 - Mod Loader
 - Mixin system
 - Event system
 - API
 - Example Mod
 - Per-World Id Remapping (Modded Tiles (i.e. blocks) only at the moment).
   - This means that changes to what modded tiles are in the game won't affect the tiles in saves, as long as the modded tiles are registered to the remapper with `Tiles.register`.
   - However, new blocks that were not present in a save may fill the id gap of removed modded blocks, since said blocks no longer exist in the remapper registry.
