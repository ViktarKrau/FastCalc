package fastcalc

object FieldType extends Enumeration {
   type FieldType = Value
   val Real, Imaginary, Radix, Exponent = Value
   def getValueFromString(string : String) : Value = string match {
     case "real" => Real
     case "imag" => Imaginary
     case "rad" => Radix
     case "exp" => Exponent
     case _ => throw new IllegalArgumentException("wrong field");
   }
 }
