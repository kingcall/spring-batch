package com.kingcall.batch.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * 这里我们处理一个简单示例，从csv文件中迁移一些财务数据至xml文件。输入文件结构很简单，每行包括一个事物，包括姓名、用户id、发生日期以及金额
 */
@Setter
@Getter
@ToString
@XmlRootElement(name = "transactionRecord")
public class Transaction {
    private String username;
    private int userId;
    private Date transactionDate;
    private double amount;
}
