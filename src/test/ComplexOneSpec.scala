package test

import fastcalc.ComplexNumber

class ComplexOneSpec extends ComplexNumberSpec {
  val TEST_COUNT = 100
  val complexOne = new ComplexNumber(1.0, 0.0)
  val complexMinusOne = new ComplexNumber(-1.0, 0.0)

  "A one ComplexNumber" should "return b if it is multiplied by b" in {
    assertEqualComplex(TEST_COUNT, () => complexOne, (a, b) => b, generateNonZeroComplex, (a, b) => a * b)
  }

  "A ComplexNumber" should "return (-getReal, -getImag) when multiplied by -1" in {
    assertEqualComplex(TEST_COUNT, () => complexMinusOne, (a, b) => new ComplexNumber(-b.getReal, -b.getImaginary),
      generateNonZeroComplex, (a, b) => a * b)
  }

}
