package PBS;

import model.Testcase;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class Proportional implements SubDomainSelection{
    public List<Testcase> total = null;

    static double p1 = 0.5;
    static double p2 = 0.5;
    static int cnt = 0;
    //p1 p2 代表位于边缘和中心的概率
    @Override
    public List<Testcase> select(List<List<Testcase>> SubDomains) {
        List<Testcase> selectedDomain = SubDomains.get(0);

        int proportion = new Random().nextInt(100);

        int l = SubDomains.size()/4;
        int r = SubDomains.size()/4 * 3;
        //边缘定义为[0,l] [r,n]
        if(l < (proportion * 100)/(SubDomains.size()) && r > (proportion * 100) / SubDomains.size()){
            p1 = (cnt * p1 )/(cnt+1);
            p2 = 1 - p1;
            cnt++;
        }
        else{
            p1 = (cnt * p1 + 1) /(cnt + 1);
            p2 = 1 - p1;
            cnt++;
        }

        if(p1 > p2){
            int a = new Random().nextInt(100);
            if(a >= 50){
                selectedDomain = SubDomains.get(new Random().nextInt(l));
            }else{
                selectedDomain = SubDomains.get(new Random().nextInt(r) + l);
            }
        }
        else{
            selectedDomain = SubDomains.get(new Random().nextInt(r-l) + l);
        }

        return selectedDomain;
    }

    @Override
    public void SetTotal(List<Testcase> total){this.total = total;}
}
