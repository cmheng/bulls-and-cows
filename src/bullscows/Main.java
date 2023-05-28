package bullscows;

import java.util.Random;
import java.util.Scanner;

public class Main {

    private static final int MAX_LENGTH = 36;
    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyz";
    public static void main(String[] args) {
        startConsole();        
    }

    private static void startConsole() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please, enter the secret code's length:");
        String input = scanner.nextLine();
        int codeLength = 0;
        try {
            codeLength = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.printf("Error: \"%s\" isn't a valid number.\n", input);
            System.exit(0);
        }

        if (codeLength > MAX_LENGTH || codeLength < 1) {
            System.out.printf("Error: can't generate a secret number with a length of %s because there aren't enough unique digits.\n", input);
            System.exit(0);
        }  

        System.out.println("Input the number of possible symbols in the code:");
        input = scanner.nextLine();
        int symbolLength = Integer.parseInt(input);
        try {
            symbolLength = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.printf("\"%s\" isn't a valid number.\n", input);
            System.exit(0);
        }
        if (symbolLength > MAX_LENGTH) {
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            System.exit(0);
        }  

        if (codeLength > symbolLength) {
            System.out.printf("Error: it's not possible to generate a code with a length of %d with %d unique symbols.\n", codeLength, symbolLength);
            System.exit(0);
        }

        String secretCode = generateSecretCode(codeLength, symbolLength);
        StringBuilder symbolRange = new StringBuilder();
        if (symbolLength <= 10) {
            symbolRange.append("0-");
            symbolRange.append(symbolLength - 1);
        } else {
            symbolRange.append("0-9, a");
            if (symbolLength > 11) {
                symbolRange.append("-");
                symbolRange.append(SYMBOLS.charAt(symbolLength - 1));
            }
        }
        System.out.printf("The secret is prepared: %s (%s).\n", "*".repeat(codeLength), symbolRange);
        System.out.println("Okay, let's start a game!");

        boolean win = false;
        int turn = 1;
        while (!win) {
            System.out.printf("Turn %d:\n", turn);
            String guess = scanner.nextLine();
            int[] result = gradeGuess(secretCode, guess);
            int bull = result[0];
            int cow = result[1];
            if (bull > 0 && cow > 0) {
                System.out.printf("Grade: %d bull%s and %d cow%s\n", bull, bull > 1 ? "s" : "", cow, cow > 1 ? "s" : "");
            } else if (bull > 0 && cow == 0) {
                System.out.printf("Grade: %d bull%s\n", bull, bull > 1 ? "s" : "");
            } else if (bull == 0 && cow > 0) {
                System.out.printf("Grade: %d cow%s\n", cow, cow > 1 ? "s" : "");
            } else {
                System.out.println("Grade: None");
            }
            if (bull == codeLength) {
                win = true;
                System.out.println("Congratulations! You guessed the secret code.");
            } else {
                turn++;
            }
        }

    }

    private static String generateSecretCode(int codeLength, int symbolLength) {
        StringBuilder secretCode = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            Random random = new Random();
            boolean doesNotExist = true;
            while (doesNotExist) {
                int index = random.nextInt(symbolLength + 1);
                char symbol = SYMBOLS.charAt(index);
                if (secretCode.indexOf(Character.toString(symbol)) < 0) {
                    secretCode.append(symbol);
                    doesNotExist = false;
                }
            }            
        }
        return secretCode.toString();
    }

    private static int[] gradeGuess(String secretCode, String guessCode) {
        int bull = 0;
        int cow = 0;
        for (int i = 0; i < guessCode.length(); i++) {
            char c = guessCode.charAt(i);
            if (c == secretCode.charAt(i)) {
                bull++;
            } else if (secretCode.indexOf(c) >= 0) {
                cow++;
            }
        }

        return new int[]{bull, cow};
    }
}