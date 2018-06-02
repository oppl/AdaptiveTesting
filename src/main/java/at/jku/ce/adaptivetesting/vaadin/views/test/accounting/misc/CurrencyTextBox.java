package at.jku.ce.adaptivetesting.vaadin.views.test.accounting.misc;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import java.util.ArrayList;
import java.util.List;

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.TextField;

public class CurrencyTextBox extends TextField implements TextChangeListener {

	private static final long serialVersionUID = 1535274733192320364L;
	private final List<ValidValueChangedListener<Float>> validValueChangedListeners = new ArrayList<>();
	private float value = 0f;
	private String lastValue = "0";
	private final String regexNumber_DotSep = "^([0-9]{1,3}(((,[0-9]{3}){0,})|[0-9]{0,}))?(\\.[0-9]{1,2})?$",
			regexNumber_ColonSep = 			  "^([0-9]{1,3}(((\\.[0-9]{3}){0,})|[0-9]{0,}))?(,[0-9]{1,2})?$";

	public CurrencyTextBox() {
		addTextChangeListener(this);
		setTextChangeEventMode(TextChangeEventMode.LAZY);
	}

	public void addListener(ValidValueChangedListener<Float> listener) {
		validValueChangedListeners.add(listener);
	}

	public void removeListener(ValidValueChangedListener<Float> listener) {
		validValueChangedListeners.remove(listener);
	}

	public float getNumericValue() {
		return value;
	}

	private void fireValidValueChangedListener() {
		Float f = value;
		for (ValidValueChangedListener<Float> validValueChangedListener : validValueChangedListeners) {
			validValueChangedListener.accept(f);
		}
	}

	@Override
	public void textChange(TextChangeEvent event) {
		String newInput = event.getText();
		if (isValidNumber(newInput)) {
			String v;

			if (newInput.length() == 0) {
				v = "0";
			} else if (newInput.matches(regexNumber_ColonSep)) {
				v = newInput.replace(".", "").replace(',', '.');
			} else {
				v = newInput.replace(",", "");
			}
			value = Float.parseFloat(v);
			lastValue = newInput;
			fireValidValueChangedListener();
		} else {
			setValue(lastValue);
		}
	}

	private boolean isValidNumber(String s) {
		// A number must not be NULL
		if (s == null) {
			return false;
		}
		// Ensure the number is valid
		return s.length() == 0 || s.matches(regexNumber_ColonSep)
				|| s.matches(regexNumber_DotSep);
	}
}
