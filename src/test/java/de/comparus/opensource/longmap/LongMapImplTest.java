package de.comparus.opensource.longmap;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class LongMapImplTest {

    private static final int MAX = 200000;
    LongMapImpl<String> longMap = new LongMapImpl<>();

    @Test
    public void test_put_get() {
        fillMap();

        Assert.assertEquals("value1", longMap.put(0, "value1"));
        Assert.assertEquals("value2", longMap.put(1000, "value2"));
        Assert.assertEquals("value3", longMap.put(-100, "value3"));

        Assert.assertEquals("value1", longMap.get(0));
        Assert.assertEquals("value2", longMap.get(1000));
        Assert.assertEquals("value3", longMap.get(-100));
    }

    @Test
    public void test_remove() {
        fillMap();

        Assert.assertEquals(longMap.put(0, "value1"), longMap.remove(0));
        Assert.assertEquals(longMap.put(1000, "value2"), longMap.remove(1000));
        Assert.assertEquals(longMap.put(-100, "value3"), longMap.remove(-100));

        long size = longMap.size();
        longMap.remove(-100);
        longMap.remove(-999);
        long newSize = longMap.size();

        Assert.assertEquals(size, newSize);
    }

    @Test
    public void test_isEmpty() {
        Assert.assertTrue(longMap.isEmpty());

        fillMap();

        Assert.assertFalse(longMap.isEmpty());

        longMap.clear();

        Assert.assertTrue(longMap.isEmpty());
    }

    @Test
    public void test_containsKey() {
        fillMap();

        long key = ThreadLocalRandom.current().nextLong(1, Integer.MAX_VALUE / 2);
        longMap.put(key, String.valueOf(key));

        Assert.assertTrue(longMap.containsKey(key));
        Assert.assertFalse(longMap.containsKey(-555));
    }

    @Test
    public void test_containsValue() {
        fillMap();

        longMap.put(-555, "value");

        Assert.assertTrue(longMap.containsValue("value"));
        Assert.assertFalse(longMap.containsValue("value1"));
    }

    @Test
    public void test_keys() {
        long[] expectedLongMapKeysArray = new long[MAX];
        for(int i = 0; i < MAX; i++) {
            long key = ThreadLocalRandom.current().nextLong(1, Integer.MAX_VALUE / 2);
            expectedLongMapKeysArray[i] = key;
            longMap.put(key, String.valueOf(i));
        }

        long[] longMapKeysArray = longMap.keys();
        expectedLongMapKeysArray = Arrays.stream(expectedLongMapKeysArray).distinct().toArray();

        Arrays.sort(expectedLongMapKeysArray);
        Arrays.sort(longMapKeysArray);

        Assert.assertArrayEquals(expectedLongMapKeysArray, longMapKeysArray);
    }

    @Test
    public void test_values() {
        Object[] expectedLongMapValuesArray = new Object[MAX];

        for(int i = 0; i < MAX; i++) {
            long key = ThreadLocalRandom.current().nextLong(1, Integer.MAX_VALUE / 2);
            expectedLongMapValuesArray[i] = String.valueOf(key);
            longMap.put(key, String.valueOf(key));
        }

        Object[] longMapValuesArray = longMap.values();
        expectedLongMapValuesArray = Arrays.stream(expectedLongMapValuesArray).distinct().toArray();

        Arrays.sort(expectedLongMapValuesArray);
        Arrays.sort(longMapValuesArray);

        Assert.assertArrayEquals(expectedLongMapValuesArray, longMapValuesArray);
    }

    @Test
    public void test_size() {
        long[] expectedLongMapKeysArray = new long[MAX];

        for(int i = 0; i < MAX; i++) {
            long key = ThreadLocalRandom.current().nextLong(1, Integer.MAX_VALUE / 2);
            expectedLongMapKeysArray[i] = key;
            longMap.put(key, String.valueOf(key));
        }

        Assert.assertNotEquals(expectedLongMapKeysArray.length, longMap.size());

        expectedLongMapKeysArray = Arrays.stream(expectedLongMapKeysArray).distinct().toArray();

        Assert.assertEquals(expectedLongMapKeysArray.length, longMap.size());
    }

    private void fillMap() {
        for(int i = 0; i < MAX; i++) {
            long key = ThreadLocalRandom.current().nextLong(1, Integer.MAX_VALUE / 2);
            longMap.put(key, String.valueOf(i));
        }
    }
}
