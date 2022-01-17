<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html data-ng-app="MyApp">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>티켓모아 관리시스템</title>
<link href="/lib/css/cmsbase.css" rel="stylesheet" type="text/css" />
<link href="/lib/css/cmsadmin.css" rel="stylesheet" type="text/css" />
<link href="/lib/css/redmond/jquery-ui-1.9.1.custom.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/lib/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="/lib/js/jquery-ui-1.10.3.custom.min.js"></script>
<script type="text/javascript" src="/lib/js/jquery.cookie.js"></script>
<!--[if !IE]> -->
<script type="text/javascript" src="/lib/js/angular.min.js"></script>
<script type="text/javascript" src="/lib/js/services/myCommon.js"></script>
<script type="text/javascript">
var app = angular.module("MyApp", ['myCommon']);
app.controller("loginCtrl", function($scope, ajaxService) {
	$("#member_id").focus();
	$scope.form={};
	if(!!$.cookie("admin_save_id")){
		$scope.form.member_id = $.cookie("admin_save_id");
		$scope.form.id_save=true;
	}
	
	$scope.login = function(){
    	ajaxService.getJSON("/giftcard/admin/login.do?mode=proc", $scope.form, function(data){
    		if(data.rst == "1"){
    			if($scope.form.id_save){
	    			$.cookie("admin_save_id", $scope.form.member_id, { path: '/admin', expires: 365 });
    			}else{
	    			$.cookie("admin_save_id", "", { path: '/admin', expires: -1 });
    			}
    			location.href="/giftcard/admin/index.do";
    		}else{
    			alert(data.msg);
    		}
    	});
	}
});
</script>
<!-- <![endif]-->

<!--[if gte IE 8]>
<script type="text/javascript" src="/lib/js/angular.min.js"></script>
<script type="text/javascript" src="/lib/js/services/myCommon.js"></script>
<script type="text/javascript">
var app = angular.module("MyApp", ['myCommon']);
app.controller("loginCtrl", function($scope, ajaxService) {
	$("#member_id").focus();
	$scope.form={};
	if(!!$.cookie("admin_save_id")){
		$scope.form.member_id = $.cookie("admin_save_id");
		$scope.form.id_save=true;
	}
	
	$scope.login = function(){
    	ajaxService.getJSON("/admin/login.do?mode=proc", $scope.form, function(data){
    		if(data.rst == "1"){
    			if($scope.form.id_save){
	    			$.cookie("admin_save_id", $scope.form.member_id, { path: '/admin', expires: 365 });
    			}else{
	    			$.cookie("admin_save_id", "", { path: '/admin', expires: -1 });
    			}
    			location.href="/admin/index.jsp";
    		}else{
    			alert(data.msg);
    		}
    	});
	}
});
</script>
<![endif]-->


<!--[if lte IE 7]>
<script type="text/javascript">
	alert("익스플로러 7버전 이하는 지원하지 않습니다.");
</script>
<![endif]-->
</head>
<body data-ng-controller="loginCtrl" class="loginbody">
  <div id="login_wrap">
    <div class="loginbox"> 
	  <div class="memberlogin">
	      <h1 class="login_tit"><img src="/images/admin/header/logo.svg"></h1>	  
		  <form id="frm" name="frm" ng-submit="login()" method="post">
		      <fieldset>
		        <legend>로그인</legend>
			      <div class="memboxlogin">		  
					  <div class="memberinput">
					    <input type="text" name="member_id" id="member_id" data-ng-model="form.member_id" >
					    <input type="password" name="member_pw" data-ng-model="form.member_pw" style="margin-top: 7px;">
					    <div class="loginchbox">
					      <label>
					        <input type="checkbox" data-ng-model="form.id_save"/> <span>아이디저장</span>
					      </label>
					    </div>
					  </div>
					  <div>
					    <span class="login_btn"><input type="submit" value="로그인"></span>
					  </div>
				  </div>	  
		      </fieldset>
		  </form>
	  </div>
	</div>	  
  </div>
  <div class="footlogin">Copryright@PARTSMOA.ALL Rights Reserved.</div>
</body>
</html>
