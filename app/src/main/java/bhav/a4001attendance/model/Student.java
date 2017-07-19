package bhav.a4001attendance.model;

public class Student {

    public final String name;
    public final String regno;
    public final String program;
    public final String school;
    public final String mode;
    public final String regStatus;
    public final String studentStatus;
    public boolean present = false;
    //6,BEE,15BEE0012,NEHA SINHA,SELECT,Regular,Registered and Approved,ADMITTED
    public Student(String program, String regno, String name, String school, String mode,
                   String regStatus, String studentStatus) {
        this.name = name;
        this.regno= regno;
        this.program = program;
        this.school = school;
        this.mode = mode;
        this.regStatus = regStatus;
        this.studentStatus = studentStatus;
    }
}
