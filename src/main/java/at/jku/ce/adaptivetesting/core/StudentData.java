package at.jku.ce.adaptivetesting.core;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by oppl on 22/11/2016.
 */
public class StudentData {

    private final String studentGender;
    private final String studentClass;
    private final String studentIDCode;
    private final Map studentGradesLastYear;
    private final Map studentGradesLastTest;


    public StudentData(String studentIDCode, String studentGender, String studentClass, Map studentGradesLastYear, Map studentGradesLastTest) {
        this.studentGender = studentGender;
        this.studentClass = studentClass;
        this.studentIDCode = studentIDCode;
        this.studentGradesLastYear = studentGradesLastYear;
        this.studentGradesLastTest = studentGradesLastTest;
    }

    public StudentData(String studentIDCode) {
        this(studentIDCode,"","",new HashMap(), new HashMap());
    }

    public String getStudentClass() {
        return studentClass;
    }

    public String getStudentIDCode() {
        return studentIDCode;
    }

    public Map getStudentGradesLastYear() {
        return studentGradesLastYear;
    }

    public Map getStudentGradesLastTest() {
        return studentGradesLastTest;
    }

    public String getStudentGender() {
        return studentGender;
    }

    public String toString() {
        return this.getStudentIDCode()+";"+this.getStudentGender()+";"+this.getStudentClass()+";"+this.getStudentGradesLastYear().toString()+";"+this.getStudentGradesLastTest().toString();
    }
}
