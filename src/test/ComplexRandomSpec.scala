package test

import fastcalc.ComplexNumber

class ComplexRandomSpec extends ComplexNumberSpec {
  val TEST_COUNT = 1000

  private def randomTest(expected : (ComplexNumber, ComplexNumber) => ComplexNumber,
                          tested : (ComplexNumber, ComplexNumber) => ComplexNumber) = {
    assertEqualComplex(TEST_COUNT, generateComplex, expected, generateComplex, tested)
  }

  "A ComplexNumber (x + iy)" should "return a complex number (" +
    "x + u, yi + vi) after adding another complex number (u, v)" in {
    randomTest((a, b) => new ComplexNumber(a.getReal + b.getReal, a.getImaginary + b.getImaginary), (a, b) => a + b)
  }

  it should "return a complex number (x - u, yi - vi) after subtracting another complex number (u, v) from it" in {
    randomTest((a, b) => new ComplexNumber(a.getReal - b.getReal, a.getImaginary - b.getImaginary), (a, b) => a - b)
  }

  it should "have radix equal to sqrt of sum of squares of real and imaginary parts" in {
    complexSelfRadixTest(100, generateNonZeroComplex,
      (a) => math.sqrt(a.getReal * a.getReal + a.getImaginary * a.getImaginary))
  }

  it should "have exponent equal to atan imag/real" in {
    complexSelfExponentTest(100, generateNonZeroComplex, (a) => math.atan(a.getImaginary / a.getReal))
  }


}