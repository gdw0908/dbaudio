package com.mc.giftcard.board;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class GiftCardBoardDAO extends EgovAbstractMapper {
	
	public Object list(Map<String, String> params){
		return list("article.list", params);
	}
	
	public Object list_category(Map<String, String> params){
		return list("article.list_category", params);
	}
	
	public Map page_info(Map<String, String> params){
		return (Map) selectByPk("article.page_info", params);
	}
	
	public Map page_info_category(Map<String, String> params){
		return (Map) selectByPk("article.page_info_category", params);
	}
	
	public Map view(Map<String, String> params){
		return (Map) selectByPk("article.article", params);
	}
	
	public int view_count(Map<String, String> params){
		return update("article.update_view_count", params);
	}
	
	public String getNextval() {
		return selectByPk("article.nextval",null).toString();
	}
	
	public int write(Map<String, String> params){
		return update("article.insert", params);
	}
	
	public int modify(Map<String, String> params){
		return update("article.update", params);
	}
	
	public int del(Map<String, String> params){
		return update("article.remove", params);
	}
	
	public Object categoryList(Map<String, String> params){
		return list("article.categoryList", params);
	}
	
	public int categoryWrite(Map<String, String> params){
		return update("article.category_insert", params);
	}
	
	public int categoryModify(Map<String, String> params){
		return update("article.category_update", params);
	}
	
	public Object commentList(Map params) {
		return list("article.comment_list_cooperation", params);
	}
	
	public int commentWrite(Map params) {
		return update("article.comment_insert", params);
	}
	
	public int commentReply(Map params) {
		return update("article.commentReply", params);		
	}

	public Map commentReplyInfo(Map params) {
		return (Map) selectByPk("article.commentReplyInfo", params);		
	}
	
	public int commentReplyInfoUpdate(Map params){
		return update("article.commentReplyInfoUpdate",params);
	}

	public int commentDel(Map params) {
		return update("article.commentDel",params);
	}
	
	public int commentDelReply(Map params) {
		return update("article.commentDelReply",params);
	}

	public int commentReplyUpdate(Map params) {
		return update("article.commentReplyUpdate",params);
	}

	public int articleCommentCnt(Map params) {
		return update("article.articleCommentCnt",params);
	}
	
	public Object getUserData(Map<String, String> params) {
		return selectByPk("article.getUserData", params);
	}
	
	public int userDataUpdateStatus(Map<String, String> params) {
		return update("article.userDataUpdateStatus", params);
	}
	
	public int busiDataUpdateStatus(Map<String, String> params) {
		return update("article.busiDataUpdateStatus", params);
	}

	public Object getUserData2(Map<String, String> params) {
		return selectByPk("article.getUserData2", params);
	}
	
	public void withDrawMemberUpdate(Map<String, String> params) {
		update("article.withDrawMemberUpdate", params);
	}
	
	public Object getSendAddressList(Map<String, String> params){
		return list("article.getSendAddressList", params);
	}
	
	public void deleteMyadressData(Map<String, String> params) {
		delete("article.deleteMyadressData", params);
	}
	
	public void updateMyDefaultData(Map<String, String> params) {
		update("article.updateMyDefaultData", params);
	}
	
	public Object getMyAddressView(Map<String, String> params) {
		return selectByPk("article.getMyAddressView", params);
	}
	
	public Object getMyAddressTotal(Map<String, String> params) {
		return selectByPk("article.getMyAddressTotal", params);
	}
	
	public void updateDefaultYnStatus(Map<String, String> params) {
		update("article.updateDefaultYnStatus", params);
	}
	
	public void insertMyAddressData(Map<String, String> params) {
		insert("article.insertMyAddressData", params);
	}
	
	public void updateMyAddressData(Map<String, String> params) {
		update("article.updateMyAddressData", params);
	}
	
	public Object getMemberInfo(Map<String, String> params) {
		return selectByPk("article.getMemberInfo", params);
	}
	
	public void resourcesItemInsert(Map<String, String> params) {
		insert("article.resourcesItemInsert", params);
	}
	
	public void resourcesItemUpdate(Map<String, String> params) {
		insert("article.resourcesItemUpdate", params);
	}

	public Object resourcesView(Map params) {
		return list("article.list_resources", params);
	}
	
	public List resourcesExcel(Map params) {
		return list("article.list_resources_excel", params);
	}
	
	public Object resourcesList(Map params) {
		return list("article.resourcesList", params);
	}
	public Object resourcesListPageInfo(Map params) {
		return selectByPk("article.page_info_resources", params);
	}
}
