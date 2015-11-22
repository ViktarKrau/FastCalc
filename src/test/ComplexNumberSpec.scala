package test

import fastcalc.ComplexNumber
import org.scalatest.FlatSpec

class ComplexNumberSpec extends FlatSpec{
  "A ComplexNumber" should "return zero if it is zero and is divided by any other number" in {
    val complexZero = new ComplexNumber()
    assert(complexZero / new ComplexNumber(1, 2) === complexZero)
  }

}
