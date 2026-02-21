package com.rafa.calcapple

import kotlin.math.*

sealed class Action {
  data class Digit(val d: Char) : Action()
  data class Op(val op: String) : Action()
  data class Unary(val fn: String) : Action()
  data object Dot : Action()
  data object Clear : Action()
  data object Sign : Action()
  data object Percent : Action()
  data object Eq : Action()
  data object Pow : Action()
  data object ConstPi : Action()
}

class CalcEngine {
  private var display = "0"
  private var sub = ""
  private var acc: Double? = null
  private var op: String? = null
  private var enteringNew = true
  private var powMode = false

  fun display() = display
  fun subDisplay() = sub

  fun press(a: Action) {
    when (a) {
      is Action.Digit -> digit(a.d)
      Action.Dot -> dot()
      Action.Clear -> clear()
      Action.Sign -> setValue(-current())
      Action.Percent -> setValue(current() / 100.0)
      is Action.Op -> setOperator(a.op)
      Action.Eq -> equal()
      Action.Pow -> pow()
      Action.ConstPi -> { setValue(Math.PI); enteringNew = true }
      is Action.Unary -> unary(a.fn)
    }
    if (a !is Action.Pow && a !is Action.Eq && a !is Action.Clear) updateSub()
  }

  private fun clear() {
    display = "0"
    sub = ""
    acc = null
    op = null
    enteringNew = true
    powMode = false
  }

  private fun digit(d: Char) {
    if (enteringNew) {
      display = d.toString()
      enteringNew = false
    } else {
      display = if (display == "0") d.toString() else (display + d).take(18)
    }
  }

  private fun dot() {
    if (enteringNew) {
      display = "0."
      enteringNew = false
      return
    }
    if (!display.contains(".")) display += "."
  }

  private fun current(): Double = display.replace(",", ".").toDoubleOrNull() ?: 0.0

  private fun setValue(v: Double) {
    display = format(v)
    enteringNew = true
  }

  private fun setOperator(newOp: String) {
    val cur = current()
    if (powMode) powMode = false

    if (op != null && !enteringNew) {
      val res = compute(acc ?: 0.0, op!!, cur)
      acc = res
      display = format(res)
    } else if (acc == null) {
      acc = cur
    }

    op = newOp
    enteringNew = true
    updateSub()
  }

  private fun equal() {
    val cur = current()

    if (powMode && acc != null) {
      val res = acc!!.pow(cur)
      display = format(if (res.isFinite()) res else 0.0)
      acc = null; op = null; powMode = false; enteringNew = true; sub = ""
      return
    }

    if (op == null) return
    val res = compute(acc ?: 0.0, op!!, cur)
    display = format(if (res.isFinite()) res else 0.0)
    acc = null
    op = null
    enteringNew = true
    sub = ""
  }

  private fun pow() {
    powMode = true
    acc = current()
    op = null
    enteringNew = true
    sub = "${format(acc!!)} ^"
  }

  private fun unary(fn: String) {
    val x = current()
    val res = when (fn) {
      "sq" -> x * x
      "sqrt" -> if (x < 0) Double.NaN else sqrt(x)
      "sin" -> sin(x)
      "cos" -> cos(x)
      "tan" -> tan(x)
      "ln" -> if (x <= 0) Double.NaN else ln(x)
      "log10" -> if (x <= 0) Double.NaN else log10(x)
      "fact" -> factorial(x.toInt())
      else -> Double.NaN
    }
    display = format(if (res.isFinite()) res else 0.0)
    enteringNew = true
  }

  private fun factorial(n: Int): Double {
    if (n < 0 || n > 170) return Double.NaN
    var r = 1.0
    for (i in 2..n) r *= i.toDouble()
    return r
  }

  private fun compute(a: Double, op: String, b: Double): Double = when (op) {
    "+" -> a + b
    "-" -> a - b
    "*" -> a * b
    "/" -> if (b == 0.0) 0.0 else a / b
    else -> b
  }

  private fun updateSub() {
    sub = if (acc != null && op != null) "${format(acc!!)} $op" else ""
  }

  private fun format(n: Double): String {
    if (!n.isFinite()) return "0"
    val absN = abs(n)
    val out = when {
      absN != 0.0 && (absN >= 1e10 || absN < 1e-7) -> "%,.6e".format(n).replace("+", "")
      else -> {
        var s = "%.10f".format(n).trimEnd('0').trimEnd('.')
        if (s == "-0") s = "0"
        if (s.length > 14) s = "%.10g".format(n).trimEnd('0').trimEnd('.')
        if (s.length > 14) s = s.take(14)
        s
      }
    }
    return out.replace(",", ".")
  }
}
