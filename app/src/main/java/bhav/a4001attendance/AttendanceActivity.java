package bhav.a4001attendance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import bhav.a4001attendance.utils.CSVUtils;
import bhav.a4001attendance.utils.FileUtils;

public class AttendanceActivity extends AppCompatActivity {

    private final static String TAG = AttendanceActivity.class.getSimpleName();

    private RecyclerView students;
    private Button saveButton;
    private AttendanceAdapter adapter;
    private InputStream is = null;
    private File csv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        students = (RecyclerView) findViewById(R.id.students);
        saveButton = (Button) findViewById(R.id.save);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle(getTitle() + " for " + getDate());

        csv = new File(FileUtils.getCSVDirectory(this),
                getIntent().getStringExtra("filename"));
        try {
            is = new FileInputStream(csv);
            adapter = new AttendanceAdapter(CSVUtils.getStudentList(is), this);
            students.setLayoutManager(new LinearLayoutManager(this));
            students.setAdapter(adapter);
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.msg_parse_failure, Toast.LENGTH_LONG).show();
            finish();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        saveButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                saveAttendance();
                return true;
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AttendanceActivity.this, R.string.action_hold, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveAttendance() {
        try {
            if (CSVUtils.saveStudentAttendance(adapter.getStudentList(), csv, getDate(), this)) {
                Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to save Attendance", Toast.LENGTH_SHORT).show();
            }
            finish();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private String getDate() {
        String date;
        Calendar c = Calendar.getInstance();
        date = String.valueOf(c.get(Calendar.DATE));
        // Java time :(
        date += '/' + String.valueOf(c.get(Calendar.MONTH) + 1);
        date += '/' + String.valueOf(c.get(Calendar.YEAR));
        return date;
    }
}
