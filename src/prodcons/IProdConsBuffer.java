package prodcons;

public interface IProdConsBuffer {
/**
* Put the message m in the buffer
**/

public void put(Message m) throws InterruptedException;
public void put(Message m, long authorIdForFeedBack) throws InterruptedException;


/**
* Put n instances of the message m in the prodcons buffer
* The current thread is blocked until all
* instances of the message have been consumed
* Any consumer of m is also blocked until all the instances of
* the message have been consumed
**/
public void put(Message m, int n) throws InterruptedException;
public void put(Message m, int n, long authorIdForFeedBack) throws InterruptedException;


/**
* Retrieve a message from the buffer,
* following a FIFO order (if M1 was put before M2, M1
* is retrieved before M2)
**/
public Message get() throws InterruptedException;
public Message get(long consumerIdForFeedBack) throws InterruptedException;

/**
* Retrieve n consecutive messages from the prodcons buffer
**/
public Message[] get(int k) throws InterruptedException;
public Message[] get(int k, long consumerIdForFeedBack) throws InterruptedException;

/**
* Returns the number of messages currently available in
* the buffer
**/
public int nmsg();
/**
* Returns the total number of messages that have
* been put in the buffer since its creation
**/
public int totmsg();
}