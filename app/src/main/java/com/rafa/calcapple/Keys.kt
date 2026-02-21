package com.rafa.calcapple

enum class Kind { NUM, FN, OP }

data class Key(val label: String, val kind: Kind, val action: Action, val wide: Boolean = false)

object Keys {

  val stdRows: List<List<Key>> = listOf(
    listOf(
      Key("AC", Kind.FN, Action.Clear),
      Key("+/−", Kind.FN, Action.Sign),
      Key("%", Kind.FN, Action.Percent),
      Key("÷", Kind.OP, Action.Op("/")),
    ),
    listOf(
      Key("7", Kind.NUM, Action.Digit('7')),
      Key("8", Kind.NUM, Action.Digit('8')),
      Key("9", Kind.NUM, Action.Digit('9')),
      Key("×", Kind.OP, Action.Op("*")),
    ),
    listOf(
      Key("4", Kind.NUM, Action.Digit('4')),
      Key("5", Kind.NUM, Action.Digit('5')),
      Key("6", Kind.NUM, Action.Digit('6')),
      Key("−", Kind.OP, Action.Op("-")),
    ),
    listOf(
      Key("1", Kind.NUM, Action.Digit('1')),
      Key("2", Kind.NUM, Action.Digit('2')),
      Key("3", Kind.NUM, Action.Digit('3')),
      Key("+", Kind.OP, Action.Op("+")),
    ),
    listOf(
      Key("0", Kind.NUM, Action.Digit('0'), wide = true),
      Key(".", Kind.NUM, Action.Dot),
      Key("=", Kind.OP, Action.Eq),
    )
  )

  private val sciHeader: List<List<Key>> = listOf(
    listOf(
      Key("AC", Kind.FN, Action.Clear),
      Key("π", Kind.FN, Action.ConstPi),
      Key("x²", Kind.FN, Action.Unary("sq")),
      Key("√", Kind.FN, Action.Unary("sqrt")),
    ),
    listOf(
      Key("sin", Kind.FN, Action.Unary("sin")),
      Key("cos", Kind.FN, Action.Unary("cos")),
      Key("tan", Kind.FN, Action.Unary("tan")),
      Key("^", Kind.OP, Action.Pow),
    ),
    listOf(
      Key("ln", Kind.FN, Action.Unary("ln")),
      Key("log", Kind.FN, Action.Unary("log10")),
      Key("n!", Kind.FN, Action.Unary("fact")),
      Key("÷", Kind.OP, Action.Op("/")),
    ),
  )

  val sciRows: List<List<Key>> = sciHeader + stdRows.drop(1)
}
