package edu.luc.cs.laufer.cs473.expressions

class ExtensibleOperations {

  def evaluate(e: Expr): Int = e match {
    case Constant(c) => c
    case Plus(l, r) => evaluate(l) + evaluate(r)
    case Minus(l, r) => evaluate(l) - evaluate(r)
    case Times(l, r) => evaluate(l) * evaluate(r)
    case Div(l, r) => evaluate(l) / evaluate(r)
  }

  def size(e: Expr): Int = e match {
    case Constant(c) => 1
    case CompositeExpr(l, r) => 1 + size(l) + size(r)
  }

  def depth(e: Expr): Int = e match {
    case Constant(c) => 1
    case CompositeExpr(l, r) => 1 + Math.max(depth(l), depth(r))
  }

  def toFormattedString(prefix: String)(e: Expr): String = e match {
    case Constant(c) => prefix + c.toString
    case Plus(l, r) => buildExprString(prefix, "Plus", toFormattedString(prefix + INDENT)(l), toFormattedString(prefix + INDENT)(r))
    case Minus(l, r) => buildExprString(prefix, "Minus", toFormattedString(prefix + INDENT)(l), toFormattedString(prefix + INDENT)(r))
    case Times(l, r) => buildExprString(prefix, "Times", toFormattedString(prefix + INDENT)(l), toFormattedString(prefix + INDENT)(r))
    case Div(l, r) => buildExprString(prefix, "Div", toFormattedString(prefix + INDENT)(l), toFormattedString(prefix + INDENT)(r))
  }

  def buildExprString(prefix: String, nodeString: String, leftString: String, rightString: String) = {
    val result = new StringBuilder(prefix)
    result.append(nodeString)
    result.append("(")
    result.append(EOL)
    result.append(leftString)
    result.append(", ")
    result.append(EOL)
    result.append(rightString)
    result.append(")")
    result.toString
  }

  val EOL = System.getProperty("line.separator")
  val INDENT = ".."
}


class ExtendedExtensibleOperations extends ExtensibleOperations {

  override def evaluate(e: Expr): Int = e match {
    case Mod(l, r) => evaluate(l) % evaluate(r)
    case UMinus(r) => -evaluate(r)
    case _ => super.evaluate(e)
  }

  override def size(e: Expr): Int = e match {
    case Mod(l, r) => 1 + size(l) + size(r)
    case UMinus(r) => 1 + size(r)
    case _ => super.size(e)
  }

  override def depth(e: Expr): Int = e match {
    case Mod(l, r) => 1 + Math.max(depth(l), depth(r))
    case UMinus(r) => 1 + depth(r)
    case _ => super.depth(e)
  }

  override def toFormattedString(prefix: String)(e: Expr): String = e match {
    case Mod(l, r) => buildExprString(prefix, "Mod", toFormattedString(prefix + INDENT)(l), toFormattedString(prefix + INDENT)(r))
    case UMinus(r) => buildUnaryExprString(prefix, "UMinus", toFormattedString(prefix + INDENT)(r))
    case _ => super.toFormattedString(prefix)(e)
  }

  def buildUnaryExprString(prefix: String, nodeString: String, exprString: String) = {
    val result = new StringBuilder(prefix)
    result.append(nodeString)
    result.append("(")
    result.append(EOL)
    result.append(exprString)
    result.append(")")
    result.toString
  }
}

object ExtendedOperations extends ExtendedExtensibleOperations {
  def toFormattedString(e: Expr): String = toFormattedString(">>")(e)
}