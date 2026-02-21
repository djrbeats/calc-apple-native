package com.rafa.calcapple

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.rafa.calcapple.databinding.FragmentCalculatorBinding

class CalculatorFragment : Fragment(R.layout.fragment_calculator) {

  private var scientific: Boolean = false
  private lateinit var b: FragmentCalculatorBinding
  private val engine = CalcEngine()

  companion object {
    fun newInstance(scientific: Boolean): CalculatorFragment {
      val f = CalculatorFragment()
      f.arguments = Bundle().apply { putBoolean("scientific", scientific) }
      return f
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    b = FragmentCalculatorBinding.bind(view)
    scientific = arguments?.getBoolean("scientific") ?: false

    render()
    buildPad()
  }

  private fun render() {
    b.display.text = engine.display()
    b.subDisplay.text = engine.subDisplay().ifBlank { " " }
  }

  private fun buildPad() {
    b.pad.removeAllViews()

    val rows = if (scientific) Keys.sciRows else Keys.stdRows

    rows.forEach { row ->
      val rowLayout = LinearLayout(requireContext()).apply {
        orientation = LinearLayout.HORIZONTAL
        layoutParams = LinearLayout.LayoutParams(
          ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply { topMargin = dp(12) }
      }

      row.forEachIndexed { index, key ->
        val btn = MaterialButton(requireContext()).apply {
          text = key.label
          setTextColor(resources.getColor(R.color.text, null))
          textSize = if (key.label.length > 2) 18f else 22f
          typeface = Typeface.DEFAULT_BOLD

          layoutParams = LinearLayout.LayoutParams(0, dp(72), if (key.wide) 2f else 1f).apply {
            marginEnd = if (index == row.size - 1) 0 else dp(12)
          }

          cornerRadius = dp(999)
          gravity = Gravity.CENTER

          val bg = when (key.kind) {
            Kind.OP -> R.color.orange
            Kind.FN -> R.color.lightKey
            Kind.NUM -> R.color.darkKey
          }
          setBackgroundColor(resources.getColor(bg, null))

          setOnClickListener {
            engine.press(key.action)
            render()
          }
        }
        rowLayout.addView(btn)
      }

      b.pad.addView(rowLayout)
    }
  }

  private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()
}
