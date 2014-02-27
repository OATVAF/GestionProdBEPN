package data;

import java.util.Iterator;

import pack.Config;
//import jxl.Cell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class StgMail {

	public String prop[];
	public int n;

	private static final int nCols = Config.getI("exp.interview.cols");
	private static final String delimiter = "\t";
	
	public StgMail(Cell[] c, int i) {
		prop = new String[nCols];
		if (c.length > 0) {
			for (int j=0; j<nCols && j<c.length; j++)
				prop[j]=c[j].getStringCellValue();
			n = i;
		}
	}
	public StgMail(Row r, int i) {
		prop = new String[nCols];
		Iterator<Cell> cells = r.cellIterator();
		for (int j=0; j<nCols && cells.hasNext(); j++) {
			Cell c = cells.next();
			prop[j]=c.getStringCellValue();
		}
		n = i;			
	}
	public String toString() {
		//return Nom + "\t" + Prenom + "\t" + Matricule + "\t" + Fct + "\t" + Email + "\t" + NumStage + "\t" + Site;
	    StringBuilder arTostr = new StringBuilder();
	    if (prop.length > 0) {
	        arTostr.append(prop[0]);
	        for (int i=1; i<prop.length; i++) {
	            arTostr.append(delimiter);
	            arTostr.append(prop[i]);
	        }
	    }
	    return arTostr.toString();	}
}
