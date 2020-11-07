import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class that implements the channel used by headquarters and space explorers to communicate.
 */
public class CommunicationChannel {

	/**
	 * Creates a {@code CommunicationChannel} object.
	 */

	ArrayBlockingQueue<Message> SpaceExplores ;
	ArrayBlockingQueue<Message> HQ ;
	private ReentrantLock mutex;
	private ReentrantLock mutex_put_SE;
	private ReentrantLock mutex_get_SE;
	private ReentrantLock mutex_get_HQ;

	HashMap<Long, ArrayList<Message>> hash;
	public CommunicationChannel() {
		SpaceExplores = new ArrayBlockingQueue<Message>(100000);
		HQ = new ArrayBlockingQueue<Message>(100000);
		mutex = new ReentrantLock();
		mutex_put_SE = new ReentrantLock();
		mutex_get_SE = new ReentrantLock();
		mutex_get_HQ = new ReentrantLock();
		hash = new HashMap<Long,ArrayList<Message>>();
	}

	/**
	 * Puts a message on the space explorer channel (i.e., where space explorers write to and 
	 * headquarters read from).
	 * 
	 * @param message
	 *            message to be put on the channel
	 */
	public void putMessageSpaceExplorerChannel(Message message) {
		try {
			mutex_put_SE.lock();
			SpaceExplores.put(message);
			mutex_put_SE.unlock();
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Gets a message from the space explorer channel (i.e., where space explorers write to and
	 * headquarters read from).
	 * 
	 * @return message from the space explorer channel
	 */
	public Message getMessageSpaceExplorerChannel(){

		try {
			mutex_get_SE.lock();
			Message message_sent = SpaceExplores.take();
			mutex_get_SE.unlock();
			return message_sent;
		} catch (InterruptedException e) {
		}
		return null;
	}


	/**
	 * Puts a message on the headquarters channel (i.e., where headquarters write to and 
	 * space explorers read from).
	 * 
	 * @param message
	 *            message to be put on the channel
	 */
	public void putMessageHeadQuarterChannel(Message message) {
		try {
			mutex.lock();
			ArrayList<Message> buff = new ArrayList<Message>();
			Long id = Thread.currentThread().getId();
			if (hash.get(id) == null)
			{
				buff = new ArrayList<Message>();
			}
			else
			{
				buff = hash.get(id);
			}

			buff.add(message);
			hash.put(id,buff);

			if (message.getData().equals(HeadQuarter.END))
			{
				int sz = buff.size();
				for (int i = 0 ; i < sz ; i++) {
					HQ.put(buff.get(i));
				}
				hash.put(id, new ArrayList<Message>());
				mutex.unlock();
			}
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Gets a message from the headquarters channel (i.e., where headquarters write to and
	 * space explorer read from).
	 * 
	 * @return message from the header quarter channel
	 */
	public Message getMessageHeadQuarterChannel() {
		Message message_sent1 = null;
		Message message_sent2 = null;
		Message message_sent = new Message(0,0," ");
		try {
			mutex_get_HQ.lock();
			message_sent1 = HQ.take();
			if (!message_sent1.getData().equals(HeadQuarter.END) && !message_sent1.getData().equals(HeadQuarter.EXIT))
			{
				message_sent2 = HQ.take();
				message_sent.setCurrentSolarSystem(message_sent2.getCurrentSolarSystem());
				message_sent.setParentSolarSystem(message_sent1.getCurrentSolarSystem());
				message_sent.setData(message_sent2.getData());
				mutex_get_HQ.unlock();
			}
			if (message_sent1.getData().equals(HeadQuarter.END) ||  message_sent1.getData().equals(HeadQuarter.EXIT)) {
				message_sent = message_sent1;
				mutex_get_HQ.unlock();
			}
		} catch (InterruptedException e) {
		}
		return message_sent;
	}
}
