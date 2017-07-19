package bhav.a4001attendance;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import bhav.a4001attendance.utils.FileUtils;

public class BaseActivity extends AppCompatActivity {

    private final static String TAG = BaseActivity.class.getSimpleName();
    private static final int REQUEST_CODE_STORAGE = 33;


    private FloatingActionButton fab;
    private RecyclerView holder;

    private ImageView nfImage;
    private TextView nfText;
    private CsvHolderAdapter adapter;

    private List<String> csvs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setTitle("Select or import file");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        holder = (RecyclerView) findViewById(R.id.holder);

        nfImage = (ImageView) findViewById(R.id.nf_image);
        nfText = (TextView) findViewById(R.id.nf_text);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat
                    .shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(this).setTitle("Permission Required")
                        .setMessage(R.string.rationale_write_storage)
                        .setCancelable(false)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                askPermission();
                            }
                        }).create().show();
            } else {
                askPermission();
            }
        }

        csvs = new ArrayList<>();
        updateList();
        adapter = new CsvHolderAdapter(csvs, this);
        holder.setLayoutManager(new LinearLayoutManager(this));
        holder.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialFilePicker()
                        .withActivity(BaseActivity.this)
                        .withRequestCode(1)
                        .withFilter(Pattern.compile(".*\\.csv$"))
                        .withFilterDirectories(false)
                        .start();
            }
        });
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, REQUEST_CODE_STORAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {

            String uri = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            String[] pieces = uri.split("/");
            try {
                if (!FileUtils.copyFile(uri,
                        FileUtils.getInternalFile(this, pieces[pieces.length - 1]))) {
                    Toast.makeText(this, "File exists, please rename", Toast.LENGTH_SHORT).show();
                }
                updateList();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Failed to load file", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateList() {
        //probably not a good idea for a big list, but meh
        csvs.clear();
        csvs.addAll(FileUtils.getCSVList(this));
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        show_nf(csvs.isEmpty());

    }

    private void show_nf(boolean show) {
        nfImage.setVisibility(show ? View.VISIBLE : View.GONE);
        nfText.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
