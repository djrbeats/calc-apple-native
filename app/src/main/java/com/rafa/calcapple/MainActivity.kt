package com.rafa.calcapple

import android.os.Bundle
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

    // A chave: desenha “edge-to-edge”, mas a gente aplica os insets corretamente no header/rodapé
    WindowCompat.setDecorFitsSystemWindows(window, false)

    b = ActivityMainBinding.inflate(layoutInflater)
    setContentView(b.root)

    // Ajusta topo (status bar) e fundo (navigation bar) DE VERDADE
    ViewCompat.setOnApplyWindowInsetsListener(b.root) { _, insets ->
      val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())

      // desce o header abaixo da status bar
      b.header.setPadding(
        b.header.paddingLeft,
        sys.top,
        b.header.paddingRight,
        b.header.paddingBottom
      )

      // garante que o conteúdo não cole na barra de navegação
      b.container.setPadding(
        b.container.paddingLeft,
        b.container.paddingTop,
        b.container.paddingRight,
        sys.bottom
      )

      insets
    }

    b.btnMenu.setOnClickListener {
      b.drawer.openDrawer(GravityCompat.START)
    }

    b.navView.setNavigationItemSelectedListener { item ->
      b.drawer.closeDrawer(GravityCompat.START)
      when (item.itemId) {
        R.id.nav_calc -> {
          b.title.text = getString(R.string.title_calc)
          supportFragmentManager.beginTransaction()
            .replace(R.id.container, CalculatorFragment.newInstance(scientific = false))
            .commit()
          true
        }
        R.id.nav_sci -> {
          b.title.text = getString(R.string.title_sci)
          supportFragmentManager.beginTransaction()
            .replace(R.id.container, CalculatorFragment.newInstance(scientific = true))
            .commit()
          true
        }
        R.id.nav_fx -> {
          b.title.text = getString(R.string.title_fx)
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
      b.title.text = getString(R.string.title_calc)
      supportFragmentManager.beginTransaction()
        .replace(R.id.container, CalculatorFragment.newInstance(scientific = false))
        .commit()
    }
  }
}
