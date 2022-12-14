package realZone;
import dt.original.Bessj;
import faultZone.FaultZone;
import model.Parameters;
import model.Testcase;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Calendar;

public class realZone_Bessj extends FaultZone {

    Bessj correct = new Bessj();
    Bessj mutation;
    Method method;
    Class<?>[] paramters;
    int pcount = 0;


    public realZone_Bessj(Constructor constructor, Method m) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        this.mutation = (Bessj) constructor.newInstance();
        pcount = m.getParameterCount();
        paramters = m.getParameterTypes();
        method = m;
    }

    @Override
    public Boolean isCorrect(Testcase testcase) throws IllegalArgumentException {
        int x = (int) testcase.getValue(0);
        double y = testcase.getValue(1);
//        System.out.println();
//        System.out.println("x = " + x + " y = " + y);
//        return false;
        try {
            double a = correct.bessj(x,y);
            double b = mutation.bessj(x,y);
            if(a==b) return true;
        }
        catch (Exception e){
            return false;
        }
        return false;
    }
}
