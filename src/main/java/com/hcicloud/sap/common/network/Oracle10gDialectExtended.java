/** 
* @Project hcicpm
* @Package com.sinovoice.hcicloud.common
* @author lixianji
* @date 2015年6月5日 下午1:44:21
*/
package com.hcicloud.sap.common.network;

import org.hibernate.dialect.Oracle10gDialect;

import java.sql.Types;

/** 
 * @Title Oracle10gDialectExtended.java
 * @Package com.sinovoice.hcicloud.common
 * @author lixianji
 * @date 2015年6月5日 下午1:44:21
 */
public class Oracle10gDialectExtended extends Oracle10gDialect {
	 public Oracle10gDialectExtended() {
	        super();
	        registerColumnType(Types.FLOAT, "NUMBER");
	    }
}
