package com.mc.web.temp;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class TransferHomeDAO extends EgovAbstractMapper {
	
	public int write(Map<String, String> params){
		return update("transfer.write", params);
	}

	public List insun_itemseqlist() {
		return list("transfer.insun_itemseqlist", null);
	}
	
	public List goods_image_list() {
		return list("transfer.goods_image_list", null);
	}
}
