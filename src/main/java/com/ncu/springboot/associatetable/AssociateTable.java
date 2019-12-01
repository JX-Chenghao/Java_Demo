package com.ncu.springboot.associatetable;


public enum AssociateTable {

    USER_ROLE("user_role", "user_id", "role_id"),
    ROLE_USER("user_role", "role_id", "user_id"),
    ROLE_PERMISSION("role_permission", "role_id", "permission_id"),
    PERMISSION_ROLE("role_permission", "permission_id", "role_id");


    private String tableName;

    private String assocCol;

    private String inverseCol;

    AssociateTable(String tableName, String assocCol, String inverseCol) {
        this.tableName = tableName;
        this.assocCol = assocCol;
        this.inverseCol = inverseCol;
    }

    public static AssociateTable[] getAllConstants() {
        return AssociateTable.class.getEnumConstants();
    }

    public String getTableName() {
        return tableName;
    }

    public String getAssocCol() {
        return assocCol;
    }

    public String getInverseCol() {
        return inverseCol;
    }

}
