package io.github.sds100.keymapper

import android.content.Context
import android.util.Log
import io.github.sds100.keymapper.data.entities.ActionEntity
import io.github.sds100.keymapper.data.entities.KeyEventTriggerKeyEntity
import io.github.sds100.keymapper.data.entities.KeyMapEntity
import io.github.sds100.keymapper.data.entities.TriggerEntity
import io.github.sds100.keymapper.data.repositories.KeyMapRepository
import java.util.UUID

/**
 * Seeds default braille keyboard shortcuts for the Braille Mapper app.
 * Optimized for 7-key braille keyboard (6 dots + function key).
 * Function key sends Ctrl, braille patterns send letters A-Z.
 */
class BrailleKeyMapSeeder(
    private val context: Context,
    private val keyMapRepository: KeyMapRepository,
) {
    companion object {
        private const val TAG = "BrailleSeeder"
        
        // Android KeyEvent keycodes
        private const val KEYCODE_CTRL_LEFT = 113
        private const val KEYCODE_A = 29
        private const val KEYCODE_B = 30
        private const val KEYCODE_C = 31
        private const val KEYCODE_D = 32
        private const val KEYCODE_E = 33
        private const val KEYCODE_F = 34
        private const val KEYCODE_G = 35
        private const val KEYCODE_H = 36
        private const val KEYCODE_I = 37
        private const val KEYCODE_J = 38
        private const val KEYCODE_K = 39
        private const val KEYCODE_L = 40
        private const val KEYCODE_M = 41
        private const val KEYCODE_N = 42
        private const val KEYCODE_O = 43
        private const val KEYCODE_P = 44
        private const val KEYCODE_Q = 45
        private const val KEYCODE_R = 46
        private const val KEYCODE_S = 47
        private const val KEYCODE_T = 48
        private const val KEYCODE_U = 49
        private const val KEYCODE_V = 50
        private const val KEYCODE_W = 51
        private const val KEYCODE_X = 52
        private const val KEYCODE_Y = 53
        private const val KEYCODE_Z = 54
        
        // App package names
        private const val PKG_WHATSAPP = "com.whatsapp"
        private const val PKG_PHONE = "com.android.dialer"
        private const val PKG_CONTACTS = "com.android.contacts"
        private const val PKG_MESSAGES = "com.google.android.apps.messaging"
        private const val PKG_NOTES = "com.google.android.keep"
        private const val PKG_CALENDAR = "com.google.android.calendar"
        private const val PKG_CLOCK = "com.google.android.deskclock"
        private const val PKG_CALCULATOR = "com.google.android.calculator"
        private const val PKG_MAPS = "com.google.android.apps.maps"
        private const val PKG_YOUTUBE = "com.google.android.youtube"
        private const val PKG_CHROME = "com.android.chrome"
        private const val PKG_GMAIL = "com.google.android.gm"
    }

    fun seedDefaults(): Int {
        return try {
            val keyMaps = createDefaultKeyMaps()
            if (keyMaps.isNotEmpty()) {
                keyMapRepository.insert(*keyMaps.toTypedArray())
                Log.i(TAG, "Seeded ${keyMaps.size} keymaps")
            }
            keyMaps.size
        } catch (e: Exception) {
            Log.e(TAG, "Seed failed", e)
            0
        }
    }

    private fun createDefaultKeyMaps(): List<KeyMapEntity> {
        return listOf(
            // ===== APPS (Most used - easy letters) =====
            createAppKeyMap(KEYCODE_W, PKG_WHATSAPP),      // W = WhatsApp
            createAppKeyMap(KEYCODE_P, PKG_PHONE),         // P = Phone/Dialer
            createAppKeyMap(KEYCODE_C, PKG_CONTACTS),      // C = Contacts
            createAppKeyMap(KEYCODE_M, PKG_MESSAGES),      // M = Messages/SMS
            createAppKeyMap(KEYCODE_N, PKG_NOTES),         // N = Notes (Google Keep)
            createAppKeyMap(KEYCODE_K, PKG_CALENDAR),      // K = Calendar (Kalendar)
            createAppKeyMap(KEYCODE_T, PKG_CLOCK),         // T = Time/Clock
            createAppKeyMap(KEYCODE_X, PKG_CALCULATOR),    // X = Calculator
            createAppKeyMap(KEYCODE_L, PKG_MAPS),          // L = Location/Maps
            createAppKeyMap(KEYCODE_Y, PKG_YOUTUBE),       // Y = YouTube
            createAppKeyMap(KEYCODE_I, PKG_CHROME),        // I = Internet/Chrome
            createAppKeyMap(KEYCODE_E, PKG_GMAIL),         // E = Email/Gmail
            
            // ===== PHONE ACTIONS =====
            createSystemKeyMap(KEYCODE_A, "answer_phone_call"),  // A = Answer
            createSystemKeyMap(KEYCODE_D, "end_phone_call"),     // D = Decline/End
            
            // ===== NAVIGATION =====
            createSystemKeyMap(KEYCODE_H, "go_home"),            // H = Home
            createSystemKeyMap(KEYCODE_B, "go_back"),            // B = Back
            createSystemKeyMap(KEYCODE_R, "open_recents"),       // R = Recents
            createSystemKeyMap(KEYCODE_J, "go_last_app"),        // J = Jump to last app
            
            // ===== SYSTEM =====
            createSystemKeyMap(KEYCODE_V, "open_assistant"),     // V = Voice assistant
            createSystemKeyMap(KEYCODE_O, "expand_notification_drawer"), // O = Open notifications
            createSystemKeyMap(KEYCODE_Q, "expand_quick_settings"),      // Q = Quick settings
            createSystemKeyMap(KEYCODE_S, "open_settings"),      // S = Settings
            createSystemKeyMap(KEYCODE_Z, "lock_device"),        // Z = Lock (sleep)
            
            // ===== UTILITIES =====
            createSystemKeyMap(KEYCODE_F, "toggle_flashlight"),  // F = Flashlight
            createSystemKeyMap(KEYCODE_U, "open_camera"),        // U = Camera (capture)
            createSystemKeyMap(KEYCODE_G, "screenshot"),         // G = Grab screenshot
        )
    }

    private fun createSystemKeyMap(letterKeyCode: Int, systemAction: String): KeyMapEntity {
        val trigger = TriggerEntity(
            keys = listOf(
                KeyEventTriggerKeyEntity(
                    keyCode = KEYCODE_CTRL_LEFT,
                    deviceId = KeyEventTriggerKeyEntity.DEVICE_ID_ANY_DEVICE,
                ),
                KeyEventTriggerKeyEntity(
                    keyCode = letterKeyCode,
                    deviceId = KeyEventTriggerKeyEntity.DEVICE_ID_ANY_DEVICE,
                ),
            ),
            mode = TriggerEntity.PARALLEL,
        )

        val action = ActionEntity(
            type = ActionEntity.Type.SYSTEM_ACTION,
            data = systemAction,
        )

        return KeyMapEntity(
            id = 0,
            trigger = trigger,
            actionList = listOf(action),
            isEnabled = true,
            uid = UUID.randomUUID().toString(),
        )
    }

    private fun createAppKeyMap(letterKeyCode: Int, packageName: String): KeyMapEntity {
        val trigger = TriggerEntity(
            keys = listOf(
                KeyEventTriggerKeyEntity(
                    keyCode = KEYCODE_CTRL_LEFT,
                    deviceId = KeyEventTriggerKeyEntity.DEVICE_ID_ANY_DEVICE,
                ),
                KeyEventTriggerKeyEntity(
                    keyCode = letterKeyCode,
                    deviceId = KeyEventTriggerKeyEntity.DEVICE_ID_ANY_DEVICE,
                ),
            ),
            mode = TriggerEntity.PARALLEL,
        )

        val action = ActionEntity(
            type = ActionEntity.Type.APP,
            data = packageName,
        )

        return KeyMapEntity(
            id = 0,
            trigger = trigger,
            actionList = listOf(action),
            isEnabled = true,
            uid = UUID.randomUUID().toString(),
        )
    }
}
