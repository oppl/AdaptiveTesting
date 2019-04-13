package at.jku.ce.adaptivetesting.core.engine;

/**
 * Created by Peter
 */

public enum TestVariants {
	RW("Rechnungswesentest", "accounting"),
	SQL("SQL-Datenmodellierungstest", "datamod"),
	MATH("Mathematiktest", "math");
	private String name, folderName;

	TestVariants(String name, String folderName) {
		this.name = name;
		this.folderName = folderName;
	}

	@Override
	public String toString() {
		return name;
	}

	public String getFolderName() {
		return "/" + folderName;
	}
}
