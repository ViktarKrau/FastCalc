package test.complexnumber

import fastcalc.ComplexNumber

import scala.annotation.tailrec


class Timetest extends ComplexNumberSpec {

  "A complex calculation" should "be completed less than in one second" in {
    @tailrec
    def timetest(iteration : Int): Unit = {
      val function = math.abs(random.nextInt() % 4) match {
        case 0 => (a : ComplexNumber, b : ComplexNumber) => a * b
        case 1 => (a : ComplexNumber, b : ComplexNumber) => a / b
        case 2 => (a : ComplexNumber, b : ComplexNumber) => a + b
        case 3 => (a : ComplexNumber, b : ComplexNumber) => a - b
      }
      val a = generateComplex()
      val b = generateNonZeroComplex()
      val start = System.nanoTime()
      function(a, b)
      val end = System.nanoTime()
      assert(end - start < 1000000)
      if (iteration != 0) timetest(iteration - 1)
    }
    timetest(5000)

  }
}
