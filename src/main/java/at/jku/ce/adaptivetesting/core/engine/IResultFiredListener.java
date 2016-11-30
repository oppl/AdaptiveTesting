package at.jku.ce.adaptivetesting.core.engine;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
public interface IResultFiredListener {

	public void resultFired(ResultFiredArgs args) throws EngineException;
}
