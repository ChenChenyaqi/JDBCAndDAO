package com.myCompany;


import JDBCUtils.JDBCMysqlUtils;

public class Main {


    public static void main(String[] args) {
        String sql = "select * from student where id = ?";
        Student student = JDBCMysqlUtils.queryStudent(Student.class,sql,1);
        System.out.println(student);
        int[] array = {1,2,25,3,5,4,5};
    }
}
