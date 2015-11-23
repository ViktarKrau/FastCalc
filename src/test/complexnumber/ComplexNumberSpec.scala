package test.complexnumber

import fastcalc.ComplexNumber
import org.scalactic.Tolerance._
import org.scalatest.FlatSpec

import scala.annotation.tailrec
import scala.util.Random


class ComplexNumberSpec extends FlatSpec {
  protected val random = new Random()
  @tailrec
  protected final def generateNonZeroRandomDouble() : Double = {
    val result = random.nextDouble() * 1000
    if (result == 0.0) generateNonZeroRandomDouble()
    else result
  }

  protected def generateNonZeroComplex() = new ComplexNumber(generateNonZeroRandomDouble(), generateNonZeroRandomDouble())
  protected def generateComplex() = new ComplexNumber(random.nextDouble(), random.nextDouble())

  @tailrec
  protected final def assertEqualResults[T](iteration : Int, valueGenerator : () => ComplexNumber,
                                            resultGenerator : (ComplexNumber, ComplexNumber) => T,
                                            otherGenerator : () => ComplexNumber,
                                            function : (ComplexNumber, ComplexNumber) => T,
                                            comparerEqual : (T, T) => Boolean) : Unit = {
    if (iteration != 0) {
      val a = valueGenerator()
      val b = otherGenerator()
      assert(comparerEqual(function(a, b), resultGenerator(a, b)))
      assertEqualResults(iteration - 1, valueGenerator, resultGenerator, otherGenerator, function, comparerEqual)
    }
  }

  protected final def assertEqualDoubleResult(iteration : Int, valueGenerator : () => ComplexNumber,
                                              resultGenerator : (ComplexNumber, ComplexNumber) => Double,
                                              otherGenerator : () => ComplexNumber,
                                              function : (ComplexNumber, ComplexNumber) => Double) : Unit = {
    assertEqualResults(iteration, valueGenerator, resultGenerator, otherGenerator, function,
      (a : Double, b : Double) => a === b +- 0.0001)
  }

  protected final def assertEqualComplexResult(iteration : Int, valueGenerator : () => ComplexNumber,
                                               resultGenerator : (ComplexNumber, ComplexNumber) => ComplexNumber,
                                               otherGenerator : () => ComplexNumber,
                                               function : (ComplexNumber, ComplexNumber) => ComplexNumber) : Unit = {
    assertEqualResults(iteration, valueGenerator, resultGenerator, otherGenerator, function,
      (a : ComplexNumber, b : ComplexNumber) => a === b)
  }

  @tailrec
  protected final def complexSelfTest(iteration : Int, valueGenerator : () => ComplexNumber,
                                      resultGenerator : (ComplexNumber) => Double,
                                      function : (ComplexNumber) => Double) : Unit = {
    if (iteration != 0) {
      val testedVal = valueGenerator()
      assert(resultGenerator(testedVal) === function(testedVal) +- 0.0001)
      complexSelfTest(iteration - 1, valueGenerator, resultGenerator, function)
    }
  }

  protected final def complexSelfRadixTest(iteration : Int, valueGenerator : () => ComplexNumber,
                                           resultGenerator : (ComplexNumber) => Double) =
    complexSelfTest(iteration, valueGenerator, resultGenerator, (a) => a.getRadix)


  protected final def complexSelfExponentTest(iteration : Int, valueGenerator : () => ComplexNumber,
                                           resultGenerator : (ComplexNumber) => Double) =
    complexSelfTest(iteration, valueGenerator, resultGenerator, (a) => a.getExponent)

  "A complex number" should "be valid, i.e. equal to itself" in {
    val complex = generateComplex()
    assert(complex === complex)
  }
}
