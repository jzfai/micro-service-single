package top.hugo.demo.factory;


/**
 * 工厂模式（普通和静态）
 *
 * @author 邝华
 * @email kuanghua@aulton.com
 * @date 2023-02-21 11:06
 * @Copyright Copyright (c)  aulton Inc. All Rights Reserved.
 **/
public class ComputerFactory {

    public static IComputer produce(String type) {
        IComputer computer = null;
        if (type.equals("personComputer")) {
            computer = new PersonComputer();
        } else if (type.equals("workComputer")) {
            computer = new WorkComputer();
        } else {
            System.out.println("不能生产");
        }
        return computer;
    }
}
