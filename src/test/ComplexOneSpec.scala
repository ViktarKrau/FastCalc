package test

import fastcalc.ComplexNumber

class ComplexOneSpec extends ComplexNumberSpec {
  val TEST_COUNT = 100
  val complexOne = new ComplexNumber(1.0, 0.0)
  private def getComplexOne() = complexOne

  "A one ComplexNumber" should "return b if it is multiplied by b" in {
    assertEqualComplex(TEST_COUNT, getComplexOne, (a, b) => b, generateNonZeroComplex, (a, b) => a * b)
  }
}
