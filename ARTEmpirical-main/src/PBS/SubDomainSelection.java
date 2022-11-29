package PBS;

import model.Testcase;

import java.util.List;

public  interface SubDomainSelection {
     List<Testcase> select(List<List<Testcase>> SubDomains);
}
