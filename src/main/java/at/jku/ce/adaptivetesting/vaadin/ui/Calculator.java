package at.jku.ce.adaptivetesting.vaadin.ui;

import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * Created by Peter
 *
 */
public class Calculator implements ClickListener {

    private double current = 0.0;
    private double stored = 0.0;
    private char lastOperationRequested = 'C';
    private final Label display = new Label("0.0");
    private Window window;

    public Calculator() {
        window = new Window("Taschenrechner");
        window.setWidth("170px");
        window.setHeight("170px");
        window.center();
        window.setResizable(false);
        GridLayout gl = new GridLayout(5, 5);
        window.setContent(gl);
        gl.addComponent(display, 0, 0, 4, 0);
        String[] operations = new String[] {
                "7", "8", "9", "/", "%",
                "4", "5", "6", "*", "",
                "1", "2", "3", "-", "",
                ",", "0", "=", "+", "C"};
        for (String caption : operations) {
            Button button = new Button(caption);
            button.addClickListener(this);
            button.setHeight(25, AbstractComponent.UNITS_PIXELS);
            button.setWidth(25, AbstractComponent.UNITS_PIXELS);
            gl.addComponent(button);
        }
    }

    public Window getWindow() {
        return window;
    }

    public void buttonClick(ClickEvent event) {

        Button button = event.getButton();
        char requestedOperation = button.getCaption().charAt(0);
        double newValue = calculate(requestedOperation);
        display.setValue(String.valueOf(newValue));
    }

    private double calculate(char requestedOperation) {
        if ('0' <= requestedOperation && requestedOperation <= '9') {
            current = current * 10 + Double.parseDouble("" + requestedOperation);
            return current;
        }
        switch (lastOperationRequested) {
            case '+':
                stored = stored + current;
                break;
            case '-':
                stored = stored - current;
                break;
            case '/':
                stored = stored / current;
                break;
            case '*':
                stored = stored * current;
                break;
            case 'C':
                stored = current;
                break;
        }
        lastOperationRequested = requestedOperation;
        current = 0.0;
        if (requestedOperation == 'C') {
            stored = 0.0;
        }
        if (requestedOperation == '%') {
            stored = stored / 100;
        }
        if (requestedOperation == ',') {
            //TODO
        }
        return stored;
    }
}
