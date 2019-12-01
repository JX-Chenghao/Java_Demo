package com.ncu.springboot.util;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SplitListUtil<T> {

    public static List<List<Long>> splitIdList(List<Long> idList, int sizeLimit) {
        List<List<Long>> ret = new ArrayList<List<Long>>();
        int totalLength = idList.size();
        while (totalLength > 0) {
            List<Long> idListInner;
            if (totalLength - sizeLimit > 0) {
                idListInner = idList.subList(totalLength - sizeLimit, totalLength);
            } else {
                idListInner = idList.subList(0, totalLength);
            }
            ret.add(idListInner);
            totalLength = totalLength - idListInner.size();
        }
        return ret;
    }

    public static List<List<String>> splitStringList(List<String> idList, int sizeLimit) {
        List<List<String>> ret = new ArrayList<List<String>>();
        int totalLength = idList.size();
        while (totalLength > 0) {
            List<String> idListInner;
            if (totalLength - sizeLimit > 0) {
                idListInner = idList.subList(totalLength - sizeLimit, totalLength);
            } else {
                idListInner = idList.subList(0, totalLength);
            }
            ret.add(idListInner);
            totalLength = totalLength - idListInner.size();
        }
        return ret;
    }

    public List<List<T>> splitBoList(List<T> boList, int sizeLimit) {
        List<List<T>> ret = new ArrayList<List<T>>();
        int totalLength = boList.size();
        while (totalLength > 0) {
            List<T> boListInner;
            if (totalLength - sizeLimit > 0) {
                boListInner = boList.subList(totalLength - sizeLimit, totalLength);
            } else {
                boListInner = boList.subList(0, totalLength);
            }
            ret.add(boListInner);
            totalLength = totalLength - boListInner.size();
        }
        return ret;
    }

    public static List<Long> string2LongList(List<String> stringList) {
        if (CollectionUtils.isEmpty(stringList)) {
            return null;
        }
        return stringList.stream().map(stringItem -> Long.parseLong(stringItem.trim())).collect(Collectors.toList());
    }

    public static List<String> long2StringList(List<Long> longList) {
        if (CollectionUtils.isEmpty(longList)) {
            return null;
        }
        return longList.stream().map(lItem -> lItem.toString()).collect(Collectors.toList());
    }

    public static <T> List<List<T>> splitList(List<T> list, int count) {
        if (list == null || list.size() == 0 || count < 1) {
            return new ArrayList<>();
        }

        List<List<T>> result = new ArrayList<>();

        int size = list.size();
        int len = (size % count == 0) ? (size / count) : ((size / count) + 1);

        int curIndex = 0;
        while(curIndex < size) {
            List<T> subList = list.subList(curIndex, (curIndex + len) > size ? size : (curIndex + len));
            result.add(subList);
            curIndex = (curIndex + len);
        }

        return result;
    }
}
