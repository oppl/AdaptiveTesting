package at.jku.ce.adaptivetesting.core.r;
import com.github.rcaller.rstuff.*;
import at.jku.ce.adaptivetesting.core.LogHelper;
import javax.script.ScriptException;

public class RConnectionProvider {
    //create only one service as rService instead of normal constructor
    private static RService rService = new RService();

    public double[] execute(String RCodeScript, String toReturn) throws ScriptException {
        rService.getRCode().addRCode(RCodeScript);
        synchronized (toReturn) {
            rService.getRCaller().runAndReturnResultOnline(toReturn);
            rService.getRCode().clearOnline();
            LogHelper.logInfo("R successfully completed");
        }
        return rService.getRCaller().getParser().getAsDoubleArray(toReturn);
    }

    public void terminate() {
        rService.getRCaller().deleteTempFiles();
        LogHelper.logInfo("R temporary data deleted");
        rService.getRCaller().StopRCallerOnline();
        LogHelper.logInfo("R successfully terminated");
    }
}