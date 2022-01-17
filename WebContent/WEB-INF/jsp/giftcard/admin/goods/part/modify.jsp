<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="main">
  <div class="titlebar">
    <h2>상품권등록</h2>
    <div> <span>통합관리</span> &gt; <span>상품관리</span> &gt; <span>상품등록</span> &gt;<span class="bar_tx">상품권등록</span> </div>
  </div>
  <div class="container"> 
    <div class="contents">
    <form id="wFrm" name="frm" method="post" novalidate="novalidate" enctype="multipart/form-data">
		<div class="contents_view">
        <table class="style_1" style="table-layout:fixed;">
          <colgroup>
          <col width="10%" />
          <col width="*" />
          <col width="10%" />
          <col width="*" />
          </colgroup>
          <tr>
            <th>상품선택</th>
            <td class="selsize" colspan="3">
	          	<select title="유통사선택" data-ng-model="form.carmakerseq" data-ng-options="item.carmakerseq as item.makernm for item in carmaker" data-ng-change="changeCarmaker()" required>
	          		<option value="">카테고리</option>
	            </select>
	          	<select title="상품권선택" data-ng-model="form.carmodelseq" data-ng-options="item.carmodelseq as item.carmodelnm for item in carmodel" data-ng-change="changeCarmodel()" required>
	          		<option value="">모델명</option>
	            </select>
            </td>
          </tr>
          <tr>
            <th>상품명</th>
            <td>
              <input type="text" class="input_1" style="width:90%;" data-ng-model="form.productnm"/>
            </td>
          </tr>
          <tr>
            <th>상품사진</th>
            <td colspan="3">
              <input type="file" name="file" id="file"/><span style="margin-left: 3px;"><a data-ng-click="uploadFile()"><img src="/images/admin/contents/s_btn_2.gif" alt="등록" /></a></span>
              <span style="font-size:11px; padding-left:5px;">*JPG, GIF, PNG 파일만 등록 가능합니다.</span>
              <div style="margin-top:10px;" class="tableimgbox">
                <div class="tableimg" data-ng-repeat="item in form.files">
                  <p style="margin-top: 3px;" class="tableimgtext">{{item.attach_nm}}</p>
                  <a data-ng-if="!!item.order_seq" data-ng-click="imgPopup('/upload/board/'+item.yyyy+'/'+item.mm+'/'+item.uuid)"><img data-ng-src="/upload/board/{{item.yyyy}}/{{item.mm}}/{{item.uuid}}" err-src="/images/common/no_image.gif" alt="사진" style="width: 150px; height: 104px;"/></a>
                  <a data-ng-if="!item.order_seq" data-ng-click="imgPopup('/upload/temp/'+item.uuid)"><img data-ng-src="/upload/temp/{{item.uuid}}" err-src="/images/common/no_image.gif" alt="사진" style="width: 150px; height: 104px;"/></a>
                  <p style="margin-top: 3px;"><a data-ng-click="removeFile($index)"><img src="/images/admin/contents/s_btn_1.gif" alt="삭제" /></a></p>
                </div>
              </div>
            </td>
          </tr>
          <tr>
            <!-- <th>사용자 판매가격 설정</th>
            <td>
              <select title="사용자 판매가격 설정 선택" data-ng-model="form.user_pricing_yn" data-ng-init="form.user_pricing_yn='Y'">
                 <option value="Y">사용</option>
                 <option value="N">사용안함</option>
               </select>
            </td> -->
            <th>사용자 판매가격</th>
            <td colspan="3">
              <input type="text" class="input_1" data-ng-model="form.user_price" data-ng-pattern="/^[0-9]+$/" data-ng-change="salePrice();supplierPrice();"/>원
              <p style="font-size:11px; padding:5px 0 0 0px;">*판매가격과 세일가격은 숫자만 입력 가능합니다.</p>
            </td>
          </tr>
          <tr>
            <th>배송료</th>
            <td colspan="3">
               <select title="배송료선택" data-ng-model="form.fee_yn" data-ng-init="form.fee_yn='N'" data-ng-change="form.fee_yn=='C'||form.fee_yn=='N'?form.fee_amount='':''">
                 <option value="C">착불</option>
                 <option value="N">무료</option>
                 <option value="Y">유료</option>
               </select>
               <label>
                 배송료<input type="text" class="input_1" style="margin-left:5px;" data-ng-model="form.fee_amount" data-ng-disabled="form.fee_yn=='C'||form.fee_yn=='N'" data-ng-pattern="/^[0-9]+$/"/>원
               </label>
            </td>
          </tr>
          <tr>
            <th>재고관리</th>
            <td colspan="3">
               <!-- <label>
                재고연동여부
                <select title="재고연동여부선택" data-ng-model="form.stock_yn" data-ng-init="form.stock_yn='Y'">
                 <option value="Y">연동함</option>
                 <option value="N">연동안함</option>
               </select>
              </label>
              <label style="margin-left:25px;"> -->
              <label>
                 재고수량<input type="text" class="input_1" style="margin-left:5px;" data-ng-model="form.stock_num" data-ng-pattern="/^[0-9]+$/" data-ng-init="form.stock_num='1'" required="required"/>개
               </label>
            </td>
          </tr>
          <tr>
            <th>검색태그</th>
            <td colspan="3">
              <input type="text" class="input_1" style="width:90%;" data-ng-model="form.search_tag"/>
              <p style="font-size:11px; padding:5px 0 0 0px;">*검색시 상품명과 검색태그가 사용됩니다.</p>
              <p style="font-size:11px; padding:5px 0 0 0px;">*이상품은 가장 잘 표현할 수 있는 단어들을 콤마(,) 로 구분해서 등록해 주세요.</p>
              <p style="font-size:11px; padding:5px 0 0 0px;">*최대 200자까지 등록 가능합니다.</p>
            </td>
          </tr>
          <tr>
            <th>상품타입</th>
            <td class="chbox" colspan="3">
              <!-- <label><input type="checkbox" data-ng-model="form.bestyn" data-ng-true-value="Y" data-ng-false-value="N" data-ng-init="form.bestyn='N'"/>베스트</label>
              <label><input type="checkbox" data-ng-model="form.eventyn" data-ng-true-value="Y" data-ng-false-value="N" data-ng-init="form.eventyn='N'"/>이벤트</label>
              <label><input type="checkbox" data-ng-model="form.newyn" data-ng-true-value="Y" data-ng-false-value="N" data-ng-init="form.newyn='N'"/>신상품</label>
              <label><input type="checkbox" data-ng-model="form.publicyn" data-ng-true-value="Y" data-ng-false-value="N" data-ng-init="form.publicyn='N'"/>인기</label> -->
              <label><input type="checkbox" data-ng-model="form.recommyn" data-ng-true-value="Y" data-ng-false-value="N" data-ng-init="form.recommyn='N'"/>추천</label>
              <!--<label><input type="checkbox" data-ng-model="form.saleyn" data-ng-true-value="Y" data-ng-false-value="N" data-ng-init="form.saleyn='N'"/>세일</label>
              <label><input type="checkbox" data-ng-model="form.planyn" data-ng-true-value="Y" data-ng-false-value="N" data-ng-init="form.planyn='N'"/>기획</label>
              <p style="font-size:11px; padding:10px 0 0 0px;">- 베스트 : 상품이 실제로 많이 팔리는 제품이 아니라 관리자가 임의로 지정한 것임</p>
              <p style="font-size:11px; padding:5px 0 0 0px;">- 이벤트 : 이벤트 매장에 노출</p>
              <p style="font-size:11px; padding:5px 0 0 0px;">- 신상품 : 신상품 매장에 및 탭에 노출</p>
              <p style="font-size:11px; padding:5px 0 0 0px;">- 인기 : 인기 상품 매장에 노출</p> -->
              <p style="font-size:11px; padding:5px 0 0 0px;">- 추천 : 추천상품매장에 노출</p>
              <!-- <p style="font-size:11px; padding:5px 0 0 0px;">- 세일 : 세일상품 매장및 탭에 노출 체크 박스 체크시 특별세일, 세일기간 선택 활성화</p>
              <p style="font-size:11px; padding:5px 0 0 0px;">- 기획 : 기획 상품 매장 및 탭에 노출</p> -->
            </td>
          </tr>
          <c:if test="${sessionScope.member.group_seq eq '1' || sessionScope.member.member_id eq 'insun'}">
          <tr>
            <th>승인여부</th>
            <td colspan="3">
              <select title="승인여부" data-ng-model="form.approval" data-ng-init="form.approval='Y'">
                 <option value="Y">승인</option>
                 <option value="N">비승인</option>
               </select>
            </td>
          </tr>
          </c:if>
          <tr>
            <th>내용</th>
            <td colspan="3">
            	<textarea style="width:100%;" id="conts" data-ng-model="form.conts" smarteditor></textarea>
            </td>
          </tr>
        </table>
      
		<div class="btn_bottom">
			<div class="r_btn">
			    <span class="bt_all">
			    	<span><input type="button" value="저장" class="btall" data-ng-click="save()"/></span>
			    </span> 
			    <span class="bt_all">
			    	<span><input type="button" value="목록" class="btall" data-ng-click="list()"/></span>
			    </span> 
			</div>
      	</div>
		</div>
      </form>
    </div>
  </div>
</div>