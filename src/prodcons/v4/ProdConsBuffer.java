package prodcons.v4;

import java.util.concurrent.Semaphore;

import prodcons.IProdConsBuffer;
import prodcons.Message;

public class ProdConsBuffer implements IProdConsBuffer {
    Message msgBuffer[];
    int bufferSize;
    int writeIndex = 0;
    int readIndex = 0;
    int totmsg = 0;
    int producerCount;
    Semaphore emptyness ;
    Semaphore fullness ;

    public ProdConsBuffer(int size) {
        bufferSize = size;
        msgBuffer = new Message[size];
        producerCount = size;
        emptyness = new Semaphore(0);
        fullness = new Semaphore(size);
    }

    @Override
    public void produce(Message m) throws InterruptedException {

        fullness.acquire();
        
        
        synchronized (this) {
            msgBuffer[writeIndex % bufferSize] = m;
            writeIndex++;
            totmsg++;
        }

        emptyness.release();
    }

    @Override
    public void produce(Message m, long authorIdForFeedBack) throws InterruptedException {

        fullness.acquire();

        synchronized (this) {
            msgBuffer[writeIndex % bufferSize] = m;
            writeIndex++;
            totmsg++;
            System.out.println("[" + this.readIndex + "/" + this.writeIndex + "] Producteur " + authorIdForFeedBack
                + " a produit " + m);
        }
        emptyness.release();
    }

    @Override
    public Message consume() throws InterruptedException {

        emptyness.acquire();
        
        Message m;

        synchronized (this) {
            m = msgBuffer[readIndex%bufferSize];
            readIndex++;
        }
        fullness.release();
        

        return m;
    }
    @Override
    public Message consume(long consumerIdForFeedBack) throws InterruptedException {

        emptyness.acquire();
        
        Message m;
        
        synchronized (this) {
            m = msgBuffer[readIndex%bufferSize];
            readIndex++;
            System.out.println("[" + this.readIndex + "/" + this.writeIndex + "] Consomateur " + consumerIdForFeedBack
                    + " a consumé " + m);
        }
        fullness.release();
        
        return m;
    }


    @Override
    public synchronized int nmsg() {
        return writeIndex - readIndex;
    }

    @Override
    public synchronized int totmsg() {
        return totmsg;
    }

    public synchronized boolean isFull() {
        return nmsg() >= bufferSize;
    }

    public synchronized boolean isEmpty() {
        return writeIndex == readIndex;
    }

}