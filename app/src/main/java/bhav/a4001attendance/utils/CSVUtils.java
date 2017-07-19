
package bhav.a4001attendance.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bhav.a4001attendance.model.Student;


public class CSVUtils {
    private static final String TAG = CSVUtils.class.getSimpleName();

    public static List<Student> getStudentList(InputStream csvInputStream) throws IOException,
            IndexOutOfBoundsException {
        List<Student> studentList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(csvInputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            studentList.add(new Student(data[1], data[2], data[3], data[4],
                    data[5], data[6], data[7]));
//            Log.d(TAG, String.valueOf(studentList.size()));
        }
        reader.close();
        studentList.remove(0);
        return studentList;
    }

    //merge with saveattendanceon pls
    public static boolean saveStudentAttendance(List<Student> studentList,
                                                File inputFile,
                                                String date,
                                                Context context)
            throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
        File tmpFile = new File(context.getDir("csv", Context.MODE_PRIVATE), inputFile.getName() +
                "-tmp");
        String line;
        String status;
        //save date as column
        line = reader.readLine();
        if (checkIfExists(line, date)) {
            reader.close();
            return false;
        }
        OutputStream csvOutputStream = new FileOutputStream(tmpFile);
        line += "," + date + "\n";
        csvOutputStream.write(line.getBytes());
        //save attendance in each rows end
        for (int idx = 0; idx < studentList.size(); idx++) {
            if ((line = reader.readLine()) != null) {
                Log.d(TAG, line);
                status = studentList.get(idx).present ? "P\n" : "A\n";
                csvOutputStream.write((line + "," + status).getBytes());
            }
        }
        reader.close();
        csvOutputStream.close();
        return inputFile.delete() && tmpFile.renameTo(inputFile);
    }

    private static boolean checkIfExists(String line, String date) {
        for (String s : line.split(",")) {
            if (s.equals(date)) {
                return true;
            }
        }
        return false;
    }

    public static boolean deleteAttendanceOn(String date, File inputFile,
                                             Context context) throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
        File tmpFile = new File(context.getDir("csv", Context.MODE_PRIVATE), inputFile.getName() +
                "-tmp");
        OutputStream csvOutputStream = new FileOutputStream(tmpFile);
        String line;
        String header = "";
        if ((line = reader.readLine()) != null) {
            String[] dates = line.split(",");
            for (String s : dates) {
                if (!s.equals(date)) {
                    header += s + ",";
                }
            }
            csvOutputStream.write((header.substring(0, header.length() - 1) + "\n").getBytes());
            for (int idx = 0; idx < dates.length; idx++) {
                if (date.equals(dates[idx])) {
                    while ((line = reader.readLine()) != null) {
                        String[] pieces = line.split(",");
                        String singleLine = "";
                        for (int jdx = 0; jdx < pieces.length; jdx++) {
                            if (jdx != idx) {
                                singleLine += pieces[jdx] + ",";
                            }
                        }
                        csvOutputStream.write((singleLine.substring(0, singleLine.length() - 1) + "\n").getBytes());
                    }
                }
            }
        }
        reader.close();
        csvOutputStream.close();
        return inputFile.delete() && tmpFile.renameTo(inputFile);
    }

    public static List<String> getAttendanceOn(String date, FileInputStream csvInputStream)
            throws IOException {
        List<String> attendance = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(csvInputStream));
        String line;
        if ((line = reader.readLine()) != null) {
            String[] dates = line.split(",");
            for (int idx = 0; idx < dates.length; idx++) {
                if (date.equals(dates[idx])) {
                    while ((line = reader.readLine()) != null) {
                        attendance.add(line.split(",")[idx]);
                    }
                    reader.close();
                    csvInputStream.close();
                    return attendance;
                }
            }
        }
        reader.close();
        csvInputStream.close();
        return new ArrayList<>();
    }

    public static boolean saveAttendanceOn(String date, File inputFile, List<Student> studentList,
                                           Context context) throws IOException {
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
        File tmpFile = new File(context.getDir("csv", Context.MODE_PRIVATE), inputFile.getName() +
                "-tmp");
        OutputStream csvOutputStream = new FileOutputStream(tmpFile);
        String line;
        int jdx = 0;
        if ((line = reader.readLine()) != null) {
            csvOutputStream.write((line + "\n").getBytes());
            String[] dates = line.split(",");
            for (int idx = 0; idx < dates.length; idx++) {
                if (date.equals(dates[idx])) {
                    while ((line = reader.readLine()) != null) {
                        String[] data = line.split(",");
                        String singleLine = "";
                        data[idx] = studentList.get(jdx++).present ? "P" : "A";
                        for (String token : data) {
                            singleLine += token + ",";
                        }
                        csvOutputStream.write((singleLine.substring(0, singleLine.length() - 1) + "\n").getBytes());
                    }
                }
            }
        }
        reader.close();
        csvOutputStream.close();
        return inputFile.delete() && tmpFile.renameTo(inputFile);
    }

    public static List<String> getDates(FileInputStream csvInputStream) throws IOException {
        List<String> dates = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(csvInputStream));
        String line;
        if ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            dates.addAll(Arrays.asList(data).subList(8, data.length));
        }
        reader.close();
        return dates;
    }
}
