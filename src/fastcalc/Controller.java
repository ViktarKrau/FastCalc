package fastcalc;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import scala.Enumeration;

import javax.swing.*;

public class Controller {
    public Controller() {

    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void onOperandChanged(KeyEvent event) {
        Object source = event.getSource();
        assert source instanceof TextField;
        TextField field = (TextField)source;
        String id = field.getId();
        String operandNumberAsString = id.substring(0, 1);
        String fieldTypeAsString = id.substring(1);
        int operandNumber = Integer.parseInt(operandNumberAsString);
        model = model.changeOperand(operandNumber, getFieldTypeValueFromString(fieldTypeAsString), event.getText());
    }

    public void onToggleNumpad(ActionEvent event) {

    }

    public void onCalculationStarted(ActionEvent event) {
        Object source = event.getSource();
        assert source instanceof Button;
        Button button = (Button)source;
        String id = button.getId();
        try {
            model.calculate(getOperationValueFromString(id));
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    Enumeration.Value getFieldTypeValueFromString(String id) throws IllegalArgumentException {
        switch (id) {
            case "real":
                return FieldType.Real();
            case "imag":
                return FieldType.Imaginary();
            case "rad":
                return FieldType.Radix();
            case "exp":
                return FieldType.Exponent();
            default:
                assert false;
        }
        throw new IllegalArgumentException("wrong field type: " + id);
    }

    Enumeration.Value getOperationValueFromString(String id) throws IllegalArgumentException {
        switch (id) {
            case "mult":
                return OperationType.Multiplication();
            case "div":
                return OperationType.Division();
            case "add":
                return OperationType.Addition();
            case "sub":
                return OperationType.Subtraction();
            default:
                assert false;
        }
        throw new IllegalArgumentException("wrong operation: ");
    }

    private Model model = null;
}
