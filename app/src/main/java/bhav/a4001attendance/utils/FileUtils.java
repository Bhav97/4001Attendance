package bhav.a4001attendance.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FileUtils {

    private final static String TAG = FileUtils.class.getSimpleName();

    private static boolean copyFile(File src, File dst) throws IOException {
        if (src.getAbsolutePath().equals(dst.getAbsolutePath())) {
            return true;
        } else {
            InputStream is = new FileInputStream(src);
            OutputStream os = new FileOutputStream(dst);
            byte[] buff = new byte[1024];
            int len;
            while ((len = is.read(buff)) > 0) {
                os.write(buff, 0, len);
            }
            is.close();
            os.close();
        }
        return true;
    }

    public static boolean copyFile(String inFileUri, File outFile) throws IOException {
        return !outFile.exists() && copyFile(new File(inFileUri), outFile);
    }

    public static boolean exportFile(File src) throws IOException {
        File dst = new File(Environment.getExternalStorageDirectory()
                + File.separator + "Attendance" + File.separator);
        Log.d(TAG, src.getName());
        Log.d(TAG, dst.getName());
        if (!dst.exists()) {
            if (!dst.mkdir()) {
                return false;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd-HH:mm:ss", Locale.ENGLISH)
                .format(new Date());
        File expFile = new File(dst.getPath() + File.separator + src.getName().split("\\.")[0]
                + timeStamp + ".csv");
        Log.d(TAG, expFile.getName());
        return copyFile(src, expFile);
    }


    public static File getInternalFile(Context context, String filename) {
        return new File(getCSVDirectory(context), filename);
    }

    public static File getCSVDirectory(Context context) {
        return context.getDir("csv", Context.MODE_PRIVATE);
    }

    public static List<String> getCSVList(Context context) {
        File csvDirectory = context.getDir("csv", Context.MODE_PRIVATE);
        List<File> files = Arrays.asList(csvDirectory.listFiles());
        List<String> csvs = new ArrayList<>();
        for (File f : files) {
            csvs.add(f.getName());
        }
        return csvs;
    }
}
