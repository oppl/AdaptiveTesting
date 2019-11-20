package at.jku.ce.adaptivetesting.views.test.accounting.misc;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import java.math.BigDecimal;

/**
 * Created by Peter
 *
 */
public class AccountingCalculator implements ClickListener {

    private BigDecimal current = new BigDecimal(0.0);
    private BigDecimal stored = new BigDecimal(0.0);
    private boolean commaEntered = false;
    private int commaCounter = 0;
    private String typed = "";
    private char lastOperationRequested = 'C';
    private final TextArea typedLabel = new TextArea("");
    private final Label display = new Label("0.0");
    private Window window;

    public AccountingCalculator() {
        window = new Window("Taschenrechner");
        window.setWidth("170px");
        window.setHeight("300px");
        window.center();
        window.setResizable(true);
        VerticalLayout v1 = new VerticalLayout();
        v1.setSizeFull();
        GridLayout gl = new GridLayout(5, 5);
        window.setContent(v1);
        v1.addComponent(typedLabel);
        typedLabel.setWidth("100%");
        typedLabel.setHeight("100%");
        typedLabel.setEnabled(false);
        typedLabel.setWordwrap(false);
        v1.addComponent(gl);
        v1.setComponentAlignment(gl, Alignment.BOTTOM_CENTER);
        v1.setExpandRatio(typedLabel, 1);
        gl.addComponent(display, 0, 0, 4, 0);
        String[] operations = new String[] {
                "7", "8", "9", "/", "%",
                "4", "5", "6", "*", "",
                "1", "2", "3", "-", "",
                ",", "0", "=", "+", "C"};
        for (String caption : operations) {
            Button button = new Button(caption);
            button.addClickListener(this);
            button.setHeight(25, AbstractComponent.Unit.PIXELS);
            button.setWidth(25, AbstractComponent.Unit.PIXELS);
            gl.addComponent(button);
        }
    }

    public Window getWindow() {
        return window;
    }

    public void buttonClick(ClickEvent event) {

        Button button = event.getButton();
        char requestedOperation = button.getCaption().charAt(0);
        BigDecimal newValue = calculate(requestedOperation);
        if(requestedOperation == 'C'){
            typed = "";
        } else if (requestedOperation == '=') {
            typed = typed + "=" + newValue.toString() + "\n";
        } else {
            if(requestedOperation != ',') {
                typed = typed + requestedOperation;
            }
        }
        typedLabel.setValue(typed);
        display.setValue(String.valueOf(newValue));
    }

    private BigDecimal calculate(char requestedOperation) {
        if ('0' <= requestedOperation && requestedOperation <= '9') {
            if(lastOperationRequested == '=' ){
                stored = new BigDecimal(0.0);
            }
            if(commaEntered){
                String s = "0.";
                for (int i = 1; i < commaCounter; i++){
                    s = s + "0";
                }
                current = current.add(new BigDecimal(s + requestedOperation));
                commaCounter++;
                if(lastOperationRequested == '=' ){
                    stored = current;
                }
                return current;
            } else {
                current = current.multiply(new BigDecimal(10)).add(new BigDecimal("" + requestedOperation));
                if(lastOperationRequested == '=' ){
                    stored = current;
                }
                return current;
            }
        }
        if (requestedOperation == ',') {
            if(commaEntered == false) {
                commaEntered = true;
                commaCounter = 1;
                typed = typed + '.';
            }
            return current;
        }
        commaEntered = false;
        commaCounter = 0;
        switch (lastOperationRequested) {
            case '+':
                stored = stored.add(current);
                break;
            case '-':
                stored = stored.subtract(current);
                break;
            case '/':
                stored = stored.divide(current);
                break;
            case '*':
                stored = stored.multiply(current);
                break;
            case 'C':
                stored = current;
                break;
        }
        lastOperationRequested = requestedOperation;
        current = new BigDecimal(0.0);
        if (requestedOperation == 'C') {
            stored = new BigDecimal(0.0);
        }
        if (requestedOperation == '%') {
            stored = stored.divide(new BigDecimal(100));
        }
        /*
        if (requestedOperation == ',') {
            commaEntered = true;
        }
        */
        return stored;
    }
}
