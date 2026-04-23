# Cyclic

> [!WARNING]  
> **Disclaimer:** This NeoForge 1.21.1 port was developed and debugged with the assistance of an AI agent. While critical crashes have been fixed and basic functionality verified, it has **not** been thoroughly tested across all edge cases. Proceed with caution on production servers.

Minecraft mod written in Java.

## ⚠️ Requirements for 1.21.1 Port
This branch has been specifically updated, refactored, and stabilized for **NeoForge 1.21.1**. 

To run this version of Cyclic, you **MUST** install the corresponding updated version of the `flib` library.

* **Minecraft Version:** `1.21.1` *(Note: This port will **NOT** work on `1.21.2+` or `1.21.4` due to major core API and Data Component changes introduced by Mojang in newer versions)*
* **NeoForge Version:** `21.1.115` or newer
* **Required Library:** [`flib` (Lothrazar/flib)](https://github.com/Lothrazar/flib) - Ensure you are using the 1.21.1 compatible build (e.g., `v0.0.12` or newer).
* **Optional API:** Curios API (for accessory support like the Climbing Glove).

## Porting Notes
For detailed technical information on the bug fixes, networking `StreamCodec` updates, fluid rendering fixes, and registry refactoring implemented in this NeoForge 1.21.1 port, please see [porting_summary.md](porting_summary.md).
