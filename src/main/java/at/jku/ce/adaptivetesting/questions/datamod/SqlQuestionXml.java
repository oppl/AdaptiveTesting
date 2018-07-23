package at.jku.ce.adaptivetesting.questions.datamod;

import at.jku.ce.adaptivetesting.questions.XmlQuestionData;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Peter
 */

@XmlRootElement(name = "sqlQuestionDataStorage")
public class SqlQuestionXml extends XmlQuestionData<SqlDataStorage> {

    private static final long serialVersionUID = -7011780466232381923L;

    public SqlQuestionXml() {
    }

    public SqlQuestionXml(SqlDataStorage solution, String questionText, float difficulty) {
        super(solution, questionText, difficulty);
    }

    @Override
    public Class<SqlDataStorage> getDataStorageClass() {
        return SqlDataStorage.class;
    }
}
