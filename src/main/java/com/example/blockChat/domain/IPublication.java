package com.example.blockChat.domain;

import java.util.Date;

public interface IPublication {
    Long getid();
    String gettag();
    String gettext();
    Long getauthor();
    String getfilename();
    Date getpostTime();
    Date getlastView();
    Long getcountNewMessage();
}
