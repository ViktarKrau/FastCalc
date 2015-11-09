package fastcalc

import javafx.scene.Scene
import javafx.scene.control.TextField

class View(scene : Scene) {

  def getResult = {
    val real = scene.lookup("#resreal").asInstanceOf[TextField].getText.toDouble
    val imag = scene.lookup("#resimag").asInstanceOf[TextField].getText.toDouble
    new ComplexNumber(real, imag)
  }

  def setResult(value : ComplexNumber) = {
    setResultFieldValue("#resreal", value.getReal)
    setResultFieldValue("#resimag", value.getImaginary)
    setResultFieldValue("#resrad", value.getRadix)
    setResultFieldValue("#resexp", value.getExponent)
  }

  def setOperand(number : Integer, value : ComplexNumber) = {
    val verifiedNumber = if (number == 1) 1 else 2
    def com(s : String) =
      "#" + verifiedNumber.toString + s
    setFieldValue(com("real"), value.getReal)
    setFieldValue(com("imag"), value.getImaginary)
    setFieldValue(com("rad"), value.getRadix)
    setFieldValue(com("exp"), value.getExponent)
  }

  private def setFieldValue(id : String, value : Double) : Unit =
    setFieldValue(id, value, (f, v) => f.setText(v.toString))

  private def setFieldValue(id : String, value : Double, outPut : (TextField, Double) => Unit) : Unit =
    scene.lookup(id) match {
      case f : TextField => outPut(f, value)
      case _ => throw new Exception("wrong field")
    }

  private def setResultFieldValue(id : String, value : Double) =
    setFieldValue(id, value, (f, v) => f.setText("%.4f".format(v)))
}
