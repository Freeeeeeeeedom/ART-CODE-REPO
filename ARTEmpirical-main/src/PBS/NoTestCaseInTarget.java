package PBS;

import model.Testcase;

import java.util.ArrayList;
import java.util.List;


/**
 * PBS 代码实现
 * K. K. Sabor and S. Thiel, “Adaptive random testing by static
 * partitioning,” in Proceedings of the 10th International Workshop on
 * Automation of Software Test (AST’15), 2015, pp. 28–32.
 *
 */

public class NoTestCaseInTarget implements SubDomainSelection{
    public List<Testcase> total = null;
    @Override
    public List<Testcase> select(List<List<Testcase>> SubDomains) {
        List<Testcase> selectedDomain = SubDomains.get(0);
        for(int i=0;i<SubDomains.size();i++){
            boolean flag = true;
            if(i-1>=0){
                List<Testcase> temp = new ArrayList<>(SubDomains.get(i-1));
                int size = temp.size();
                temp.retainAll(total);
                if(temp.size() != size) flag = false;
            }
            if(i+1<SubDomains.size()){
                List<Testcase> temp = new ArrayList<>(SubDomains.get(i+1));
                int size = temp.size();
                temp.retainAll(total);
                if(size != temp.size()) flag = false;
            }
            if(flag){
                List<Testcase> temp = new ArrayList<>(SubDomains.get(i));
                int size = temp.size();
                temp.retainAll(total);
                if(temp.size() != size) continue;
                selectedDomain = SubDomains.get(i);
                break;
            }
        }
        return selectedDomain;
    }

    @Override
    public void SetTotal(List<Testcase> total){this.total = total;}
}
