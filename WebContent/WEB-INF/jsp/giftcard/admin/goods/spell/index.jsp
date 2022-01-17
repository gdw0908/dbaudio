<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${!(sessionScope.member.group_seq eq '1' || sessionScope.member.group_seq eq '3')}">
<c:redirect url="/noPermission.do"/>
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
		tab : "${empty param.tab?'all':param.tab}",
		group_seq : "${sessionScope.member.group_seq}",
		com_seq : "${sessionScope.member.com_seq}",
		com_nm : "${sessionScope.member.com_nm}",
		status : (function(){
			var tab = "${empty param.tab?'all':param.tab}";
			if(tab == '1'){
				return '99';
			}else if(tab == '2'){
				return '1';
			}else if(tab == '3'){
				return '3';
			}else if(tab == '4'){
				return '5';
			}else if(tab == '5'){
				return '6';
			}else if(tab == '6'){
				return '9';
			}else if(tab == '7'){
				return '13';
			}else if(tab == '8'){
				return '19';
			}else if(tab == '9'){
				return '23';
			}else{
				return "";
			}
		})()
	};
	$rootScope.main = {};
});
app.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
	$routeProvider
		.when('/list', {controller: 'listCtrl', templateUrl : 'list.do'})
		.when('/write', {controller: 'writeCtrl', templateUrl : 'write.do'})
		.when('/modify/:cart_no', {controller: 'modifyCtrl', templateUrl : 'modify.do'})
		.otherwise({redirectTo: '/list' });
}]);
app.controller("mainCtrl", function($scope, $location, $window, ajaxService) {
	$scope.main.tabmove = function(tab){
		$scope.param.tab = tab;
		if(tab == '1'){
			$scope.param.status = '99';
		}else if(tab == '2'){
			$scope.param.status = '1';
		}else if(tab == '3'){
			$scope.param.status = '3';
		}else if(tab == '4'){
			$scope.param.status = '5';
		}else if(tab == '5'){
			$scope.param.status = '6';
		}else if(tab == '6'){
			$scope.param.status = '9';
		}else if(tab == '7'){
			$scope.param.status = '13';
		}else if(tab == '8'){
			$scope.param.status = '19';
		}else if(tab == '9'){
			$scope.param.status = '23';
		}else{
			$scope.param.status = '';
		}
		
		$location.path("#/list");
	}
	
	ajaxService.getAsyncJSON("/json/list/code.codeList.do", {code_group_seq : '46'}, function(data){
		$scope.main.paytyp = data;
	});

	ajaxService.getAsyncJSON("/json/list/code.codeList.do", {code_group_seq : '45'}, function(data){
		$scope.main.status = data;
	});

	ajaxService.getAsyncJSON("/json/list/code.codeList.do", {code_group_seq : '42'}, function(data){
		$scope.main.delivery = data;
	});
	
	$scope.list = function(){
		$location.path("/list");
	}
	ajaxService.getJSON("/json/list/part.cooperationList.do", {}, function(data){
		$scope.main.cooperationList = data;
	});
});
app.controller("listCtrl", function($scope, $window, $routeParams, $compile, ajaxService, dialogService, $filter) {
	$scope.param.cpage = $scope.param.cpage||1;
	var searchParam = angular.copy($scope.param);
	$scope.searchInit = function(){
		$scope.param = angular.copy(searchParam);
	}

	ajaxService.getAsyncJSON("/json/list/code.codeList.do", {code_group_seq : '39'}, function(data){
		$scope.paytyp = data;
	});

	ajaxService.getAsyncJSON("/json/list/code.codeList.do", {code_group_seq : '40'}, function(data){
		$scope.status = data;
	});
	
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
		ajaxService.getJSON('/json/list/spell.list.do?cpage='+n, $scope.param, function(data){
			$scope.board.list = data;
		});
		ajaxService.getJSON('/json/request/spell.pagination.do', $scope.param, function(data){
			$scope.board.currentPage = n;
			$scope.board.totalCount = data.totalcount;
			$scope.board.totalPage = data.totalpage;
		});
		$scope.param.tot = 'Y';
		ajaxService.getJSON('/json/request/spell.getAmount.do', $scope.param, function(data){
			$scope.board.amount = data.amount;
		});
		$scope.param.tot = 'N';
	};
	
	$scope.openCooperationSearch = function(){
		var options = {
				autoOpen: false,
				modal: true,
				width: "800",
				height: "600",
				close: function(event, ui) {
				}
			};
		
		dialogService.open("CooperationSearchDialog","/lib/js/partials/cooperationSearch.html", {}, options)
		.then(
			function(result) {
				$scope.param.com_nm = result.com_nm;
				$scope.param.com_seq = result.seq;
			},
			function(error) {
			}
		);
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
	
	$scope.excelDown = function(){
		var param = angular.copy($scope.param);
		param.rows=999999;
		$("#excelFrm").empty();
		$.each(param, function(key, val){
			$("#excelFrm").append("<input type='hidden' name='"+key+"' value='"+val+"'/>");
		});
		$("#excelFrm").submit();
	}
});

app.controller("modifyCtrl", function($scope, $window, $routeParams, ajaxService, dialogService, $filter, $timeout) {
	$scope.form = {};
	var angularDateFilter = $filter('myDate');
	ajaxService.getJSON("/giftcard/admin/goods/spell/index.do?mode=view", {cart_no : $routeParams.cart_no}, function(data){
		$scope.view = data.view;
		$scope.form = {
			cart_no : 	data.view.cart_no,
			status : 	data.view.status,
			delivery : 	data.view.delivery,
			ch_c_no : 	data.view.ch_c_no,
			change_dt : (function(data){
				var rst;
				if($.inArray(data.status, ["13"]) > -1 ){//교환신청중
					rst = angularDateFilter(data.ch_dt, 'yyyy-MM-dd');
				}else if($.inArray(data.status, ["15"]) > -1 ){//교환제품 도착
					rst = angularDateFilter(data.ch_d_dt, 'yyyy-MM-dd');
				}else if($.inArray(data.status, ["17"]) > -1 ){//교환제품 발송
					rst = angularDateFilter(data.ch_b_dt, 'yyyy-MM-dd');
				}else if($.inArray(data.status, ["11"]) > -1 ){//반품제품도착일
					rst = angularDateFilter(data.ban_d_dt, 'yyyy-MM-dd');
				}else if($.inArray(data.status, ["12"]) > -1 ){//반품완료일
					rst = angularDateFilter(data.ban_c_dt, 'yyyy-MM-dd');
				}else if($.inArray(data.status, ["9"]) > -1 ){//반품배송일
					rst = angularDateFilter(data.ban_b_dt, 'yyyy-MM-dd');
				}else if($.inArray(data.status, ["19"]) > -1 ){//환불신청일
					rst = angularDateFilter(data.han_dt, 'yyyy-MM-dd');
				}else if($.inArray(data.status, ["21"]) > -1 ){//환불제품도착일
					rst = angularDateFilter(data.han_d_dt, 'yyyy-MM-dd');
				}else if($.inArray(data.status, ["22"]) > -1 ){//환불완료일
					rst = angularDateFilter(data.han_c_dt, 'yyyy-MM-dd');
				}else if($.inArray(data.status, ["3"]) > -1 ){//결제취소신청일
					rst = angularDateFilter(data.pay_c_dt, 'yyyy-MM-dd');
				}else if($.inArray(data.status, ["5"]) > -1 ){//결제취소완료일
					rst = angularDateFilter(data.pay_e_dt, 'yyyy-MM-dd');
				}else if($.inArray(data.status, ["2"]) > -1 ){//주문취소일
					rst = angularDateFilter(data.order_c_dt, 'yyyy-MM-dd');
				}else if($.inArray(data.status, ["7"]) > -1 ){//배송중
					rst = angularDateFilter(data.ba_dt, 'yyyy-MM-dd');
				}else{
					rst = angularDateFilter(Date.now(), 'yyyy-MM-dd');
				}
				return rst;
			})(data.view)
		}
	});
	
	$scope.isSongjang = function(){
		if($.inArray($scope.form.status, ["7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"]) > -1 ){
			return true;
		}else{
			return false;
		}
	}
	
	$scope.getFeeAmt = function(){
		var val="";
		if($scope.view.fee_yn=='C'){
			val="착불";
		}else if($scope.view.fee_yn=='N'){
			val="무료"
		}else{
			if($scope.view.cod_yn=="Y"){
				val = $scope.view.fee_amt + " 원";
			}else{
				val = $scope.view.fee_amt + " 원(미결제)";
			}
		}
		return val;
	}
	
	$scope.open_specification = function(str)
	{
		var popup = window.open('/admin/goods/spell/specification_pop.do?cart_no='+str,'specification_pop','width='+screen.availWidth+',height='+screen.availHeight+',toolbar=no,menubar=no,location=no,scrollbars=yes,status=no,resizable=no,fullscreen=no,channelmode=no,left=0,top=0');
		if(popup != null) popup.focus();
	}
	
	$scope.save = function(){
		
		if($scope.frm.$invalid){
			if($scope.frm.$error.required){
				alert("필수값을 입력하여 주십시오.");
			}else{
				alert("값이 유효하지 않습니다.");
			}
			$("#wFrm .ng-invalid")[0].focus();
			return false;
		}
		var txt = (function(status){
			var rst;
			for(var i in $scope.main.status) {
				var item = $scope.main.status[i];
				if(item.code == status){
					rst = item.code_nm;
					break;
				}
			} 
			return rst;
		})($scope.form.status);
		if(confirm("주문상태를 ("+ txt +")로 변경 하시겠습니까?")){
			ajaxService.getJSON("/admin/goods/spell/index.do?mode=modify", {jData : JSON.stringify($scope.form)}, function(data){
				alert("처리되었습니다.");
				$scope.list();
			});
		}
	}
});

app.controller("cooperationSearchCtrl", function($scope, $window, $routeParams, ajaxService, dialogService) {
	$scope.param = {
		condition : "com_nm"
	};
	$scope.board = {};
	$scope.board.go = function(n){
		ajaxService.getJSON('/json/list/cooperation.list.do?cpage='+n, $scope.param, function(data){
			$scope.board.list = data;
		});
		ajaxService.getJSON('/json/request/cooperation.pagination.do', $scope.param, function(data){
			$scope.board.currentPage = n;
			$scope.board.totalCount = data.totalcount;
			$scope.board.totalPage = data.totalpage;
		});
	};
	
	$scope.send = function(item) {
		dialogService.close("CooperationSearchDialog", item);
	};

	$scope.cancel = function() {
		dialogService.cancel("CooperationSearchDialog");
	};
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
<body data-ng-controller="mainCtrl">
  <div data-ng-view data-ng-cloak></div>
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
  <form id="excelFrm" action="/admin/goods/spell/index.do?mode=excelDown" method="post">
  </form>
</body>
</html>