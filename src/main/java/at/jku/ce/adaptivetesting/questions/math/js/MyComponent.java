package at.jku.ce.adaptivetesting.questions.math.js;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;

import java.io.Serializable;
import java.util.ArrayList;

@JavaScript({"https://cdn.geogebra.org/apps/deployggb.js", "geogebra-int.js", "mycomponent-connector.js"})
public class MyComponent extends AbstractJavaScriptComponent {
    public interface ValueChangeListener extends Serializable {
        void valueChange();
    }
    ArrayList<ValueChangeListener> listeners =
            new ArrayList<ValueChangeListener>();
    public void addValueChangeListener(
            ValueChangeListener listener) {
        listeners.add(listener);
    }

    public void setValue(String value) {
        getState().value = value;
    }

    public String getValue() {
        return getState().value;
    }

    @Override
    protected MyComponentState getState() {
        return (MyComponentState) super.getState();
    }
}
