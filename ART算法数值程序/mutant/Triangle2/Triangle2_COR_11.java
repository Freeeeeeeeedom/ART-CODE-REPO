package dt.mutant.Triangle2;
import dt.original.*;

// This is mutant program.
// Author : ysma

public class Triangle2_COR_11 extends Triangle2
{

    public int exe( int a, int b, int c )
    {
        if (a <= 0 || b <= 0 || c <= 0) {
            return 1;
        }
        int tmp = 0;
        if (a == b) {
            tmp = tmp + 1;
        }
        if (a == c) {
            tmp = tmp + 2;
        }
        if (b == c) {
            tmp = tmp + 3;
        }
        if (tmp == 0) {
            if (a + b <= c || b + c <= a || a + c <= b) {
                tmp = 1;
            } else {
                tmp = 2;
            }
            return tmp;
        }
        if (tmp > 3) {
            tmp = 4;
        } else {
            if (tmp == 1 && a + b > c) {
                tmp = 3;
            } else {
                if (tmp == 2 || a + c > b) {
                    tmp = 3;
                } else {
                    if (tmp == 3 && b + c > a) {
                        tmp = 3;
                    } else {
                        tmp = 1;
                    }
                }
            }
        }
        return tmp;
    }

}
