package hataru.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.hataru.R
import com.example.hataru.presentation.onboarding.screens.ThirdScreen
import com.google.android.material.bottomnavigation.BottomNavigationView

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val thirdScreen = supportFragmentManager.findFragmentById(R.id.thirdScreen) as? ThirdScreen

        thirdScreen?.getNavigateToMainActivity()?.observe(this, Observer { shouldNavigate ->
            if (shouldNavigate) {
                setNavigation()
            }
        })

        supportActionBar?.hide()
    }

    private fun setNavigation() {
        val navView: BottomNavigationView = findViewById(R.id.bottom_navigation_view)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        setupWithNavController(navView, navController)
    }
}