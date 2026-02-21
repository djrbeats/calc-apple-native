package com.rafa.calcapple

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.rafa.calcapple.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

  private lateinit var b: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // AQUI é o pulo do gato: a gente controla os insets (status bar) na mão
    WindowCompat.setDecorFitsSystemWindows(window, false)

    b = ActivityMainBinding.inflate(layoutInflater)
    setContentView(b.root)

    // aplica padding do topo (status bar) no header e evita layout “subir”
    ViewCompat.setOnApplyWindowInsetsListener(b.root) { _, insets ->
      val sysBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
      // header desce certinho
      b.header.setPadding(
        b.header.paddingLeft,
        sysBars.top + dp(10),
        b.header.paddingRight,
        b.header.paddingBottom
      )

      // dá um respiro no fundo pra não colar na navigation bar
      val lp = b.container.layoutParams as ViewGroup.MarginLayoutParams
      lp.bottomMargin = sysBars.bottom
      b.container.layoutParams = lp

      insets
    }

    b.btnMenu.setOnClickListener { b.drawer.openDrawer(GravityCompat.START) }

    b.navView.setNavigationItemSelectedListener { item ->
      b.drawer.closeDrawer(GravityCompat.START)
      when (item.itemId) {
        R.id.nav_calc -> {
          b.title.text = "Calculadora"
          supportFragmentManager.beginTransaction()
            .replace(R.id.container, CalculatorFragment.newInstance(scientific = false))
            .commit()
          true
        }
        R.id.nav_sci -> {
          b.title.text = "Científica"
          supportFragmentManager.beginTransaction()
            .replace(R.id.container, CalculatorFragment.newInstance(scientific = true))
            .commit()
          true
        }
        R.id.nav_fx -> {
          b.title.text = "Moedas"
          supportFragmentManager.beginTransaction()
            .replace(R.id.container, CurrencyFragment())
            .commit()
          true
        }
        else -> false
      }
    }

    if (savedInstanceState == null) {
      b.navView.setCheckedItem(R.id.nav_calc)
      supportFragmentManager.beginTransaction()
        .replace(R.id.container, CalculatorFragment.newInstance(scientific = false))
        .commit()
    }
  }

  private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()
}
