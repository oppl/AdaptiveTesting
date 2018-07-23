package at.jku.ce.adaptivetesting.views;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;

public class SingleComponentLayout extends GridLayout {

	private static final long serialVersionUID = -5260460847590307512L;

	public SingleComponentLayout() {
		super(1, 1);
		setSizeFull();
	}

	@Override
	public void addComponent(Component component, int column1, int row1,
			int column2, int row2) throws OverlapsException,
			OutOfBoundsException {
		removeAllComponents();
		super.addComponent(component, 0, 0, 0, 0);
	}

}
