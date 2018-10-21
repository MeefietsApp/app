package nl.hypothermic.meefietsen.integrity;

import nl.hypothermic.mfsrv.obj.auth.TelephoneNum;

public class PhoneNumberFormatter {

    public static TelephoneNum toTelephoneNum(String userInput) throws PhoneNumberParseException {
        userInput = userInput.replaceAll("\\s+","");

        if (userInput.startsWith("00")) {
            userInput.replaceFirst("00", "+");
        }

        if (userInput.startsWith("+")) {

            // Verenigde Staten (+1)
            if (userInput.length() == 12 &&
                    userInput.charAt(2) == '1') {
                return new TelephoneNum(1, Integer.valueOf(userInput.substring(3)));
            }

            // Nederland (+31)
            if (userInput.length() == 12 &&
                    userInput.charAt(2) == '3' &&
                    userInput.charAt(3) == '1' &&
                    userInput.charAt(4) == '6') {
                return new TelephoneNum(31, Integer.valueOf(userInput.substring(4)));
            }

            // TODO meer landen...

        } else if (userInput.startsWith("06") && userInput.length() == 10) {
            return new TelephoneNum(31, Integer.valueOf(userInput.substring(2)));
        }

        throw new PhoneNumberParseException(userInput);
    }

    public static class PhoneNumberParseException extends Exception {

        public PhoneNumberParseException(String cause) {
            super(cause);
        }
    }
}
