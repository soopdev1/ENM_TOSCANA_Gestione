package rc.so.cf;

import static rc.so.db.Action.insertTR;
import static rc.so.util.Utility.estraiEccezione;
import org.joda.time.DateTime;

public class DataPanel {

    public static String getCF(String nameField, String surnameField, String fm, DateTime birtDate, String town) {

        String name = nameField.toUpperCase();
        String surname = surnameField.toUpperCase();

        String birthday = birtDate.dayOfMonth().getAsString();
        String month = String.valueOf(birtDate.monthOfYear().get());
        String year = birtDate.year().getAsString();

        String fiscalCode = ComputeFiscalCode.computeSurname(surname);
        fiscalCode += ComputeFiscalCode.computeName(name);
        fiscalCode += ComputeFiscalCode.computeDateOfBirth(birthday, month, year, fm.toLowerCase());
        fiscalCode += town;
        try {
            fiscalCode += ComputeFiscalCode.computeControlChar(fiscalCode);
            if (fiscalCode.length() == 16) {
                return fiscalCode;
            }
        } catch (Exception ex) {
            insertTR("E", "SERVICE", estraiEccezione(ex));
        }

        return "";
    }

}
