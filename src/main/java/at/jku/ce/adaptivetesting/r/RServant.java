package at.jku.ce.adaptivetesting.r;
import com.github.rcaller.rstuff.*;
import at.jku.ce.adaptivetesting.core.LogHelper;
import javax.script.ScriptException;

public class RServant {
    //create only one service as servant instead of normal constructor
    private static RService servant = new RService();

    public double[] execute(String RCodeScript, String toReturn) throws ScriptException {
        servant.getRCode().addRCode(RCodeScript);
        synchronized (toReturn) {
            servant.getRCaller().runAndReturnResultOnline(toReturn);
            servant.getRCode().clearOnline();
            LogHelper.logInfo("R successfully completed");
        }
        return servant.getRCaller().getParser().getAsDoubleArray(toReturn);
    }

    public void terminate() {
        servant.getRCaller().deleteTempFiles();
        LogHelper.logInfo("R temporary data deleted");
        servant.getRCaller().StopRCallerOnline();
        LogHelper.logInfo("R successfully terminated");
    }
}