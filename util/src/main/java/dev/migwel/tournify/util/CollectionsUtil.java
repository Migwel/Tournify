package dev.migwel.tournify.util;

import java.util.Collection;

public final class CollectionsUtil {

    private CollectionsUtil() {
        //util
    }

    /*
     * Compares content of collections
     * @return: true if both collections contain the same items (order does not matter)
     *          false if one collection contains items not present in the other collection
     */
    public static <T> boolean containsSameItems(Collection<T> c1, Collection<T> c2) {
        boolean bothEmpty = isEmptyOrNull(c1) && isEmptyOrNull(c2);
        if(bothEmpty) {
            return true;
        }
        boolean atLeastOneEmpty = isEmptyOrNull(c1) || isEmptyOrNull(c2);
        if(atLeastOneEmpty) {
            return false;
        }

        return c2.containsAll(c1);
    }

    public static boolean isEmptyOrNull(Collection<?> c) {
        return c == null || c.isEmpty();
    }

    public static boolean hasItems(Collection<?> c) {
        return !isEmptyOrNull(c);
    }
}
