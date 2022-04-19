package warnings;

public class WarningData {
    private static String warningData = "";

    public static void addWarningData(String warningDataToAdd) {
        if (warningData.equals("")) {
            warningData += warningDataToAdd;
        } else if (!(warningData.indexOf(warningDataToAdd) !=-1? true: false)) {
            warningData += warningDataToAdd;
        }
    }

    public static String getWarningData() {
        return warningData;
    }
}
