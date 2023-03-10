package com.example.finalpro.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationVO {
    private int notif_no;
    private String custid;
    private int qna_no;
    private Date notif_date;
}
