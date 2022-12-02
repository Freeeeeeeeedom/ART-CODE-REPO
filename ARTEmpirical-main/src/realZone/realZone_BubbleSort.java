package realZone;
import dt.original.Bessj;
import dt.original.BubbleSort;
import faultZone.FaultZone;
import model.Parameters;
import model.Testcase;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

public class realZone_BubbleSort extends FaultZone {

    BubbleSort correct = new BubbleSort();
    BubbleSort mutation;
    Method method;
    Class<?>[] paramters;
    int pcount = 0;


    public realZone_BubbleSort(Constructor constructor, Method m) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        this.mutation = (BubbleSort) constructor.newInstance();
        pcount = m.getParameterCount();
        paramters = m.getParameterTypes();
        method = m;
    }

    @Override
    public Boolean isCorrect(Testcase testcase) throws IllegalArgumentException {
        int x = (int) testcase.getValue(0);
        int y = (int) testcase.getValue(1);
//        System.out.println();
//        System.out.println("x = " + x + " y = " + y);
//        return false;
        int length = new Random().nextInt(100) + 2;
        int[] arr = new int[length];
        arr[0] = x;
        arr[1] = y;
        for(int i=0;i<length;i++){
            arr[i] = x * y * (1 + new Random().nextInt(10));
        }
        try {
            int[] a = arr.clone();
            int[] b = arr.clone();
            correct.exe(a);
            mutation.exe(b);
            if(Arrays.equals(a, b)) return true;
        }
        catch (Exception e){
            return false;
        }
        return false;
    }
}
