class candyBowl {

    // Declare buffer
    private int[] buffer;
    private int in = 0;
    private int out = 0;
    private int count = 0;
    private int size;

    // Constructor initializes buffer to arg size
    candyBowl(int size) {
        this.size = size;
        buffer = new int[size];
    }

    synchronized public void put() {
        while (count != 0) try {wait();} catch (InterruptedException e) {}
        count = size;
        in = (in + 1) % size;
        notifyAll();
    }

    synchronized public int get() {
        while (count == 0) try {wait();} catch (InterruptedException e) {}
        int return_value = buffer[out];
        --count;
        out = (out + 1) % size;
        notifyAll();
        return (return_value);
    }
}

class producer extends Thread {
    public boolean stop = false;
    String name;

    candyBowl candy_;
    producer(candyBowl c, String n) {
        candy_ = c;
        name = n;
    }

    public void run() {
        try {
            while(!stop) {
                Thread.sleep(100);
                candy_.put();
                System.out.println(name + " filled the bowl");
            }
        } catch (InterruptedException e) { System.out.println("oops!"); }
    }

}

class consumer extends Thread {
    candyBowl candy_;
    String name;
    public boolean stop = false;

    consumer(candyBowl c, String n) {
        candy_ = c;
        name = n;
    }

    public void run() {
        try {
            Thread.sleep(200);
            while(!stop) {
                candy_.get();
                System.out.println(name + " obtained a piece of candy");
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) { System.out.println("oops"); }
    }
}

public class Main {

    public static void main(String args[]) {

        candyBowl Buff = new candyBowl(10);
        producer Prod = new producer(Buff, "Teaching Assistant");
        consumer Cons = new consumer(Buff, "Faculty Member #1");
        consumer Cons2 = new consumer(Buff, "Faculty Member #2");
        consumer Cons3 = new consumer(Buff, "Faculty Member #3");

        Prod.start();
        Cons.start();
        Cons2.start();
        Cons3.start();
    }
}
