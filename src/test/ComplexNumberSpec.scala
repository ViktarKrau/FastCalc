package test

import fastcalc.ComplexNumber
import org.scalatest.FlatSpec

import scala.annotation.tailrec
import scala.util.Random

class ComplexNumberSpec extends FlatSpec {
  protected val random = new Random()
  @tailrec
  protected final def generateNonZeroRandomDouble() : Double = {
    val result = random.nextDouble()
    if (result == 0.0) generateNonZeroRandomDouble()
    else result
  }

  @tailrec
  protected final def assertEqualComplex(iteration : Int, valueGenerator : () => ComplexNumber,
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
}
