package at.jku.ce.adaptivetesting.r;

/*This file is part of the project "Reisisoft Adaptive Testing",
 * which is licenced under LGPL v3+. You may find a copy in the source,
 * or obtain one at http://www.gnu.org/licenses/lgpl-3.0-standalone.html */
import java.io.ByteArrayOutputStream;
import javax.script.ScriptException;

import com.github.rcaller.rstuff.*;
import at.jku.ce.adaptivetesting.core.LogHelper;

public class RProvider {

	private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

	public void run(RCaller caller, RCode code) throws ScriptException {
		caller.setRCode(code);
		synchronized (byteArrayOutputStream) {
			byteArrayOutputStream.reset();
			try {
				caller.redirectROutputToStream(byteArrayOutputStream);
				caller.runOnly();
			} catch (Exception e) {
				LogHelper.logRError(byteArrayOutputStream.toString());
				throw new ScriptException(e);
			} finally {
				caller.StopRCallerOnline();
				if (byteArrayOutputStream.size() > 0) {
					LogHelper.logRError(byteArrayOutputStream.toString());
				} else {
					LogHelper.logInfo("R calculation successful");
				}
			}
		}
	}

	public RCaller getRCaller() throws ScriptException {
		return new RService().getRCaller();
	}

	public RCode getRCode() {
		return new RService().getRCode();
	}

	public ROutputParser execute(RCaller caller, RCode code, String toReturn)
			throws ScriptException {
		caller.setRCode(code);
		synchronized (byteArrayOutputStream) {
			byteArrayOutputStream.reset();
			try {
				caller.redirectROutputToStream(byteArrayOutputStream);
				caller.runAndReturnResult(toReturn);
			} catch (Exception e) {
				LogHelper.logRError(byteArrayOutputStream.toString());
				throw new ScriptException(e);
			} finally {
				caller.StopRCallerOnline();
				if (byteArrayOutputStream.size() > 0) {
					LogHelper.logRError(byteArrayOutputStream.toString());
				} else {
					LogHelper.logInfo("R calculation successful");
				}
			}
		}
		return caller.getParser();
	}
}