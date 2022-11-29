import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class PassportValidator {

    static Credential[] getCredentials(String[] input) {
        List<Credential> credentials = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length; i++) {
            if (!input[i].isBlank()) {
                sb.append(" ").append(input[i]);
            } else {
                credentials.add(new Credential(sb.toString()));
                sb = new StringBuilder();
            }

            if (i == input.length - 1) {
                credentials.add(new Credential(sb.toString()));
            }
        }

        Credential credArr[] = new Credential[credentials.size()];
        credentials.toArray(credArr);
        return credArr;
    }

    public static void main(String args[]) {
        try {
            String input = args[0];

            List<String> inputList = Files.lines(Paths.get(input))
                                        .collect(Collectors.toList());
            String[] inputs = new String[inputList.size()];
            inputList.toArray(inputs);
            
            Credential[] allCredentials = getCredentials(inputs);
            int validCred = 0;
            for (Credential cred : allCredentials) {
                if (cred.isValid()) {
                    validCred++;
                }
            }
            
            System.out.println("There are " + allCredentials.length + " credentials");
            System.out.println("Valid credentials: " + validCred);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}