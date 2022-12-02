# ART总结

**ART测试小组：**

| 学号      | 成员   |
| --------- | ------ |
| 201250026 | 胡睿   |
| 201250013 | 杨昊锦 |

## 1.项目介绍

本项目针对数值程序领域，旨在复现现有的经典的自适应随机测试算法，并对其进行有效性比较。项目的基本框架来自于 [ARTEmpirical-main](https://box.nju.edu.cn/d/9288e9a87a5c4c3c961e/), 我们在框架代码的基础上实现了多种自适应测试算法，以及检测真实数值程序的测试功能。

对于此次实验，我们实现了以下功能：

1. 对自适应测试算法的实现，

## 2. 主体框架介绍

**src/faultZone**

Simulation settings, no modification required strore the various failure pattern including:

| Class                      | Intro                                                        |
| -------------------------- | ------------------------------------------------------------ |
| FaultZone (abstract class) | check whether the testcase is located in this failure region |
| FaultZone_Block            | the block failure pattern                                    |
| FaultZone_Point            | the point failure patter                                     |
| FautZone_Strip             | The Strip failure pattern                                    |

### **src/algorithm**

 ART  Algorithms, all extend model/AbstractART

stored algorithms included:

| Class  | Intro                                                        |
| ------ | ------------------------------------------------------------ |
| fscs   | The basic method of ART generates several candidates, select the furthest one from executed testcases, using Euclidean Distance, including FSCS_art |
| hybrid | Combine several methods together to generate and select testcases , including Divide_Conquer_art, EAR_qrt |
| mart   | Patition the input boundary to a source domain and several mirror partition. include Mirror_art,  RBMT_art |
| pbs    | Divides the input domain into a number of subdomins, choosing one as the location within which to generate the next test case |
| qrs    | Using Low Discrepancy Sequence to generate a more uniform distribution including QRS_Halton_art, QRS_Sobol_art |
| rrt    | After selecting a testcase, stop selecting testcases around executed include ORRT |
| sbs    | Uses search-based algorithms to achieve the even-spreading of test cases over the input domain |

### **src/model**

| Class              | Intro                                                        |
| ------------------ | ------------------------------------------------------------ |
| AbstractART        | abstract method, all ART algorithms must inherit from this class |
| Dimension          | determine the dimension of input domain.                     |
| DomainBoundary     | determine the boundary of input domain.                      |
| Parameters         | This class stores some of the parameters necessary for the ART algorithm. |
| TestCase           | The generated test cases by various ART algorithms           |
| ThreadWithCallback | For multi-threaded concurrency, terminate the current thread when executing a partial variant of a dead loop or exception |

### **src/simulation**

Some necessary parameters are hard-coded into the code and subsequently need to be decoupled into the `src/model/Parameters`. At this stage, the parameters can be modified directly in the code to test the correctness of the algorithm.

| Class             | Intro                                                        |
| ----------------- | ------------------------------------------------------------ |
| StoreResults      | Store results                                                |
| TestEffectiveness | This class mainly tests the effectiveness of the ART algorithm under simulation |
| TestEfficiency    | This class mainly tests the efficiency of the ART algorithm under simulation |

### **src/dt**

Here stores the data programme used in our test, data source from [南大云盘 NJU Box](https://box.nju.edu.cn/d/9288e9a87a5c4c3c961e/)

### src/realZone

| Class                 | Intro                                                        |
| --------------------- | ------------------------------------------------------------ |
| ClassUtil             | Method and class name resolution for real program classes    |
| RealTestEffectiveness | This class mainly tests the effectiveness of the ART algorithm under a real program |
| RealTestEfficiency    | This class mainly tests the efficiency of the ART algorithm under a real program |

## 3. 测试算法介绍

### **1. FSCS**

框架代码提供的基础算法

来源：2006-A Survey on Adaptive Random Testing

FSCS算法的逻辑是优先选择离未触发缺陷的测试用例更远的测试用例。ART认为，在测试用例的设计中，能诱发错误的输入从分布上来看更倾向于集中在一块，因此生成的输出结果应当远离已执行的，未触发错误的测试用例。

伪代码：

接口介绍

| 接口                                          | 功能                                                         |
| --------------------------------------------- | ------------------------------------------------------------ |
| public FSCS_art(model.DomainBoundary ,Double) | 基础的构造函数                                               |
| public Testcase Best_Candidate()              | 继承自AbstractART类，实现逻辑和伪代码展示相同                |
| public void TestEffiency(int)                 | 继承自AbstractART类，测试ART的效率，生成pointNum个测试用例所需的时间 |

### **2. ORRT**

Original Restricted Random Test

来源：2006-DBLP-Restricted Random Testing: Adaptive Random Testing by Exclusion.

ORRT的底层逻辑和FSCS相似，同样认为错误输入相对集中，因此ORRT期望对正确输入的筛选分布达到接近1，从而使得每次输入都能够诱发错误。ORRT在已执行的正确输入周围设置隔离域，隔离域对生成用例区的总覆盖度是一定的，新生成的测试用例在隔离域之外。

接口介绍

| 接口                                    | 功能                                                         |
| --------------------------------------- | ------------------------------------------------------------ |
| public ORRT_art(DomainBoundary, Double) | 构造函数                                                     |
| public Testcase Best_Candidate()        | 继承自AbstractART类，实现逻辑和算法展示相同，设置一个ExecutionZone，判定新生成用例于已生成输入的欧氏距离 |
| public void TestEffiency(int)           | 继承自AbstractART类，测试ART的效率，生成pointNum个测试用例所需的时间 |

### **3. PBS**

 Partitioning-Based Strategy

来源：2006-A Survey on Adaptive Random Testing

算法介绍：PBS主要由两个过程，第一个是分区模块，第二个是子块选择，首先将测试域的测试用例集按照分区算法分成SubDomains，然后根据子块选择算法选择指定子块，从字块中选出BestCandidate(),我们这里采用随机分区的方式，对分区后的测试集采用四种不同的子块选择算法。

伪代码：

![img](https://cq964bfarp.feishu.cn/space/api/box/stream/download/asynccode/?code=MjVhOTQxZTg3M2NmZTg0M2QxYTFkZDJkY2Y5N2RlOWJfbnVkeTlVY3poT29oVEZFVk96aEdXd0xkd3p3TmZydmtfVG9rZW46Ym94Y25VMzdyZXpWaVlPcHlOcERZS2dXUUZlXzE2Njk5ODg2NDc6MTY2OTk5MjI0N19WNA)

#### **Partitioning-schema component**

子域划分准则

**静态分区**

测试人员提前分区

**随机分区**

随机生成Test Case作为断点进行划分，将输入域划分为更小的子域

**二分分区**

类似于静态分区，但是动态分区

**迭代分区**

与二分分区相反，修改现有的分区，输入域分成大小的相等的子域

#### **Subdomain-Selection Component**

子域选择模块

1. Maximum size 选择

1. FewestPreviouslyGenerated 选择

1. NoTestCaseInTargert 选择

1. Proportional 选择

具体实现，由于PBS的四种实现的主题逻辑相似，我们采用策略模式，通过添加成员变量strategy，四种stategy通过实现共同的接口来实现代码的复用

| public PBS_art(DomainBoundary inputBoundary, Double p)       | 构造函数                                                     |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| public Partitioning()                                        | 分区算法，我们这里根据不同的子块选择设定了不同的分区算法     |
| public Best_candidate()                                      | 根据不同的子块选择strategy，选出bestcandidate并返回          |
| public void TestEffiency(int)                                | 继承自AbstractART类，测试ART的效率，生成pointNum个测试用例所需的时间 |
| **public interface SubDomainSelection**                      | 公共接口，包含select和settotal两个方法                       |
| public class MaximumSize implements SubDomainSelection       | MaximumSize的子块选择策略具体实现                            |
| public class FewestPreviouslyGenerated implements SubDomainSelection | FewestPreviouslyGenerated的子块选择策略具体实现              |
| public class NoTestCaseInTarget implements SubDomainSelection | NoTestCaseInTarget 的子块选择策略具体实现                    |
| public class Proportional implements SubDomainSelection      | Proportional 的子块选择策略具体实现                          |

### **4. QRS**

 Quasi-Random Strategy

来源：2006-A Survey on Adaptive Random Testing

算法介绍：由于随机测试的输入生成实际上并不够随机，或者说纯随机生成的输入在输入域中并不均匀分布，因此QRS选择使用范德皮尔序列，即低差异序列来实现伪随机生成，由QRS生成的随机序列相较于纯随机生成更加均匀，因此对缺陷检测效率更高。

伪代码：

根据Quasi-random-sequence-selection component选择的随机序列的不同，我们提供了两种算法实现。

#### **4.1 QRS_Halton_art**

来源：2006-COMPUTATIONAL INVESTIGATIONS OF QUASIRANDOM SEQUENCES IN GENERATING TEST CASES FOR SPECIFICATION-BASED TESTS

这种算法使用Halton序列进行测试用例的生成。Halton序列通过将正常顺序的自然数通过base进制展开，再倒转指数进行表示，从而生成更均匀的随机数序列

接口介绍：

| public QRS_Halton_art(DomainBoundary inputBoundary, Double p) | 构造函数                                                     |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| public Testcase Best_candidate()                             | 先利用Halton序列生成一个测试用例，并返回该测试用例           |
| private void generateHalton(int num)                         | /读入需要生成的测试用例数目，为每个输入生成一个不重复的随机数，分别将该随机数用2和3展开（默认2维） |
| double RadicalInverse(int Base, int i)                       | i为第i位自然数，该算法将i按照base进制展开，再换成范德皮尔序列表示 |
| double Halton(int Dimension, int Index)                      | 调用RadicalInverse，返回生成数                               |
| public void TestEffiency(int)                                | 继承自AbstractART类，测试ART的效率，生成pointNum个测试用例所需的时间 |

#### **4.2 QRS_Sobol_art**

来源：2006-COMPUTATIONAL INVESTIGATIONS OF QUASIRANDOM SEQUENCES IN GENERATING TEST CASES FOR SPECIFICATION-BASED TESTS

这种算法使用Sobol序列进行测试用例的生成。Sobol序列是固定为2为底展开的Halton形式的序列，在实现Sobol序列时，我们使用了 [Sobol sequence generator](https://web.maths.unsw.edu.au/~fkuo/sobol/)中提供好的生成矩阵。放在文件中/ARTEmpirical-main/new-joe-kuo-7.21201

接口介绍

| public QRS_Sobol_art(DomainBoundary inputBoundary, Double p) throws IOException | 构造函数，同时载入生成矩阵                                   |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| public void setC() throws IOException                        | 将生成矩阵的内容从文件中读入到矩阵C中                        |
| public Testcase Best_candidate()                             | 调用generateSobol生成一个测试用例                            |
| public void generateSobol(int num)                           | Sobol序列与Halton的略有不同                                  |
| private double Sobol(int i, int Dimension)                   | 使用位操作实现，提高效率                                     |
| public void TestEffiency(int)                                | 继承自AbstractART类，测试ART的效率，生成pointNum个测试用例所需的时间 |

### **5. SBS**

Search-Based Strategy

来源：2006-A Survey on Adaptive Random Testing

算法介绍：SBS生成一个初始测试集群PT（大小为ps），其中每个测试集（大小为N）是随机生成的。然后使用基于搜索的算法来迭代演化迭代到其下一代。

伪代码：

![img](https://cq964bfarp.feishu.cn/space/api/box/stream/download/asynccode/?code=ZWU0M2NlMDA4OGI3YTg2MGIzZDVhMTFkOGIwOThiODJfSjRZMlVwV0RYUzZqeEJ0ekt3TVl3VWdKRzM5TW9qVUtfVG9rZW46Ym94Y25qMlVOSVYwbVdBMkVhbmk2cnNlTjk0XzE2Njk5ODg2NDc6MTY2OTk5MjI0N19WNA)

| public SBS_art(DomainBoundary inputBoundary, Double p) | 构造函数                                                     |
| ------------------------------------------------------ | ------------------------------------------------------------ |
| public Fitness()                                       | Fitness用于计算Testcase集的适配函数值                        |
| public Best_candidate()                                | 根据不同的子块选择strategy，选出bestcandidate并返回          |
| public void TestEffiency(int)                          | 继承自AbstractART类，测试ART的效率，生成pointNum个测试用例所需的时间 |
| **public interface Evolution**                         | 公共接口，包含evolution方法                                  |
| public class GeneticAlgorithm implements Evolution     | 基于遗传算法的演化策略类                                     |
| public class HillClimbing implements Evolution         | 基于登山算法的演化策略类                                     |
| public class LocalSpreding implements Evolution        | 基于本地传播算法的演化策略类                                 |
| public class SimulatedAnnealing implements Evolution   | 基于模拟退火算法的演化策略类                                 |
| public class SimulatedRepulsion implements Evolution   | 基于模拟驱逐算法的演化策略类                                 |

### **6.  Hybrid Strategies**

复合形ART算法，将作用在ART算法不同位置的方法结合使用。

来源：2006-A Survey on Adaptive Random Testing

算法介绍：由于复合算法在不同实现中的实现方式完全不同，因此将在算法详情中介绍

#### **6.1 Divide_Conquer_art**

STFCS + PBS

来源：2013-The ART of Divide and Conquer

算法介绍：将inputBoundary按照维度划分为SubDomain，这里我们设置的维度是2*2，当已执行的程序数目小于特定的lambda是，按照FSCS的方式生成测试用例。否则将已执行的测试用例按照位置分别存入到不同的SubDomain中，在当前数目最少的SubDomain的范围中随机生成测试用例，并加入到对应的SubDomain中。

接口介绍：

| public Divide_Conquer_art(DomainBoundary inputBoundary, Double p) | 构造函数                                                     |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| public Testcase Best_candidate()                             | 算法介绍的代码实现，应注意的是在该函数中实现了对SubDomain的插入和排序 |
| public void testEfficiency(int pointNum)                     | 继承自AbstractART类，测试ART的效率，生成pointNum个测试用例所需的时间 |

#### **6.2 EAR_art**

SBS + QRS

来源：2009-A Novel Evolutionary Approach for Adaptive Random Testing

算法介绍：EAR算法使用QRS中的低差异序列生成一个初始种群，再使用遗传算法迭代测试用例。在这里的具体实现中，我们SBS部分使用了sbs.GeneticAlgorithm和qrs.Sobol

接口介绍：

| public EAR_art(DomainBoundary inputBoundary, Double p) throws IOException | 构造函数                                                     |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| public void setC() throws IOException                        | 与QRS中的setC()相同                                          |
| public Testcase Best_candidate()                             | 先生成Sobol序列的测试用例集，再使用遗传算法生成测试用例      |
| public void generateSobol(int num)                           | 同QRS_Sobol_art中的同名函数                                  |
| public double Fitness(List<Testcase> T)                      | 同SBS的Fitness                                               |
| public void testEfficiency(int pointNum)                     | 继承自AbstractART类，测试ART的效率，生成pointNum个测试用例所需的时间 |

### **7. MART**

Mirror Adaptive Random Test

来源：2019-Random Border Mirror Transform: A diversity based approach to an effective and efficient mirror adaptive random testing

算法介绍：MART算法和PBS有点相似，不同的是Mirror的分区方式和分区后对测试用例的处理手段。MART将inputBoundary均匀的分成SourceDomain和Mirror Domain，并根据SourceDomain中的测试输入生成镜像输入。

#### **7.1 MART**

来源：2019-Random Border Mirror Transform: A diversity based approach to an effective and efficient mirror adaptive random testing

接口介绍：

| public Mirror_art(DomainBoundary inputBoundary, Double p) | 构造函数                                                     |
| --------------------------------------------------------- | ------------------------------------------------------------ |
| public Testcase Best_candidate()                          | 实现生成用例和镜像用例，使用FSCS选出新的测试用例             |
| public void testEfficiency(int pointNum)                  | 继承自AbstractART类，测试ART的效率，生成pointNum个测试用例所需的时间 |

#### **7.2 RBMT**

来源：2019-Random Border Mirror Transform: A diversity based approach to an effective and efficient mirror adaptive random testing

算法介绍：和普通的MART不同的是，RBMT对每一个镜像的MirrorDomain进行一个二维的偏移（我们固定了测试用例的维度）

接口介绍：

| RBMT_art(DomainBoundary inputBoundary, Double p) | 构造函数                                                     |
| ------------------------------------------------ | ------------------------------------------------------------ |
| public Testcase Best_candidate()                 | 功能内先生成一个测试用例，再生成它和各镜像的镜像用例，加上每个镜像用例的偏移生成新的测试用例 |
| public void testEfficiency(int pointNum)         | 继承自AbstractART类，测试ART的效率，生成pointNum个测试用例所需的时间 |

## 4. 测试框架介绍

### 1. Simulation

模拟环境下通过TestEffectiveness和TestEfficiency来分别检测各个算法的F_measure和平均检测性能（耗时）

### 2. RealZone

真实环境下我们通过RealTestEffectiveness来检测个个算法在不同的真实程序和其变异体的检测中的F_measure，并对最终结果打包

具体实现如下

```Java
String packName = "dt.mutant." + originalName;
Constructor<AbstractART> constructor = method.getConstructor(DomainBoundary.class, Double.class);

Set<Class<?>> mutations = ClassUtil.getClasses(packName);
//获取该包名下的所有类
int index = 0;
for(Class<?> mutation : mutations){
    System.out.print(index);
    index++;

    FaultZone fr = new realZone_Bessj(mutation.getConstructor(),mutation.getMethods()[0]);

    AbstractART art_block = constructor.newInstance(inputBoundary, Parameters.lp);

    ThreadWithCallback callback = new ThreadWithCallback(inputBoundary,art_block,fr);
    ExecutorService executor = Executors.newFixedThreadPool(2);

    Future future = executor.submit(callback::call);
    try{
        Object result = future.get(2, TimeUnit.SECONDS);
        temp = (int) result;
    }
    catch (Exception ex){

    }
    finally {
        FmeasureResults.add(temp);
        System.out.println("_" + temp + "\t");
        sumsRRT += temp;
        executor.shutdown();
    }
}
```

1. ## 测试结果

我们将测试的结果打包成zip文件放在了Result/RealTest中

## 5. 一些困难的算法和加分项

