(function(ang, $){
	ang.module('myCommon', [])	
	.service('ajaxService',function($q, $log){
		this.getJSON = function(targetURL, params, callback){
			var deferred = $q.defer();
			
			$.ajax({
				url : targetURL, 
				type: "POST", 
				data : params, 
				dataType : "json", 
				async: false, 
				cache : false, 
				success : function(data){
					deferred.resolve(data);
				},
				error : function(data){
					deferred.reject(data);
				}
			}).error(function(jqXHR,textStatus,errorThrown){
				if(jqXHR.status == 999){
					if(confirm("세션이 끊어졌습니다.\n로그인 후 다시 시도해 주세요.\n로그인페이지로 이동하시겠습니까?")){
						window.top.location.href = "/giftcard/admin/login.do";
					}
				}else{
					alert("=================오류내용=================\n" + jqXHR.responseText+"\n===================END==================");
				}
//				$log.error(jqXHR.responseText);
	 		});
			
			deferred.promise.then(function(data){
				if(!!callback){
					callback(data);
				}
			});
			return deferred.promise;
		};
		this.getAsyncJSON = function(targetURL, params, callback){
			var deferred = $q.defer();
			
			$.ajax({
				url : targetURL, 
				type: "POST", 
				data : params, 
				dataType : "json", 
				async: true, 
				cache : false, 
				success : function(data){
					deferred.resolve(data);
				},
				error : function(data){
					deferred.reject(data);
				}
			}).error(function(jqXHR,textStatus,errorThrown){
				if(jqXHR.status == 999){
					if(confirm("세션이 끊어졌습니다.\n로그인 후 다시 시도해 주세요.\n로그인페이지로 이동하시겠습니까?")){
						window.top.location.href = "/giftcard/admin/login.do";
					}
				}else{
					alert("=================오류내용=================\n" + jqXHR.responseText+"\n===================END==================");
				}
//				$log.error(jqXHR.responseText);
	 		});
			
			deferred.promise.then(function(data){
				if(!!callback){
					callback(data);
				}
			});
			return deferred.promise;
		};
		this.getXML = function(targetURL, params, callback){
			var deferred = $q.defer();
			
			$.ajax({
				url : targetURL, 
				type: "POST", 
				data : params, 
				dataType : "xml", 
				async: true, 
				cache : false, 
				success : function(data){
					deferred.resolve(data);
				},
				error : function(data){
					deferred.reject(data);
				}
			}).error(function(jqXHR,textStatus,errorThrown){
				if(jqXHR.status == 999){
					if(confirm("세션이 끊어졌습니다.\n로그인 후 다시 시도해 주세요.\n로그인페이지로 이동하시겠습니까?")){
						window.top.location.href = "/giftcard/admin/login.do";
					}
				}else{
					alert("=================오류내용=================\n" + jqXHR.responseText+"\n===================END==================");
				}
//				$log.error(jqXHR.responseText);
	 		});
			
			deferred.promise.then(function(data){
				if(!!callback){
					callback(data);
				}
			});
			return deferred.promise;
		};
	});
})(angular, jQuery=(typeof window.jQuery=='undefined'?(function(){alert("jQuery 를 import 하세요."); return "";})():jQuery));