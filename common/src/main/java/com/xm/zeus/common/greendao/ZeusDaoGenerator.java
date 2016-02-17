package com.xm.zeus.common.greendao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Generator GreenDao
 *
 * @author fengy
 */
public class ZeusDaoGenerator {

    public static void main(String[] args) throws Exception {

        // TODO : DaoGenerator does not work

        Schema schema = new Schema(1, "com.xm.zeus.common.greendao");

        addBizLog(schema);

        schema.setDefaultJavaPackageDao("com.xm.zeus.common.greendao.dao");
        schema.enableKeepSectionsByDefault();
        schema.enableActiveEntitiesByDefault();
        new DaoGenerator().generateAll(schema, "./common/src/main/java-gen");
    }

    /**
     * Log
     */
    private static void addBizLog(Schema schema) {
        Entity setting = schema.addEntity("BizLog");

        setting.setTableName("Log");
        setting.addIdProperty();
        setting.addStringProperty("User");
        setting.addDateProperty("Time");
        setting.addStringProperty("Content");
        setting.addStringProperty("Remark");

    }

}
