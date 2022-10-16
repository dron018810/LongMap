package de.comparus.opensource.longmap;

import java.util.Arrays;

public class LongMapImpl<V> implements LongMap<V> {

    private LongMapTableElement<V>[] table;
    private static final int capacity = 16;
    private static final float loadFactor = 0.75f;
    private static final int tableIncreaseCoef = 2;
    private int size;
    private int maxSize;

    public LongMapImpl() {
        table = new LongMapTableElement[capacity];
        size = 0;
        maxSize = (int) (capacity * loadFactor);
    }

    public V put(long key, V value) {
         if (size > maxSize) {
            resizeTable();
         }

        int index = getTableElementIndex(key);

        LongMapTableElement<V> newTableElement = new LongMapTableElement<>(key, value, null);

        if (table[index] == null) {
            table[index] = newTableElement;
        } else {
            LongMapTableElement<V> currentTableElement = table[index];
            while (currentTableElement != null) {
                if (currentTableElement.getKey() == key) {
                    currentTableElement.setValue(value);
                    return value;
                }
                if (currentTableElement.getNext() == null) {
                    currentTableElement.setNext(newTableElement);
                    break;
                }
                currentTableElement = currentTableElement.getNext();
            }
        }
        size++;
        return value;
    }

    public V get(long key) {
        V value = null;
        int index = getTableElementIndex(key);
        LongMapTableElement<V> tableElement = table[index];
        while (tableElement != null) {
            if (tableElement.getKey() == key) {
                value = tableElement.getValue();
                break;
            }
            tableElement = tableElement.getNext();
        }
        return value;
    }

    public V remove(long key) {
        int index = getTableElementIndex(key);

        if (table[index] == null) {
            return null;
        }

        LongMapTableElement<V> tableElement = table[index];
        V value;
        int count = 0;

        while (tableElement != null && tableElement.getKey() != key) {
            count++;
            tableElement = tableElement.getNext();
        }

        if (count > 0) {
            tableElement = table[index];
            for (int i = 0; i < count - 1; i++) {
                tableElement = tableElement.getNext();
            }
            if (tableElement.getNext() == null) {
                return null;
            }
            value = tableElement.getNext().getValue();
            tableElement.setNext(tableElement.getNext().getNext());
        } else {
            value = table[index].getValue();
            table[index] = table[index].getNext();
        }
        size--;
        return value;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsKey(long key) {
        int index = getTableElementIndex(key);
        LongMapTableElement<V> tableElement = table[index];
        while (tableElement != null) {
            if (tableElement.getKey() == key) {
                return true;
            }
            tableElement = tableElement.getNext();
        }
        return false;
    }

    public boolean containsValue(V value) {
        for (int i = 0; i < table.length; i++) {
            LongMapTableElement<V> tableElement = table[i];
            while (tableElement != null) {
                if (tableElement.getValue().equals(value)) {
                    return true;
                }
                tableElement = tableElement.getNext();
            }
        }
        return false;
    }

    public long[] keys() {
        if (size == 0) {
            return null;
        }

        long[] result = new long[size];
        int keyIndex = 0;

        for (LongMapTableElement<V> tableElement : table) {
            while (tableElement != null) {
                result[keyIndex] = tableElement.getKey();
                tableElement = tableElement.getNext();
                keyIndex++;
            }
        }

        return result;
    }

    public V[] values() {
        if (size == 0) {
            return null;
        }

        V[] result = (V[]) new Object[size];
        int keyIndex = 0;

        for (LongMapTableElement<V> tableElement : table) {
            if (tableElement != null) {
                LongMapTableElement<V> currentTableElement = tableElement;
                while (currentTableElement != null) {
                    result[keyIndex] = currentTableElement.getValue();
                    currentTableElement = currentTableElement.getNext();
                    keyIndex++;
                }
            }
        }
        return result;
    }

    public long size() {
        return size;
    }

    public void clear() {
        Arrays.fill(table, null);
        size = 0;
    }

    private int getTableElementIndex(long key) {
        return Math.abs(Long.hashCode(key) % table.length);
    }

    private void resizeTable() {
        LongMapTableElement<V>[] oldTable = table;
        int newCapacity = table.length * tableIncreaseCoef;
        maxSize = (int) (newCapacity * loadFactor);
        table = new LongMapTableElement[newCapacity];
        size = 0;
        for (LongMapTableElement<V> tableElement : oldTable) {
            while (tableElement != null) {
                put(tableElement.getKey(), tableElement.getValue());
                tableElement = tableElement.getNext();
            }
        }
    }

    private static class LongMapTableElement<V> {
        private final long key;
        private V value;
        private LongMapTableElement<V> next;

        LongMapTableElement(long key, V value, LongMapTableElement<V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final long getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public void setValue(V newValue) {
            this.value = newValue;
        }

        public LongMapTableElement<V> getNext() {
            return next;
        }

        public void setNext(LongMapTableElement<V> next) {
            this.next = next;
        }
    }
}