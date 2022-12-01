package realZone;
import dt.original.Bessj;
import faultZone.FaultZone;
import model.Testcase;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;

public class realZone_Bessj extends FaultZone {

    Bessj correct = new Bessj();
    Bessj mutation;
    public realZone_Bessj(Constructor constructor) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        this.mutation = (Bessj) constructor.newInstance();
    }

    @Override
    public Boolean isCorrect(Testcase testcase) throws IllegalArgumentException {
        boolean jutice = true;
        int x = (int) testcase.getValue(0);
        double y = testcase.getValue(1);
//        System.out.println();
//        System.out.println("x = " + x + " y = " + y);
//        return false;
        try {
            double a = correct.bessj(x,y);
            double b = mutation.bessj(x,y);
            return a == b;
        }
        catch (Exception e){
            return false;
        }
    }
}
