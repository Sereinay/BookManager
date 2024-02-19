package book.manage;

import book.manage.entity.Book;
import book.manage.entity.Borrow;
import book.manage.entity.Student;
import book.manage.sql.SqlUtil;
import lombok.extern.java.Log;
import org.apache.ibatis.io.Resources;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.LogManager;

@Log
public class Main {
    public static void main(String[] args)  {
            try(Scanner scanner = new Scanner(System.in)){
                LogManager manager = LogManager.getLogManager();
                manager.readConfiguration(Resources.getResourceAsStream("logging.properties"));

                while (true){
                System.out.println("=========================");
                System.out.println("1.录入学生信息");
                System.out.println("2.录入书籍信息");
                System.out.println("3.添加借阅信息");
                System.out.println("4.查询借阅信息");
                System.out.println("5.查询学生信息");
                System.out.println("6.查询书籍信息");
                System.out.println("输入你想要执行的操作（输入其他任意数字退出）：");
                int input;
                try{
                    input = scanner.nextInt();
                    }catch (Exception e){
                    return;
                }
                scanner.nextLine();
                switch (input){
                    case 1:
                        addStudent(scanner);
                        break;
                    case 2:
                        addBook(scanner);
                        break;
                    case 3:
                        addBorrow(scanner);
                        break;
                    case 4:
                        showBorrow();
                        break;
                    case 5:
                        getStudentList();
                        break;
                    case 6:
                        gerBookList();
                        break;
                    default:
                        return;
                }

            }
        } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }
    private  static void getStudentList(){
        SqlUtil.doSqlWork(mapper -> {
            List<Student> studentList = mapper.getStudentList();
            studentList.forEach(student -> {
                System.out.println(student.getName()+" "+student.getSex()+" "+student.getSid()+" "+student.getGrade());
            });
        });
    }
    private  static void gerBookList(){
        SqlUtil.doSqlWork(mapper -> {
            List<Book> bookList = mapper.getBookList();
            bookList.forEach(book -> {
                System.out.println(book.getTitle()+" "+book.getDesc()+" "+book.getBid()+" "+book.getPrice());
            });
        });
    }
    private static void showBorrow(){
        SqlUtil.doSqlWork(mapper -> {
            List<Borrow> borrowList = mapper.getBorrowList();
            borrowList.forEach(borrow -> {
                System.out.println(borrow.getStudent().getName()+"->"+borrow.getBook().getTitle());
            });
        });
    }
    private static void addBorrow(Scanner scanner){
        System.out.println("请输入学生号：");
        String sid = scanner.nextLine();
        int sid_ = Integer.parseInt(sid);
        System.out.println("请输入书籍号：");
        String bid = scanner.nextLine();
        int bid_ = Integer.parseInt(bid);
        SqlUtil.doSqlWork(mapper->{
            int re = mapper.addBorrow(sid_,bid_);
            if (re >0){
                System.out.println("借阅成功！");
                log.info("新添加了一组借阅信息。"+"学号："+sid_+"书号"+bid_);
            }
            else
                System.out.println("借阅失败！请重试。");
        });

    }
    private static void addStudent(Scanner scanner){
        System.out.println("请输入学生名字：");
        String name = scanner.nextLine();
        System.out.println("请输入学生性别（男/女）：");
        String sex = scanner.nextLine();
        System.out.println("请输入学生年级：");
        String grade = scanner.nextLine();
        int g = Integer.parseInt(grade);
        Student student = new Student(name,sex,g);
        SqlUtil.doSqlWork(mapper ->{
            int re = mapper.addStudent(student);
            if (re > 0) {
                System.out.println("学生信息录入成功！");
                log.info("新添加了一条学生信息："+student);
            }
            else System.out.println("学生信息录入失败，请重试！");
        });
    }

    private static void addBook(Scanner scanner){
        System.out.println("请输入书籍名：");
        String title = scanner.nextLine();
        System.out.println("请输入书籍描述：");
        String desc= scanner.nextLine();
        System.out.println("请输入书籍价格：");
        String price = scanner.nextLine();
        double p = Double.parseDouble(price);
        Book book = new Book(title,desc,p);
        SqlUtil.doSqlWork(mapper ->{
            int re = mapper.addBook(book);
            if (re > 0) {
                System.out.println("书籍信息录入成功！");
                log.info("新添加了一条书籍信息：" + book);
            }
            else System.out.println("书籍信息录入失败，请重试！");
        });
    }
}
