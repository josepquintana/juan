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

}
