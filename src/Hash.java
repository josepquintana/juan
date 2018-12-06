import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash
{
    public static String SHA1(String input) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        byte[] result = sha1.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (byte aResult : result) {
            sb.append(Integer.toString((aResult & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static boolean compareSHA256(String input, String hash) {

        return false;
    }

    public static boolean compareSHA512(String input, String hash) {

        return false;
    }

}
