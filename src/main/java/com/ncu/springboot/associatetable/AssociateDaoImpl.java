package com.ncu.springboot.associatetable;

import com.ncu.springboot.util.SplitListUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class AssociateDaoImpl implements AssociateDao {
    private static final String NAMESPACE = "com.ncu.springboot.associatetable.AssociateDao.";

    private static final String INSERT_SQL = "insert";

    private static final String TABLE="table";

    private static final String CLAUSE="clause";

    private static final String COLS="cols";

    private static final String INSERT_FIELD="insertField";

    private static final String ASSOC_COL="assocCol";

    private static final String INVERSE_COL="inverseCol";

    private static final String ID_LIST="idList";

    private static final String AS_ASSOC_COL =" as assocCol";

    private static final String AS_INVERSE_COL =" as inverseCol";

    /**
     * 数据库in操作 最大数
     */
    public static final int BATCH_SIZE_LIMIT = 999;

    @Autowired
    protected SqlSession sqlSession;

    @Override
    public void batchInsert(AssociateTable table, Long assocCol, List<Long> inverseCols) {
        String insertField = table.getAssocCol() + "," + table.getInverseCol();
        for (Long inverseCol : inverseCols) {
            sqlSession.insert(NAMESPACE + INSERT_SQL, buildInsertMap(table,insertField,assocCol.toString(),inverseCol.toString()));
        }
    }

    @Override
    public void batchInsert(AssociateTable table, String assocCol, List<String> inverseCols) {
        String insertField = table.getAssocCol() + "," + table.getInverseCol();
        for (String inverseCol : inverseCols) {
            sqlSession.insert(NAMESPACE + INSERT_SQL, buildInsertMap(table,insertField,assocCol,inverseCol));
        }
    }

    private Map<String, String> buildInsertMap(AssociateTable table,String insertField,String assocCol,String inverseCol) {
        Map<String, String> map = new HashMap<>();
        map.put(TABLE, table.getTableName());
        map.put(INSERT_FIELD, insertField);
        map.put(ASSOC_COL, assocCol);
        map.put(INVERSE_COL, inverseCol);
        return map;
    }

    private Map<String, String> buildDelMap (AssociateTable table,String key){
        String clause = table.getAssocCol() + "=" + key;
        Map<String, String> map = new HashMap<>();
        map.put(TABLE, table.getTableName());
        map.put(CLAUSE, clause);
        return map;
    }

    private static final String DELETE_SQL = "deleteByAssoc";

    @Override
    public int deleteByAssoc(AssociateTable table, String key) {
        return sqlSession.delete(NAMESPACE + DELETE_SQL, buildDelMap(table,key));
    }

    @Override
    public int deleteByAssoc(AssociateTable table, Long id) {
        return sqlSession.delete(NAMESPACE + DELETE_SQL,  buildDelMap(table,id.toString()));
    }

    private static final String DELETE_IDS_SQL = "deleteByAssocIdList";

    @Override
    public int deleteByAssocIdList(AssociateTable table, List<Long> idList) {
        List<String> stringList = SplitListUtil.long2StringList(idList);
        return this.deleteByAssocKeyList(table,stringList);
    }

    @Override
    public int deleteByAssocKeyList(AssociateTable table, List<String> keyList) {
        if (keyList.size() > BATCH_SIZE_LIMIT) {
            List<List<String>> stringSplitList = SplitListUtil.splitStringList(keyList,BATCH_SIZE_LIMIT);
            int deleteCount = 0;
            for (List<String> innerStringList : stringSplitList) {
                deleteCount += sqlSession.delete(NAMESPACE + DELETE_IDS_SQL, buildDelListMap(table,innerStringList));
            }
            return deleteCount;
        }
        return sqlSession.delete(NAMESPACE + DELETE_IDS_SQL, buildDelListMap(table,keyList));

    }

    private Map<String, Object> buildDelListMap (AssociateTable table, List<String> keyList){
        Map<String, Object> map = new HashMap<>();
        map.put(TABLE, table.getTableName());
        map.put(ASSOC_COL, table.getAssocCol());
        map.put(ID_LIST, keyList);
        return map;
    }

    private static final String SELECT_LONG_VALUE_SQL = "listLongValueByAssoc";

    @Override
    public List<Long> listByAssoc(AssociateTable table, Long id) {
        return sqlSession.selectList(NAMESPACE + SELECT_LONG_VALUE_SQL, listMap(table, id.toString()));
    }

    private static final String SELECT_STRING_VALUE_SQL = "listStringValueByAssoc";

    @Override
    public List<String> listByAssoc(AssociateTable table, String key) {
        return sqlSession.selectList(NAMESPACE + SELECT_STRING_VALUE_SQL, listMap(table, key));
    }

    private Map<String, String> listMap(AssociateTable table, String key) {
        String cols = table.getInverseCol() + AS_INVERSE_COL;
        String clause = table.getAssocCol() + "=" + key;
        Map<String, String> map = new HashMap<>();
        map.put(TABLE, table.getTableName());
        map.put(COLS, cols);
        map.put(CLAUSE, clause);
        return map;
    }

    private Map<String, String> listAllMap(AssociateTable table, String key) {
        String cols = table.getAssocCol() + AS_ASSOC_COL + "," + table.getInverseCol() + AS_INVERSE_COL;
        String clause = table.getAssocCol() + "=" + key;
        Map<String, String> map = new HashMap<>();
        map.put(TABLE, table.getTableName());
        map.put(COLS, cols);
        map.put(CLAUSE, clause);
        return map;
    }

}
