package at.jku.ce.adaptivetesting.core.engine;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by oppl on 22/11/2016.
 */
public class StudentData {

    private final String studentGender;
    private final String studentExperience;
    private final String studentClass;
    private final String studentIDCode;
    private final Map studentGradesLastYear;
    private final Map studentGradesLastTest;
    private final String quizName;

    // Accounting Student
    public StudentData(String studentIDCode, String quizName, String studentGender, String studentClass, Map studentGradesLastYear, Map studentGradesLastTest) {
        this.studentGender = studentGender;
        this.studentClass = studentClass;
        this.studentIDCode = studentIDCode;
        this.quizName = quizName;
        this.studentExperience = "";
        this.studentGradesLastYear = studentGradesLastYear;
        this.studentGradesLastTest = studentGradesLastTest;
    }

    // Datamod Student
    public StudentData(String studentIDCode, String quizName, String studentGender, String studentExperience) {
        this.studentGender = studentGender;
        this.studentClass = "";
        this.studentIDCode = studentIDCode;
        this.quizName = quizName;
        this.studentExperience = studentExperience;
        this.studentGradesLastYear = new HashMap();
        this.studentGradesLastTest = new HashMap();
    }

    public StudentData(String studentIDCode) {
        this(studentIDCode,"","","", new HashMap(), new HashMap());
    }

    public String getStudentIDCode() {
        return studentIDCode;
    }

    public String getQuizName() {
        return quizName;
    }

    public String getStudentExperience() {
        return studentExperience;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public String getStudentGender() {
        return studentGender;
    }

    public Map getStudentGradesLastYear() {
        return studentGradesLastYear;
    }

    public Map getStudentGradesLastTest() {
        return studentGradesLastTest;
    }

    public String toString() {
        String [] returnValues = {
                this.getStudentIDCode(),
                this.getStudentGender(),
                this.getStudentClass(),
                this.getStudentExperience(),
                this.getStudentGradesLastYear().toString(),
                this.getStudentGradesLastTest().toString()
        };
        String returnValue = "";
        for (int i = 0; i < returnValues.length; i++) {
            if (returnValues[i].equals("{}")) returnValues[i] = "";
            if (!(returnValues[i].isEmpty())) {
                returnValue = returnValue + returnValues[i] + "; ";
            }
        }
        return returnValue;
    }
}
