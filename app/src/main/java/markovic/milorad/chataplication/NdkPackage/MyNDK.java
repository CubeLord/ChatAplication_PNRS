package markovic.milorad.chataplication.NdkPackage;

public class MyNDK {
    static {
        System.loadLibrary("myJni");
    }

    public native String getMyString();

    public native String encryptString(String S, String key);

    public native String decryptString(String S, String key);
}
