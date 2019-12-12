package dev.migwel.tournify.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CollectionsUtilTest {

    @Test
    void isEmptyOrNullTest_True() {
        assertTrue(CollectionsUtil.isEmptyOrNull(null));
        assertTrue(CollectionsUtil.isEmptyOrNull(new ArrayList<>()));
    }

    @Test
    void isEmptyOrNullTest_False() {
        List<String> listWithNull = new ArrayList<>();
        listWithNull.add(null);
        assertFalse(CollectionsUtil.isEmptyOrNull(listWithNull));
        assertFalse(CollectionsUtil.isEmptyOrNull(List.of(1)));
        assertFalse(CollectionsUtil.isEmptyOrNull(List.of(listWithNull)));
        assertFalse(CollectionsUtil.isEmptyOrNull(List.of(List.of(1))));
    }

    @Test
    void hasItemsTest_True() {
        List<String> listWithNull = new ArrayList<>();
        listWithNull.add(null);
        assertTrue(CollectionsUtil.hasItems(listWithNull));
        assertTrue(CollectionsUtil.hasItems(List.of(1)));
        assertTrue(CollectionsUtil.hasItems(List.of(listWithNull)));
        assertTrue(CollectionsUtil.hasItems(List.of(List.of(1))));
    }

    @Test
    void hasItemsTest_False() {
        assertFalse(CollectionsUtil.hasItems(null));
        assertFalse(CollectionsUtil.hasItems(new ArrayList<>()));
    }

    @Test
    void containsSameItemsTest_listContainsSameItems() {
        List<Integer> listInt = List.of(1, 2, 3, 4, 5);
        List<Integer> sameListInt = List.of(1, 2, 3, 4, 5);
        assertTrue(CollectionsUtil.containsSameItems(listInt, sameListInt));
    }

    @Test
    void containsSameItemsTest_compareCollectionWithItself() {
        List<Integer> listInt = List.of(1, 2, 3, 4, 5);
        assertTrue(CollectionsUtil.containsSameItems(listInt, listInt));
    }

    @Test
    void containsSameItemsTest_sameContentDifferentKind() {
        List<Integer> listInt = List.of(1, 2, 3, 4, 5);
        Set<Integer> setInt = Set.of(1, 2, 3, 4, 5);
        assertTrue(CollectionsUtil.containsSameItems(listInt, setInt));
    }

    @Test
    void containsSameItemsTest_sameContentDifferentOrder() {
        List<Integer> listInt = List.of(1, 2, 3, 4, 5);
        List<Integer> sameListInt = List.of(4, 2, 3, 1, 5);
        assertTrue(CollectionsUtil.containsSameItems(listInt, sameListInt));
    }

    @Test
    void containsSameItemsTest_differentContent() {
        List<Integer> listInt = List.of(1, 2, 3, 4, 5);
        List<Integer> differentListInt = List.of(6, 7, 8, 9, 10);
        assertFalse(CollectionsUtil.containsSameItems(listInt, differentListInt));
    }
}
