# Braille Launcher - Default Shortcuts Guide
## ESP32 Braille Keyboard Integration

Your ESP32 sends `Ctrl + Letter` when you hold the function button (Pin 12) + braille pattern.
These shortcuts are pre-installed when you first launch the app.

### All Default Shortcuts (Master + Braille Letter)

#### Phone Controls
| Braille | Letter | Action |
|---------|--------|--------|
| ⠁ | A | Answer incoming call |
| ⠙ | D | End/Decline call |

#### Navigation
| Braille | Letter | Action |
|---------|--------|--------|
| ⠛ | G | Go Home |
| ⠃ | B | Go Back |
| ⠗ | R | Recent Apps |
| ⠚ | J | Switch to Last App |

#### System
| Braille | Letter | Action |
|---------|--------|--------|
| ⠺ | W | Voice Assistant (Google) |
| ⠝ | N | Open Notifications |
| ⠟ | Q | Quick Settings |
| ⠎ | S | Open Settings |
| ⠇ | L | Lock Device |
| ⠑ | E | Collapse Status Bar |
| ⠞ | T | Take Screenshot |

#### Utilities
| Braille | Letter | Action |
|---------|--------|--------|
| ⠋ | F | Toggle Flashlight |
| ⠓ | H | Toggle Bluetooth |
| ⠊ | I | Toggle WiFi |
| ⠕ | O | Open Camera |
| ⠍ | M | Toggle Mute |
| ⠅ | K | Keyboard Picker |

#### Media Controls
| Braille | Letter | Action |
|---------|--------|--------|
| ⠏ | P | Play/Pause |
| ⠭ | X | Next Track |
| ⠵ | Z | Previous Track |

#### Text/Clipboard
| Braille | Letter | Action |
|---------|--------|--------|
| ⠽ | Y | Copy |
| ⠧ | V | Paste |
| ⠥ | U | Dismiss Notification |

### Building the App

```bash
# Debug build
./gradlew assembleDebug

# Release build (requires signing key)
./gradlew assembleRelease
```

The APK will be at: `app/build/outputs/apk/debug/braille-launcher-*.apk`

### What Changed from Key Mapper
- App name: "Braille Launcher"
- Package ID: `io.github.cybersentinals.braillelauncher`
- 25 default shortcuts pre-configured for blind/visually impaired users
- Shortcuts use Ctrl+Letter combos from your ESP32 braille keyboard
