package com.ncu.springboot.associatetable;


import java.util.List;

public interface AssociateDao {

    void batchInsert(AssociateTable table, Long assocCol, List<Long> inverseCols);

    void batchInsert(AssociateTable table, String assocCol, List<String> inverseCols);

    int deleteByAssoc(AssociateTable table, Long id);

    int deleteByAssoc(AssociateTable table, String key);

    int deleteByAssocIdList(AssociateTable table, List<Long> ids);

    int deleteByAssocKeyList(AssociateTable table, List<String> keys);

    List<Long> listByAssoc(AssociateTable table, Long id);

    List<String> listByAssoc(AssociateTable table, String key);

}
