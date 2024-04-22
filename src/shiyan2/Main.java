package shiyan2;
import java.util.*;

 class MemoryManerger {
    private int PageSize;
    private int numPages;
    private int[] PageTable;
    private Stack<Integer> pageStack;
    private int pageFaults; //缺页次数

    public MemoryManerger(int pageSize, int numPages){
        this.numPages = numPages;
        this.PageSize = pageSize;
        this.PageTable = new int[numPages];
        Arrays.fill(PageTable,-1); //初始化页表，-1表示未分配进程
        this.pageStack = new Stack<>();
        this.pageFaults = 0;
    }

    public void allocateMemory(int processID,int memorySize){
        int numPageNeeded = memorySize / numPages;  //计算所需要的页数
        for(int i = 0 ; i < numPageNeeded ; i ++){
            if(pageStack.size() < numPages){
                int pageNum = pageStack.size();
                PageTable[pageNum] = processID; //更新页表
                pageStack.push(pageNum); //将页面加入栈顶
            }
            else{
                //内存已满，进行页面置换
                int pageReplace = pageStack.pop(); //弹出栈底
                PageTable[pageReplace] = processID;
                pageStack.push(pageReplace);
            }
        }
    }

    public int accessPages(String accessSequence){
        for(char c:accessSequence.toCharArray()){
            int pageNum = Character.getNumericValue(c);
            if(!pageStack.contains(pageNum)){  //判断是否发生缺页
                pageFaults ++;
            }
            pageStack.remove((Integer) pageNum);  //将页面移到栈顶
            pageStack.push(pageNum);
        }
        return pageFaults;
    }

     public String accessAddress(int address){
         int pageNum = address / PageSize;
         if(pageStack.contains(pageNum)){
             return "物理地址：" + address;
         }else{
             // 缺页调入
             int pageReplace = pageStack.pop(); // 弹出栈底
             int oldPage = PageTable[pageReplace]; // 获取旧页号
             PageTable[pageReplace] = pageNum; // 更新页表
             pageStack.push(pageReplace); // 将新页号加入栈顶
             return "缺页调入后的地址：" + (oldPage * PageSize);
         }
     }
 }

public class Main {
    public static void main(String[] args){
        MemoryManerger manerger = new MemoryManerger(1,100);
        //分配内存给进程A
        manerger.allocateMemory(1,4);
        System.out.println("缺页次数：" + manerger.accessPages("0213546374733553111723410"));

        //访问特定地址
        int[] addresses = {0XCC, 0xAF8, 0x1234};
        for(int address : addresses){
            System.out.println(address);
            System.out.println(manerger.accessAddress(address));
        }
    }
}


//import java.util.*;
//
//class Memory_Manager {
//    private int memorySize;
//    private int pageSize;
//    private int pageCount;
//    private int[] pageTable;
//    private Stack<Integer> pageStack;
//    private int pageFaultCount;
//
//    public Memory_Manager(int memorySize, int pageSize) {
//        this.memorySize = memorySize;
//        this.pageSize = pageSize;
//        this.pageCount = memorySize / pageSize;
//        this.pageTable = new int[pageCount];
//        this.pageStack = new Stack<>();
//        this.pageFaultCount = 0;
//    }
//
//    public void request(int processSize, int requestSize) {
//        int pageCountNeeded = processSize / pageSize;
//        if (processSize % pageSize != 0) {
//            pageCountNeeded++;
//        }
//
//        if (pageCountNeeded <= requestSize) {
//            allocate(processSize);
//        } else {
//            System.out.println("Insufficient memory!");
//        }
//    }
//
//    private void allocate(int processSize) {
//        int pageCountNeeded = processSize / pageSize;
//        if (processSize % pageSize != 0) {
//            pageCountNeeded++;
//        }
//
//        for (int i = 0; i < pageCountNeeded; i++) {
//            if (pageStack.size() < pageCount) {
//                int pageNumber = pageStack.size();
//                pageStack.push(pageNumber);
//                pageTable[pageNumber] = 1; // Mark page as allocated
//            } else {
//                int pageNumber = pageStack.pop();
//                pageTable[pageNumber] = 0; // Mark page as not allocated
//                pageStack.push(pageNumber);
//            }
//        }
//    }
//
//    public void access(String accessSequence) {
//        for (int i = 0; i < accessSequence.length(); i++) {
//            int pageNumber = Character.getNumericValue(accessSequence.charAt(i));
//            if (pageTable[pageNumber] == 0) {
//                handlePageFault(pageNumber);
//            }
//        }
//    }
//
//    private void handlePageFault(int pageNumber) {
//        pageFaultCount++;
//        System.out.println("Page fault for page " + pageNumber);
//        // Simulating page replacement by pushing the accessed page to the top of the stack
//        pageStack.removeElement(pageNumber);
//        pageStack.push(pageNumber);
//        pageTable[pageNumber] = 1; // Mark page as allocated
//    }
//
//    public int getPageFaultCount() {
//        return pageFaultCount;
//    }
//
//    public int translateAddress(String address) {
//        int pageNumber = Integer.parseInt(address.substring(0, 2), 16) / pageSize;
//        if (pageTable[pageNumber] == 1) {
//            // Page is in memory
//            int offset = Integer.parseInt(address.substring(2), 16) % pageSize;
//            return pageNumber * pageSize + offset;
//        } else {
//            // Page fault
//            handlePageFault(pageNumber);
//            System.out.println("Page fault for address " + address);
//            return pageNumber * pageSize;
//        }
//    }
//}
//
//public class Main {
//    public static void main(String[] args) {
//        Memory_Manager manager = new Memory_Manager(100, 1); // Memory size: 100 pages, Page size: 1K
//
//        // Step 1: Allocate memory for process A
//        manager.request(8, 4); // Process size: 8K, Request size: 4 pages
//
//        // Step 2: Establish stack-based management
//
//        // Step 3: Process access sequence and calculate page faults
//        String accessSequence = "0213546374733553111723410";
//        manager.access(accessSequence);
//        System.out.println("Total page faults: " + manager.getPageFaultCount());
//
//        // Step 4: Test addresses
//        String[] testAddresses = {"00CC", "0AF8", "1234"};
//        for (String address : testAddresses) {
//            int physicalAddress = manager.translateAddress(address);
//            System.out.println("Physical address for " + address + ": " + physicalAddress);
//        }
//    }
//}


