package com.myCompany;

import lombok.Getter;
import lombok.Setter;


/**
 * ORM编程思想（object relational mapping）
 * 一个数据表对应一个java类
 * 表中的一条记录对应java类的一个对象
 * 表中的一个字段对应java类的一个属性
 */
@Getter
@Setter
public class Student {
    private int id;
    private String stuId;
    private String name;
    private int age;
    private String address;
    private String phone;

    public Student() {
    }

    public Student(int id, String stuId, String name, int age, String address, String phone) {
        this.id = id;
        this.stuId = stuId;
        this.name = name;
        this.age = age;
        this.address = address;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Student [id=" + id + ", stuId=" + stuId + ", name=" + name + ", age=" + age + ", address=" + address + ",phone=" + phone + "]";
    }
}
