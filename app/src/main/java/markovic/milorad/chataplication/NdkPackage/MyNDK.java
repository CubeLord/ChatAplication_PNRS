package markovic.milorad.chataplication.NdkPackage;

public class MyNDK {
    static {
        System.loadLibrary("libmyjni.so");
    }

    public native String getMyString();
}
