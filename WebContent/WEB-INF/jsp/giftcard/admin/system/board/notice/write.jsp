<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="main">
  <div class="titlebar">
    <h2>공지사항</h2>
    <div> <span>통합관리</span> &gt; <span>시스템관리</span> &gt;<span>공지사항</span> &gt;<span class="bar_tx">등록</span> </div>
  </div>
  <div class="container"> 
    <div class="contents">
    <form id="wFrm" name="frm" method="post" novalidate="novalidate" enctype="multipart/form-data">
      <table class="style_1">
        <colgroup>
          <col width="15%" />
          <col width="*" />
        </colgroup>
        <tr>
          <th>작성자</th>
          <td>${sessionScope.member.member_nm }</td>
        </tr>
        <tr>
          <th>제목</th>
          <td><input type="text" class="input_1" style="width:90%" data-ng-model="form.title" required/></td>
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
	          	<p style="margin-top: 3px;">{{item.attach_nm}}({{item.size|number}}) <a data-ng-click="removeFile($index)"><img src="/images/admin/contents/s_btn_1.gif" alt="삭제" /></a></p>
	          </div>
	      </td>
        </tr>
      </table>
	   <div class="btn_bottom">
        <div class="r_btn">
          <span class="bt_all"><span><input type="button" value="등록" class="btall" data-ng-click="save()"/></span></span> 
          <span class="bt_all"><span><input type="button" value="목록" class="btall" data-ng-click="list()"/></span></span>
        </div>
      </div>
	</form>      
    </div>
  </div>
</div>
