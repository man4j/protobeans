package org.protobeans.core.util;

public class SnowFlakeIdGenerator {
    //Если под timestamp выделить 41 бит, да еще и отнимать значения, которые уже прошли, то данный алгоритм будет работать до 2080 года
    //Максимальное число помещающееся в 41 бит - 2199023255551
    //Кол-во миллисекунд которые уже давно прошли - 1288834974657
    //Т.к. мы их отнимаем, то максимальный таймстемп = 2199023255551 + 1288834974657 = Wed Jul 10 20:30:30 MSK 2080
    private static final long TIMESTAMP_BITS = 41L;
    private static final long TWEPOCH = 1288834974657L;
    private static final int AVAILABLE_BITS = Long.SIZE - 1;//Необходимо для работы со знаковыми числами
    private static final long TIMESTAMP_SHIFT = AVAILABLE_BITS - TIMESTAMP_BITS;
    
    private final int clientId;
    private final int sequenceMax;
    private final int sequenceShift;
    
    private long lastTimestamp;
    private long sequence;
    
    private volatile long timeShiftCount;
    private volatile long sequenceOverflowCount;

    /**
     * @param maxClients Max planned clients in your cluster
     * @param clientId Client ID from 0 to (maxClients - 1)
     */
    public SnowFlakeIdGenerator(long maxClients, int clientId) {
        if (maxClients <= 0 || maxClients > 1024) {
            throw new IllegalStateException("Too many clients");
        }

        int clientIdBits = Long.toBinaryString(maxClients - 1).length();
        int maxClientId = -1 ^ (-1 << clientIdBits);
        
        if (clientId < 0 || clientId > maxClientId) {
            throw new IllegalStateException("Incorrect clientId. Min client id is 0. Max client id is: " + maxClientId);
        }
        
        long sequenceBits = AVAILABLE_BITS - TIMESTAMP_BITS - clientIdBits;
                
        sequenceShift = clientIdBits;
        
        sequenceMax = -1 ^ (-1 << sequenceBits);//максимальное число последовательности, которое может быть сгенерировано в 1 миллисекунду
        
        this.clientId = clientId;
    }
    
    public int getSequenceMax() {
        return sequenceMax;
    }
    
    public long getTimeShiftCount() {
        return timeShiftCount;
    }
    
    public long getSequenceOverflowCount() {
        return sequenceOverflowCount;
    }
    
    /**
     * @return Signed positive long number in format 0|timestampBits|sequenceBits|clientIdBits
     * 
     * Возвращаемое число обладает следующими свойствами:
     * 1. Уникальное в пределах кластера из maxClients
     * 2. Труднопредсказуемое в будущем, т.к. зависит от времени
     * 3. Гарантированно возрастает в пределах одного генератора (при условии, что часы не будут переводить в момент неактивности программы)
     * 4. Возрастает с определенной погрешностью в пределах кластера, т.к. невозможно обеспечить идеальную синхронизацию часов между машинами
     */
    public synchronized long generateLongId() {
        long timestamp = System.currentTimeMillis();
        
        if(timestamp < lastTimestamp) {//Значит перевели часы
            timeShiftCount++;
            
            timestamp = tilNextMillis(lastTimestamp);//Ждем следующего момента
        }
        
        if (lastTimestamp == timestamp) {
            sequence++;
            
            if (sequence > sequenceMax) {
                sequenceOverflowCount++;
                
                sequence = 0;
                
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }
        
        lastTimestamp = timestamp;
        //0|timestampBits|sequenceBits|clientIdBits
        return ((timestamp - TWEPOCH) << TIMESTAMP_SHIFT) | (sequence << sequenceShift) | clientId;
    }

    private long tilNextMillis(long lastTs) {
        long timestamp = System.currentTimeMillis();
        
        while (timestamp <= lastTs) {
            timestamp = System.currentTimeMillis();
        }
        
        return timestamp;
    }
}
