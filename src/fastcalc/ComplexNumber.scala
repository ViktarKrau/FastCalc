package fastcalc

import org.scalactic._
import TripleEquals._
import Tolerance._

object ComplexNumber {
  private def calcRadix(real : Double, imaginary : Double) =
    math.sqrt(real * real + imaginary * imaginary)
  private def calcExponent(real : Double, imaginary : Double) =
    if(real == 0.0 || imaginary == 0.0) 0.0
    else math.atan(imaginary / real)
  private def calcReal(radix : Double, exponent : Double) =
    radix * math.cos(exponent)
  private def calcImaginary(radix : Double, exponent : Double) =
    radix * math.sin(exponent)
  private def equalNumbers(a : Double, b : Double) =
    a === b +- .0005
  private def equalComplex(a : ComplexNumber, b : ComplexNumber) = {
    equalNumbers(a.getReal, b.getReal) &&
      equalNumbers(a.getImaginary, b.getImaginary) &&
      equalNumbers(a.getRadix, b.getRadix) &&
      equalNumbers(a.getExponent, b.getExponent)
  }
}

class ComplexNumber(real : Double, imaginary : Double, radix : Double, exponent : Double) {
  def this(real: Double, imaginary: Double) =
    this(real, imaginary, ComplexNumber.calcRadix(real, imaginary), ComplexNumber.calcExponent(real, imaginary))
  def this() = this(0, 0)

  def getReal = real
  def getImaginary = imaginary
  def getRadix = radix
  def getExponent = exponent

  def +(other : ComplexNumber) =
    new ComplexNumber(real + other.getReal, imaginary + other.getImaginary)
  def -(other : ComplexNumber) =
    new ComplexNumber(real - other.getReal, imaginary - other.getImaginary)
  def *(other : ComplexNumber) = {
    val newradix = radix * other.getRadix
    makeComplexFromRadixAndExp(newradix, if (newradix != 0.0) other.getExponent + exponent else 0.0)
  }
  def /(other : ComplexNumber) = {
    if (other.getReal == 0.0) throw new ArithmeticException()
    val newradix = radix / other.getRadix
    makeComplexFromRadixAndExp(newradix, if (newradix != 0.0) exponent - other.getExponent else 0.0)
  }

  override def toString = super.toString + " with values: "  +
    real.toString + " " + imaginary.toString + " " + radix.toString + " " + exponent.toString

  def ===(other : ComplexNumber) =
    ComplexNumber.equalComplex(this, other)

  def setReal(value : Double) =
    new ComplexNumber(value, imaginary)
  def setImaginary(value : Double) =
    new ComplexNumber(real, value)
  def setRadix(value: Double) =
    makeComplexFromRadixAndExp(value, exponent)
  def setExponent(value : Double) =
    makeComplexFromRadixAndExp(radix, value)

  private def makeComplexFromRadixAndExp(radix : Double, exponent : Double) = {
    val real = ComplexNumber.calcReal(radix, exponent)
    val imaginary = ComplexNumber.calcImaginary(radix, exponent)
    new ComplexNumber(real, imaginary, radix, exponent)
  }
}
