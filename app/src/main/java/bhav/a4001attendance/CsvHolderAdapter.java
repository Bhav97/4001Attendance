package bhav.a4001attendance;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import bhav.a4001attendance.utils.FileUtils;
import bhav.a4001attendance.widget.AutoResizeTextView;

public class CsvHolderAdapter extends RecyclerView.Adapter<CsvHolderAdapter.CsvViewHolder> {

    private List<String> csvs;
    private Context context;

    public CsvHolderAdapter(List<String> csvs, Context context) {
        this.csvs = csvs;
        this.context = context;
    }


    @Override
    public CsvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CsvViewHolder(((LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.itemview_csv, parent, false));
    }

    @Override
    public void onBindViewHolder(CsvViewHolder holder, int position) {
        holder.name.setText(csvs.get(position).split("\\.")[0]);
    }

    @Override
    public int getItemCount() {
        return csvs.size();
    }

    public class CsvViewHolder extends RecyclerView.ViewHolder {

        public AutoResizeTextView name;
        public ImageView more;

        public CsvViewHolder(View itemView) {
            super(itemView);
            name = (AutoResizeTextView) itemView.findViewById(R.id.name);
            more = (ImageView) itemView.findViewById(R.id.more);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent att = new Intent(context, AttendanceActivity.class);
                    att.putExtra("filename", csvs.get(getAdapterPosition()));
                    context.startActivity(att);
                }
            });
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setMessage("What would you like to do?")
                            .setTitle("CSV Options")
                            .setNegativeButton("Export", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        FileUtils.exportFile(FileUtils
                                                .getInternalFile(context, csvs.get(getAdapterPosition())));
                                        Toast.makeText(context, "Exported to /sdcard/Attendance",
                                                Toast.LENGTH_SHORT).show();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Toast.makeText(context, R.string.msg_export_fail, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent edit = new Intent(context, EditActivity.class);
                            edit.putExtra("filename", csvs.get(getAdapterPosition()));
                            context.startActivity(edit);
                        }
                    }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setCancelable(true).create().show();
                }
            });
        }
    }
}
