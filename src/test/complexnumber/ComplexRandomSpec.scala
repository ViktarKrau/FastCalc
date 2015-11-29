package test.complexnumber

import fastcalc.ComplexNumber

class ComplexRandomSpec extends ComplexNumberSpec {
  val TEST_COUNT = 1000

  private def randomTest(expected : (ComplexNumber, ComplexNumber) => ComplexNumber,
                          tested : (ComplexNumber, ComplexNumber) => ComplexNumber) = {
    assertEqualComplexResult(TEST_COUNT, generateComplex, expected, generateComplex, tested)
  }

  "A ComplexNumber (x + iy)" should "return a complex number (" +
    "x + u, yi + vi) after adding another complex number (u, v)" in {
    randomTest((a, b) => new ComplexNumber(a.getReal + b.getReal, a.getImaginary + b.getImaginary), (a, b) => a + b)
  }

  it should "return a complex number (x - u, yi - vi) after subtracting another complex number (u, v) from it" in {
    randomTest((a, b) => new ComplexNumber(a.getReal - b.getReal, a.getImaginary - b.getImaginary), (a, b) => a - b)
  }

  it should "have radix equal to sqrt of sum of squares of real and imaginary parts" in {
    complexSelfRadixTest(TEST_COUNT, generateNonZeroComplex,
      (a) => math.sqrt(a.getReal * a.getReal + a.getImaginary * a.getImaginary))
  }

  it should "have exponent equal to atan imag/real" in {
    complexSelfExponentTest(TEST_COUNT, generateNonZeroComplex, (a) => math.atan(a.getImaginary / a.getReal))
  }

  it should "have real equal to rad * cos(e)" in {
    complexSelfTest(TEST_COUNT, generateNonZeroComplex, (a) => a.getReal, (a) => a.getRadix * math.cos(a.getExponent))
  }

  it should "have imaginary part equal to rad * sin(e)" in {
    complexSelfTest(TEST_COUNT, generateNonZeroComplex, (a) => a.getImaginary,
      (a) => a.getRadix * math.sin(a.getExponent))
  }

  it should "when multiplied by b return complex number, radix of which is equal to it\' radix * b\'s radix" in {
    assertEqualDoubleResult(TEST_COUNT, generateNonZeroComplex, (a, b) => (a * b).getRadix, generateNonZeroComplex,
      (a, b) => a.getRadix * b.getRadix)
  }

}