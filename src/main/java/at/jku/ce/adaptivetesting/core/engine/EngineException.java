package at.jku.ce.adaptivetesting.core.engine;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
public class EngineException extends Exception {

	private static final long serialVersionUID = 7510476603149718996L;

	public EngineException(String message) {
		super(message);
	}

	public EngineException(Throwable cause) {
		super(cause.getMessage(), cause);
	}

}
