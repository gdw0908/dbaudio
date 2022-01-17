<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html data-ng-app="MyApp">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>파츠모아 관리시스템</title>
<link href="/lib/css/cmsbase.css" rel="stylesheet" type="text/css" />
<link href="/lib/css/cmsadmin.css" rel="stylesheet" type="text/css" />
<link href="/lib/css/redmond/jquery-ui-1.9.1.custom.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/lib/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="/lib/js/jquery-ui-1.10.3.custom.min.js"></script>
<script type="text/javascript" src="/lib/js/angular.min.js"></script>
<script type="text/javascript" src="/lib/js/angular-route.min.js"></script>
<script type="text/javascript" src="/lib/js/filters/myFilter.js"></script>
<script type="text/javascript" src="/lib/js/filters/ngRange.js"></script>
<script type="text/javascript" src="/lib/js/services/myCommon.js"></script>
<script type="text/javascript" src="/lib/js/services/dialog-service.js"></script>
<script type="text/javascript" src="/lib/js/directives/myUtil.js"></script>
<script type="text/javascript" src="/lib/js/directives/myPagination.js"></script>
<script type="text/javascript" src="/lib/js/directives/sortable.js"></script>
<script type="text/javascript">
var app = angular.module("MyApp", ['dialogService', 'ngRoute', 'myUtil', 'myPagination', 'myCommon', 'ui.sortable']);
app.run(function($rootScope){
	$rootScope.param = {
	};
});
app.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
	$routeProvider
		.when('/list', {controller: 'listCtrl', templateUrl : 'list.do'})
		.otherwise({redirectTo: '/list' });
}]);
app.controller("mainCtrl", function($scope, $window, ajaxService) {
	
});
app.controller("listCtrl", function($scope, $window, $compile, ajaxService, dialogService) {
	$scope.group = {};
	$scope.code = {};
	$scope.group.go = function(n){
		ajaxService.getJSON('/json/list/code.groupList.do?cpage='+n, $scope.param, function(data){
			$scope.group.list = data;
		});
		ajaxService.getJSON('/json/request/code.page_info.do', $scope.param, function(data){
			$scope.group.currentPage = n;
			$scope.group.totalCount = data.totalcount;
			$scope.group.totalPage = data.totalpage;
		});
	};
	
	$scope.group.add = function(){
		var options = {
				autoOpen: false,
				modal: true,
				width: "auto",
				height: "auto",
				close: function(event, ui) {
// 					console.log("Predefined close");
				}
			};

		dialogService.open("groupFormDialog","groupDialogTemplete.html", {}, options)
		.then(
			function(result) {
				if(result=="1"){
					$scope.group.go(1);
				}
			},
			function(error) {
			}
		);
	}
	$scope.group.modify = function(item){
		var options = {
				autoOpen: false,
				modal: true,
				width: "auto",
				height: "auto",
				close: function(event, ui) {
// 					console.log("Predefined close");
				}
			};
		
		var param = angular.copy(item);
		dialogService.open("groupFormDialog","groupDialogTemplete.html", param, options)
		.then(
			function(result) {
				if(result=="1"){
					$scope.group.go(1);
				}
			},
			function(error) {
			}
		);
	}
	$scope.group.remove = function(item){
		if(!confirm("정말 삭제하시겠습니까?")){
			return;
		}
		var param = angular.copy(item);
		ajaxService.getJSON("/json/update/code.groupDelete.do", param, function(data){
			if(data.rst == '1'){
  				$scope.group.go($scope.group.currentPage);
  			}
		});
	}
	
	$scope.code.openDialog = function(item){
		
		var options = {
				autoOpen: false,
				modal: true,
				width: "650",
				height: "750",
				close: function(event, ui) {
// 					console.log("Predefined close");
				}
			};
		
		var param = angular.copy(item);
		dialogService.open("codeListDialog","codeListDialogTemplete.html", param, options)
		.then(
			function(result) {
				if(result=="1"){
					$scope.group.go(1);
				}
			},
			function(error) {
			}
		);
	};
	
});

app.controller('groupFormCtrl', function($scope, $timeout, ajaxService, dialogService) {
	$scope.form = $scope.model;//파라미터
	$scope.rst = {};
	$scope.saveClick = function() {
		if($scope.frm.$invalid){
			if($scope.frm.$error.required){
				alert("필수값을 입력하여 주십시오.");
			}else{
				alert("값이 유효하지 않습니다.");
			}
			$(".ng-invalid")[1].focus();
			return false;
		}
		
		var url = "/json/update/code.updateGroup.do";
		if(!$scope.form.code_group_seq){
			url = "/json/insert/code.insertGroup.do";
		}
		
		ajaxService.getJSON(url, $scope.form, function(data){
			dialogService.close("groupFormDialog", data.rst);
		});
	};

	$scope.cancelClick = function() {
		dialogService.cancel("groupFormDialog");
	};

});
app.controller('codeListCtrl', function($scope, $timeout, ajaxService, dialogService) {
	$scope.rst = {};
	$scope.form = $scope.model;//파라미터
	$scope.getList = function(){
		ajaxService.getJSON('/json/list/code.codeList.do', $scope.form, function(data){
			$scope.list = data;
		});
	}
	
	$scope.openCodeFormDialog = function(item){
		var options = {
				autoOpen: false,
				modal: true,
				width: "350",
				height: "750",
				close: function(event, ui) {
// 					console.log("Predefined close");
				}
			};
		
		var param = (function(){
			var rst = {};
			if(!!item){
				rst = angular.copy(item);
				rst.group_nm = $scope.form.group_nm;
			}else{
				rst = {code_group_seq : $scope.form.code_group_seq};
			}
			return rst;
		})();
		dialogService.open("codeFormDialog","codeFormDialogTemplete.html", param, options)
		.then(
			function(result) {
				if(result=="1"){
					$scope.getList();
				}
			},
			function(error) {
			}
		);
	}
	
	$scope.sortableOptions = {
		realign : false,
		axis: 'y',
		placeholder: "ui-state-highlight",
		update: function(e, ui) {
			if(confirm("정렬 순서를 변경하시겠습니까?")){
				this.realign = true;
        	}else{
        		ui.item.sortable.cancel();
				this.realign = false;
        	}
		},
		stop : function(e, ui){
			if(this.realign){
				var dataset = $.map($scope.list, function(item, i){
					return {code_seq: item.code_seq, order_seq : i}; 
				});
				ajaxService.getJSON("/code/updateOrder.do", {jData : JSON.stringify({list : dataset})}, function(data){
					if(data.rst == '1'){
		  				alert("정상 처리 되었습니다.");
		  			}
				});
			}
		}
    };

	$scope.codeRemove = function(item) {
		if(!confirm("정말 삭제하시겠습니까?")){
			return;
		}
		var param = angular.copy(item);
		ajaxService.getJSON('/json/list/code.codeDelete.do', param, function(data){
			$scope.getList();
		});
	};
	$scope.cancelClick = function() {
		dialogService.cancel("groupFormDialog");
	};

});
app.controller('codeFormCtrl', function($scope, $timeout, ajaxService, dialogService) {
	$scope.form = $scope.model;//파라미터
	$scope.rst = {};
	if(!$scope.form.use_yn){
		$scope.form.use_yn="Y";
	}
	$scope.saveClick = function() {
		if($scope.frm.$invalid){
			if($scope.frm.$error.required){
				alert("필수값을 입력하여 주십시오.");
			}else{
				alert("값이 유효하지 않습니다.");
			}
			$("#cFrm .ng-invalid")[0].focus();
			return false;
		}
		
		var url = "/json/update/code.updateCode.do";
		if(!$scope.form.code_seq){
			url = "/json/insert/code.insertCode.do";
		}
		
		ajaxService.getJSON(url, $scope.form, function(data){
			dialogService.close("codeFormDialog", data.rst);
		});
	};

	$scope.cancelClick = function() {
		dialogService.cancel("codeFormDialog");
	};

});

</script>
</head>
<body data-ng-controller="mainCtrl">
  <div data-ng-view data-ng-cloak></div>
</body>
<script type="text/ng-template" id="groupDialogTemplete.html">
	<div ng-controller="groupFormCtrl" title="그룹편집">
		<form name="frm" method="post" novalidate="novalidate">
		<input type="hidden" data-ng-model="form.code_group_seq">
        <table class="style_2">
         <colgroup>
          <col width="25%" />
          <col width="*" />
         </colgroup>
          <tr>
            <th>그룹명칭</th>
            <td><input type="text" data-ng-model="form.group_nm" class="normal" style="width:330px;" required></td>
          </tr>
        </table>
        <div class="btn_bottom">
	        <div class="r_btn">
	          <span class="bt_all"><span>
	          <input type="button" value="저장" class="btall" data-ng-click="saveClick()"/>
	          </span></span> 
	          <span class="bt_all"><span>
	          <input type="button" value="취소" class="btall" data-ng-click="cancelClick()"/>
	          </span></span>
	        </div>
		</div>
		</form>		
	</div>
</script>
<script type="text/ng-template" id="codeListDialogTemplete.html">
	<div ng-controller="codeListCtrl" data-ng-init="getList()" title="코드목록">
        <div class="btn_bottom">
	        <div class="r_btn">
	          <span class="bt_all">
				<span>
	          		<input type="button" value="코드추가" class="btall" data-ng-click="openCodeFormDialog(model)"/>
	          	</span>
			  </span> 
	        </div>
		</div><br />
		<table class="style_2">
		  <col width="80" />
		  <col width="*" />
		  <col width="*" />
		  <col width="*" />
		  <col width="*" />
		  <col width="*" />
		  <col width="90" />
		  <col width="80" />
			<thead>
				<tr>
					<th class="center">번호</th>
					<th class="center">코드</th>
					<th class="center">코드명</th>
					<th class="center">값1</th>
					<th class="center">값2</th>
					<th class="center">기타</th>
					<th class="center">사용여부</th>
					<th class="center">삭제</th>
				</tr>
			</thead>
			<tbody ui-sortable="sortableOptions" ng-model="list">
				<tr data-ng-if="list.length==0"><td colspan="8">결과가 없습니다.</td></tr>
				<tr data-ng-repeat="item in list">
					<td>{{item.code_seq}}</td>
					<td><a data-ng-click="openCodeFormDialog(item)">{{item.code}}</a></td>
					<td>{{item.code_nm}}</td>
					<td>{{item.val1}}</td>
					<td>{{item.val2}}</td>
					<td>{{item.etc}}</td>
					<td>{{item.use_yn}}</td>
					<td><a data-ng-click="codeRemove(item)">삭제</a></td>
				</tr>
			</tbody>
		</table>
	</div>
</script>
<script type="text/ng-template" id="codeFormDialogTemplete.html">
	<div ng-controller="codeFormCtrl" title="그룹편집">
		<form id="cFrm" name="frm" method="post" novalidate="novalidate">
		<input type="hidden" data-ng-model="form.code_group_seq" required/>
		<input type="hidden" data-ng-model="form.code_seq"/>
        <table class="style_2">
        <colgroup>
            <col width="120" />
            <col width="" />
            </colgroup>
        <tr>
        	<th>그룹</th>
        	<td>{{form.group_nm}} {{form.code_group_seq}}</td>
        </tr>
        <tr>
        	<th>코드</th>
        	<td><input type="text" data-ng-model="form.code" class="normal" required/></td>
        </tr>
        <tr>
        	<th>코드명</th>
        	<td><input type="text" data-ng-model="form.code_nm" class="normal" required/></td>
        </tr>
        <tr>
        	<th>값1</th>
        	<td><input type="text" data-ng-model="form.val1" class="normal" /></td>
        </tr>
        <tr>
        	<th>값2</th>
        	<td><input type="text" data-ng-model="form.val2" class="normal" /></td>
        </tr>
        <tr>
        	<th>기타</th>
        	<td><input type="text" data-ng-model="form.etc" class="normal" /></td>
        </tr>
        <tr>
        	<th>사용여부</th>
        	<td>
        	<ol class="select">
        	  <li><label><input type="radio" data-ng-model="form.use_yn" value="Y"/>사용</label></li>
        	  <li><label><input type="radio" data-ng-model="form.use_yn" value="N"/>사용안함</label></li>
        	</ol>
        	</td>
        </tr>
        </table>
        <div class="btn_bottom">
	        <div class="r_btn">
	          <span class="bt_all">
				<span>
	          		<input type="button" value="저장" class="btall" data-ng-click="saveClick()"/>
	          	</span>
			  </span> 
	          <span class="bt_all">
				<span>
	          		<input type="button" value="취소" class="btall" data-ng-click="cancelClick()"/>
	          	</span>
			  </span> 
	        </div>
		</div>
		</form>		
	</div>
</script>
</html>
