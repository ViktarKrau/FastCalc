package fastcalc;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import scala.Enumeration;

public class Controller {
    public Controller() {

    }

    private class FieldTextChangeListener implements javafx.beans.value.ChangeListener<String> {
        public FieldTextChangeListener(TextField field) {
            this.field = field;
        }

        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            field.textProperty().removeListener(this);
            String id = field.getId();
            String operandNumberAsString = id.substring(0, 1);
            String fieldTypeAsString = id.substring(1);
            int operandNumber = Integer.parseInt(operandNumberAsString);
            try {
                model = model.changeOperand(operandNumber, getFieldTypeValueFromString(fieldTypeAsString), newValue);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        private Enumeration.Value getFieldTypeValueFromString(String id) throws IllegalArgumentException {
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

        private TextField field;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void onOperandChanged(KeyEvent event) {
        Object source = event.getSource();
        assert source instanceof TextField;
        TextField field = (TextField) source;
        if (event.getCharacter().length() != 1) {
            event.consume();
            return;
        }
        char c = event.getCharacter().charAt(0);
        if ((c < '0' || c > '9')) {
            if (c == '-') {
                field.textProperty().addListener(new FieldTextChangeListener(field));
                String text = field.getText();
                if (text.charAt(0) == '-') {
                    text = text.substring(1);
                }
                else {
                    text = "-" + text;
                }
                field.setText(text);
            }
            event.consume();
            return;
        }

        field.textProperty().addListener(new FieldTextChangeListener(field));

    }

    public void onCopyToFirst(ActionEvent event) {
        onCopyToOperand(event, 1);
    }

    public void onCopyToSecond(ActionEvent event) {
        onCopyToOperand(event, 2);
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

    private void onCopyToOperand(ActionEvent event, int opNumber) {
        model = model.copyFromResult(opNumber);
    }

    private static Enumeration.Value getOperationValueFromString(String id) throws IllegalArgumentException {
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
