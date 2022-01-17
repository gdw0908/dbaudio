<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html data-ng-app="MyApp">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=10" />
<title>티켓모아 통합관리 시스템</title>
<link href="/lib/css/cmsbase.css" rel="stylesheet" type="text/css" />
<link href="/lib/css/cmsadmin.css" rel="stylesheet" type="text/css" />
<link href="/lib/css/redmond/jquery-ui-1.9.1.custom.min.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/lib/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="/lib/js/jquery-ui-1.10.3.custom.min.js"></script>
<script type="text/javascript" src="/lib/js/jquery.form.js"></script>
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
var app = angular.module("MyApp", ['dialogService', 'ngRoute', 'myUtil', 'myFilter', 'myPagination', 'myCommon', 'ui.sortable', 'myNmap', 'myEditor']);
app.run(function($rootScope){
	$rootScope.param = {
		board_seq : "3"
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
	ajaxService.getJSON("/json/list/code.codeList.do", {code_group_seq : '5'}, function(data){
		$scope.main.tel = data;
	});	
	$scope.list = function(){
		$location.path("/list");
	}
});
app.controller("listCtrl", function($scope, $window, $routeParams, $compile, ajaxService, dialogService) {
	$scope.board = {};
	$scope.board.go = function(n){
		ajaxService.getJSON('/json/list/article.list.do?cpage='+n, $scope.param, function(data){
			$scope.board.list = data;
		});
		ajaxService.getJSON('/json/request/article.page_info.do', $scope.param, function(data){
			$scope.board.currentPage = n;
			$scope.board.totalCount = data.totalcount;
			$scope.board.totalPage = data.totalpage;
		});
	};
});

app.controller("writeCtrl", function($scope, $window, $routeParams, $rootScope, ajaxService, dialogService) {
	$scope.form = {
		board_seq : $rootScope.param.board_seq,
		files : [],
		charge_tel : ''
	};
	
	$scope.uploadFile = function(){
		var filename = $("#file").val();
		filename = filename.slice(filename.indexOf(".") + 1).toLowerCase();
		if(filename == ""){
			return false;
		}
		/*if($.inArray(filename, ["gif", "jpg", "jpeg", "png", "bmp"]) < 0 ){
			alert("gif, jpg, jpeg, png, bmp 파일만 올려주시기 바랍니다.");
			return false;
		}
		*/
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
		var charge_tel = $scope.form.tel1+"-"+$scope.form.tel2+"-"+$scope.form.tel3;
		$scope.form.charge_tel = charge_tel;
		ajaxService.getJSON("/giftcard/admin/system/board/forces/index.do?mode=write", {jData : JSON.stringify($scope.form)}, function(data){
			$scope.list();
		});
	}
});

app.controller("modifyCtrl", function($scope, $window, $routeParams, ajaxService, dialogService) {
	$scope.form = {};
	$scope.comment = {};
	ajaxService.getJSON("/giftcard/admin/system/board/forces/index.do?mode=view", {article_seq : $routeParams.seq, board_seq : $scope.param.board_seq}, function(data){
		$scope.form = data.view;
		$scope.cfrm.article_seq = data.view.article_seq; 
		if(!data.view.charge_tel == ''){
			var tel = data.view.charge_tel;
			$scope.form.tel1 =  tel.substring(0,tel.indexOf("-"));
			$scope.form.tel2 = tel.substring(tel.indexOf("-")+1,tel.lastIndexOf("-"));
			$scope.form.tel3 = tel.substring(tel.lastIndexOf("-")+1,tel.length);
		}else{
			$scope.form.tel1 = '02';
		}
		$scope.form.files = data.files;
		$scope.form.removeFiles = [];
		ajaxService.getJSON("/giftcard/admin/system/board/forces/index.do?mode=commentList", {jData : JSON.stringify({article_seq : $routeParams.seq})}, function(data){
			$scope.comment = data.comment;
		});	
		
	});
	
	$scope.removeFile = function(idx){
		$scope.form.removeFiles.push($scope.form.files[idx]);
		$scope.form.files.splice(idx, 1);
	}
	
	$scope.addFile = function(data){
		$scope.$apply(function(){
			$scope.form.files.push(data);
		});
	}
	
	$scope.uploadFile = function(){
		var filename = $("#file").val();
		filename = filename.slice(filename.indexOf(".") + 1).toLowerCase();
		if(filename == ""){
			return false;
		}
		/*wif($.inArray(filename, ["gif", "jpg", "jpeg", "png", "bmp"]) < 0 ){
			alert("gif, jpg, jpeg, png, bmp 파일만 올려주시기 바랍니다.");
			return false;
		}
		*/
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
		var charge_tel = $scope.form.tel1+"-"+$scope.form.tel2+"-"+$scope.form.tel3;
		$scope.form.charge_tel = charge_tel;
		ajaxService.getJSON("/giftcard/admin/system/board/forces/index.do?mode=modify", {jData : JSON.stringify($scope.form)}, function(data){
			$scope.list();
		});
	}

	$scope.del = function(){
		if(confirm("삭제하시겠습니까?")){
			ajaxService.getJSON("/giftcard/admin/system/board/forces/index.do?mode=del", {jData : JSON.stringify($scope.form)}, function(data){
				$scope.list();
			});
		}
	}
	
	
	$scope.commentSubmit = function(){
		if($scope.cfrm.$invalid){
			if($scope.cfrm.$error.required){
				alert("필수값을 입력하여 주십시오.");
			}else{
				alert("값이 유효하지 않습니다.");
			}
			$("#cFrm .ng-invalid")[0].focus();
			return false;
		}
		
			
		ajaxService.getJSON("/giftcard/admin/system/board/forces/index.do?mode=commentInsert", {jData : JSON.stringify($scope.cfrm)}, function(data){
			ajaxService.getJSON("/giftcard/admin/system/board/forces/index.do?mode=commentList", {jData : JSON.stringify({article_seq : $routeParams.seq})}, function(data2){
				$scope.cfrm.conts = "";
				$scope.comment = data2.comment;
			});	
		});	
	}
	
	
	$scope.commentReplyOpen = function(idx){
		$(".comment_reply").css("display","none");
		$("#cp"+idx).val("");
		$("#c"+idx).css("display","block");
		return false;		
	}
	
	$scope.commentReplyClose = function(idx){
		$("#c"+idx).css("display","none");
		return false;		
	}
	
	$scope.commentReply = function(idx){
		var textarea = $("#cp"+idx).val();
		if(textarea != ""){
			var array = {
				conts : textarea,
				comment_seq : idx				
			}
			ajaxService.getJSON("/giftcard/admin/system/board/forces/index.do?mode=commentReply", {jData : JSON.stringify(array)}, function(data){
				ajaxService.getJSON("/giftcard/admin/system/board/forces/index.do?mode=commentList", {jData : JSON.stringify({article_seq : $routeParams.seq})}, function(data2){
					$scope.comment = data2.comment;
				});
			});				
		}
	}

	$scope.commentDel = function(idx){
		if(confirm("정말 삭제하시겠습니까?")){
			ajaxService.getJSON("/giftcard/admin/system/board/forces/index.do?mode=commentDel", {jData : JSON.stringify({comment_seq : idx})}, function(data){
				ajaxService.getJSON("/giftcard/admin/system/board/forces/index.do?mode=commentList", {jData : JSON.stringify({article_seq : $routeParams.seq})}, function(data2){
					$scope.comment = data2.comment;
				});
			});	
		}
	}
	
	$scope.commentUpdateWindow = function(idx){
		var options = {
			autoOpen: false,
			modal: true,
			width: "750",
			height: "180",
			close: function(event, ui) {}
		};
		ajaxService.getJSON("/giftcard/admin/system/board/forces/index.do?mode=commentReplyInfo", {comment_seq : idx}, function(data){
			data.comment_seq = idx;
			dialogService.open("commentUpdate","/lib/js/partials/adminComment.html", data, options).then(
				function(result) {
					$scope.comment = result;
				},
				function(error) {}
			);
		});
	}
	
	$scope.mms = function(item){
		if(confirm("답변완료 문자를 발송하시겠습니까?")){
			$scope.mmsSend = {
					tran_msg :"요청하신 문의에 대한 답글이 입력되었습니다 - 티켓모아 ",
					tran_phone : item.charge_tel,
					tran_callback : "1544-6444"
			};
			ajaxService.getJSON("/giftcard/admin/system/send/sms/index.do?mode=sms_write", {jData : JSON.stringify($scope.mmsSend)}, function(data){
				if(data.rst == "1"){
					alert("문자 발송 요청이 되었습니다.");
				}else{
					alert("문자 발송 요청에 실패하였습니다.");
				}
			});
		}		
	}
});
	
	

app.controller("replyCtrl", function($scope, $window, $routeParams, ajaxService, dialogService) {
	$scope.commentUpdate = function(){
		ajaxService.getJSON("/giftcard/admin/system/board/forces/index.do?mode=commentReplyUpdate", {jData : JSON.stringify($scope.model)}, function(data){
			ajaxService.getJSON("/giftcard/admin/system/board/forces/index.do?mode=commentList", {jData : JSON.stringify({article_seq : $routeParams.seq})}, function(data2){
				dialogService.close("commentUpdate", data2.comment);
			});
		});
	}
});


</script>
</head>
<body data-ng-controller="mainCtrl">
  <div data-ng-view data-ng-cloak></div>
</body>
</html>
