<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="main">
  <div class="titlebar">
    <h2>문의사항</h2>
    <div> <span>통합관리</span> &gt; <span>시스템관리</span> &gt;<span>문의사항</span> &gt;<span class="bar_tx">등록</span> </div>
  </div>
  <div class="container"> 
    <div class="contents">
    <form id="wFrm" name="frm" method="post" novalidate="novalidate">
      <table class="style_1">
        <colgroup>
          <col width="15%" />
          <col width="*" />
        </colgroup>
        <tr>
          <th>작성자</th>
          <td>{{form.reg_nm }}</td>
        </tr>
        <tr>
          <th>제목</th>
          <td><input type="text" class="input_1" style="width:90%" data-ng-model="form.title" required/></td>
        </tr>
        <tr>
          <th>연락처</th>
          <td>
            <select title="전화번호 앞자리 선택" data-ng-model="form.tel1" data-ng-options="item.code as item.code for item in main.tel">
            </select>
            <input type="text" class="input_1" data-ng-model="form.tel2" style="width:60px" /> - <input type="text" class="input_1" data-ng-model="form.tel3"  style="width:60px" />
          </td>
        </tr>
        <tr>
          <th>공개여부</th>
          <td>
          	<input type="radio" class="input_1" data-ng-model="form.public_yn" value="Y" /> 공개 
          	<input type="radio" class="input_1" data-ng-model="form.public_yn" value="N" /> 비공개 
          </td>
        </tr>
        <tr>
          <th>상태</th>
          <td>
          	<input type="radio" class="input_1" data-ng-model="form.status" value="0" /> 접수완료 
          	<input type="radio" class="input_1" data-ng-model="form.status" value="1" /> 답변대기 
          	<input type="radio" class="input_1" data-ng-model="form.status" value="2" /> 답변완료
          </td>
        </tr>
        <tr>
          <th>내용</th>
          <td>
			<textarea style="width:100%;" id="conts" data-ng-model="form.conts" required smarteditor></textarea>
          </td>
        </tr>
        <tr>
          <th rowspan="2">첨부파일 </th>
          <td><input type="file" name="file" id="file" onchange="angular.element(this).scope().uploadFile()"/></td>
        </tr>
        <tr> 
          <td class="tddiv" id="attach_div">
	          <div data-ng-repeat="item in form.files">
	          	<p style="margin-top: 3px;"><a data-ng-href="/download.do?uuid={{item.uuid}}">{{item.attach_nm}}</a> <a data-ng-click="removeFile($index)"><img src="/images/admin/contents/s_btn_1.gif" alt="삭제" /></a></p>
	          </div>
	      </td>
        </tr>
      </table>
	  <div class="btn_bottom">
        <div class="r_btn">
          <span class="bt_all"><span><input type="button" value="등록" class="btall" data-ng-click="save()"/></span></span>
          <span class="bt_all"><span><input type="button" value="삭제" class="btall" data-ng-click="del()"/></span></span> 
          <span class="bt_all"><span><input type="button" value="목록" class="btall" data-ng-click="list()"/></span></span>
          <span class="bt_all"><span><input type="button" value="답변완료 문자발송" class="btall" data-ng-click="mms(form)"/></span></span>
        </div>
      </div>
	</form>  




      <!-- 답글 리스트  -->
      <div class="commentbox">
        <div class="commentlist">
          
          
		  <ul>
		  	<li data-ng-if="comment.length == 0"></li>
            <li data-ng-repeat="item in comment" data-ng-class="{next : item.lvl != '0'}">
              <div>{{item.reg_id}} <span class="comment_date">{{item.reg_dt}}</span>
                  <span><a href="" data-ng-click="commentReplyOpen(item.comment_seq)"><img src="/images/admin/contents/comm_btn_1.gif" alt="답글"/></a></span>
                  <span><a href="" data-ng-click="commentReplyClose(item.comment_seq)"><img src="/images/admin/contents/comm_btn_2.gif" alt="취소" /></a></span>
                  <span><a href="" data-ng-click="commentDel(item.comment_seq)"><img src="/images/admin/contents/comm_btn_3.gif" alt="삭제" /></a></span>
                  <span><a href="" data-ng-click="commentUpdateWindow(item.comment_seq)"><img src="/images/admin/contents/comm_btn_4.gif" alt="수정" /></a></span>
              </div>
              <div class="textcomment">
              <span data-ng-class="{textname : item.lvl != '0'}">{{item.reply_id != null ? '@'+item.reply_id : ''}}</span>
              <pre>{{item.conts}}</pre>
              </div>
              <ul class="comment_reply" id="c{{item.comment_seq}}" style="display:none">
              	<li class="next">
	                <div class="comment">
			           <p class="comment_text">
			             <textarea rows="" cols="" data-ng-model="cp.conts" id="cp{{item.comment_seq}}"></textarea>
			           </p>
			           <p class="comment_bt"><img src="/images/admin/contents/btn_comment.gif" alt="답글등록" style="cursor:pointer;" data-ng-click="commentReply(item.comment_seq);"/></p>
			        </div>
	            </li>
              </ul>
            </li>
          </ul>
          
          
        </div>
        
        <form id="cFrm" name="cfrm" method="post" novalidate="novalidate">
        <div class="comment ctfirst">
           <p class="comment_text">
             <textarea rows="" cols="" data-ng-model="cfrm.conts" required ></textarea>
           </p>
           <p class="comment_bt"><img src="/images/admin/contents/btn_comment.gif" alt="답글등록" style="cursor:pointer;" data-ng-click="commentSubmit();"/></p>
        </div>
        </form>
        
        <p class="comm_txt">댓글은 <b>1000</b> 자 내외로 작성해주시기 바랍니다.</p>
      </div> 
      <!-- 답글 리스트  end-->
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
      
    </div>
  </div>
</div>
