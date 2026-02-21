package com.rafa.calcapple

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import com.rafa.calcapple.databinding.FragmentCurrencyBinding

class CurrencyFragment : Fragment(R.layout.fragment_currency) {

  private lateinit var b: FragmentCurrencyBinding
  private val prefs by lazy { requireContext().getSharedPreferences("fx", 0) }

  private fun getRate(code: String, def: String): Double {
    val s = prefs.getString("rate_$code", def) ?: def
    return s.replace(",", ".").toDoubleOrNull() ?: def.toDouble()
  }

  private fun setRate(code: String, v: String) {
    prefs.edit().putString("rate_$code", v).apply()
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    b = FragmentCurrencyBinding.bind(view)

    if (!prefs.contains("rate_BRL")) {
      setRate("BRL", "5.00")
      setRate("USD", "1.00")
      setRate("EUR", "0.92")
      setRate("GBP", "0.79")
      setRate("ARS", "850.0")
      setRate("PYG", "7300.0")
    }

    b.rBrl.setText(prefs.getString("rate_BRL","5.00"))
    b.rUsd.setText(prefs.getString("rate_USD","1.00"))
    b.rEur.setText(prefs.getString("rate_EUR","0.92"))
    b.rGbp.setText(prefs.getString("rate_GBP","0.79"))
    b.rArs.setText(prefs.getString("rate_ARS","850.0"))
    b.rPyg.setText(prefs.getString("rate_PYG","7300.0"))

    val watcher = object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
      override fun afterTextChanged(s: Editable?) { compute() }
    }

    listOf(b.amount, b.from, b.to, b.rBrl, b.rUsd, b.rEur, b.rGbp, b.rArs, b.rPyg).forEach {
      it.addTextChangedListener(watcher)
    }

    compute()
  }

  private fun compute() {
    setRate("BRL", b.rBrl.text.toString().sanitizeNum())
    setRate("USD", b.rUsd.text.toString().sanitizeNum())
    setRate("EUR", b.rEur.text.toString().sanitizeNum())
    setRate("GBP", b.rGbp.text.toString().sanitizeNum())
    setRate("ARS", b.rArs.text.toString().sanitizeNum())
    setRate("PYG", b.rPyg.text.toString().sanitizeNum())

    val amount = b.amount.text.toString().sanitizeNum().toDoubleOrNull() ?: run {
      b.result.text = "—"
      return
    }

    val from = b.from.text.toString().trim().uppercase().ifBlank { "USD" }
    val to = b.to.text.toString().trim().uppercase().ifBlank { "BRL" }

    val rFrom = getRate(from, if (from=="USD") "1.00" else "1.00")
    val rTo = getRate(to, if (to=="USD") "1.00" else "1.00")
    if (rFrom == 0.0) { b.result.text = "—"; return }

    val usd = amount / rFrom
    val out = usd * rTo
    b.result.text = "${out.format()} $to"
  }

  private fun String.sanitizeNum(): String =
    replace(Regex("[^0-9.,]"), "").replace(",", ".")

  private fun Double.format(): String =
    if (!isFinite()) "0" else String.format("%.6f", this).trimEnd('0').trimEnd('.')
}
