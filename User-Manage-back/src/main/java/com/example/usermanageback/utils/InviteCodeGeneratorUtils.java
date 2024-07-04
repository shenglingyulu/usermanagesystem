package com.example.usermanageback.utils;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class InviteCodeGeneratorUtils {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 8;
    private static final Set<String> generatedCodes = new HashSet<>();
    private static final SecureRandom random = new SecureRandom();

    /**
     * Generates a unique invite code.
     * @return a unique invite code
     */
    public static String generateUniqueCode() {
        String code;
        do {
            code = generateCode();
        } while (generatedCodes.contains(code));

        generatedCodes.add(code);
        return code;
    }

    /**
     * Generates a random invite code.
     * @return a random invite code
     */
    private static String generateCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }
}
