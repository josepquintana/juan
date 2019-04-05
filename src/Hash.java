import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash
{
    public static String MD5(String input) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("MD5");
        byte[] encodedHash = sha1.digest(input.getBytes(StandardCharsets.UTF_8));   // maybe only ASCII ¿?
        StringBuffer hexString = new StringBuffer();
        for (byte anEncodedHash : encodedHash) {
            hexString.append(Integer.toString((anEncodedHash & 0xff) + 0x100, 16).substring(1));
        }
        return hexString.toString();
    }

    public static String SHA1(String input) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        byte[] encodedHash = sha1.digest(input.getBytes(StandardCharsets.UTF_8));   // maybe only ASCII ¿?
        StringBuffer hexString = new StringBuffer();
        for (byte anEncodedHash : encodedHash) {
            hexString.append(Integer.toString((anEncodedHash & 0xff) + 0x100, 16).substring(1));
        }
        return hexString.toString();
    }

    public static String SHA256(String input) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = sha256.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuffer hexString = new StringBuffer();
        for (byte anEncodedHash : encodedHash) {
            hexString.append(Integer.toString((anEncodedHash & 0xff) + 0x100, 16).substring(1));
        }
        return hexString.toString();
    }

    public static String SHA512(String input) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-512");
        byte[] encodedHash = sha256.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuffer hexString = new StringBuffer();
        for (byte anEncodedHash : encodedHash) {
            hexString.append(Integer.toString((anEncodedHash & 0xff) + 0x100, 16).substring(1));
        }
        return hexString.toString();
    }

    protected static boolean isValidHash(String s, String type) { // only useful if hash is read as an argument via console
        if(Main.types.get(0).equalsIgnoreCase(type)) return isValidMD5(s);
        if(Main.types.get(1).equalsIgnoreCase(type)) return isValidSHA1(s);
        if(Main.types.get(2).equalsIgnoreCase(type)) return isValidSHA256(s);
        if(Main.types.get(3).equalsIgnoreCase(type)) return isValidSHA512(s);
        if(type.equalsIgnoreCase("all"))  return (isValidMD5(s) || isValidSHA1(s) || isValidSHA256(s) || isValidSHA512(s));
        else return false;
    }

    private static boolean isValidMD5(String s) {
        return s.matches("^[a-fA-F0-9]{32}$");
    }

    private static boolean isValidSHA1(String s) {
        return s.matches("^[a-fA-F0-9]{40}$");
    }

    private static boolean isValidSHA256(String s) {
        return s.matches("^[a-fA-F0-9]{64}$");
    }

    private static boolean isValidSHA512(String s) {
        return s.matches("^[a-fA-F0-9]{128}$");
    }

}
