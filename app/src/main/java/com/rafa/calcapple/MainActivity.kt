package com.rafa.calcapple

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.rafa.calcapple.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

  private lateinit var b: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    b = ActivityMainBinding.inflate(layoutInflater)
    setContentView(b.root)

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
}
