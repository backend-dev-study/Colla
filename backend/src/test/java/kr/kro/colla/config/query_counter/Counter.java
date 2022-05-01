package kr.kro.colla.config.query_counter;

public class Counter {

    private int count;
    private boolean flag;

    public Counter() {
    }

    public void startQueryCount() {
        this.flag = true;
    }

    public void countQuery() {
        this.count += 1;
    }

    public int getQueryCount() {
        return this.count;
    }

    public boolean getFlag() {
        return this.flag;
    }

    public void printQueryCount() {
        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        System.out.println("Query Count : " + this.count + "번");
        System.out.println("ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");

        clearQueryCount();
    }

    public void clearQueryCount() {
        this.flag = false;
        this.count = 0;
    }

}
