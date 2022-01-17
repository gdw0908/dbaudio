<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${!(sessionScope.member.group_seq eq '1' || sessionScope.member.group_seq eq '3')}">
<%-- <c:redirect url="/noPermission.do"/> --%>
</c:if>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html data-ng-app="MyApp">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>파츠모아 통합관리 시스템</title>
<link href="/lib/css/cmsbase.css" rel="stylesheet" type="text/css" />
<link href="/lib/css/cmsadmin.css" rel="stylesheet" type="text/css" />
<link href="/lib/css/redmond/jquery-ui-1.9.1.custom.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/lib/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="/lib/js/jquery-ui-1.10.3.custom.min.js"></script>
<script type="text/javascript" src="/lib/js/jquery.form.js"></script>
<script type="text/javascript" src="/lib/js/jcarousellite_1.0.1.js"></script>
<script type="text/javascript" src="/lib/js/angular.min.js"></script>
<script type="text/javascript" src="/lib/js/angular-route.min.js"></script>
<script type="text/javascript" src="/lib/js/filters/myFilter.js"></script>
<script type="text/javascript" src="/lib/js/filters/ngRange.js"></script>
<script type="text/javascript" src="/lib/js/services/myCommon.js"></script>
<script type="text/javascript" src="/lib/js/services/dialog-service.js"></script>
<script type="text/javascript" src="/lib/js/directives/myUtil.js"></script>
<script type="text/javascript" src="/lib/js/directives/myPagination.js"></script>
<script type="text/javascript" src="/lib/js/directives/sortable.js"></script>
<script type="text/javascript" src="/smarteditor/js/HuskyEZCreator.js" charset="utf-8"></script>
<script type="text/javascript" src="/lib/js/directives/myNmap.js"></script>
<script type="text/javascript" src="/lib/js/directives/myEditor.js"></script>
<script type="text/javascript" src="http://openapi.map.naver.com/openapi/naverMap.naver?ver=2.0&key=<spring:eval expression="@config['navar.map.key']" />"></script>
<script type="text/javascript">
var app = angular.module("MyApp", ['dialogService', 'ngRoute', 'myUtil', 'myPagination', 'myCommon', 'ui.sortable', 'myNmap', 'myFilter', 'myEditor', 'ngRange']);
app.run(function($rootScope){
	$rootScope.param = {
		gubun : "1",
		group_seq : "${sessionScope.member.group_seq}",
		com_seq : "${sessionScope.member.com_seq}",
		com_nm : "${sessionScope.member.com_nm}"
	};
	$rootScope.main = {};
});
app.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
	$routeProvider
		.when('/list', {controller: 'listCtrl', templateUrl : 'list.do'})
		.when('/write', {controller: 'writeCtrl', templateUrl : 'write.do'})
		.when('/modify/:seq', {controller: 'modifyCtrl', templateUrl : 'modify.do'})
		.otherwise({redirectTo: '/list' });
}]);
app.controller("mainCtrl", function($scope, $location, $window, ajaxService) {
	$scope.list = function(){
		$location.path("/list");
	}
	$scope.main.year = function(){
		return new Date().getFullYear();
	}
});
app.controller("listCtrl", function($scope, $window, $routeParams, $compile, ajaxService, dialogService, $filter) {
	$scope.param.cpage = $scope.param.cpage||1;
	<c:if test = "${sessionScope.member.group_seq ne '1'}">
	$scope.param.session_member_id = '${sessionScope.member.member_id}'
	</c:if>
	
	ajaxService.getAsyncJSON("/json/list/old_code.carmaker.do", $scope.param, function(data){
		$scope.carmaker = data;
		$scope.carmodel = "";
		$scope.cargrade = "";
		$scope.changeCarmaker();
	});
	$scope.changeCarmaker = function(){
		ajaxService.getJSON("/json/list/old_code.carmodel.do", $scope.param, function(data){
			$scope.carmodel = data;
			$scope.cargrade = "";
		});
	}
	
	var angularDateFilter = $filter('date');
	$scope.search_yesterday = function(){
		var sdt = new Date().setDate(new Date().getDate() -1);
		$scope.param.sdate = angularDateFilter(sdt, 'yyyy-MM-dd');
		$scope.param.edate = angularDateFilter(sdt, 'yyyy-MM-dd');
	}
	
	$scope.search_today = function(){
		$scope.param.sdate = angularDateFilter(new Date(), 'yyyy-MM-dd');
		$scope.param.edate = angularDateFilter(new Date(), 'yyyy-MM-dd');
	}
	
	$scope.search_week = function(){
		var sdt = new Date().setDate(new Date().getDate() -7);
		var edt = new Date();
		$scope.param.sdate = angularDateFilter(sdt, 'yyyy-MM-dd');
		$scope.param.edate = angularDateFilter(edt, 'yyyy-MM-dd');
	}
	
	$scope.search_month = function(){
		var sdt = new Date().setDate(new Date().getDate() -30);
		var edt = new Date();
		$scope.param.sdate = angularDateFilter(sdt, 'yyyy-MM-dd');
		$scope.param.edate = angularDateFilter(edt, 'yyyy-MM-dd');
	}
	
	$scope.search_cmonth = function(){
		var today = new Date();
		var sdt = new Date().setDate(1);
		var edt = new Date(today.getFullYear(), today.getMonth()+1, 1).setDate(0);
		$scope.param.sdate = angularDateFilter(sdt, 'yyyy-MM-dd');
		$scope.param.edate = angularDateFilter(edt, 'yyyy-MM-dd');
	}
	
	$scope.search_bmonth = function(){
		var today = new Date();
		var sdt = new Date(today.getFullYear(), today.getMonth()-1, 1).setDate(1);
		var edt = new Date().setDate(0);
		$scope.param.sdate = angularDateFilter(sdt, 'yyyy-MM-dd');
		$scope.param.edate = angularDateFilter(edt, 'yyyy-MM-dd');
	}
	
	$scope.search_all = function(){
		$scope.param.sdate = "";
		$scope.param.edate = "";
	}
	
	$scope.chk_all_btn = function(){
		for (var i = 0; i < $scope.board.list.length; i++) {
			var item = $scope.board.list[i];
			if($scope.board.chk_all){
				item.check = "Y";
			}else{
				item.check = "N";
			}
		}
	}
	
	$scope.board = {};
	$scope.board.go = function(n){
		$scope.param.cpage=n;
		ajaxService.getJSON('/giftcard/admin/goods/part/index.do?mode=list&cpage='+n, $scope.param, function(data){
			$scope.board.list = data.list;
			$scope.board.currentPage = n;
			$scope.board.totalCount = data.pagination.totalcount;
			$scope.board.totalPage = data.pagination.totalpage;
		});
	};
	$scope.del = function(){
		if(!confirm("정말 삭제 하시겠습니까?")){
			return false;
		}
		
		var param = (function(list){
			var rst = [];
			for ( var i in list) {
				var item = list[i];
				if(angular.equals("Y", item.check)){
					rst.push({item_seq : item.item_seq});
				}
			}
			return rst;
		})($scope.board.list);
		
		ajaxService.getJSON("/giftcard/admin/goods/part/index.do?mode=del", {jData : JSON.stringify({del : param})}, function(data){
			if(data.rst == '1'){
				$scope.board.go(1);
			}else{
				alert(data.msg);
			}
		});
	}
	
	$scope.imgPopupList = function(item){
		var options = {
				autoOpen: false,
				modal: true,
				width: "720",
				height: "600",
				close: function(event, ui) {
				}
			};
		
		dialogService.open("ImgPopupListDialog", "imgPopupListTemplete.html", item, options)
		.then(
			function(result) {				
			},
			function(error) {
			}
		);
	}

	$scope.resetCopyParam = angular.copy($scope.param);
	$scope.resetPartSearch = function(){
		$scope.param.carmakerseq = $scope.resetCopyParam.carmakerseq;
		$scope.param.carmodelseq = $scope.resetCopyParam.carmodelseq;
	}
	
});
app.controller("writeCtrl", function($scope, $window, $routeParams, ajaxService, dialogService, $timeout) {
	$scope.form = {
		gubun : $scope.param.gubun,
		com_nm : $scope.param.com_nm,
		com_seq : $scope.param.com_seq,
		files : []
	};
	
	ajaxService.getJSON("/json/list/old_code.carmaker.do", {}, function(data){
		$scope.carmaker = data;
		$scope.carmodel = "";
		$scope.cargrade = "";
		$scope.changeCarmaker();
	});
	$scope.changeCarmaker = function(){
		ajaxService.getJSON("/json/list/old_code.carmodel.do", $scope.form, function(data){
			$scope.carmodel = data;
			$scope.cargrade = "";
		});
	}
	
	$scope.uploadFile = function(){
		var filename = $("#file").val();
		filename = filename.slice(filename.indexOf(".") + 1).toLowerCase();
		if($.inArray(filename, ["gif", "jpg", "jpeg", "png", "bmp"]) < 0 ){
			alert("gif, jpg, jpeg, png, bmp 파일만 올려주시기 바랍니다.");
			return false;
		}
    	$("#wFrm").ajaxSubmit({
    		url : '/ajaxUpload.do',
    		iframe: true,
    		dataType : "json",
    		uploadProgress : function(event, position, total, percentComplete){
    		},
    		success : function(data){
    			$scope.addFile(data);
    		},
    		error: function(e){
    			alert(e.responseText);
    		}
    	});
	}
	
	$scope.addFile = function(data){
		$scope.$apply(function(){
			$scope.form.files.push(data);
		});
	}
	
	$scope.removeFile = function(idx){
		$scope.form.files.splice(idx, 1);
	}
	
	$scope.imgPopup = function(img){
		var options = {
				autoOpen: false,
				modal: true,
				width: "800",
				height: "650",
				close: function(event, ui) {
				}
			};
		
		dialogService.open("ImgPopupDialog", "imgPopupTemplete.html", {img : img}, options)
		.then(
			function(result) {				
			},
			function(error) {
			}
		);
	}
	
	$scope.$watch("form.carmodelseq", function(newVal, oldVal){
		if(angular.equals(newVal, oldVal)){
			return;
		}
		$scope.productnm_sum();
	});
	
	$scope.productnm_sum = function(){
		var f = $scope.form;
		var rst = "";
		for (var i in $scope.carmodel) {
			var item = $scope.carmodel[i];
			if(angular.equals(item.carmodelseq, f.carmodelseq)){
				rst = item.carmodelnm;
				break;
			}
		}
		$scope.form.user_price = rst;
	}
	
	$scope.save = function(){
		
// 		if($scope.form.files.length == 0){
// 			alert("상품사진을 하나이상 올리셔야 합니다.");
// 			return false;
// 		}
		
		if($scope.form.user_pricing_yn == 'N' && $scope.form.supplier_pricing_yn == 'N'){
			alert("판매가격은 한개 이상 선택하셔야 합니다.");
			$("[data-ng-model='form.user_pricing_yn']").focus();
			return false;
		}
			
		if($scope.frm.$invalid){
			if($scope.frm.$error.required){
				alert("필수값을 입력하여 주십시오.");
			}else{
				alert("값이 유효하지 않습니다.");
			}
			$("#wFrm .ng-invalid")[0].focus();
			return false;
		}
		$scope.form.discount_rate = $scope.form.discount_rate2; 
		ajaxService.getJSON("/giftcard/admin/goods/part/index.do?mode=write", {jData : JSON.stringify($scope.form)}, function(data){
			$scope.list();
		});
	}
	
	var copyForm;
	$timeout(function(){
		copyForm = {
			form : angular.copy($scope.form),
			carmodel : angular.copy($scope.carmodel),
			cargrade : angular.copy($scope.cargrade)
		};
	}, 500);
	
	$scope.resetForm = function(){
		$scope.carmaker = angular.copy(copyForm.carmaker);
		$scope.carmodel = angular.copy(copyForm.carmodel);
		$scope.form = angular.copy(copyForm.form);
	}
});
app.controller("modifyCtrl", function($scope, $window, $routeParams, ajaxService, dialogService, $filter, $timeout) {
	$scope.form = {
		files : []
	};
	var angularDateFilter = $filter('myDate');
	ajaxService.getJSON("/giftcard/admin/goods/part/index.do?mode=view", {seq : $routeParams.seq}, function(data){
		$scope.form = data.view;
		$scope.form.sale_sdate = angularDateFilter(data.view.sale_sdate, 'yyyy-MM-dd');
		$scope.form.sale_edate = angularDateFilter(data.view.sale_edate, 'yyyy-MM-dd');
		$scope.form.files = data.files;
		$scope.form.removeFiles = [];
		if(!$scope.form.productnm){
			$timeout(function(){
				$scope.productnm_sum();
			}, 1000);
		}
	});
	
	ajaxService.getJSON("/json/list/old_code.carmaker.do", {}, function(data){
		$scope.carmaker = data;
		$scope.carmodel = "";
		$scope.cargrade = "";
		$scope.changeCarmaker();
	});
	$scope.changeCarmaker = function(){
		ajaxService.getJSON("/json/list/old_code.carmodel.do", $scope.form, function(data){
			$scope.carmodel = data;
			$scope.cargrade = "";
		});
	}

	$scope.uploadFile = function(){
		var filename = $("#file").val();
		filename = filename.slice(filename.indexOf(".") + 1).toLowerCase();
		if($.inArray(filename, ["gif", "jpg", "jpeg", "png", "bmp"]) < 0 ){
			alert("gif, jpg, jpeg, png, bmp 파일만 올려주시기 바랍니다.");
			return false;
		}
    	$("#wFrm").ajaxSubmit({
    		url : '/ajaxUpload.do',
    		iframe: true,
    		dataType : "json",
    		uploadProgress : function(event, position, total, percentComplete){
    		},
    		success : function(data){
    			$scope.addFile(data);
    		},
    		error: function(e){
    			alert(e.responseText);
    		}
    	});
	}
	
	$scope.addFile = function(data){
		$scope.$apply(function(){
			$scope.form.files.push(data);
		});
	}
	
	$scope.removeFile = function(idx){
		$scope.form.removeFiles.push($scope.form.files[idx]);
		$scope.form.files.splice(idx, 1);
	}
	
	$scope.imgPopup = function(img){
		var options = {
				autoOpen: false,
				modal: true,
				width: "800",
				height: "650",
				close: function(event, ui) {
				}
			};
		
		dialogService.open("ImgPopupDialog", "imgPopupTemplete.html", {img : img}, options)
		.then(
			function(result) {				
			},
			function(error) {
			}
		);
	}
	
	$scope.$watch("form.carmodelseq", function(newVal, oldVal){
		if(angular.equals(newVal, oldVal)){
			return;
		}
		$scope.productnm_sum();
	});
	
	$scope.productnm_sum = function(){
		var f = $scope.form;
		var rst = "";
		for (var i in $scope.carmodel) {
			var item = $scope.carmodel[i];
			if(angular.equals(item.carmodelseq, f.carmodelseq)){
				rst = item.carmodelnm;
				break;
			}
		}
		$scope.form.user_price = rst;
	}
	
	
	$scope.save = function(){
		
// 		if($scope.form.files.length == 0){
// 			alert("상품사진을 하나이상 올리셔야 합니다.");
// 			return false;
// 		}
		
		if($scope.form.user_pricing_yn == 'N' && $scope.form.supplier_pricing_yn == 'N'){
			alert("판매가격은 한개 이상 선택하셔야 합니다.");
			$("[data-ng-model='form.user_pricing_yn']").focus();
			return false;
		}
			
		if($scope.frm.$invalid){
			if($scope.frm.$error.required){
				alert("필수값을 입력하여 주십시오.");
			}else{
				alert("값이 유효하지 않습니다.");
			}
			$("#wFrm .ng-invalid")[0].focus();
			return false;
		}
		$scope.form.discount_rate = $scope.form.discount_rate2; 
		ajaxService.getJSON("/giftcard/admin/goods/part/index.do?mode=modify", {jData : JSON.stringify($scope.form)}, function(data){
			$scope.list();
		});
	}
	
	var copyForm;
	$timeout(function(){
		copyForm = {
			form : angular.copy($scope.form),
			carmaker : angular.copy($scope.carmaker),
			carmodel : angular.copy($scope.carmodel)
		};
	}, 500);
	
	$scope.resetForm = function(){
		$scope.carmaker = angular.copy(copyForm.carmaker);
		$scope.carmodel = angular.copy(copyForm.carmodel);
		$scope.form = angular.copy(copyForm.form);
	}
});

app.controller("imgPopupListCtrl", function($scope, $timeout, ajaxService, dialogService) {
	ajaxService.getJSON('/json/list/part.photoList.do', $scope.model, function(data){
		$scope.photoList = data;
		if($scope.photoList.length==0){
			alert("등록된 이미지가 없습니다.");
			dialogService.cancel("ImgPopupListDialog");
		}
	});
	
	$scope.slide = function(){
		$(".carousel").jCarouselLite({
			auto: 3000,
			speed: 500,
			visible: 1,
			btnPrev : "#roll_prev",
			btnNext : "#roll_next",
		});
	}
});
</script>
</head>
<body data-ng-controller="mainCtrl" data-ng-cloak>
  <div data-ng-view data-ng-cloak></div>
  <script type="text/ng-template" id="excelUploadTemplete.html">
	<div ng-controller="excelUploadCtrl" title="엑셀일괄등록">
	<form name="excelFrm" id="excelFrm" method="post" novalidate="novalidate" enctype="multipart/form-data">
		<table class="style_3">
            <colgroup>
              <col width="20%" />
              <col width="80%" />
            </colgroup>
            <tr>
              <th>파일첨부</th>
              <td><input type="file" id="excelfile" name="excelfile" required/></td>
            </tr>
		</table>
      
		<div class="btn_bottom">
        	<div class="r_btn">
          		<span class="bt_all">
          			<span><input type="button" value="업로드" class="btall" data-ng-click="upload()"/></span>
          		</span> 
        	</div>
      	</div>
	</form>
	</div>
  </script>
  <script type="text/ng-template" id="imgPopupTemplete.html">
	<div title="이미지 크게보기">
		<img src="{{model.img}}" style="width:100%;"/>
	</div>
  </script>
  <script type="text/ng-template" id="imgPopupListTemplete.html">
	<div ng-controller="imgPopupListCtrl" title="이미지 크게보기">
        <div class="carousel">
            <ul>
                <li data-ng-repeat="item in photoList" on-finish-render="slide()"><img src="/upload/board/{{item.yyyy}}/{{item.mm}}/{{item.uuid}}" data-err-src="/images/common/no_image.gif" style="width:700px;"/></li>
            </ul>
			<div class="arrow">
				<span><a href="#" id="roll_prev"><img src="/images/container/brand_arrow_l.gif" alt="이전"></a></span>
				<span><a href="#" id="roll_next"><img src="/images/container/brand_arrow_r.gif" alt="다음"></a></span>
			</div>
        </div>
	</div>
  </script>
</body>
</html>