package com.b07group47.taamcollectionmanager;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import java.util.ArrayList;
/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SearcherTest {
    @Test
    public void construct() {
        FilteredSearcher fs = new FilteredSearcher();
        assertNotNull(fs);
    }

    @Test
    public void search() {
        FilteredSearcher fs = new FilteredSearcher();
        ArrayList<Item> itemList = fs.search(8, null, null, null);
        assertNotNull(itemList);
        assertNotEquals(itemList.size(), 0);
        assertEquals(itemList.get(0).getLotNumber(), 8);
    }



    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}