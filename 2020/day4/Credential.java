import java.util.Arrays;
import java.util.List;

class Credential {

    String byr;
    String iyr;
    String eyr;
    String hgt;
    String hcl;
    String ecl;
    String pid;
    String cid;

    Credential(String cred) {
        parseCredential(cred.trim());
    }

    void parseCredential(String cred) {
        String fields[] = cred.split(" ");

        for (String field : fields) {
            String keyValue[] = field.split(":");

            if (keyValue.length == 2) {
                assignValue(keyValue[0], keyValue[1]);
            }
        }
    }

    void assignValue(String field, String value) {
        switch (field) {
            case "byr" -> byr = value;
            case "iyr" -> iyr = value;
            case "eyr" -> eyr = value;
            case "hgt" -> hgt = value;
            case "hcl" -> hcl = value;
            case "ecl" -> ecl = value;
            case "pid" -> pid = value;
            case "cid" -> cid = value;
            default -> System.out.println("Error: Unknown field [\"" + field + "\"]");
        }
    }

    boolean isValid() {
        return isBYR() && isIYR() && isEYR() &&
               isHGT() && isHCL() && isECL() && isPID();
    }

    boolean isBYR() {
        try {
            int byear = Integer.parseInt(byr);
            return byear >= 1920 && byear <= 2002;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    boolean isIYR() {
        try {
            int iyear = Integer.parseInt(iyr);
            return iyear >= 2010 && iyear <= 2020;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    boolean isEYR() {
        try {
            int eyear = Integer.parseInt(eyr);
            return eyear >= 2020 && eyear <= 2030;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    boolean isHGT() {
        try {
            String[] splited = hgt.split("cm");
            int height;
            if (isInteger(splited[0])) {
                height = Integer.parseInt(splited[0]);

                return height >= 150 && height <= 193;
            } else {
                splited = hgt.split("in");
                height = Integer.parseInt(splited[0]);

                return height >= 59 && height <= 76;
            }
            
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    boolean isHCL() {
        try {
            String[] hairColor = hcl.split("#");

            if (hairColor.length != 2 || hairColor[1].length() != 6) 
                return false;
            
            for (char c : hairColor[1].toCharArray()) {
                if (!Character.isDigit(c) && !Character.isAlphabetic(c)) {
                    return false;
                }
            }
            
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    boolean isECL() {
        List<String> colors = Arrays.asList("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

        return colors.contains(ecl);
    }

    boolean isPID() {
        try {
            if (pid.length() != 9)
                return false;

            for (char c : pid.toCharArray()) {
                if (!Character.isDigit(c))
                    return false;
            }

            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(new String[]{byr, iyr, eyr, hgt, hcl, ecl, pid, cid});
    }
}
