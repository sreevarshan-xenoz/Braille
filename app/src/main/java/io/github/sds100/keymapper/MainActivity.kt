package io.github.sds100.keymapper

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import io.github.sds100.keymapper.base.BaseMainActivity
import io.github.sds100.keymapper.base.R
import io.github.sds100.keymapper.base.databinding.ActivityMainBinding
import io.github.sds100.keymapper.base.utils.ui.DialogProvider
import io.github.sds100.keymapper.base.utils.ui.showDialogs
import io.github.sds100.keymapper.data.repositories.KeyMapRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseMainActivity() {

    @Inject
    lateinit var dialogProvider: DialogProvider

    @Inject
    lateinit var keyMapRepository: KeyMapRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        val navController = binding.container.getFragment<NavHostFragment>().navController
        val fragmentNavigator =
            navController.navigatorProvider.getNavigator(FragmentNavigator::class.java)

        val homeDest = fragmentNavigator.createDestination().apply {
            id = R.id.home_fragment
            setClassName(MainFragment::class.java.name)
        }

        navController.graph = navController.navInflater.inflate(R.navigation.nav_base_app).apply {
            addDestination(homeDest)
            setStartDestination(R.id.home_fragment)
        }

        dialogProvider.showDialogs(this, binding.coordinatorLayout)

        // Seed braille defaults
        seedBrailleDefaults()
    }

    private fun seedBrailleDefaults() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                val lastVersion = prefs.getInt(KEY_SEED_VERSION, 0)
                
                // Skip if already seeded with current version
                if (lastVersion >= SEED_VERSION) return@launch

                // Delete old keymaps if upgrading from previous seed version
                if (lastVersion > 0) {
                    val existing = keyMapRepository.getAll().first()
                    if (existing.isNotEmpty()) {
                        keyMapRepository.delete(*existing.map { it.uid }.toTypedArray())
                        Log.i(TAG, "Deleted ${existing.size} old keymaps for re-seed")
                    }
                }

                // Seed new defaults
                val seeder = BrailleKeyMapSeeder(this@MainActivity, keyMapRepository)
                val count = seeder.seedDefaults()
                
                if (count > 0) {
                    prefs.edit().putInt(KEY_SEED_VERSION, SEED_VERSION).apply()
                    Log.i(TAG, "Seeded $count shortcuts (v$SEED_VERSION)")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Seed failed", e)
            }
        }
    }

    companion object {
        private const val TAG = "BrailleMapper"
        private const val PREFS_NAME = "braille_mapper"
        private const val KEY_SEED_VERSION = "seed_version"
        // Increment this when shortcuts change to force re-seed
        private const val SEED_VERSION = 2
    }
}
