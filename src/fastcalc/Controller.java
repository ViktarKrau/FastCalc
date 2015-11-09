package fastcalc;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import jfx.messagebox.MessageBox;
import scala.Enumeration;

public class Controller {
    static final double MINIMIZED_HEIGHT = 230.0;
    static final double MAXIMIZED_HEIGHT = 430.0;

    public Controller() {

    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void initializeTextField(TextField field) {
        field.focusedProperty().addListener(((observable, oldValue, newValue) -> {
            lastSelectedTextField = field;
            caretPosition = field.getCaretPosition();
        }));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void onNumpadButtonClicked(ActionEvent event) {
        Object source = event.getSource();
        assert source instanceof Button;
        Button button = (Button)source;
        String number = button.getId();
        if (lastSelectedTextField != null) {
            lastSelectedTextField.textProperty().addListener(new FieldTextChangeListener(lastSelectedTextField));
            try {
                lastSelectedTextField.insertText(caretPosition, number);
            } catch(IndexOutOfBoundsException ignored) {
                caretPosition = 0;
                lastSelectedTextField.insertText(caretPosition, number);
            }
            ++caretPosition;
        }
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


    public void onOperandChanged(KeyEvent event) {
        Object source = event.getSource();
        assert source instanceof TextField;
        TextField field = (TextField) source;
        if (event.getCharacter().length() != 1) {
            event.consume();
            return;
        }
        String character = event.getCharacter();
        char c = character.isEmpty()? '0' : character.charAt(0);
        if ((c < '0' || c > '9')) {
            if (c == '-') {
                field.textProperty().addListener(new FieldTextChangeListener(field));
                String text = field.getText();
                if (!text.isEmpty() && text.charAt(0) == '-') {
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

    @SuppressWarnings("UnusedParameters")
    public void onCopyToFirst(ActionEvent event) {
        onCopyToOperand(1);
    }

    @SuppressWarnings("UnusedParameters")
    public void onCopyToSecond(ActionEvent event) {
        onCopyToOperand(2);
    }

    public void onToggleNumpad(ActionEvent event) {
        Object source = event.getSource();
        assert source instanceof Button;
        if (stage == null) {
            return;
        }
        Button button = (Button)source;
        if (numpadEnabled) {
            //disable numpad
            button.setText("Numpad off");
            stage.setHeight(MINIMIZED_HEIGHT);
        } else {
            //enable numpad
            button.setText("Numpad on");
            stage.setHeight(MAXIMIZED_HEIGHT);
        }
        numpadEnabled = !numpadEnabled;
    }

    public void onCalculationStarted(ActionEvent event) {
        Object source = event.getSource();
        assert source instanceof Button;
        Button button = (Button)source;
        String id = button.getId();
        try {
            model.calculate(getOperationValueFromString(id));
        } catch (ArithmeticException ignored) {
            MessageBox.show(null, "You can not divide by zero.", "Zero division", MessageBox.OK);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void onCopyToOperand(int opNumber) {
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

    TextField lastSelectedTextField = null;
    private Model model = null;
    private Stage stage = null;
    private int caretPosition = 0;
    private boolean numpadEnabled = true;
}
