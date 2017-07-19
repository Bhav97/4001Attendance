package bhav.a4001attendance;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import bhav.a4001attendance.utils.FileUtils;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private final static String TAG = ExampleInstrumentedTest.class.getSimpleName();
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("bhav.a4001attendance", appContext.getPackageName());
    }

    @Test
    //doesn't fucking work :(
    public void testCSVList() {
        List<String> stu = FileUtils.getCSVList(InstrumentationRegistry.getTargetContext());
        if(stu.size() != 0) {
            Log.d(TAG, stu.get(0));
        } else {
            Log.d(TAG, "0");
        }
    }
}
