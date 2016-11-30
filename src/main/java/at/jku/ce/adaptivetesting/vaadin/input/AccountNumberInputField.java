package at.jku.ce.adaptivetesting.vaadin.input;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import java.util.ArrayList;
import java.util.List;

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.TextField;

public class AccountNumberInputField extends TextField implements
		TextChangeListener {

	private static final long serialVersionUID = 1535274733192320364L;
	private final List<ValidValueChangedListener<Integer>> validValueChangedListeners = new ArrayList<>();
	private int value = 0;
	private String lastValue = "00";
	private final String regex = "^[0-9]{0,2}$";

	public AccountNumberInputField() {
		addTextChangeListener(this);
		setTextChangeEventMode(TextChangeEventMode.LAZY);
		setTextChangeTimeout(2000);
	}

	public void addListener(ValidValueChangedListener<Integer> listener) {
		validValueChangedListeners.add(listener);
	}

	public void removeListener(ValidValueChangedListener<Integer> listener) {
		validValueChangedListeners.remove(listener);
	}

	public int getAccountNumber() {
		return value;
	}

	private void fireValidValueChangedListener() {
		Integer f = value;
		for (ValidValueChangedListener<Integer> validValueChangedListener : validValueChangedListeners) {
			validValueChangedListener.accept(f);
		}
	}

	@Override
	public void textChange(TextChangeEvent event) {
		String newInput = event.getText();
		if (isValidNumber(newInput)) {
			value = Integer.parseInt(newInput);
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
		return s.length() == 2 && s.matches(regex);
	}
}
