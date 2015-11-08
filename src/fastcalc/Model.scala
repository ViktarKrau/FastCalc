package fastcalc

import FieldType._
import OperationType._

class Model(view : View, operand1 : ComplexNumber, operand2 : ComplexNumber) {
  view.setOperand(1, operand1)
  view.setOperand(2, operand2)

  def this(view : View) = this(view, new ComplexNumber, new ComplexNumber)

  def calculate(operationType: OperationType) =
    operationType match {
      case Addition => view.setResult(operand1 + operand2)
      case Subtraction => view.setResult(operand1 - operand2)
      case Multiplication => view.setResult(operand1 * operand2)
      case Division => view.setResult(operand1 / operand2)
    }

  def copyFromResult(operandNumber : Integer): Model = {
    val result = view.getResult;
    if (operandNumber == 1) new Model(view, result, operand2)
    else new Model(view, operand1, result)
  }

  def changeOperand(operandNumber : Integer, fieldType: FieldType, strValue : String) = {
    val numberValue = if (!strValue.isEmpty) strValue.toDouble else 0.0
    val selectedOperand = if (operandNumber == 1) operand1 else operand2
    val newValue = fieldType match {
      case Real => selectedOperand setReal numberValue
      case Imaginary => selectedOperand setImaginary numberValue
      case Radix => selectedOperand setRadix numberValue
      case Exponent => selectedOperand setExponent numberValue
    }
    if (operandNumber == 1) new Model(view, newValue, operand2)
    else new Model(view, operand1, newValue)
  }
}
