package PBS;

import model.Testcase;

import java.util.List;

public class NoTestCaseInTarget implements SubDomainSelection{
    public List<Testcase> total = null;
    @Override
    public List<Testcase> select(List<List<Testcase>> SubDomains) {
        return null;
    }

    @Override
    public void SetTotal(List<Testcase> total){
        this.total = total;
    }
}
