package fastcalc

object OperationType extends Enumeration {
   type OperationType = Value
   val Addition, Subtraction, Division, Multiplication = Value
   def getValueFromString(string : String) : Value = string match {
     case "mult" => Multiplication
     case "div" => Division
     case "add" => Addition
     case "sub" => Subtraction
     case _ => throw new IllegalArgumentException("wrong operation: ");
   }
 }
