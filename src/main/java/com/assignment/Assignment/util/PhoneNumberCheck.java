package com.assignment.Assignment.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PhoneNumberCheck {

    public boolean isValidPhoneNumber (String phoneNumber) {
        int length = phoneNumber.length();
        int startIndex = 0;
        boolean isValid=true;
        if (!(length == 10 || length == 13)) {
            isValid = false;
        }

        if (phoneNumber.charAt(0) == '+') {
            startIndex = 1;
            if (!phoneNumber.substring(1, 3).equals("91")) {
                isValid = false;
            }
        }
        for (int i = startIndex; i < length; i++) {
            if (phoneNumber.charAt(i) < '0' || phoneNumber.charAt(i) > '9') {
                isValid = false;
            }
        }
        return isValid;
    }
}
