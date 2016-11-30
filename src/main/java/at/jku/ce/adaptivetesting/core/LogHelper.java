package at.jku.ce.adaptivetesting.core;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogHelper {
	private static Logger logger = LoggerFactory.getLogger("AdaptiveTesting");
	{

	}

	public static void logThrowable(Throwable throwable) {
		logger.error("An exception occured:" + throwable.getMessage(),
				throwable);
	}

	public static void logRError(String string) {
		logger.error("An error occured inside R:\t" + string);
	}

	public static void logError(String string) {
		logger.error("There was an error while executing... Error message:\t"
				+ string);
	}

	public static void logInfo(String string) {
		logger.info(string);
	}

}
