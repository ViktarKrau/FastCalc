package test

import fastcalc.ComplexNumber
import org.scalatest.FlatSpec

import scala.annotation.tailrec
import scala.util.Random

class ComplexNumberSpec extends FlatSpec {
  val MAX_TEST_COUNT = 100
  val random = new Random()
  @tailrec
  private def generateNonZeroRandomDouble() : Double = {
    val result = random.nextDouble()
    if (result == 0.0) generateNonZeroRandomDouble()
    else result
  }

  @tailrec
  private def assertEqualComplex(iteration : Int, valueGenerator : () => ComplexNumber,
                                 resultGenerator : (ComplexNumber, ComplexNumber) => ComplexNumber,
                                 otherGenerator : () => ComplexNumber,
                                 function : (ComplexNumber, ComplexNumber) => ComplexNumber) : Unit = {
    if (iteration != 0) {
      val a = valueGenerator()
      val b = otherGenerator()
      assert(function(a, b) === resultGenerator(a, b))
      assertEqualComplex(iteration - 1, valueGenerator, resultGenerator, otherGenerator, function)
    }
  }

  val complexZero = new ComplexNumber()
  private def getComplexZero() = complexZero
  private def getComplexZero(a : ComplexNumber, b : ComplexNumber) = complexZero
  private def generateNonZeroComplex() = new ComplexNumber(generateNonZeroRandomDouble(), generateNonZeroRandomDouble())
  private def generateComplex() = new ComplexNumber(random.nextDouble(), random.nextDouble())

  private def testComplexZero(resultGenerator : (ComplexNumber, ComplexNumber) => ComplexNumber,
                              otherGenerator : () => ComplexNumber,
                              function : (ComplexNumber, ComplexNumber) => ComplexNumber) =
    assertEqualComplex(MAX_TEST_COUNT, getComplexZero, resultGenerator, otherGenerator, function)

  "A zero ComplexNumber" should "return zero if it is is divided by any other non-zero complex number" in {
    testComplexZero(getComplexZero, generateNonZeroComplex, (a, b) => a / b)
  }

  it should "return zero if it is multiplied by any other non-zero complex number" in {
    testComplexZero(getComplexZero, generateNonZeroComplex, (a, b) => a * b)
  }

  it should "return other number if it is added to other number" in {
    testComplexZero((a, b) => b, generateComplex, (a, b) => a + b)
  }

  it should "return other number if it is subtracted from other number" in {
    testComplexZero((a, b) => b, generateComplex, (a, b) => b - a)
  }

  it should "return complex number, which is equal to -b when b is subtracted from it" in {
    testComplexZero((a, b) => new ComplexNumber(-b.getReal, -b.getImaginary), generateComplex, (a, b) => a - b)
  }


}
