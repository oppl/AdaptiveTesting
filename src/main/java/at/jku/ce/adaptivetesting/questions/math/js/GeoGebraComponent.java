package at.jku.ce.adaptivetesting.questions.math.js;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractJavaScriptComponent;
import com.vaadin.ui.JavaScriptFunction;
import elemental.json.JsonArray;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The server-side representation of the GeoGebra-Component.
 * Uses the JS-Files for the client-side JS-Component
 * and the JS-Connector that enables the interaction between the two sides.
 * The necessary JavaScript for the usage of the GeoGebra-JS-API is
 * also loaded.
 */
@JavaScript({"https://cdn.geogebra.org/apps/deployggb.js", "geogebra-integration2.js", "geogebra-connector.js"})
public class GeoGebraComponent extends AbstractJavaScriptComponent {

    /**
     * Server-side JS-Function that is used in the geogebra-connector
     * to pass a callback from the client side to the server side
     * (in this case the exercise fraction).
     * The data is passed as a JSON-Array.
     */
    public GeoGebraComponent() {
        addFunction("onClick", new JavaScriptFunction() {
            @Override
            public void call(JsonArray arguments) {
                //Get the exercise fraction
                double result = arguments.getNumber(0);
                //Set the value of the shared state
                setValue(Double.toString(result));
                //Notify change listeners
                for (ValueChangeListener listener: listeners)
                    listener.valueChange();
            }
        });
    }

    public interface ValueChangeListener extends Serializable {
        void valueChange();
    }

    //List of listeners that listen for a change of the component state
    ArrayList<ValueChangeListener> listeners =
            new ArrayList<ValueChangeListener>();

    /**
     * Adds a value change listener to the list of listeners.
     *
     * @param listener
     */
    public void addValueChangeListener(
            ValueChangeListener listener) {
        listeners.add(listener);
    }

    /**
     * Setter for the value inside the shared state.
     *
     * @param value
     */
    public void setValue(String value) {
        getState().value = value;
    }

    /**
     * Getter for the value inside the shared state.
     *
     * @return
     */
    public String getValue() {
        return getState().value;
    }

    /**
     * Getter for the shared state between server side and client side.
     *
     * @return
     */
    @Override
    protected GeoGebraComponentState getState() {
        return (GeoGebraComponentState) super.getState();
    }
}
