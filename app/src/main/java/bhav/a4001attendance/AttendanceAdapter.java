package bhav.a4001attendance;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import bhav.a4001attendance.model.Student;
import bhav.a4001attendance.widget.AutoResizeTextView;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.StudentHolder> {

    private final List<Student> studentList;
    private final Context context;

    public AttendanceAdapter(List<Student> studentList, Context context) {
        this.studentList = studentList;
        this.context = context;

    }

    @Override
    public StudentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        StudentHolder sh =
        return new StudentHolder(((LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.itemview_student, parent, false));
//        sh.present.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                sh.get
//            }
//        });

//        return sh;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    @Override
    public void onViewRecycled(StudentHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onBindViewHolder(StudentHolder holder, int position) {
        final Student student = getStudent(position);
        holder.present.setChecked(student.present);
//        holder.present.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                student.present = isChecked;
//            }
//        });
        holder.name.setText(student.name);
        holder.regno.setText(student.regno);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public Student getStudent(int pos) {
        return studentList.get(pos);
    }

    public class StudentHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView regno;
        public CheckBox present;

        public StudentHolder(View itemView) {
            super(itemView);
            name = (AutoResizeTextView) itemView.findViewById(R.id.name);
            regno = (TextView) itemView.findViewById(R.id.regno);
            present = (CheckBox) itemView.findViewById(R.id.present);

            present.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    getStudent(getAdapterPosition()).present = isChecked;
                }
            });
        }
    }
}
