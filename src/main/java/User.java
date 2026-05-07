public class User {

    private String username;
    private String password;
    private int failedAttempts = 0;
    private boolean blocked = false;
    private long blockStartTime = 0;

    private void checkUsername(String username) throws Exception {
        if (username.length() > 50) {
            throw new Exception("Username is too long, try something shorter");
        }
        // Must contain '@'
        if (!username.contains("@")) {
            throw new Exception("Please enter a valid Email as username");
        }
        // Split into two parts: before and after '@'
        String[] parts = username.split("@");

        if (parts.length != 2) {
            throw new Exception("Please enter a valid Email as username");
        }

        String part1 = parts[0];
        String rest = parts[1];
        // Neither part can be empty
        if (part1.length() == 0 || rest.length() == 0) {
            throw new Exception("Please enter a valid Email as username");
        }
        // Find the last dot in the domain
        int lastDotIndex = rest.lastIndexOf(".");

        if (lastDotIndex == -1) {
            throw new Exception("Please enter a valid Email as username");
        }
        // Split domain into two parts
        String part2 = rest.substring(0, lastDotIndex);
        String part3 = rest.substring(lastDotIndex + 1);

        if (part2.length() == 0 || part3.length() == 0) {
            throw new Exception("Please enter a valid Email as username");
        }
        // Validate part1 (local part of email)
        for (int i = 0; i < part1.length(); i++) {
            char c = part1.charAt(i);

            if (!Character.isLetterOrDigit(c) &&
                    c != '.' && c != '_' && c != '-' && c != '+' && c != '%') {
                throw new Exception("Please enter a valid Email as username");
            }
        }
        // part2 must start with letter or digit
        if (!Character.isLetterOrDigit(part2.charAt(0))) {
            throw new Exception("Please enter a valid Email as username");
        }
        // Validate part2 characters
        for (int i = 0; i < part2.length(); i++) {
            char c = part2.charAt(i);

            if (!Character.isLetterOrDigit(c) && c != '.' && c != '-') {
                throw new Exception("Please enter a valid Email as username");
            }
        }

        if (part3.length() < 2) {
            throw new Exception("Please enter a valid Email as username");
        }
        // part3 must contain only letters
        for (int i = 0; i < part3.length(); i++) {
            char c = part3.charAt(i);

            if (!Character.isLetter(c)) {
                throw new Exception("Please enter a valid Email as username");
            }
        }
    }

    private void checkPassword(String password) throws Exception {
        if (password.length() < 8) {
            throw new Exception("Your password is too short, add more characters");
        }

        if (password.length() > 12) {
            throw new Exception("Your password is too long, try a shorter one");
        }

        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSymbol = false;
        // Check each character in password
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (c == '#' || c == '@' || c == '!' || c == '+' || c == '$' || c == '&' || c == '^'|| c == '%' || c=='*' || c=='('|| c==')'|| c=='_') {
                hasSymbol = true;
            } else {
                throw new Exception("Please enter a valid password");
            }
        }
        // Must contain at least one letter, digit and symbol
        if (!hasLetter || !hasDigit || !hasSymbol) {
            throw new Exception("Please enter a valid password");
        }
    }
    // Constructor: creates a user only if username and password are valid
    public User(String username, String password) throws Exception {
        checkUsername(username);
        checkPassword(password);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public synchronized void increaseFailedAttempts() {
        failedAttempts++;
    }

    public synchronized int getFailedAttempts() {
        return failedAttempts;
    }

    public synchronized void resetFailedAttempts() {
        failedAttempts = 0;
    }

    public synchronized boolean isBlocked() {
        return blocked;
    }

    public synchronized void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public synchronized long getBlockStartTime() {
        return blockStartTime;
    }

    public synchronized void setBlockStartTime(long blockStartTime) {
        this.blockStartTime = blockStartTime;
    }

    @Override
    public String toString() {
        return username + " " + password;
    }
}