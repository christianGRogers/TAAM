package com.b07group47.taamcollectionmanager;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import androidx.test.ext.junit.runners.AndroidJUnit4;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SearcherTest {
    @Test
    public void construct() {
        ArtifactQueryFactory fs = new ArtifactQueryFactory();
        assertNotNull(fs);
    }

    @Test
    public void search() {
        ArtifactQueryFactory fs = new ArtifactQueryFactory();
    }



    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}