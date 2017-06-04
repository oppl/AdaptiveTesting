package at.jku.ce.adaptivetesting.r;

/*
Created by Peter Baumann
 */
import javax.script.ScriptException;
import com.github.rcaller.rstuff.*;
import at.jku.ce.adaptivetesting.core.LogHelper;

public class RProvider {

	private RCaller caller;
	private RCode code;

	public RProvider() {
		caller = RCaller.create();
		code = RCode.create();
		caller.setRCode(code);
	}

	public double[] execute(String RCodeScript, String toReturn) throws ScriptException {
		code.addRCode(RCodeScript);
		synchronized (toReturn) {
			caller.runAndReturnResultOnline(toReturn);
			code.clearOnline();
			LogHelper.logInfo("R successfully completed");
		}
		return caller.getParser().getAsDoubleArray(toReturn);
	}

	public void terminate() {
		caller.deleteTempFiles();
		LogHelper.logInfo("R temporaty data deleted");
		caller.StopRCallerOnline();
		LogHelper.logInfo("R successfully terminated");
	}
}