/*
This program implements a multithreading algorithm to print numbers in a specific sequence.
The sequence consists of alternating zeros and numbers, where odd and even numbers are printed separately by different threads.

Algorithm:
- Three threads are created: one prints zero, one prints odd numbers, and one prints even numbers.
- A `Lock` and `Condition` variables are used to synchronize the threads.
- The zero thread prints '0' and signals either the odd or even thread based on the next number.
- The odd and even threads print their respective numbers and signal the zero thread.
- This continues until all numbers up to `n` are printed.
*/

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Class responsible for printing numbers in a synchronized manner
class NumberPrinter {
    public void printZero() { System.out.print("0"); }
    public void printEven(int num) { System.out.print(num); }
    public void printOdd(int num) { System.out.print(num); }
}

// Thread controller to manage the sequence of printing
class ThreadController {
    private final int n; // Upper limit of numbers to print
    private final NumberPrinter printer;
    private final Lock lock = new ReentrantLock();
    private final Condition zeroCondition = lock.newCondition();
    private final Condition oddCondition = lock.newCondition();
    private final Condition evenCondition = lock.newCondition();
    
    private enum State { ZERO, ODD, EVEN }
    private State currentState = State.ZERO;
    private int currentNumber = 1;

    // Constructor to initialize the printer and number limit
    public ThreadController(int n, NumberPrinter printer) {
        this.n = n;
        this.printer = printer;
    }

    // Starts all threads and waits for them to finish
    public void start() {
        Thread zeroThread = new Thread(this::zeroTask);
        Thread oddThread = new Thread(this::oddTask);
        Thread evenThread = new Thread(this::evenTask);

        zeroThread.start();
        oddThread.start();
        evenThread.start();

        try {
            zeroThread.join();
            oddThread.join();
            evenThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Thread function that prints zero and signals the next number
    private void zeroTask() {
        for (int i = 0; i < n; i++) {
            lock.lock();
            try {
                while (currentState != State.ZERO) {
                    zeroCondition.await();
                }
                printer.printZero();
                currentState = (currentNumber % 2 == 1) ? State.ODD : State.EVEN;
                if (currentState == State.ODD) {
                    oddCondition.signal();
                } else {
                    evenCondition.signal();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
        lock.lock();
        try {
            currentNumber = n + 1;
            oddCondition.signalAll();
            evenCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    // Thread function that prints odd numbers when signaled
    private void oddTask() {
        while (true) {
            lock.lock();
            try {
                if (currentNumber > n) break;
                while (currentState != State.ODD || currentNumber % 2 != 1) {
                    oddCondition.await();
                    if (currentNumber > n) break;
                }
                if (currentNumber > n) break;
                printer.printOdd(currentNumber);
                currentNumber++;
                currentState = State.ZERO;
                zeroCondition.signal();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }

    // Thread function that prints even numbers when signaled
    private void evenTask() {
        while (true) {
            lock.lock();
            try {
                if (currentNumber > n) break;
                while (currentState != State.EVEN || currentNumber % 2 != 0) {
                    evenCondition.await();
                    if (currentNumber > n) break;
                }
                if (currentNumber > n) break;
                printer.printEven(currentNumber);
                currentNumber++;
                currentState = State.ZERO;
                zeroCondition.signal();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }
    }
}

// Main class to initiate the program execution
public class Question_5_a {
    public static void main(String[] args) {
        int n = 5; // Maximum number to be printed
        NumberPrinter printer = new NumberPrinter();
        ThreadController controller = new ThreadController(n, printer);
        controller.start();
    }
}

/*
This program creates three threads to print a sequence of numbers with alternating zeros.
- The zero thread prints '0' and signals either the odd or even thread.
- The odd thread prints odd numbers, and the even thread prints even numbers.
- Synchronization is achieved using locks and conditions to ensure proper sequence.

*/
