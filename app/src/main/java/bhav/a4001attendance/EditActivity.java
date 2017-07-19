package bhav.a4001attendance;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import bhav.a4001attendance.utils.CSVUtils;
import bhav.a4001attendance.utils.FileUtils;

//todo merge into attendance activity
public class EditActivity extends AppCompatActivity {

    private final static String TAG = EditActivity.class.getSimpleName();

    private RecyclerView students;
    private Button saveButton;
    private AttendanceAdapter adapter;
    File csv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        students = (RecyclerView) findViewById(R.id.students);
        saveButton = (Button) findViewById(R.id.save);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("Editing for");

        csv = new File(FileUtils.getCSVDirectory(this),
                getIntent().getStringExtra("filename"));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditActivity.this, "Tap and hold to save", Toast.LENGTH_SHORT).show();
            }
        });

        try {
            adapter = new AttendanceAdapter(CSVUtils.getStudentList(new FileInputStream(csv)), this);
            students.setLayoutManager(new LinearLayoutManager(this));
            students.setAdapter(adapter);
            List<String> dates = CSVUtils.getDates(new FileInputStream(csv));
            if (dates.isEmpty()) {
                Toast.makeText(this, "No Attendance data to edit", Toast.LENGTH_SHORT).show();
                finish();
            }
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                    android.R.layout.select_dialog_singlechoice);
            arrayAdapter.addAll(dates);

            new AlertDialog.Builder(this)
                    .setTitle("Select Date")
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            finish();
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final String date = arrayAdapter.getItem(which);
                    EditActivity.this.setTitle(EditActivity.this.getTitle() + " " + date);
                    new AlertDialog.Builder(EditActivity.this)
                            .setMessage(date)
                            .setTitle("Date Selected")
                            .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    try {
                                        List<String> attendance = CSVUtils.getAttendanceOn(date,
                                                new FileInputStream(csv));
                                        for(int idx=0; idx< attendance.size();idx++) {
                                            adapter.getStudentList().get(idx).present
                                                    = (attendance.get(idx).equals("P"));
                                            adapter.notifyDataSetChanged();

                                        }
                                        saveButton.setOnLongClickListener(new View.OnLongClickListener() {
                                            @Override
                                            public boolean onLongClick(View v) {
                                                try {
                                                    if(CSVUtils.saveAttendanceOn(date, csv, adapter.getStudentList(),
                                                            EditActivity.this)) {
                                                        Toast.makeText(EditActivity.this, "Saved successfully", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(EditActivity.this, "Epic Fail!", Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                    Toast.makeText(EditActivity.this, "Could not save", Toast.LENGTH_SHORT).show();
                                                }
                                                finish();
                                                return true;
                                            }
                                        });
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Toast.makeText(EditActivity.this, "Failed to retrieve attendance", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            })
                            .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        CSVUtils.deleteAttendanceOn(date, csv, EditActivity.this);
                                        Toast.makeText(EditActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Toast.makeText(EditActivity.this, "Could not delete", Toast.LENGTH_SHORT).show();
                                    }
                                    dialog.dismiss();
                                    finish();
                                }
                            })
                            .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            })
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    finish();
                                }
                            })
                            .setCancelable(false)
                            .create().show();
                }
            }).create().show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.msg_parse_failure, Toast.LENGTH_LONG).show();
            finish();
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