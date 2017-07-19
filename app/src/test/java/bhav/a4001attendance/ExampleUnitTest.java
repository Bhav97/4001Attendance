package bhav.a4001attendance;

import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import bhav.a4001attendance.model.Student;
import bhav.a4001attendance.utils.CSVUtils;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void parserTest() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("sample.csv");
        List<Student> studentList = CSVUtils.getStudentList(is);
        for (Student s: studentList) {
            System.out.println(s.name + " - " + s.regno);
        }
        assertEquals("Anubhav Raina" , studentList.get(1).name);
        assertEquals("15BEE0002" , studentList.get(1).regno);
    }
}