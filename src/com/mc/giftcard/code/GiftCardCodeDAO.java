package com.mc.giftcard.code;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class GiftCardCodeDAO extends EgovAbstractMapper {
	public int updateCodeOrderSeq(Map<String, String> params) {
		return update("code.updateCodeOrderSeq", params);
	}
	public List oldCodeList(Map<String, String> params) {
		return list("old_code.codeList", params);
	}
	public List codeList(Map<String, String> params) {
		return list("code.codeList", params);
	}
	public List sido(Map<String, String> params) {
		return list("code.sido", params);
	}
	public List sigungu(Map<String, String> params) {
		return list("code.sigungu", params);
	}
	public List dong(Map<String, String> params) {
		return list("code.dong", params);
	}
}