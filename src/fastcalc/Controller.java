package fastcalc;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import jfx.messagebox.MessageBox;

public class Controller {
    static final double MINIMIZED_HEIGHT = 230.0;
    static final double MAXIMIZED_HEIGHT = 430.0;

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
                model = model.changeOperand(operandNumber, FieldType.getValueFromString(fieldTypeAsString), newValue);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
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
            model.calculate(OperationType.getValueFromString(id));
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

    TextField lastSelectedTextField = null;
    private Model model = null;
    private Stage stage = null;
    private int caretPosition = 0;
    private boolean numpadEnabled = true;
}
