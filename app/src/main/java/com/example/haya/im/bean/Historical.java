package com.example.haya.im.bean;

public class Historical {

    private String name;
    private String text;
    private TYPE type;
    private boolean flag = false;
    private int count = 0;

    public Historical(String name, String text, TYPE type) {
        this.name = name;
        this.text = text;
        this.type = type;
    }

    public Historical(String name, String type, String flag, int count) {
        this.name = name;
        this.type = TYPE.valueOf(type);
        if ("true".equals(flag)) {
            this.flag = true;
        } else {
            this.flag = false;
        }
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public Historical setCount(int count) {
        this.count = count;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TYPE getType() {
        return type;
    }

    public Historical setType(TYPE type) {
        this.type = type;
        return this;
    }

    public boolean isFlag() {
        return flag;
    }

    public Historical setFlag(boolean flag) {
        this.flag = flag;
        return this;
    }

    public enum TYPE{
        NOTICE,
        NOTICE_ADD,
        NOTICE_ACCRPTED,
        NORMAL,
        SELF
    }
}
