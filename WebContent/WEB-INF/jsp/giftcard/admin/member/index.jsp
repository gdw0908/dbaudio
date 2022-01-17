<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
var app = angular.module("MyApp", ['dialogService', 'ngRoute', 'myUtil', 'myFilter', 'myPagination', 'myCommon', 'ui.sortable']);
app.run(function($rootScope){
	$rootScope.param = {
// 		group_seq : "${sessionScope.member.group_seq}"
	};
	$rootScope.main = {};
});
app.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
	$routeProvider
		.when('/list/:group_seq', {controller: 'listCtrl', templateUrl : 'list.do'})
		.when('/write/:group_seq', {controller: 'writeCtrl', templateUrl : 'write.do'})
		.when('/modify/:member_seq', {controller: 'modifyCtrl', templateUrl : 'modify.do'})
		.otherwise({redirectTo: '/list/1' });//1:관리자, 2:일반회원
}]);
app.controller("mainCtrl", function($scope, $location, $window, ajaxService) {
// 	ajaxService.getJSON("/json/list/code.codeList.do", {code_group_seq : '1'}, function(data){
// 		$scope.main.member_type = data;
// 	});
	ajaxService.getAsyncJSON("/json/list/code.codeList.do", {code_group_seq : '5'}, function(data){
		$scope.main.tel1 = data;
	});
	ajaxService.getAsyncJSON("/json/list/code.codeList.do", {code_group_seq : '4'}, function(data){
		$scope.main.cell1 = data;
	});
	ajaxService.getAsyncJSON("/json/list/code.codeList.do", {code_group_seq : '6'}, function(data){
		$scope.main.email2 = data;
	});
	$scope.main.openAddr = function(){
		var param = "";
		if(arguments[0]){
			param = "?fun="+arguments[0];
		}
		$window.open("/addr/road.jsp"+param,"addr","width=570,height=420");
	}
	$scope.list = function(){
		$location.path("/list");
	}
	
	$scope.list1 = function(g_seq){
		$location.path("/list/" + g_seq);
	}
});
app.controller("listCtrl", function($scope, $window, $location, $routeParams, $compile, ajaxService, dialogService) {
	$scope.param.cpage = $scope.param.cpage||1;
	$scope.param.group_seq = $routeParams.group_seq;
	$scope.board = {};
	$scope.board.go = function(n){
		$scope.param.cpage=n;
		ajaxService.getJSON('/json/list/member.list.do?cpage='+n, $scope.param, function(data){
			$scope.board.list = data;
		});
		ajaxService.getJSON('/json/request/member.pagination.do', $scope.param, function(data){
			$scope.board.currentPage = n;
			$scope.board.totalCount = data.totalcount;
			$scope.board.totalPage = data.totalpage;
		});
	};
	$scope.goPage = function(pagenum){
		$scope.param.keyword = "";
		$scope.param.cpage = 1;	
		$scope.param.group_seq = pagenum;
		$scope.board.go(1);
	}
	$scope.login = function(item){
		if(!confirm(item.member_nm + "님의 아이디로 로그인 하시겠습니까?")){
			return false;
		}
		ajaxService.getJSON("/giftcard/admin/login.do?mode=superLogin", item, function(data){
			if(data.rst == '1'){
				alert(item.member_nm + "님의 아이디로 로그인 하였습니다.");
				window.top.location.href="/";
			}else{
				alert(data.msg);
			}
		});
	}
	
	$scope.passwordInit = function(item){
		if(!confirm("비밀번호를 초기화 하시겠습니까?")){
			return false;
		}
		ajaxService.getJSON("/giftcard/member/member.do?mode=passwordInit", item, function(data){
			if(data.rst == '1'){
				alert("비밀번호를 초기화 했습니다.");
			}else{
				alert(data.msg);
			}
		});
	}
});
app.controller("writeCtrl", function($scope, $window, $routeParams, ajaxService, dialogService) {
	$scope.form = {
		group_seq : $routeParams.group_seq,
		basongji : []
	};
	$scope.duplicateCheckMemberId = function(){
		if($scope.form.member_id === undefined){
			return false;
		}
		ajaxService.getJSON("/json/request/member.duplicateCheckMemberId.do", $scope.form, function(data){
		  	$scope.form.isDuplicateId = data.cnt>0?true:false;
		});
	}
	
	$window.setAddr = function(roadAddrPart1, addrDetail, zipNo, jibunAddr) {
		var zip = zipNo.split("-");
		$scope.$apply(function(){
			$scope.form.zip1 = zip[0];
			$scope.form.zip2 = zip[1];
			$scope.form.addr1 = jibunAddr;
			$scope.form.addr2 = addrDetail;
		});
    }
	
	$scope.basongjiOpen = function(){
		var options = {
				autoOpen: false,
				modal: true,
				width: "700",
				height: "auto",
				close: function(event, ui) {
				}
			};
		
		dialogService.open("basongjiDialog","/lib/js/partials/basongji.html", {}, options)
		.then(
			function(result) {
				if(result.default_yn=='Y'){
					for (var i = 0; i < $scope.form.basongji.length; i++) {//다른 배송지는 초기화
						$scope.form.basongji[i].default_yn='N';
					}
				}
				$scope.form.basongji.push(result);
			},
			function(error) {
			}
		);
	}
	
	$scope.basongjiModify = function(idx, item){
		var options = {
				autoOpen: false,
				modal: true,
				width: "700",
				height: "auto",
				close: function(event, ui) {
				}
			};
		
		dialogService.open("basongjiDialog","/lib/js/partials/basongji.html", angular.copy(item), options)
		.then(
			function(result) {
				if(result.default_yn=='Y'){
					for (var i = 0; i < $scope.form.basongji.length; i++) {//다른 배송지는 초기화
						$scope.form.basongji[i].default_yn='N';
					}
				}
				$scope.form.basongji.splice(idx,1, result);
			},
			function(error) {
			}
		);
	}
	
	$scope.basongjiCheckAll = function(){
		$("#basongji_t input[type=checkbox]").prop("checked", true);
	}
	
	$scope.basongjiCheckCancel = function(){
		$("#basongji_t input[type=checkbox]").prop("checked", false);
	}
	
	$scope.basongjiDefault = function(){
		for (var i = 0; i < $scope.form.basongji.length; i++) {//다른 배송지는 초기화
			$scope.form.basongji[i].default_yn='N';
		}
		$scope.form.basongji[$("#basongji_t input[type=checkbox]:checked").val()].default_yn='Y';
	}
	
	$scope.basongjiRemove = function(){
		$($("#basongji_t input[type=checkbox]:checked").get().reverse()).each(function(){
			$scope.form.basongji.splice($(this).val(), 1);
		});
	}
	
	$scope.save = function(){
		if($scope.duplicateCheckMemberId()){
			alert("아이디중복을 확인해주세요.");
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
		/*
		if($scope.form.basongji.length <= 0){
			alert("배송지를 한곳 이상 입력해 주세요.");
			return false;
		}
		*/
		
		ajaxService.getJSON("/giftcard/member/member.do?mode=write", {jData : JSON.stringify($scope.form)}, function(data){
			$scope.list();
		});
	}
});
app.controller("modifyCtrl", function($scope, $window, $routeParams, ajaxService, dialogService) {
	$scope.form = {};
	ajaxService.getJSON("/giftcard/member/member.do?mode=view", {member_seq : $routeParams.member_seq}, function(data){
		$scope.form = data.view;
		$scope.form.basongji = data.basongji;
	});
	
	$window.setAddr = function(roadAddrPart1, addrDetail, zipNo, jibunAddr) {
		var zip = zipNo.split("-");
		$scope.$apply(function(){
			$scope.form.zip1 = zip[0];
			$scope.form.zip2 = zip[1];
			$scope.form.addr1 = jibunAddr;
			$scope.form.addr2 = addrDetail;
		});
    }
	
	$scope.basongjiOpen = function(){
		var options = {
				autoOpen: false,
				modal: true,
				width: "700",
				height: "auto",
				close: function(event, ui) {
				}
			};
		
		dialogService.open("basongjiDialog","/lib/js/partials/basongji.html", {}, options)
		.then(
			function(result) {
				if(result.default_yn=='Y'){
					for (var i = 0; i < $scope.form.basongji.length; i++) {//다른 배송지는 초기화
						$scope.form.basongji[i].default_yn='N';
					}
				}
				$scope.form.basongji.push(result);
			},
			function(error) {
			}
		);
	}
	
	$scope.basongjiModify = function(idx, item){
		var options = {
				autoOpen: false,
				modal: true,
				width: "700",
				height: "auto",
				close: function(event, ui) {
				}
			};
		
		dialogService.open("basongjiDialog","/lib/js/partials/basongji.html", angular.copy(item), options)
		.then(
			function(result) {
				if(result.default_yn=='Y'){
					for (var i = 0; i < $scope.form.basongji.length; i++) {//다른 배송지는 초기화
						$scope.form.basongji[i].default_yn='N';
					}
				}
				$scope.form.basongji.splice(idx,1, result);
			},
			function(error) {
			}
		);
	}
	
	$scope.basongjiCheckAll = function(){
		$("#basongji_t input[type=checkbox]").prop("checked", true);
	}
	
	$scope.basongjiCheckCancel = function(){
		$("#basongji_t input[type=checkbox]").prop("checked", false);
	}
	
	$scope.basongjiDefault = function(){
		for (var i = 0; i < $scope.form.basongji.length; i++) {//다른 배송지는 초기화
			$scope.form.basongji[i].default_yn='N';
		}
		$scope.form.basongji[$("#basongji_t input[type=checkbox]:checked").val()].default_yn='Y';
	}
	
	$scope.basongjiRemove = function(){
		$($("#basongji_t input[type=checkbox]:checked").get().reverse()).each(function(){
			$scope.form.basongji.splice($(this).val(), 1);
		});
	}
	
	$scope.passwordInit = function(item){
		if(!confirm("비밀번호를 초기화 하시겠습니까?")){
			return false;
		}
		ajaxService.getJSON("/giftcard/member/member.do?mode=passwordInit", item, function(data){
			if(data.rst == '1'){
				alert("비밀번호를 초기화 했습니다.");
			}else{
				alert(data.msg);
			}
		});
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
		
		/*
		if($scope.form.basongji.length <= 0){
			alert("배송지를 한곳 이상 입력해 주세요.");
			return false;
		}
		*/
		
		ajaxService.getJSON("/giftcard/member/member.do?mode=modify", {jData : JSON.stringify($scope.form)}, function(data){
			$scope.list();
		});
	}
	
});
app.controller('basongjiCtrl', function($scope, $window, ajaxService, dialogService) {
	$scope.data = $scope.model;
	
	$window.setAddrB = function(roadAddrPart1, addrDetail, zipNo, jibunAddr) {
		var zip = zipNo.split("-");
		$scope.$apply(function(){
			$scope.data.zip1 = zip[0];
			$scope.data.zip2 = zip[1];
			$scope.data.addr1 = jibunAddr;
			$scope.data.addr2 = addrDetail;
		});
    }
	
	$scope.save = function() {
		if($scope.frm.$invalid){
			if($scope.frm.$error.required){
				alert("필수값을 입력하여 주십시오.");
			}else{
				alert("값이 유효하지 않습니다.");
			}
			$("#basongjiFrm .ng-invalid")[0].focus();
			return false;
		}
		dialogService.close("basongjiDialog", $scope.data);
	};

	$scope.cancel = function() {
		dialogService.cancel("basongjiDialog");
	};
});
</script>
</head>
<body data-ng-controller="mainCtrl">
  <div data-ng-view data-ng-cloak></div>
</body>
</html>
